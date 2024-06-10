package elkar_ekin.app.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
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
import elkar_ekin.app.model.DefaultTask;
import elkar_ekin.app.model.HistoricTask;
import elkar_ekin.app.model.NewsItem;
import elkar_ekin.app.model.Task;
import elkar_ekin.app.model.User;
import elkar_ekin.app.repositories.DefaultTaskRepository;
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

	@Autowired
    private DefaultTaskRepository defaultTaskRepository;

	private final TaskService taskService;
	private final HistoricTaskService historicTaskService;

	public AdminController(TaskService taskService, HistoricTaskService hisotricTaskService) {
		this.taskService = taskService;
		this.historicTaskService = hisotricTaskService;
	}

	// Adds a NewsItemDto object to the model
	@ModelAttribute("newsItemDto")
    public NewsItemDto newsItemDto() {
        return new NewsItemDto();
    }

	@ModelAttribute
	public void commonUser (Model model, Principal principal) {
		//Gets the user that is in the session and saves it in the "user" attribute
		String username=principal.getName();
		user = userRepository.findByUsername(username);
		System.out.println(user);
		model.addAttribute("user", user);
	}

	@GetMapping("/index")
	public String adminIndex (Model model, Principal principal) {
		//Loads the five news items from the news list
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
		//Loads all the news Items from the database
		List<NewsItem> allNewsItems = newsItemService.getAllNewsItems();
	    //Sets the current page 
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
	    //Sets the current page 
		model.addAttribute("currentPage", "newsItemCreate");
		model.addAttribute("newsItemDto", newsItemDto);
		return "admin/newsItemForm";
	}

	@PostMapping({"createNewsItem"})
	public String createNewsItem(@ModelAttribute("newsItem") NewsItemDto newsItemDto, BindingResult result) {
		//If the newsItem has errors it redirects to the creating page
		if (result.hasErrors()) {
			return "/admin-view/createNewsItem";
		}
		newsItemDto.setUser(user);
		newsItemService.save(newsItemDto);
		return "redirect:/admin-view/newsItem/list";
	}

	@GetMapping("/newsItem/{newsItemID}/delete")
	public String deleteNewsItem(@PathVariable("newsItemID") String newsItemID, Model model) {
		//Deletes the selected newsItemID
		newsItemService.deleteNewsItem(Long.parseLong(newsItemID));
		return "redirect:/admin-view/newsItem/list";
	}

	@GetMapping({"/newsItem/{newsItemID}/edit"})
	public String showNewsItemForm(@PathVariable("newsItemID") Long newsItemID, Model model) {
		NewsItemDto newsItemDto = new NewsItemDto();
		if (newsItemID != null) {
			//Gets news Items by the ID obtained from the URL
			NewsItem newsItem = newsItemService.getNewsItemByID(newsItemID);
			if (newsItem != null) {
				//Saved the newsItem data in NewsItemDto
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
			// Updates news item if ID exists
			newsItemService.editNewsItem(newsItemDto.getNewsItemID(), newsItemDto);
		} else {
			// Creates new news item
			NewsItemDto newNewsItemDto = new NewsItemDto();
			newNewsItemDto.setUser(user);
			newsItemService.save(newNewsItemDto);
		}
		return "redirect:/admin-view/newsItem/list";
	}

	@GetMapping({"/clients/list", "/clients/"})
	public String listClients (Model model, Principal principal) {
		model.addAttribute("currentPage", "clientList");
		//Load all the clients (users with role "C") from the database
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
		//Delete the user that has the userID of the path variable
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
		if(client.getImagePath() != null){
			checkProfilePicture(client);
		}

		Long amount = taskRepository.countByClient(client);
		model.addAttribute("amount", amount);

		List<HistoricTask> historicTasks = new ArrayList<>();
		
		historicTasks = historicTaskService.getFirstFiveVolunteerTasks(Long.parseLong(clientID));
		model.addAttribute("currentPage", "taskHistory");
		if (historicTasks == null) {
			model.addAttribute("message", "No hay tareas disponibles.");
			} else {
		List<DefaultTask> defaultTasks = new ArrayList<>();
		if (historicTasks != null) {
			for (HistoricTask task : historicTasks) {
				DefaultTask defaultTask = defaultTaskRepository.findById(task.getTaskDefaultID().getDefaultTaskID()).orElse(null);
				if (defaultTask != null) {
					defaultTasks.add(defaultTask);
				}
				}
			model.addAttribute("defaultTaskList", defaultTasks);
		}
	}

		model.addAttribute("taskList", historicTasks);
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
		// Find the volunteer by their ID
		String admin = principal.getName();
		guest = userRepository.findByUsername(admin);
		model.addAttribute("guest", guest);

		User volunteer = userRepository.findByUserID(Long.parseLong(volunteerID));
		model.addAttribute("user", volunteer);
		if(volunteer.getImagePath() != null){
			checkProfilePicture(volunteer);
		}

    	// Count the number of tasks assigned to the volunteer and add to the model
		Long amount = taskRepository.countByVolunteer(volunteer);
		model.addAttribute("amount", amount);

		List<HistoricTask> historicTasks = new ArrayList<>();
		
		// Get the first five historic tasks for the volunteer
		historicTasks = historicTaskService.getFirstFiveVolunteerTasks(Long.parseLong(volunteerID));
		model.addAttribute("currentPage", "taskHistory");
		if (historicTasks == null) {
			model.addAttribute("message", "No hay tareas disponibles.");
			} 
		else {
			// If there are no historic tasks, add a message to the model
			List<DefaultTask> defaultTasks = new ArrayList<>();
			if (historicTasks != null) {
			for (HistoricTask task : historicTasks) {
				DefaultTask defaultTask = defaultTaskRepository.findById(task.getTaskDefaultID().getDefaultTaskID()).orElse(null);
				if (defaultTask != null) {
					defaultTasks.add(defaultTask);
				}
				}
			// Add the default tasks to the model
			model.addAttribute("defaultTaskList", defaultTasks);
			}
		}
		// Add the historic tasks to the model and return the user-specific view
		model.addAttribute("taskList", historicTasks);
		return "admin/userSpecific";
	}


	@GetMapping("/tasks")
	public String showTaskList(Model model, Principal principal) {
		// Get all tasks from the task service
        List<Task> clientTasks = taskService.getAllTasks();
		// Set the current page attribute
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
		// Set the current page attribute
		model.addAttribute("currentPage", "deleteTask");
		// Delete the task by its ID
		taskService.deleteTask(Long.parseLong(taskID));
		return "redirect:/admin-view/tasks";
	}
  
	@GetMapping("/chat")
	public String showChat(Model model) {
		// Set the current page attribute
		model.addAttribute("currentPage", "chat");
		// Add the current user to the model
		model.addAttribute("user", user);
		return "admin/chat";
	} 
}