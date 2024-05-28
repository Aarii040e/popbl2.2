package elkar_ekin.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import elkar_ekin.app.dto.TaskDto;
import elkar_ekin.app.model.Task;
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
	// @Override
	// public Task save(TaskDto taskDto) {
	// 	Task task = new Task(taskDto.getTaskDefaultID(), taskDto.getDescription(), taskDto.getDate(), taskDto.getState(),
	// 	 taskDto.getStartTime(), taskDto.getEndTime(), taskDto.getLocation(), taskDto.getClient());
	// 	 return taskRepository.save(task);

	// }

	


	
}
