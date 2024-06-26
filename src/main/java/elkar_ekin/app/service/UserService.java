package elkar_ekin.app.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import elkar_ekin.app.dto.UserDto;
import elkar_ekin.app.model.Task;
import elkar_ekin.app.model.User;


public interface UserService {
	public boolean usernameExists(String username);

	User update (UserDto userDto, UserDetails userDetails);

	public User save(UserDto userDto);

	public List<UserDto> getAllUsersExcluding(Long userId);

	public List<Task> getSavedTasksByUser(User user);

	public User findByUsername(String username);

	List<UserDto> getRelevantUsersForChat(Long userId, String role);
	
	public void deleteUser(Long clientId);
}
