package elkar_ekin.app.service;

import org.springframework.security.core.userdetails.UserDetails;

import elkar_ekin.app.dto.UserDto;
import elkar_ekin.app.model.User;


public interface UserService {
	public boolean usernameExists(String username);

	User update (UserDto userDto, UserDetails userDetails);

	public User save(UserDto userDto);

}
