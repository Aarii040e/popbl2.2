package elkar_ekin.app.service;


import elkar_ekin.app.dto.UserDto;
import elkar_ekin.app.model.User;


public interface UserService {
	User save (UserDto userDto);

}
