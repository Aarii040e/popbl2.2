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

    //HORRELA!???????????????????????????????
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

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessageDto>> findChatMessages(@PathVariable String senderId, @PathVariable String recipientId) {
        List<ChatMessageDto> list = chatMessageService.findChatMessages(senderId, recipientId);
        return ResponseEntity.ok(list);
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageDto chatMessageDto) {
        ChatMessageDto savedMsg = chatMessageService.save(chatMessageDto);

        String recipientID = String.valueOf(chatMessageDto.getRecipientID());
        ChatNotificationDto notification = new ChatNotificationDto(savedMsg.getChatMessageID(), savedMsg.getSenderID(), savedMsg.getRecipientID(), savedMsg.getContent());
        
        //We send the message from the sender to the recipient
        messagingTemplate.convertAndSendToUser(recipientID, "/queue/messages", notification);        
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> findExcludedUsers() {
        List<UserDto> userList = userService.getAllUsersExcluding(user.getUserID());
        return ResponseEntity.ok(userList);
    }
    
    @GetMapping("/chat")
    public String showChat(Model model) {
		model.addAttribute("user", user);
        return "admin/chat";
    } 
}
