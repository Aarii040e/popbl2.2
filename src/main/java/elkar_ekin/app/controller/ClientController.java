package elkar_ekin.app.controller;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import elkar_ekin.app.dto.LocationDto;
import elkar_ekin.app.dto.TaskDto;
import elkar_ekin.app.dto.UserDto;
import elkar_ekin.app.model.DefaultTask;
import elkar_ekin.app.model.Location;
import elkar_ekin.app.model.NewsItem;
import elkar_ekin.app.model.Task;
import elkar_ekin.app.model.User;
import elkar_ekin.app.repositories.DefaultTaskRepository;
import elkar_ekin.app.repositories.TaskRepository;
import elkar_ekin.app.repositories.UserRepository;
import elkar_ekin.app.service.LocationService;
import elkar_ekin.app.service.NewsItemService;
import elkar_ekin.app.service.TaskService;
import elkar_ekin.app.service.UserService;

@Controller
@RequestMapping("/client-view")
@SessionAttributes("userDto")
public class ClientController {

	private User client;
	private User guest;

	private DefaultTask defaultTask;
	private TaskDto editTask;

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	NewsItemService newsItemService;

	@Autowired
	private UserRepository repository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private DefaultTaskRepository defaultTaskRepository;

	private final UserService userService;
	private final LocationService locationService;
	private final TaskService taskService;

	public ClientController(UserService userService, LocationService locationService, TaskService taskService) {
		this.userService = userService;
		this.locationService = locationService;
		this.taskService = taskService;
	}

