package elkar_ekin.app.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import elkar_ekin.app.dto.LocationDto;
import elkar_ekin.app.dto.TaskDto;
import elkar_ekin.app.dto.UserDto;
import elkar_ekin.app.model.DefaultTask;
import elkar_ekin.app.model.Location;
import elkar_ekin.app.model.User;
import elkar_ekin.app.repositories.DefaultTaskRepository;
import elkar_ekin.app.repositories.UserRepository;
import elkar_ekin.app.service.LocationService;
import elkar_ekin.app.service.TaskService;
import elkar_ekin.app.service.UserService;

@Controller
@RequestMapping("/client-view")
@SessionAttributes("userDto")
public class ClientController {

	private User client;
	private DefaultTask defaultTask;

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	private UserRepository repository;

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
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
		model.addAttribute("currentPage", "index");
		return "index";
	}

	@GetMapping("/user")
	public String clientUser(Model model) {
		model.addAttribute("currentPage", "user");
		return "user";
	}

	@PostMapping("/user/update")
	public String clientUpdateUser(@ModelAttribute("userDto") UserDto userDto, BindingResult result, Model model,
			Principal principal) {
		if (result.hasErrors()) {
			return "user";
		}
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		userService.update(userDto, userDetails);
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
			return "redirect:/client-view//createTask/step2";
		}
		return "redirect:/client-view/taskList";
	}

	// Error detection
	public boolean hasErrorsTask(TaskDto taskDto, Model model) {
		LocalTime startTime = taskDto.getStartTime();
		LocalTime endTime = taskDto.getEndTime();
		Location location = taskDto.getLocation();
		if (location.getPostCode().toString().length() != 5) {
			model.addAttribute("error", "error.wrongPostCode");
			return true;
		}
		if (endTime.isAfter(startTime)) {
			model.addAttribute("error", "error.wrongStartEndTimes");
			return true;
		}
		return false;
	}
}
