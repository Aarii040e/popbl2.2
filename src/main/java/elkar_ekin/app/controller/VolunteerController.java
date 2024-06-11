package elkar_ekin.app.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import elkar_ekin.app.model.Task;
import elkar_ekin.app.model.User;
import elkar_ekin.app.repositories.DefaultTaskRepository;
import elkar_ekin.app.repositories.TaskRepository;
import elkar_ekin.app.repositories.UserRepository;
import elkar_ekin.app.service.TaskService;
import elkar_ekin.app.service.HistoricTaskService;
import elkar_ekin.app.service.NewsItemService;
import elkar_ekin.app.service.UserService;

@Controller
@RequestMapping("/volunteer-view")
@PreAuthorize("hasRole('VOLUNTEER')")
public class VolunteerController extends BaseController{
	
	private final UserService userService;
	private final TaskRepository taskRepository;
	
    @Autowired
    private UserRepository userRepository;
	@Autowired
	private TaskService taskService;

	private User user;

	public VolunteerController(UserRepository userRepository, TaskRepository taskRepository,
                            DefaultTaskRepository defaultTaskRepository, 
			UserDetailsService userDetailsService, HistoricTaskService historicTaskService, TaskService taskService, NewsItemService newsItemService,
			UserService userService) {
				super(userRepository, taskRepository, defaultTaskRepository, userDetailsService, historicTaskService, newsItemService, userService);
				this.userService = userService;
				this.taskRepository = taskRepository;
	}

	@ModelAttribute
    public void initUser(Model model, Principal principal) {
        if (user == null) {  // Ensure initialization happens only once
            super.commonUser(model, principal);
            user = (User) model.getAttribute("user");
        }
    }

	//// Display the list of tasks available to volunteers
	@GetMapping({"/task/list", "/task/"})
	public String showTaskList (Model model, Principal principal) {
		String username=principal.getName();
		User user = userRepository.findByUsername(username);
		
		setTaskListAsAttribute(model, principal);
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

	// Manage volunteer tasks such as signing up, saving, or unsigning
	@GetMapping("/task/{taskID}/{action}")
	public String manageVolunteerTask(@PathVariable("taskID") String taskID, @PathVariable("action") String action, 
	Model model, Principal principal, RedirectAttributes redirectAttributes) {
		// Obtain the task by ID
		Task task = taskService.getTaskByID(Long.parseLong(taskID));
		User volunteer = userService.findByUsername(principal.getName());
		switch (action) {
			case "signUp":
				task.setVolunteer(volunteer);
				task.setState("closed");
				break;
			case "save":
				if (!task.getSavedUsers().contains(volunteer)) {
					task.getSavedUsers().add(volunteer);
				}
				break;
			case "delete":
				if (task.getSavedUsers().contains(volunteer)) {
					task.getSavedUsers().remove(volunteer);
				}
				break;
			case "unsign":
				task.setVolunteer(null);
				task.setState("active"); // provisional name
				break;
			default:
				redirectAttributes.addFlashAttribute("error", "Unknown action.");
				return "redirect:/volunteer-view/task/list";
		}

		// Save the task
		taskRepository.save(task);

		// Update the task list in the model
		setTaskListAsAttribute(model, principal);

		// Determine the redirect URL based on the action
		String redirectUrl = "redirect:/volunteer-view/";
		if (action.equals("signUp") || action.equals("unsign")) {
			redirectUrl += "signedUp";
		} else {
			redirectUrl += "task/list";
		}

		return redirectUrl;
	}


	// Display the list of tasks that the volunteer can sign up for
	private void setTaskListAsAttribute(Model model, Principal principal){
        List<Task> allTasks = taskService.getAllActiveTasks();
		model.addAttribute("currentPage", "volunteerTaskList");
		if (allTasks == null) {
			model.addAttribute("message", "No hay tareas disponibles.");
		} else {
			model.addAttribute("taskList", allTasks);
		}
	}

	// Display the list of tasks that the volunteer has signed up for
	@GetMapping("/signedUp")
	public String showSignedUpTaskList (Model model, Principal principal) {
		List<Task> allTasks = taskService.getVolunteerTasks(user);
		model.addAttribute("currentPage", "signedUp");
		if (allTasks == null) {
			model.addAttribute("message", "No hay tareas disponibles.");
		} else {
			model.addAttribute("taskList", allTasks);
		}
		return "volunteer/taskList";
	}


    // Display the list of saved tasks by the volunteer
	@GetMapping({"/saved"})
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