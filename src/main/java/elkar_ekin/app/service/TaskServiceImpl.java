// package elkar_ekin.app.service;

// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.time.LocalTime;
// import java.util.List;
// import java.util.Optional;
// import java.util.stream.Collectors;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import elkar_ekin.app.dto.TaskDto;
// import elkar_ekin.app.model.Task;
// import elkar_ekin.app.model.User;
// import elkar_ekin.app.repositories.TaskRepository;
// import elkar_ekin.app.repositories.UserRepository;

// @Service
// public class TaskServiceImpl implements TaskService {

// 	@Autowired
// 	private TaskRepository taskRepository;
// 	@Autowired
// 	private UserRepository userRepository;

// 	@Override
// 	public Task save(TaskDto taskDto) {
// 		// Fetch the client from the repository
// 		User client = userRepository.findById(taskDto.getClient().getUserID())
//         .orElseThrow(() -> new IllegalArgumentException("Invalid client ID: " + taskDto.getClient().getUserID()));
    
//     // Fetch the volunteer from the repository if applicable
//     User volunteer = null;
//     if (taskDto.getVolunteer() != null) {
//         volunteer = userRepository.findById(taskDto.getVolunteer().getUserID())
//             .orElseThrow(() -> new IllegalArgumentException("Invalid volunteer ID: " + taskDto.getVolunteer().getUserID()));
//     }

//     // Create the Task entity
//     Task task = new Task(taskDto.getTaskDefaultID(), taskDto.getDescription(), taskDto.getDate(),
//         taskDto.getState(), taskDto.getStartTime(), taskDto.getEndTime(), taskDto.getLocation(), client, volunteer);
    
//     // Save and return the task
//     return taskRepository.save(task);
// }

// 	@Override
// 	public List<Task> getAllTasks() {
//         return taskRepository.findAll();
// 	}
	
// 	@Override
//     public List<Task> getTasksByUser(User client, LocalDateTime dateTime) {
//         LocalTime time = dateTime.toLocalTime();
//         return taskRepository.findTasksByClientAndCurrentTimeAfter(client, time);
//     }


// 	@Override
// 	public String deleteTask(Long taskID) {
// 		Optional<Task> taskOptional = taskRepository.findById(taskID);
// 		Task task = null;
// 		if (taskOptional.isPresent()) {
// 			task = taskOptional.get();
// 		}
// 		taskRepository.delete(task);
// 		return "DELETED";
// 	}

// 	@Override
// 	public Task getTaskByID(Long taskID) {
// 		Optional<Task> taskOptional = taskRepository.findById(taskID);
// 		Task task = new Task();
// 		if (taskOptional.isPresent()) {
// 			task = taskOptional.get();
// 		}
// 		return task;
// 	}

// 	@Override
// 	public void editTask(Long taskID, TaskDto taskDto) {
// 		Task existingTask = taskRepository.findById(taskID)
// 				.orElseThrow(() -> new IllegalArgumentException("Invalid task ID"));
// 		existingTask.setLocation(taskDto.getLocation());
// 		existingTask.setDate(taskDto.getDate());
// 		existingTask.setDescription(taskDto.getDescription());
// 		existingTask.setStartTime(taskDto.getStartTime());
// 		existingTask.setEndTime(taskDto.getEndTime());
// 		existingTask.setVolunteer(taskDto.getVolunteer());
// 		taskRepository.save(existingTask);
//     }
	
// 	@Override
// public List<Task> getAllActiveTasks(LocalDateTime dateTime) {
//     	LocalTime time = dateTime.toLocalTime();
//         return taskRepository.findTasksByClientAndCurrentTimeAfter(null, time);
//     }

	
// 	public List<Task> getAllPastTasks(User volunteer) {
// 		LocalDateTime currentDate = LocalDateTime.now();
//         List<Task> tasks = taskRepository.findTasksByClientAndCurrentTimeAfter(volunteer, currentDate.toLocalTime());

//         // Si necesitas transformar los datos de alguna manera, puedes hacerlo aquí
// 		return tasks.stream().filter(item -> {
// 			LocalDate date = item.getDate();
// 				LocalTime endTime = item.getEndTime();
// 				LocalDateTime taskDateTime = LocalDateTime.of(date, endTime);
// 				return taskDateTime.isBefore(currentDate);
// 		}).map(item -> {
//             Task task = new Task();
// 			task.setTaskID(item.getTaskID());
//             task.setDescription(item.getDescription());
//             task.setDate(item.getDate());
//             task.setStartTime(item.getStartTime());
// 			task.setEndTime(item.getEndTime());
// 			task.setState(item.getState());
// 			task.setLocation(item.getLocation());
// 			task.setTaskDefaultID(item.getTaskDefaultID());
// 			return task;
//         }).collect(Collectors.toList());
// 	}

