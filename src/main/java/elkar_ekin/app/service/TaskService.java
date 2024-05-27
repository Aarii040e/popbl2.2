package elkar_ekin.app.service;

import elkar_ekin.app.dto.TaskDto;
import elkar_ekin.app.model.Task;


public interface TaskService {
	
	public Task save (TaskDto taskDto);

	//HEMENDIK JARRAITU
	// void saveLocationInUser(UserDto userDto, LocationDto locationDto);
}
