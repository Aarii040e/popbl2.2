package elkar_ekin.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import elkar_ekin.app.model.User;
import elkar_ekin.app.dto.ChatMessageDto;
import elkar_ekin.app.dto.ChatNotificationDto;
import elkar_ekin.app.dto.UserDto;
import elkar_ekin.app.repositories.UserRepository;
import elkar_ekin.app.service.ChatMessageService;
import elkar_ekin.app.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    
    User user;

    @Autowired
    private ChatMessageService chatMessageService;

    
	@Autowired
	private UserRepository repository;


    @Autowired
	UserService userService;

    
    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @ModelAttribute
	public void commonUser (Model model, Principal principal) {
		if (principal != null) {
			String username=principal.getName();
			user = repository.findByUsername(username);
			model.addAttribute("user", user);
		}
	}

    // Endpoint to fetch chat messages between a sender and recipient
    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessageDto>> findChatMessages(@PathVariable String senderId, @PathVariable String recipientId) {
        List<ChatMessageDto> list = chatMessageService.findChatMessages(senderId, recipientId);
        return ResponseEntity.ok(list);
    }

    // Handles incoming chat messages and sends them to the recipient
    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageDto chatMessageDto) {
        ChatMessageDto savedMsg = chatMessageService.save(chatMessageDto);

        String recipientID = String.valueOf(chatMessageDto.getRecipientID());
        ChatNotificationDto notification = new ChatNotificationDto(savedMsg.getChatMessageID(), savedMsg.getSenderID(), savedMsg.getRecipientID(), savedMsg.getContent());
        // Sends the notification to the recipient
        messagingTemplate.convertAndSendToUser(recipientID, "/queue/messages", notification);        
    }

    // Endpoint to fetch relevant users for chat based on the logged-in user's role
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> findRelevantUsers() {
        List<UserDto> userList = userService.getRelevantUsersForChat(user.getUserID(), user.getRole());
        return ResponseEntity.ok(userList);
    }
    
}