// 	@Override
// 	public List<Task> getFirstFivePastTasks(User volunteer) {
// 		LocalDateTime currentDate = LocalDateTime.now();
//         List<Task> tasks = taskRepository.findTasksByClientAndCurrentTimeAfter(volunteer, currentDate.toLocalTime());
        
//         // Si necesitas transformar los datos de alguna manera, puedes hacerlo aquí
// 		return tasks.stream().filter(item -> {
// 			LocalDate date = item.getDate();
// 			LocalTime endTime = item.getEndTime();
// 			LocalDateTime taskDateTime = LocalDateTime.of(date, endTime);
// 			return taskDateTime.isBefore(currentDate);
// 		})
//         .limit(5).map(item -> {
//             Task task = new Task();
// 			task.setTaskID(item.getTaskID());
//             task.setDescription(item.getDescription());
//             task.setDate(item.getDate());
//             task.setStartTime(item.getStartTime());
// 			task.setEndTime(item.getEndTime());
// 			task.setState(item.getState());
// 			task.setLocation(item.getLocation());
// 			task.setTaskDefaultID(item.getTaskDefaultID());
// 			return task;
//         }).collect(Collectors.toList());
//     }

// 	@Override
// 	public List<Task> getVolunteerTasks(User volunteer) {
//         return taskRepository.findTasksByVolunteer(volunteer);
// 	}

// 	@Override
// 	public List<Task> getPastVolunteerTasks(User volunteer) {
// 		LocalDateTime currentDate = LocalDateTime.now();
// 		return taskRepository.findTasksByVolunteer(volunteer).stream()
// 			.filter(item -> LocalDateTime.of(item.getDate(), item.getEndTime()).isBefore(currentDate))
// 			.collect(Collectors.toList());
// 	}

// 	@Override
// 	public List<Task> getFirstFiveVolunteerTasks(User volunteer) {
// 		LocalDateTime currentDate = LocalDateTime.now();
// 		return taskRepository.findTasksByVolunteer(volunteer).stream()
// 			.filter(item -> LocalDateTime.of(item.getDate(), item.getEndTime()).isBefore(currentDate))
// 			.limit(5)
// 			.collect(Collectors.toList());
// 	}

// }
package elkar_ekin.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import elkar_ekin.app.dto.TaskDto;
import elkar_ekin.app.model.Task;
import elkar_ekin.app.model.User;
import elkar_ekin.app.repositories.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    @Transactional
    public Task save(TaskDto taskDto) {
        Task task = new Task(
            taskDto.getTaskDefaultID(), taskDto.getDescription(), taskDto.getDate(),
            taskDto.getState(), taskDto.getStartTime(), taskDto.getEndTime(),
            taskDto.getLocation(), taskDto.getClient(), taskDto.getVolunteer()
        );
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    @Transactional
    public List<Task> getTasksByUser(User user) {
        LocalDateTime currentDate = LocalDateTime.now();
        return taskRepository.findTasksByClient(user).stream()
            .filter(task -> LocalDateTime.of(task.getDate(), task.getEndTime()).isAfter(currentDate))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String deleteTask(Long taskID) {
        Optional<Task> taskOptional = taskRepository.findById(taskID);
        taskOptional.ifPresent(taskRepository::delete);
        return "DELETED";
    }

    @Override
    @Transactional
    public Task getTaskByID(Long taskID) {
        return taskRepository.findById(taskID).orElse(null);
    }

    @Override
    @Transactional
    public void editTask(Long taskID, TaskDto taskDto) {
        Task existingTask = taskRepository.findById(taskID)
            .orElseThrow(() -> new IllegalArgumentException("Invalid task ID"));
        existingTask.setLocation(taskDto.getLocation());
        existingTask.setDate(taskDto.getDate());
        existingTask.setDescription(taskDto.getDescription());
        existingTask.setStartTime(taskDto.getStartTime());
        existingTask.setEndTime(taskDto.getEndTime());
        existingTask.setVolunteer(taskDto.getVolunteer());
        taskRepository.save(existingTask);
    }

    @Override
    @Transactional
    public List<Task> getAllActiveTasks() {
        return taskRepository.findActiveTasks();
    }

    @Override
    @Transactional
    public List<Task> getVolunteerTasks(User volunteer) {
        return taskRepository.findTasksByVolunteer(volunteer);
    }
}
