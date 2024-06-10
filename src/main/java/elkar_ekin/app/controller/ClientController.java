package elkar_ekin.app.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import elkar_ekin.app.dto.LocationDto;
import elkar_ekin.app.dto.TaskDto;
import elkar_ekin.app.dto.UserDto;
import elkar_ekin.app.model.DefaultTask;
import elkar_ekin.app.model.Location;
import elkar_ekin.app.model.Task;
import elkar_ekin.app.model.User;
import elkar_ekin.app.repositories.DefaultTaskRepository;
import elkar_ekin.app.repositories.TaskRepository;
import elkar_ekin.app.repositories.UserRepository;
import elkar_ekin.app.service.HistoricTaskService;
import elkar_ekin.app.service.LocationService;
import elkar_ekin.app.service.NewsItemService;
import elkar_ekin.app.service.TaskService;
import elkar_ekin.app.service.UserService;

@Controller
@RequestMapping("/client-view")
@PreAuthorize("hasRole('CLIENT')")
public class ClientController extends BaseController {

    private User user;
    private DefaultTask defaultTask;

    @Autowired
    private UserService userService;
    
    @Autowired
    private DefaultTaskRepository defaultTaskRepository;
	@Autowired
    private TaskService taskService;
    private final LocationService locationService;

    public ClientController(UserRepository userRepository, TaskRepository taskRepository,
                            UserDetailsService userDetailsService, HistoricTaskService historicTaskService, 
                            NewsItemService newsItemService, UserService userService, 
                            LocationService locationService) {
        super(userRepository, taskRepository, userDetailsService, historicTaskService, newsItemService, userService);
        this.locationService = locationService;
    }

    @ModelAttribute("taskDto")
    public TaskDto taskDto() {
        return new TaskDto();
    }

    @ModelAttribute("locationDto")
    public LocationDto locationDto() {
        return new LocationDto();
    }

    @ModelAttribute("defaultTask")
    public DefaultTask defaultTask() {
        return new DefaultTask();
    }

    @ModelAttribute
    public void initUser(Model model, Principal principal) {
        if (user == null) {
            super.commonUser(model, principal);
            user = (User) model.getAttribute("user");
        }
    }

    @GetMapping("/createTask/step1")
    public String createTaskStep1(Model model) {
        model.addAttribute("currentPage", "createTask");
        return "client/createTask_1";
    }

    @PostMapping("/createTask/step1")
    public String processStep1(@ModelAttribute("defaultTask") DefaultTask task, BindingResult result) {
        defaultTask = defaultTaskRepository.findByName(task.getName());
        return "redirect:/client-view/createTask/step2";
    }

    @GetMapping("/createTask/step2")
    public String createTaskStep2(Model model) {
        model.addAttribute("currentPage", "createTask");
        return "client/createTask_2";
    }

    @PostMapping("/createTask/step2")
    public String processStep2(@ModelAttribute("taskDto") TaskDto taskDto, BindingResult result, Model model,
                               @RequestParam(name = "postal_code") String postalCode,
                               @RequestParam(name = "town") String town,
                               @RequestParam(name = "direction") String direction,
                               @RequestParam(name = "province") String province,
                               @RequestParam(name = "date") String strDate,
                               @RequestParam(name = "startTime", required = false) String strStime,
                               @RequestParam(name = "endTime", required = false) String strEtime,
                               @RequestParam(name = "volunteer", required = false) String volunteerName) {

        populateTaskDto(taskDto, postalCode, town, direction, province, strDate, strStime, strEtime, volunteerName);
    
        taskDto.setTaskDefaultID(defaultTask);
    
        if (!hasErrorsTask(taskDto, model)) {
            taskService.save(taskDto);
            return "redirect:/client-view/index";
        }
        return "client/createTask_2";
    }

    @GetMapping("/task/{taskID}/delete")
    public String deleteTask(@PathVariable("taskID") String taskID) {
        taskService.deleteTask(Long.parseLong(taskID));
        return "redirect:/client-view/tasks";
    }

    @GetMapping("/tasks")
    public String showTaskList(Model model, Principal principal) {
        User client = userService.findByUsername(principal.getName());
        List<Task> clientTasks = taskService.getTasksByUser(client);
        model.addAttribute("currentPage", "clientTaskList");
        if (clientTasks.isEmpty()) {
            model.addAttribute("message", "No hay tareas disponibles.");
        } else {
            model.addAttribute("taskList", clientTasks);
        }
        return "client/taskList";
    }


