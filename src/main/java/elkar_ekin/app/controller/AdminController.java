package elkar_ekin.app.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

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

import elkar_ekin.app.dto.NewsItemDto;
import elkar_ekin.app.model.HistoricTask;
import elkar_ekin.app.model.NewsItem;
import elkar_ekin.app.model.Task;
import elkar_ekin.app.model.User;
import elkar_ekin.app.repositories.TaskRepository;
import elkar_ekin.app.repositories.UserRepository;
import elkar_ekin.app.service.HistoricTaskService;
import elkar_ekin.app.service.NewsItemService;
import elkar_ekin.app.service.TaskService;
import elkar_ekin.app.service.UserService;

@Controller
@RequestMapping("/admin-view")
public class AdminController {

	private User user;
	private User guest;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	NewsItemService newsItemService;

	@Autowired
    private UserService userService;

	private final TaskService taskService;
	private final HistoricTaskService historicTaskService;

	public AdminController(TaskService taskService, HistoricTaskService hisotricTaskService) {
		this.taskService = taskService;
		this.historicTaskService = hisotricTaskService;
	}

	@ModelAttribute("newsItemDto")
    public NewsItemDto newsItemDto() {
        return new NewsItemDto();
    }

	@ModelAttribute
	public void commonUser (Model model, Principal principal) {
		String username=principal.getName();
		user = userRepository.findByUsername(username);
		System.out.println(user);
		model.addAttribute("user", user);
	}

	@GetMapping("/index")
	public String adminIndex (Model model, Principal principal) {
		List<NewsItem> allNewsItems = newsItemService.getLastFiveNewsItems();
		if (allNewsItems == null) {
			model.addAttribute("message", "No hay noticias disponibles.");
		} else {
			model.addAttribute("newsItemList", allNewsItems);
		}

		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
		model.addAttribute("currentPage", "index");
		return "admin/index";
	}

	@GetMapping({"/newsItem/", "/newsItem/list"})
	public String listNewsItem (Model model, Principal principal) {
		List<NewsItem> allNewsItems = newsItemService.getAllNewsItems();
		model.addAttribute("currentPage", "newsItemList");
		if (allNewsItems == null) {
			model.addAttribute("message", "No hay noticias disponibles.");
		} else {
			model.addAttribute("newsItemList", allNewsItems);
		}
		return "admin/newsItemList";
	}

	@GetMapping({"/newsItem/create"})
	public String showNewsForm(Model model) {
		NewsItemDto newsItemDto = new NewsItemDto();
		model.addAttribute("currentPage", "newsItemCreate");
		model.addAttribute("newsItemDto", newsItemDto);
		return "admin/newsItemForm";
	}

	@PostMapping({"createNewsItem"})
	public String createNewsItem(@ModelAttribute("newsItem") NewsItemDto newsItemDto, BindingResult result) {
		if (result.hasErrors()) {
			return "/admin-view/createNewsItem";
		}
		newsItemDto.setUser(user);
		newsItemService.save(newsItemDto);
		return "redirect:/admin-view/newsItem/list";
	}

	@GetMapping("/newsItem/{newsItemID}/delete")
	public String deleteNewsItem(@PathVariable("newsItemID") String newsItemID, Model model) {
		newsItemService.deleteNewsItem(Long.parseLong(newsItemID));
		return "redirect:/admin-view/newsItem/list";
	}


	@GetMapping({"/newsItem/{newsItemID}/edit"})
	public String showNewsItemForm(@PathVariable("newsItemID") Long newsItemID, Model model) {
		NewsItemDto newsItemDto = new NewsItemDto();
		if (newsItemID != null) {
			NewsItem newsItem = newsItemService.getNewsItemByID(newsItemID);
			if (newsItem != null) {
				newsItemDto.setNewsItemID(newsItemID);
				newsItemDto.setUser(newsItem.getUser());
				newsItemDto.setTitle(newsItem.getTitle());
				newsItemDto.setBody(newsItem.getBody());
			}
		}
		model.addAttribute("newsItemDto", newsItemDto);
		model.addAttribute("isEdit", newsItemID != null);
		return "admin/newsItemForm";
	}
	
	@PostMapping({"editNewsItem"})
	public String createOrUpdateNewsItem(@ModelAttribute("newsItemDto") NewsItemDto newsItemDto, BindingResult result) {
		if (result.hasErrors()) {
			return "admin/newsItemForm";
		}
		if (newsItemDto != null) {
			// Es una actualización
			newsItemService.editNewsItem(newsItemDto.getNewsItemID(), newsItemDto);
		} else {
			// Es una creación
			NewsItemDto newNewsItemDto = new NewsItemDto();
			newNewsItemDto.setUser(user);
			newsItemService.save(newNewsItemDto);
		}
		return "redirect:/admin-view/newsItem/list";
	}

