package elkar_ekin.app.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
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
import elkar_ekin.app.model.DefaultTask;
import elkar_ekin.app.model.HistoricTask;
import elkar_ekin.app.model.NewsItem;
import elkar_ekin.app.model.User;
import elkar_ekin.app.repositories.DefaultTaskRepository;
import elkar_ekin.app.repositories.TaskRepository;
import elkar_ekin.app.repositories.UserRepository;
import elkar_ekin.app.service.HistoricTaskService;
import elkar_ekin.app.service.NewsItemService;
import elkar_ekin.app.service.UserService;

@SessionAttributes("userDto")
public abstract class BaseController {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final DefaultTaskRepository defaultTaskRepository;
    private final UserDetailsService userDetailsService;
    private final HistoricTaskService historicTaskService;
    private final UserService userService;
    private final NewsItemService newsItemService;

    private User guest;
    private User user;
    private String returnRole;

    public BaseController(UserRepository userRepository, TaskRepository taskRepository,
                          DefaultTaskRepository defaultTaskRepository, 
                          UserDetailsService userDetailsService, 
                          HistoricTaskService historicTaskService, 
                          NewsItemService newsItemService, 
                          UserService userService) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.defaultTaskRepository = defaultTaskRepository;
        this.userDetailsService = userDetailsService;
        this.historicTaskService = historicTaskService;
        this.newsItemService = newsItemService;
        this.userService = userService;
    }

    @ModelAttribute("userDto")
    public UserDto userDto() {
        return new UserDto();
    }

    @ModelAttribute
    public void commonUser(Model model, Principal principal) {
        //Gets the user that is in the session and saves it in the "user" attribute
        //It saves the role in the "returnRole" variable
        String username = principal.getName();
        user = userRepository.findByUsername(username);
        model.addAttribute("user", user);

        switch (user.getRole()) {
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
    public String indexView(Model model, Principal principal, Authentication authentication) {
        //Loads all the last five news items to show them in the horizontal view
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
        //Sets the current page 
        model.addAttribute("currentPage", "index");

        String role = authentication.getAuthorities().stream().findFirst().get().getAuthority();
        model.addAttribute("role", role);

        return returnRole + "/index";
    }

    @GetMapping("/user")
    public String userView(Model model, Principal principal) {
        //Sets the current page 
        model.addAttribute("currentPage", "user");

        if (principal != null) {
            guest = userRepository.findByUsername(principal.getName());
            model.addAttribute("guest", guest);

            User user = (User) model.getAttribute("user");
            if (user != null) {
                checkProfilePicture(user);

                Long amount = 0L;
                if ("V".equals(user.getRole())) {
                    amount = taskRepository.countByVolunteer(user);
                } else if ("C".equals(user.getRole())) {
                    amount = taskRepository.countByClient(user);
                }
                model.addAttribute("amount", amount);

                Long userId = guest.getUserID();
                List<HistoricTask> historicTasks = null;
                if ("C".equals(user.getRole())) {
                    historicTasks = historicTaskService.getFirstFiveClientTasks(userId);
                } else if ("V".equals(user.getRole())) {
                    historicTasks = historicTaskService.getFirstFiveVolunteerTasks(userId);
                }

                List<DefaultTask> defaultTasks = new ArrayList<>();
                if (historicTasks != null) {
                    for (HistoricTask task : historicTasks) {
                        DefaultTask defaultTask = defaultTaskRepository.findById(task.getTaskDefaultID().getDefaultTaskID()).orElse(null);
                        if (defaultTask != null) {
                            defaultTasks.add(defaultTask);
                        }
                    }
                }

                model.addAttribute("taskList", historicTasks);
                model.addAttribute("defaultTaskList", defaultTasks);
            }
        }
        return returnRole + "/user";
    }

    private void checkProfilePicture(User user) {
        //Searches the profile picture path in the /public/img/ folder and sets it to the user
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
            return returnRole + "/user";
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        //Updates the data from the userDto to the user model
        userService.update(userDto, userDetails);
        model.addAttribute("guest", guest);
        model.addAttribute("message", "Updated Successfully!");
        return returnRole + "/user";
    }

    @GetMapping("/history")
    public String showTaskHistory(Model model, Principal principal) {
        Long volunteerId = user.getUserID();
        //Loads all the tasks where the client tasks part
        List<HistoricTask> clientTasks = historicTaskService.getFirstFivePastTasks(volunteerId);
        //Sets the current page 
        model.addAttribute("currentPage", "taskHistory");
        if (clientTasks == null) {
            model.addAttribute("message", "No hay tareas disponibles.");
        } else {
            List<HistoricTask> historicTasks = null;
            if ("C".equals(user.getRole())) {
                historicTasks = historicTaskService.getFirstFiveClientTasks(volunteerId);
            } else if ("V".equals(user.getRole())) {
                historicTasks = historicTaskService.getFirstFiveVolunteerTasks(volunteerId);
            }
                List<DefaultTask> defaultTasks = new ArrayList<>();
                if (historicTasks != null) {
                    for (HistoricTask task : historicTasks) {
                        DefaultTask defaultTask = defaultTaskRepository.findById(task.getTaskDefaultID().getDefaultTaskID()).orElse(null);
                        if (defaultTask != null) {
                            defaultTasks.add(defaultTask);
                        }
                    }
                }

                model.addAttribute("taskList", historicTasks);
                model.addAttribute("defaultTaskList", defaultTasks);
        }
        
        return returnRole + "/historicTaskList";
    }

                

    @GetMapping("/chat")
    public String showChat(Model model) {
        //Sets the current page 
        model.addAttribute("currentPage", "chat");
        model.addAttribute("user", user);
        return returnRole + "/chat";
    }
}
