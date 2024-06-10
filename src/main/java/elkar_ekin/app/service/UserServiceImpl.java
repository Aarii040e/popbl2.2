package elkar_ekin.app.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import elkar_ekin.app.dto.UserDto;
import elkar_ekin.app.model.ChatRoom;
import elkar_ekin.app.model.Task;
import elkar_ekin.app.model.User;
import elkar_ekin.app.repositories.ChatRoomRepository;
import elkar_ekin.app.repositories.TaskRepository;
import elkar_ekin.app.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private TaskRepository taskRepository;

	@Override
	public User save(UserDto userDto) {
		User user = new User(userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()), userDto.getRole(), userDto.getName(),
				userDto.getSurname1(), userDto.getSurname2(), userDto.getGender(), userDto.getBirthDate(),userDto.getLocation(),userDto.getTelephone(), userDto.getEmail(), userDto.getDescription(), userDto.getImageFile().getOriginalFilename());
		return userRepository.save(user);
	}
	
	@Override
	public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

	@Override
	public User update(UserDto userDto, UserDetails userDetails) {
		User user = userRepository.findByUsername(userDetails.getUsername());

		MultipartFile image = userDto.getImageFile();
        try {
			if (image != null) {
				String uploadDir = "public/img/";
				Path uploadPath = Paths.get(uploadDir);
				if(!Files.exists(uploadPath)){
					Files.createDirectories(uploadPath);
				}
				try (InputStream inputStream = image.getInputStream()){
					Files.copy(inputStream, Paths.get(uploadDir + image.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
				}
			}
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

		user.setDescription(userDto.getDescription());
		System.out.println(userDto.getImageFile().getOriginalFilename());
		if (userDto.getImageFile().getOriginalFilename() != "") user.setImagePath(userDto.getImageFile().getOriginalFilename());
		return userRepository.save(user);
	}

	@Override
	public List<UserDto> getAllUsersExcluding(Long userId) {
		List<User> userList = userRepository.findAllExcludingUser(userId);
		List<UserDto> userDtos= new ArrayList<>();
		UserDto aux;

		for (User user : userList) {
			aux = new UserDto(user.getUserID(),user.getName(),user.getSurname1(),user.getSurname2(),user.getUsername(),user.getPassword(),user.getRole(),
			user.getGender(), user.getBirthDate(), user.getLocation(), user.getTelephone(), user.getEmail(), user.getDescription());
			userDtos.add(aux);
		}		
		return userDtos;
	}

	@Override
	public List<Task> getSavedTasksByUser(User user) {
        return userRepository.findSavedTasksByUsername(user.getUsername());
    }

	@Override
	public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<UserDto> getRelevantUsersForChat(Long userId, String role) {
        List<User> relevantUsers;

        switch (role) {
            case "A":
                relevantUsers = userRepository.findAll(); // Admins see all users
                break;
            case "V":
                relevantUsers = userRepository.findVolunteersContacts(userId); // Custom query to get clients and admins related to the volunteer
                break;
            case "C":
                relevantUsers = userRepository.findClientsContacts(userId); // Custom query to get volunteers and admins related to the client
                break;
            default:
                relevantUsers = List.of();
                break;
        }

        return relevantUsers.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setUserID(user.getUserID());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }

	@Transactional
    public void deleteUser(Long clientId) {
        Optional<User> userOptional = userRepository.findById(clientId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Fetch all related chat rooms to manage references
            List<ChatRoom> chatRoomsAsRecipient = chatRoomRepository.findAllByRecipient(user);
            List<ChatRoom> chatRoomsAsSender = chatRoomRepository.findAllBySender(user);

            // Clear messages in chat rooms
            for (ChatRoom chatRoom : chatRoomsAsRecipient) {
                chatRoom.clearMessages();
            }
            for (ChatRoom chatRoom : chatRoomsAsSender) {
                chatRoom.clearMessages();
            }

            // Save changes to detach references
            chatRoomRepository.saveAll(chatRoomsAsRecipient);
            chatRoomRepository.saveAll(chatRoomsAsSender);

            // Delete chat rooms after clearing messages
            chatRoomRepository.deleteAll(chatRoomsAsRecipient);
            chatRoomRepository.deleteAll(chatRoomsAsSender);

            // Delete related tasks
            taskRepository.deleteByClient_UserID(clientId);

            // Delete user image if exists
            Path imagePath = Paths.get("public/img", user.getImagePath());
            try {
                Files.delete(imagePath);
            } catch (Exception e) {
                System.out.println("Failed to delete image: " + e.getMessage());
            }

            // Finally, delete the user
            userRepository.delete(user);
        } else {
            throw new EntityNotFoundException("User not found with ID: " + clientId);
        }
    }

	
}
