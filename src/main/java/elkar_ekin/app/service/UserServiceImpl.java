package elkar_ekin.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import elkar_ekin.app.dto.UserDto;
import elkar_ekin.app.model.User;
import elkar_ekin.app.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserRepository repository;

	@Override
	public User save(UserDto userDto) {
		User user = new User(userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()), userDto.getRole(), userDto.getName(),
				userDto.getSurname1(), userDto.getSurname2(), userDto.getGender(), userDto.getBirthDate(),userDto.getLocation(),userDto.getTelephone(), userDto.getEmail(), userDto.getDescription(), userDto.getImagePath());
		return userRepository.save(user);
	}
	
	@Override
	public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

	@Override
	public User update(UserDto userDto, UserDetails userDetails) {
		User user = userRepository.findByUsername(userDetails.getUsername());
		user.setDescription(userDto.getDescription());
		return userRepository.save(user);
	}
}