    @GetMapping({ "/task/{taskID}/edit" })
    public String showTaskForm(@PathVariable("taskID") Long taskID, Model model) {
        TaskDto taskDto = prepareTaskDto(taskID);
        model.addAttribute("taskDto", taskDto);
        model.addAttribute("isEdit", taskID != null);
        model.addAttribute("currentPage", "editTask");
        return "client/editTask";
    }

    @PostMapping({ "/editTask" })
    public String updateTask(@ModelAttribute("taskDto") TaskDto taskDto, BindingResult result,
                            @RequestParam(name = "task_ID", required = false) String strTaskId,
                            @RequestParam(name = "postal_code", required = true) String postalCode,
                            @RequestParam(name = "town", required = true) String town,
                            @RequestParam(name = "direction", required = true) String direction,
                            @RequestParam(name = "province", required = true) String province,
                            @RequestParam(name = "date", required = true) String strDate,
                            @RequestParam(name = "state", required = true) String state,
                            @RequestParam(name = "defaulTask", required = true) String defaulTask,
                            @RequestParam(name = "startTime", required = true) String strStime,
                            @RequestParam(name = "endTime", required = true) String strEtime,
                            @RequestParam(name = "volunteer", required = false) String volunteerName,
                            Model model) {

        defaultTask = defaultTaskRepository.findByName(defaulTask);

        populateTaskDto(taskDto, postalCode, town, direction, province, strDate, strStime, strEtime, volunteerName);
        taskDto.setState(state);

        if (!hasErrorsTask(taskDto, model)) {
            taskService.editTask(taskDto.getTaskID(), taskDto);
			return "redirect:/client-view/tasks";
		}
		return "redirect:/client-view/task/" + taskDto.getTaskID() + "/edit";
    }

    private void populateTaskDto(TaskDto taskDto, String postalCode, String town, String direction,
                                 String province, String strDate, String strStime, String strEtime,
                                 String volunteerName) {
        taskDto.setDate(parseDate(strDate));
        taskDto.setStartTime(parseTime(strStime));
        taskDto.setEndTime(parseTime(strEtime));

        LocationDto locationDto = new LocationDto(postalCode, direction, town, province);
        Location location = locationService.saveLocation(locationDto);
        taskDto.setLocation(location);

        User volunteer = userRepository.findByUsername(volunteerName);
        if (volunteer != null) {
			taskDto.setVolunteer(volunteer);
		}
        taskDto.setClient(user);
        taskDto.setTaskDefaultID(defaultTask);
    }

    private LocalDate parseDate(String strDate) {
		return LocalDate.parse(strDate);
	}

	private LocalTime parseTime(String strTime) {
        if (strTime == null || strTime.isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            return LocalTime.parse(strTime, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private TaskDto prepareTaskDto(Long taskID) {
        TaskDto taskDto = new TaskDto();
        if (taskID != null) {
            Task task = taskService.getTaskByID(taskID);
            if (task != null) {
                taskDto.setTaskDefaultID(task.getTaskDefaultID());
                taskDto.setTaskID(taskID);
                taskDto.setDescription(task.getDescription());
                taskDto.setDate(task.getDate());
                taskDto.setState(task.getState());
                taskDto.setStartTime(task.getStartTime());
                taskDto.setEndTime(task.getEndTime());
                taskDto.setLocation(task.getLocation());
                taskDto.setClient(task.getClient());
                taskDto.setVolunteer(task.getVolunteer());
            }
        }
        return taskDto;
    }

    // Error detection
    private boolean hasErrorsTask(TaskDto taskDto, Model model) {
        LocalDate taskDate = taskDto.getDate();
        LocalTime startTime = taskDto.getStartTime();
        LocalTime endTime = taskDto.getEndTime();
        Location location = taskDto.getLocation();
        LocalDate currentDate = LocalDate.now();
        LocalTime now = LocalTime.now();

        if (location.getPostCode().length() != 5) {
            model.addAttribute("error", "error.wrongPostCode");
            return true;
        }
        if (startTime != null && endTime != null && startTime.isAfter(endTime)) {
            model.addAttribute("error", "error.wrongStartEndTimes");
            return true;
        }
        if (currentDate.isAfter(taskDate)) {
            model.addAttribute("error", "error.wrongDate");
            return true;
        }
        if ((now.isAfter(endTime) || now.isAfter(startTime)) && currentDate.isEqual(taskDate)) {
            model.addAttribute("error", "error.timeAfterActual");
            return true;
        }
        return false;
    }

    @GetMapping("/ratings")
    public String showRatings(Model model) {
        model.addAttribute("currentPage", "ratings");
        List<UserDto> userList = userService.getAllUsersExcluding(user.getUserID());
        model.addAttribute("userList", userList);
        return "client/ratings";
    }
}
