package elkar_ekin.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import elkar_ekin.app.dto.TaskDto;
import elkar_ekin.app.dto.UserDto;
import elkar_ekin.app.model.Task;
import elkar_ekin.app.model.User;
import elkar_ekin.app.repositories.TaskRepository;
import elkar_ekin.app.repositories.UserRepository;

@Service
public class TaskServiceImpl implements TaskService {


	@Autowired
	private TaskRepository taskRepository;

	@Override
	public Task save(TaskDto taskDto) {
		//Pass data from UserDto to User
		Task task = new task(taskDto.getDescription(), taskDto.getEstimatedTime(), taskDto.getDate(), taskDto.getState(), taskDto.getStartTime(), taskDto.setEndTime(null),
		taskDto.getLocation(), taskDto.getCategory() , taskDto.getVolunteer() );
		return taskRepository.save(task);

		
		// User user = new User(userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()), userDto.getRole(), userDto.getName(),
		// 		userDto.getSurname1(), userDto.getSurname2(), userDto.getGender(), userDto.getBirthDate(),
		// 		userDto.getPostCode(), userDto.getDirection(), userDto.getTown(), userDto.getProvince(),
		// 		userDto.getTelephone(), userDto.getEmail(), userDto.getDescription(), userDto.getImagePath());
		// return userRepository.save(user);
	}

}
