package elkar_ekin.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import elkar_ekin.app.dto.ChatMessageDto;
import elkar_ekin.app.model.ChatMessage;
import elkar_ekin.app.model.ChatRoom;
import elkar_ekin.app.model.User;
import elkar_ekin.app.repositories.ChatMessageRepository;
import elkar_ekin.app.repositories.ChatRoomRepository;
import elkar_ekin.app.repositories.UserRepository;

@Service
public class ChatMessageService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ChatRoomRepository chatRoomRepository;

	@Autowired
	private ChatMessageRepository chatMessageRepository;

	@Autowired
	private ChatRoomService chatRoomService;

    public ChatMessageDto save(ChatMessageDto chatMessageDto){
        String senderID = String.valueOf(chatMessageDto.getSenderID());
        String recipientID = String.valueOf(chatMessageDto.getRecipientID());
        String chatRoomID = chatRoomService.getChatRoomId(senderID, recipientID, true);
        chatMessageDto.setChatRoomID(chatRoomID);
        
        ChatRoom chatRoom=null;
        Optional<ChatRoom> chatRoomAux = chatRoomRepository.findById(chatRoomID);
        if(!chatRoomAux.isEmpty()){
            chatRoom=chatRoomAux.get();
        }
        User sender = userRepository.findByUserID(chatMessageDto.getSenderID());
        User recipient = userRepository.findByUserID(chatMessageDto.getRecipientID());
        ChatMessage chatMessage = new ChatMessage(chatMessageDto.getChatMessageID(), chatRoom, sender, recipient, chatMessageDto.getContent());
        chatMessage = chatMessageRepository.save(chatMessage);
        chatMessageDto.setChatMessageID(chatMessage.getMessageID());
        return chatMessageDto;
    }

    public List<ChatMessageDto> findChatMessages(String senderID, String recipientID){
        //We just want to find not create a new chatRoom
        String chatRoomID = chatRoomService.getChatRoomId(senderID, recipientID, false);
        List<ChatMessage> messagesAux = chatMessageRepository.findByChatRoomChatRoomID(chatRoomID);
        ChatMessageDto message;
        List<ChatMessageDto> messageDtos = new ArrayList<>();
        for (ChatMessage aux : messagesAux) {
            message = new ChatMessageDto(aux.getMessageID(), aux.getChatRoom().getChatRoomID(), 
            aux.getSender().getUserID(), aux.getRecipient().getUserID(), aux.getContent());
            messageDtos.add(message);
        }
        return messageDtos;

    }

}
