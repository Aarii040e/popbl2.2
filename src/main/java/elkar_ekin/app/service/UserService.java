package elkar_ekin.app.service;

import org.springframework.security.core.userdetails.UserDetails;

import elkar_ekin.app.dto.UserDto;
import elkar_ekin.app.model.User;


public interface UserService {
	User save (UserDto userDto);
	User update (UserDto userDto, UserDetails userDetails);

	//HEMENDIK JARRAITU
	// void saveLocationInUser(UserDto userDto, LocationDto locationDto);
}
