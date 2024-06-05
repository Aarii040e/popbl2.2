package elkar_ekin.app.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import elkar_ekin.app.dto.TaskDto;
import elkar_ekin.app.dto.UserDto;
import elkar_ekin.app.model.NewsItem;
import elkar_ekin.app.model.Task;
import elkar_ekin.app.model.User;
import elkar_ekin.app.repositories.TaskRepository;
import elkar_ekin.app.repositories.UserRepository;
import elkar_ekin.app.service.TaskService;
import elkar_ekin.app.service.NewsItemService;
import elkar_ekin.app.service.UserService;

@Controller
@RequestMapping("/volunteer-view")
public class VolunteerController {

	@Autowired
	private UserRepository repository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	TaskService taskService;

	@Autowired
	NewsItemService newsItemService;

	private User guest;
	private User user;

	private final UserService userService;

	public VolunteerController (UserService userService) {
        this.userService = userService;
    }

	@ModelAttribute
	public void commonUser (Model model, Principal principal) {
		String username=principal.getName();
		user = repository.findByUsername(username);
		model.addAttribute("user", user);
	}

	@GetMapping("/index")
	public String volunteerIndex (Model model, Principal principal) {
		List<NewsItem> allNewsItems = newsItemService.getLastFiveNewsItems();
		if (allNewsItems == null) {
			model.addAttribute("message", "No hay noticias disponibles.");
		} else {
			model.addAttribute("newsItemList", allNewsItems);
		}
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
		model.addAttribute("currentPage", "index");
		return "volunteer/index";
	}

	@GetMapping("/user")
	public String clientUser (Model model, Principal principal) {
		model.addAttribute("currentPage", "user");
		
		String admin = principal.getName();
		guest = repository.findByUsername(admin);
		model.addAttribute("guest", guest);
		
		User user = (User) model.getAttribute("user");
		
		Long amount = taskRepository.countByVolunteer(user);
		model.addAttribute("amount", amount);
		
		List<Task> volunteerTasks = taskService.getFirstFiveVolunteerTasks(user);
		if (volunteerTasks == null) {
			model.addAttribute("message", "No hay tareas disponibles.");
		} else {
			model.addAttribute("taskList", volunteerTasks);
		}
		
		return "volunteer/user";
	}
	
	@PostMapping("/user/update")
    public String clientUpdateUser (@ModelAttribute("userDto") UserDto userDto, BindingResult result, Model model, Principal principal) {
		if (result.hasErrors()) {
			return "volunteer/user";
        }
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        userService.update(userDto, userDetails);
		model.addAttribute("guest", guest);
        model.addAttribute("message", "Updated Successfully!");
        return "user";
    }
	
	@GetMapping({"/task/list", "/task/"})
	public String showTaskList (Model model, Principal principal) {
		setTaskListAsAttribute(model);
		model.addAttribute("currentPage", "volunteerTaskList");
		//Saved tasks for current user
		List<Task> savedTasks = userService.getSavedTasksByUser(user);
		List<String> idList = new ArrayList<>();
		for (Task task : savedTasks) {
			idList.add(String.valueOf(task.getTaskID()));
		}
		model.addAttribute("savedTaskIDs", idList);
		return "volunteer/taskList";
	}

	@GetMapping("/task/{taskID}/signUp")
    public String singUpVolunteerToTask (@PathVariable("taskID") String taskID, Model model, Principal principal, RedirectAttributes redirectAttributes) {
		Task task = taskService.getTaskByID(Long.parseLong(taskID));
        task.setVolunteer(user);
		task.setState("closed"); // nombre provisional
		taskRepository.save(task);
		// redirectAttributes.addFlashAttribute("error", "You have signed up to the task!"); // no se visualiza
        return "redirect:/volunteer-view/signedUp";
    }

	@GetMapping("/signedUp")
	public String showSignedUpTaskList (Model model, Principal principal) {
		// User user = (User) model.getAttribute("user");
		List<Task> allTasks = taskService.getVolunteerTasks(user);
		model.addAttribute("currentPage", "signedUp");
		if (allTasks == null) {
			model.addAttribute("message", "No hay tareas disponibles.");
		} else {
			model.addAttribute("taskList", allTasks);
		}
		return "volunteer/taskList";
	}

	@GetMapping("/history")
	public String showTaskHistory(Model model, Principal principal) {
		User user = (User) model.getAttribute("user");
		List<Task> clientTasks = taskService.getPastVolunteerTasks(user);
		model.addAttribute("currentPage", "taskHistory");
		if (clientTasks == null) {
			model.addAttribute("message", "No hay tareas disponibles.");
		} else {
			model.addAttribute("taskList", clientTasks);
		}
		return "volunteer/taskList";
	}

	@GetMapping("/chat")
    public String showChat(Model model) {
		model.addAttribute("currentPage", "chat");
		model.addAttribute("user", user);
        return "volunteer/chat";
    }

	@GetMapping("/task/{taskID}/save")
    public String saveVolunteerToTask (@PathVariable("taskID") String taskID, Model model, Principal principal, RedirectAttributes redirectAttributes) {
		// Obtener la tarea por ID
        Task task = taskService.getTaskByID(Long.parseLong(taskID));
        // Añadir el usuario a la tarea
        if (!task.getSavedUsers().contains(user)) {
			task.getSavedUsers().add(user);
		}
        // Guardar la tarea
        taskRepository.save(task);
		setTaskListAsAttribute(model);
        return "redirect:/volunteer-view/task/list";
    }

	@GetMapping("/task/{taskID}/delete")
    public String deleteSafeVolunteerToTask (@PathVariable("taskID") String taskID, Model model, Principal principal, RedirectAttributes redirectAttributes) {
		// Obtener la tarea por ID
        Task task = taskService.getTaskByID(Long.parseLong(taskID));
        // Añadir el usuario a la tarea
        if (task.getSavedUsers().contains(user)) {
			task.getSavedUsers().remove(user);
		}
        // Guardar la tarea
        taskRepository.save(task);
		setTaskListAsAttribute(model);
        return "redirect:/volunteer-view/task/list";
    }

	private List<Task> setTaskListAsAttribute(Model model){
		List<Task> allTasks = taskService.getAllActiveTasks();
		model.addAttribute("currentPage", "volunteerTaskList");
		if (allTasks == null) {
			model.addAttribute("message", "No hay tareas disponibles.");
		} else {
			model.addAttribute("taskList", allTasks);
		}
		return allTasks;
	}

	@GetMapping({"/saved_jobs"})
	public String showSavedTaskList (Model model, Principal principal) {
		List<Task> savedTasks = userService.getSavedTasksByUser(user);
		
		// Filtrar tareas con usuario nulo
		List<Task> tasksWithNullUser = savedTasks.stream()
        .filter(task -> task.getVolunteer() == null)
        .collect(Collectors.toList());

		model.addAttribute("currentPage", "savedTaskList");
		if (tasksWithNullUser == null) {
			model.addAttribute("message", "No hay tareas guardadas.");
		} else {
			model.addAttribute("taskList", tasksWithNullUser);
		}
		List<String> idList = new ArrayList<>();
		for (Task task : tasksWithNullUser) {
			idList.add(String.valueOf(task.getTaskID()));
		}
		model.addAttribute("savedTaskIDs", idList);
		return "volunteer/taskList";
	}
}