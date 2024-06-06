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
import elkar_ekin.app.model.NewsItem;
import elkar_ekin.app.model.Task;
import elkar_ekin.app.model.User;
import elkar_ekin.app.repositories.TaskRepository;
import elkar_ekin.app.repositories.UserRepository;
import elkar_ekin.app.service.NewsItemService;
import elkar_ekin.app.service.TaskService;

@Controller
@RequestMapping("/admin-view")
public class AdminController {

	private User user;
	private User guest;

	@Autowired
	private UserRepository repository;

	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	NewsItemService newsItemService;

	private final TaskService taskService;

	public AdminController(TaskService taskService) {
		this.taskService = taskService;
	}

	@ModelAttribute("newsItemDto")
    public NewsItemDto newsItemDto() {
        return new NewsItemDto();
    }

	@ModelAttribute
	public void commonUser (Model model, Principal principal) {
		String username=principal.getName();
		user = repository.findByUsername(username);
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

	@PostMapping("/createNewsItem")
	public String createNewsItem(@ModelAttribute("newsItem") NewsItemDto newsItemDto, BindingResult result) {
		if (result.hasErrors()) {
			return "/admin-view/createNewsItem";
		}
		newsItemDto.setUser(user);
		newsItemService.save(newsItemDto);
		return "admin/newsItemList";
	}

	@GetMapping(value = "/newsItem/{newsItemID}/delete")
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
			newsItemDto.setUser(user);
			newsItemService.save(newsItemDto);
		}
		return "redirect:/admin-view/newsItem/list";
	}

	@GetMapping({"/clients/list", "/clients/"})
	public String listClients (Model model, Principal principal) {
		model.addAttribute("currentPage", "clientList");
		List<User> userList = repository.getUsersByRole("C");
		if (userList == null) {
			model.addAttribute("message", "No hay clientes disponibles.");
		} else {
			model.addAttribute("userList", userList);
		}
		return "admin/userList";
	}

	@GetMapping(value = "/clients/{clientID}/delete")
	public String deleteClient(@PathVariable("clientID") String clientID, Model model) {
		//Delete the image if exists
		User client = repository.findByUserID(Long.parseLong(clientID));
		Path imagePath = Paths.get("public/img", client.getImagePath());
		try{
			Files.delete(imagePath);
		}catch(Exception e){
		}

		taskRepository.deleteByClient_UserID(Long.parseLong(clientID));
		
		repository.deleteById(Long.parseLong(clientID));
		return "redirect:/admin-view/clients/list";
	}
	
	// @GetMapping(value = "/clients/{clientID}/delete")
    // public String deleteClient(@PathVariable("clientID") String clientID, Model model) {
    //     // Eliminar tareas asociadas al cliente y luego el cliente
    //     customerService.deleteCustomer(Long.parseLong(clientID));
    //     return "redirect:/admin-view/clients/list";
    // }
	@GetMapping(value = "/clients/{clientID}")
	public String viewClient(@PathVariable("clientID") String clientID, Model model, Principal principal) {

		String admin = principal.getName();
		guest = repository.findByUsername(admin);
		model.addAttribute("guest", guest);

		User client = repository.findByUserID(Long.parseLong(clientID));
		model.addAttribute("user", client);

		Long amount = taskRepository.countByClient(client);
		model.addAttribute("amount", amount);

		List<Task> clientTasks = taskService.getFirstFivePastTasks(client);
		if (clientTasks == null) {
			model.addAttribute("message", "No hay tareas disponibles.");
		} else {
			model.addAttribute("taskList", clientTasks);
		}

		return "admin/userSpecific";
	}

	@GetMapping({"/volunteers/list", "/volunteers/"})
	public String listVolunteers (Model model, Principal principal) {
		model.addAttribute("currentPage", "volunteerList");
		List<User> userList = repository.getUsersByRole("V");
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
		User volunteer = repository.findByUserID(Long.parseLong(volunteerID));
		Path imagePath = Paths.get("public/img", volunteer.getImagePath());
		try{
			Files.delete(imagePath);
		}catch(Exception e){
		}
		repository.deleteById(Long.parseLong(volunteerID));
		return "redirect:/admin-view/volunteers/list";
	}
	
	@GetMapping(value = "/volunteers/{volunteerID}")
	public String viewVolunteer(@PathVariable("volunteerID") String volunteerID, Model model, Principal principal) {
		
		String admin = principal.getName();
		guest = repository.findByUsername(admin);
		model.addAttribute("guest", guest);

		User volunteer = repository.findByUserID(Long.parseLong(volunteerID));
		model.addAttribute("user", volunteer);

		Long amount = taskRepository.countByVolunteer(volunteer);
		model.addAttribute("amount", amount);

		List<Task> volunteerTasks = taskService.getFirstFiveVolunteerTasks(volunteer);
		if (volunteerTasks == null) {
			model.addAttribute("message", "No hay tareas disponibles.");
		} else {
			model.addAttribute("taskList", volunteerTasks);
		}

		return "admin/userSpecific";
	}
	@GetMapping("/tasks")
	public String showTaskList(Model model, Principal principal) {
		List<Task> allTasks = taskService.getAllActiveTasks();
		model.addAttribute("currentPage", "taskList");
		if (allTasks == null) {
			model.addAttribute("message", "No hay tareas disponibles.");
		} else {
			model.addAttribute("taskList", allTasks);
		}
		return "admin/taskList";
	}
	@GetMapping(value = "/task/{taskID}/delete")
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
}