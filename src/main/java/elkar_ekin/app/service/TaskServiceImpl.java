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
        Task existingTask = taskRepository.findById(taskID).orElseThrow(() -> new IllegalArgumentException("Invalid task ID"));
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
    public List<Task> getAllActiveTasks() { // active --> >sysdate();
        return taskRepository.findActiveTasks();
    }

    @Override
    @Transactional
    public List<Task> getVolunteerTasks(User volunteer) {      // Get tasks assigned to a volunteer
        return taskRepository.findTasksByVolunteer(volunteer);
    }
}
