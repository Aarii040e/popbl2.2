package elkar_ekin.app.service;

import java.util.List;

import elkar_ekin.app.dto.TaskDto;
import elkar_ekin.app.model.Task;
import elkar_ekin.app.model.User;


public interface TaskService {

	public Task save (TaskDto taskDto);
	public List<Task> getAllTasks();

	// 	public List<Task> getAllTasks();

    public void editTask(Long taskID, TaskDto taskDto);

	public String deleteTask(Long taskID);

	public Task getTaskByID(Long taskID);

	public List<Task> getAllActiveTasks();
	public List<Task> getVolunteerTasks(User volunteer);

}
