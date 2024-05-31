package elkar_ekin.app.controller;

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

import elkar_ekin.app.dto.UserDto;
import elkar_ekin.app.model.NewsItem;
import elkar_ekin.app.model.Task;
import elkar_ekin.app.model.User;
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
	UserDetailsService userDetailsService;

	@Autowired
	TaskService taskService;
	NewsItemService newsItemService;

	private final UserService userService;

	public VolunteerController (UserService userService) {
        this.userService = userService;
    }


	@ModelAttribute
	public void commonUser (Model model, Principal principal) {
		String username=principal.getName();
		User user = repository.findByUsername(username);
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
		return "index";
	}

	@GetMapping("/user")
	public String clientUser (Model model) {
		model.addAttribute("currentPage", "user");
		return "user";
	}

	@PostMapping("/user/update")
    public String clientUpdateUser (@ModelAttribute("userDto") UserDto userDto, BindingResult result, Model model, Principal principal) {
		if (result.hasErrors()) {
            return "user";
        }
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        userService.update(userDto, userDetails);
        model.addAttribute("message", "Updated Successfully!");
        return "user";
    }
	@GetMapping("/taskList")
	public String showTaskList (Model model, Principal principal) {
		List<Task> allTasks = taskService.getAllTasks();
		model.addAttribute("currentPage", "taskList");
		if (allTasks == null) {
			model.addAttribute("message", "No hay tareas disponibles.");
		} else {
			model.addAttribute("taskList", allTasks);
		}
		return "volunteer/taskList";
	}
	@PostMapping("/task/singUp")
    public String singUpVolunteerToTask (@PathVariable("taskID") String taskID, Model model, Principal principal) {
		Task task = taskService.getTaskByID(Long.parseLong(taskID));
		
       /*  userService.update(userDto, userDetails); */
        model.addAttribute("message", "Updated Successfully!");
        return "user";
    }
}
