package elkar_ekin.app.service;

import java.util.List;

import elkar_ekin.app.dto.TaskDto;
import elkar_ekin.app.model.Task;


public interface TaskService {

	public Task save (TaskDto taskDto);
	public List<Task> getAllTasks();
	public Task getTaskByID(Long id);
}