	@ModelAttribute("userDto")
	public UserDto userDto() {
		return new UserDto();
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
	public void commonUser(Model model, Principal principal) {
		String username = principal.getName();
		client = repository.findByUsername(username);
		model.addAttribute("user", client);
	}

	@GetMapping("/index")
	public String clientIndex(Model model, Principal principal) {
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
	public String clientUser(Model model, Principal principal) {
		String admin = principal.getName();
		guest = repository.findByUsername(admin);
		model.addAttribute("guest", guest);

		model.addAttribute("currentPage", "user");
		return "user";
	}

	@PostMapping("/user/update")
	public String clientUpdateUser(@ModelAttribute("userDto") UserDto userDto, BindingResult result, Model model, Principal principal) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		userService.update(userDto, userDetails);
		model.addAttribute("guest", guest);
		model.addAttribute("message", "Updated Successfully!");
		return "user";
	}

	@GetMapping("/createTask/step1")
	public String createTask_step1(Model model) {
		model.addAttribute("currentPage", "createTask");
		return "client/createTask_1";
	}

	@PostMapping("/createTask/step1")
	public String processStep1(@ModelAttribute("defaultTask") DefaultTask task, BindingResult result, Model model,
			Principal principal) {
		defaultTask = defaultTaskRepository.findByName(task.getName());
		return "redirect:/client-view/createTask/step2";
	}

	@GetMapping("/createTask/step2")
	public String createTask_step2(Model model) {
		return "client/createTask_2";
	}

	@PostMapping("/createTask/step2")
	public String processStep2(@ModelAttribute("taskDto") TaskDto taskDto, BindingResult result, Model model,
			Principal principal,
			@RequestParam(name = "postal_code", required = true) String postal_code,
			@RequestParam(name = "town", required = true) String town,
			@RequestParam(name = "direction", required = true) String direction,
			@RequestParam(name = "province", required = true) String province,
			@RequestParam(name = "date", required = true) String strDate,
			@RequestParam(name = "startTime", required = false) String strStime,
			@RequestParam(name = "endTime", required = false) String strEtime,
			@RequestParam(name = "volunteer", required = false) String volunteerName) {

		// Save date in taskDto
		LocalDate date = LocalDate.parse(strDate);
		taskDto.setDate(date);

		// Save times in taskDto
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = null;
		LocalTime endTime = null;
		try {
			startTime = LocalTime.parse(strStime, formatter);
			endTime = LocalTime.parse(strEtime, formatter);
		} catch (DateTimeParseException e) {
		}
		taskDto.setStartTime(startTime);
		taskDto.setEndTime(endTime);

		// Save location in task
		LocationDto locationDto = new LocationDto(postal_code, direction, town, province);
		Location location = locationService.saveLocation(locationDto);
		taskDto.setLocation(location);

		// Save volunteer
		User volunteer = repository.findByUsername(volunteerName);
		if (volunteer != null) {
			taskDto.setVolunteer(volunteer);
		}
		// Save client
		taskDto.setClient(client);
		// Save defaultTask
		taskDto.setTaskDefaultID(defaultTask);

		// Set task state to ACTIVE
		taskDto.setState("active");

		// Error detection
		if (!hasErrorsTask(taskDto, model)) {
			taskService.save(taskDto);
			return "redirect:/client-view/index";
		}
		return "redirect:/client-view/createTask/step2";
	}

	@GetMapping(value = "/task/{taskID}/delete")
	public String deleteTask(@PathVariable("taskID") String taskID, Model model) {
		model.addAttribute("currentPage", "deleteTask");
		taskService.deleteTask(Long.parseLong(taskID));
		return "redirect:/client-view/tasks";
	}

	@GetMapping("/tasks")
	public String showTaskList(Model model, Principal principal) {
		List<Task> clientTasks = taskService.getTasksByUser(client);
		model.addAttribute("currentPage", "taskList");
		if (clientTasks == null) {
			model.addAttribute("message", "No hay tareas disponibles.");
		} else {
			model.addAttribute("taskList", clientTasks);
		}
		return "client/taskList";
	}

	@GetMapping({ "/task/{taskID}/edit" })
	public String showTaskForm(@PathVariable("taskID") Long taskID, Model model) {
		model.addAttribute("currentPage", "editTask");
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
				if (task.getVolunteer() != null){
					taskDto.setVolunteer(task.getVolunteer());
				}
			}
		}
		model.addAttribute("taskDto", taskDto);
		model.addAttribute("isEdit", taskID != null);
		return "client/editTask";
	}

	@PostMapping({ "/editTask" })
	public String UpdateTask(@ModelAttribute("taskDto") TaskDto taskDto, BindingResult result,
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
			@RequestParam(name = "volunteer", required = false) String volunteerName) {
		

		defaultTask = defaultTaskRepository.findByName(defaulTask);

		LocalDate date = LocalDate.parse(strDate);
		taskDto.setDate(date);

		// Save times in taskDto
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startTime = null;
		LocalTime endTime = null;
		try {
			startTime = LocalTime.parse(strStime, formatter);
			endTime = LocalTime.parse(strEtime, formatter);
		} catch (DateTimeParseException e) {
		}
		taskDto.setStartTime(startTime);
		taskDto.setEndTime(endTime);

		// Save location in task
		LocationDto locationDto = new LocationDto(postalCode, direction, town, province);
		Location location = locationService.saveLocation(locationDto);
		taskDto.setLocation(location);

		// Save volunteer
		User volunteer = repository.findByUsername(volunteerName);
		if (volunteer != null) {
			taskDto.setVolunteer(volunteer);
		}
		// Save client
		taskDto.setClient(client);
		// Save defaultTask
		taskDto.setTaskDefaultID(defaultTask);

		// Set task state to ACTIVE
		taskDto.setState(state);

		if (taskDto != null) {
			// Es una actualización
			taskService.editTask(taskDto.getTaskID(), taskDto);
			return "redirect:/client-view/tasks";
		}
		return "redirect:/client-view/task/{taskID}/edit";
	}

	// Error detection
	public boolean hasErrorsTask(TaskDto taskDto, Model model) {
		LocalDate taskDate = taskDto.getDate();
		LocalTime startTime = taskDto.getStartTime();
		LocalTime endTime = taskDto.getEndTime();
		Location location = taskDto.getLocation();
		LocalDate currentDate = LocalDate.now();
		if (location.getPostCode().toString().length() != 5) {
			model.addAttribute("error", "error.wrongPostCode");
			return true;
		}
		if (startTime.isAfter(endTime)) {
			model.addAttribute("error", "error.wrongStartEndTimes");
			return true;
		}
		if (currentDate.isAfter(taskDate)) {
			model.addAttribute("error", "error.wrongDate");
			return true;
		}
		return false;
	}
}