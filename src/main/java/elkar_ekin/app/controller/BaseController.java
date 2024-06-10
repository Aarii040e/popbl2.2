package elkar_ekin.app.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import elkar_ekin.app.dto.UserDto;
import elkar_ekin.app.model.HistoricTask;
import elkar_ekin.app.model.NewsItem;
import elkar_ekin.app.model.User;
import elkar_ekin.app.repositories.TaskRepository;
import elkar_ekin.app.repositories.UserRepository;
import elkar_ekin.app.service.HistoricTaskService;
import elkar_ekin.app.service.NewsItemService;
import elkar_ekin.app.service.UserService;


@SessionAttributes("userDto")
public abstract class BaseController {
    


	UserRepository userRepository;
    private final TaskRepository taskRepository;
	private final UserDetailsService userDetailsService;
    private final HistoricTaskService historicTaskService;
    private final UserService userService;
	private final NewsItemService newsItemService;

    private User guest;
	private User user;
	private String returnRole;

    public BaseController(UserRepository userRepository, TaskRepository taskRepository,
            UserDetailsService userDetailsService, HistoricTaskService historicTaskService, NewsItemService newsItemService, UserService userService) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.historicTaskService = historicTaskService;
        this.userDetailsService = userDetailsService;
        this.newsItemService = newsItemService;
        this.userService = userService;
    }

	@ModelAttribute("userDto")
    public UserDto userDto() {
        return new UserDto();
    }

    @ModelAttribute
	public void commonUser (Model model, Principal principal) {
		String username=principal.getName();
		user = userRepository.findByUsername(username);
		model.addAttribute("user", user);

		switch(user.getRole()){
			case "A":
				returnRole = "admin";
				break;
			case "V":
				returnRole = "volunteer";
				break;
			case "C":
				returnRole = "client";
				break;
			default:
				returnRole = "";
				break;
		}
	}

    @GetMapping("/index")
	public String indexView (Model model, Principal principal, Authentication authentication) {
		List<NewsItem> allNewsItems = newsItemService.getLastFiveNewsItems();
		if (allNewsItems == null) {
			model.addAttribute("message", "No hay noticias disponibles.");
		} else {
			model.addAttribute("newsItemList", allNewsItems);
		}
		
		if (principal != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("user", userDetails);
        }

		model.addAttribute("currentPage", "index");

		String role = authentication.getAuthorities().stream()
                                    .findFirst().get().getAuthority();
        model.addAttribute("role", role);


		return returnRole+"/index";
	}

	@GetMapping("/user")
	public String userView (Model model, Principal principal, String returnString) {
		model.addAttribute("currentPage", "user");
		
		if (principal != null) {
            guest = userRepository.findByUsername(principal.getName());
            model.addAttribute("guest", guest);

            User user = (User) model.getAttribute("user");
            if (user != null) {
                checkProfilePicture(user);

                if (user.getRole().equals("V")) {
                    Long amount = taskRepository.countByVolunteer(user);
                    model.addAttribute("amount", amount);
    
                    Long volunteerId = user.getUserID(); // Assuming User class has a getUserID() method
                    List<HistoricTask> volunteerTasks = historicTaskService.getFirstFiveVolunteerTasks(volunteerId);
                    if (volunteerTasks == null) {
                        model.addAttribute("message", "No hay tareas disponibles.");
                    } else {
                        model.addAttribute("taskList", volunteerTasks);
                    }
                }

                else if (user.getRole().equals("C")) {
                    Long amount = taskRepository.countByClient(user);
                    model.addAttribute("amount", amount);
    
                    Long clientId = user.getUserID(); // Assuming User class has a getUserID() method
                    List<HistoricTask> clientTasks = historicTaskService.getFirstFivePastTasks(clientId);
                    if (clientTasks == null) {
                        model.addAttribute("message", "No hay tareas disponibles.");
                    } else {
                        model.addAttribute("taskList", clientTasks);
                    }
                }

            }
        }
		return returnRole+"/user";
	}

    public void checkProfilePicture(User user) {
        if (user != null && user.getImagePath() != null) {
            final Path imageLocation = Paths.get("public/img");
            Path filePath = imageLocation.resolve(user.getImagePath());

            if (!Files.exists(filePath) || !Files.isReadable(filePath) || user.getImagePath().isEmpty()) {
                user.setImagePath(null);
            }
        }
    }
    
    @PostMapping("/user/update")
    public String updateUserView(@ModelAttribute("userDto") UserDto userDto, BindingResult result, Model model, Principal principal) {
		if (result.hasErrors()) {
			return "redirect:/" + returnRole + "-view/user";
        }
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        userService.update(userDto, userDetails);
		model.addAttribute("guest", guest);
        model.addAttribute("message", "Updated Successfully!");
        return "redirect:/" + returnRole + "-view/user";
    }
    
    @GetMapping("/history")
	public String showTaskHistory(Model model, Principal principal) {
        if (user.getRole().equals("V")) {
            Long volunteerId = user.getUserID(); // Assuming User class has a getUserID() method
            List<HistoricTask> volunteerTasks = historicTaskService.getFirstFiveVolunteerTasks(volunteerId);
            if (volunteerTasks == null) {
                model.addAttribute("message", "No hay tareas disponibles.");
            } else {
                model.addAttribute("taskList", volunteerTasks);
            }
        }

        else if (user.getRole().equals("C")) {
            Long clientId = user.getUserID(); // Assuming User class has a getUserID() method
            List<HistoricTask> clientTasks = historicTaskService.getFirstFivePastTasks(clientId);
            if (clientTasks == null) {
                model.addAttribute("message", "No hay tareas disponibles.");
            } else {
                model.addAttribute("taskList", clientTasks);
            }
        }

		return returnRole+"/taskList";
	}

    @GetMapping("/chat")
    public String showChat(Model model) {
		model.addAttribute("currentPage", "chat");
		model.addAttribute("user", user);
        return returnRole+"/chat";
    }

}
