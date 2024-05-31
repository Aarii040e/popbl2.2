package elkar_ekin.app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import elkar_ekin.app.dto.TaskDto;
import elkar_ekin.app.model.Task;
import elkar_ekin.app.model.User;
import elkar_ekin.app.repositories.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	private TaskRepository taskRepository;

	@Override
	public Task save(TaskDto taskDto) {
		Task task = new Task(taskDto.getTaskDefaultID(), taskDto.getDescription(), taskDto.getDate(), taskDto.getState(),
		 taskDto.getStartTime(), taskDto.getEndTime(), taskDto.getLocation(), taskDto.getClient(), taskDto.getVolunteer());
		 return taskRepository.save(task);

	}

	@Override
	public List<Task> getAllTasks() {
		// List<NewsItem> newsItems = newsItemRepository.findAll();
        List<Task> tasks = taskRepository.findAll();

        // Si necesitas transformar los datos de alguna manera, puedes hacerlo aquí
        return tasks.stream().map(item -> {
            Task task = new Task();
			task.setTaskID(item.getTaskID());
            task.setDescription(item.getDescription());
            task.setDate(item.getDate());
            task.setStartTime(item.getStartTime());
			task.setEndTime(item.getEndTime());
			task.setState(item.getState());
			task.setLocation(item.getLocation());
			task.setTaskDefaultID(item.getTaskDefaultID());
            return task;
        }).collect(Collectors.toList());
	}
	
	@Override
    public String deleteTask(Long taskID) {
        Optional<Task> taskOptional = taskRepository.findById(taskID);
		Task task = null;
		if(taskOptional.isPresent()) {
			task = taskOptional.get();
		}
		taskRepository.delete(task);
		return "DELETED";
    }

    @Override
	public Task getTaskByID(Long taskID) {
		Optional<Task> taskOptional = taskRepository.findById(taskID);
		Task task = new Task();
		if (taskOptional.isPresent()) {
            task = taskOptional.get();
		}
		return task;
	}

    @Override
    public void editTask(Long taskID, TaskDto taskDto) {
        Task existingTask = taskRepository.findById(taskID).orElseThrow(() -> new IllegalArgumentException("Invalid task ID"));
        existingTask.setLocation(taskDto.getLocation());
        existingTask.setDate(taskDto.getDate());
		existingTask.setDescription(taskDto.getDescription());
		existingTask.setStartTime(taskDto.getStartTime());
		existingTask.setEndTime(taskDto.getEndTime());
        taskRepository.save(existingTask);
    }
	@Override
	public List<Task> getAllActiveTasks() {
		// List<NewsItem> newsItems = newsItemRepository.findAll();
        List<Task> tasks = taskRepository.findAll();

        // Si necesitas transformar los datos de alguna manera, puedes hacerlo aquí
        return tasks.stream().map(item -> {
            Task task = new Task();
			task.setTaskID(item.getTaskID());
            task.setDescription(item.getDescription());
            task.setDate(item.getDate());
            task.setStartTime(item.getStartTime());
			task.setEndTime(item.getEndTime());
			task.setState(item.getState());
			task.setLocation(item.getLocation());
			task.setTaskDefaultID(item.getTaskDefaultID());
			
            return task;
        }).collect(Collectors.toList());
	}

	@Override
	public List<Task> getVolunteerTasks(User volunteer) {
		// List<NewsItem> newsItems = newsItemRepository.findAll();
        List<Task> tasks = taskRepository.findAll();

        // Si necesitas transformar los datos de alguna manera, puedes hacerlo aquí
        return tasks.stream().map(item -> {
            Task task = new Task();
			task.setTaskID(item.getTaskID());
            task.setDescription(item.getDescription());
            task.setDate(item.getDate());
            task.setStartTime(item.getStartTime());
			task.setEndTime(item.getEndTime());
			task.setState(item.getState());
			task.setLocation(item.getLocation());
			task.setTaskDefaultID(item.getTaskDefaultID());
            return task;
        }).collect(Collectors.toList());
	}


	
}