	@GetMapping({"/clients/list", "/clients/"})
	public String listClients (Model model, Principal principal) {
		model.addAttribute("currentPage", "clientList");
		List<User> userList = userRepository.getUsersByRole("C");
		if (userList == null) {
			model.addAttribute("message", "No hay clientes disponibles.");
		} else {
			model.addAttribute("userList", userList);
		}
		return "admin/userList";
	}

	@GetMapping("/clients/{clientID}/delete")
    public String deleteClient(@PathVariable("clientID") Long clientId) {
        userService.deleteUser(clientId);
        return "redirect:/admin-view/clients/list";
    }
	
	@GetMapping("/clients/{clientID}")
	public String viewClient(@PathVariable("clientID") String clientID, Model model, Principal principal) {

		String admin = principal.getName();
		guest = userRepository.findByUsername(admin);
		model.addAttribute("guest", guest);

		User client = userRepository.findByUserID(Long.parseLong(clientID));
		model.addAttribute("user", client);
		checkProfilePicture(client);

		Long amount = taskRepository.countByClient(client);
		model.addAttribute("amount", amount);

		List<HistoricTask> clientTasks = historicTaskService.getFirstFivePastTasks(Long.parseLong(clientID));
		if (clientTasks == null) {
			model.addAttribute("message", "No hay tareas disponibles.");
		} else {
			model.addAttribute("taskList", clientTasks);
		}
		return "admin/userSpecific";
	}

	public void checkProfilePicture(User user) {
		final Path imageLocation = Paths.get("public/img");

		Path filePath = imageLocation.resolve(user.getImagePath());

		if (!Files.exists(filePath) || !Files.isReadable(filePath) || user.getImagePath().isEmpty()) {
			user.setImagePath(null);
		}
	}

	@GetMapping({"/volunteers/list", "/volunteers/"})
	public String listVolunteers (Model model, Principal principal) {
		model.addAttribute("currentPage", "volunteerList");
		List<User> userList = userRepository.getUsersByRole("V");
		if (userList == null) {
			model.addAttribute("message", "No hay clientes disponibles.");
		} else {
			model.addAttribute("userList", userList);
		}
		return "admin/userList";
	}

	@GetMapping("/volunteers/{volunteerID}/delete")
	public String deleteVolunteer(@PathVariable("volunteerID") String volunteerID, Model model) {
		//Delete the image if exists
		User volunteer = userRepository.findByUserID(Long.parseLong(volunteerID));
		Path imagePath = Paths.get("public/img", volunteer.getImagePath());
		try{
			Files.delete(imagePath);
		}catch(Exception e){
		}
		userRepository.deleteById(Long.parseLong(volunteerID));
		return "redirect:/admin-view/volunteers/list";
	}
	
	@GetMapping("/volunteers/{volunteerID}")
	public String viewVolunteer(@PathVariable("volunteerID") String volunteerID, Model model, Principal principal) {
		
		String admin = principal.getName();
		guest = userRepository.findByUsername(admin);
		model.addAttribute("guest", guest);

		User volunteer = userRepository.findByUserID(Long.parseLong(volunteerID));
		model.addAttribute("user", volunteer);
		checkProfilePicture(volunteer);

		Long amount = taskRepository.countByVolunteer(volunteer);
		model.addAttribute("amount", amount);

		List<HistoricTask> volunteerTasks = historicTaskService.getFirstFiveVolunteerTasks(volunteer.getUserID());
		if (volunteerTasks == null) {
			model.addAttribute("message", "No hay tareas disponibles.");
		} else {
			model.addAttribute("taskList", volunteerTasks);
		}

		return "admin/userSpecific";
	}
	@GetMapping("/tasks")
	public String showTaskList(Model model, Principal principal) {
        List<Task> clientTasks = taskService.getAllTasks();
        model.addAttribute("currentPage", "volunteerTaskList");
        if (clientTasks.isEmpty()) {
            model.addAttribute("message", "No hay tareas disponibles.");
        } else {
            model.addAttribute("taskList", clientTasks);
        }
        return "admin/taskList";
    }

	@GetMapping("/task/{taskID}/delete")
	public String deleteTask(@PathVariable("taskID") String taskID, Model model) {
		model.addAttribute("currentPage", "deleteTask");
		taskService.deleteTask(Long.parseLong(taskID));
		return "redirect:/admin-view/tasks";
	}
  
	@GetMapping("/chat")
	public String showChat(Model model) {
		model.addAttribute("currentPage", "chat");
		model.addAttribute("user", user);
		return "admin/chat";
	} 

	@GetMapping("/history")
	public String showTaskHistory(Model model, Principal principal) {
		User user = (User) model.getAttribute("user");
		List<HistoricTask> clientTasks = historicTaskService.getPastVolunteerTasks(user.getUserID());
		model.addAttribute("currentPage", "taskHistory");
		if (clientTasks == null) {
			model.addAttribute("message", "No hay tareas disponibles.");
		} else {
			model.addAttribute("taskList", clientTasks);
		}
		return "admin/taskList";
	}
}