package elkar_ekin.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import elkar_ekin.app.model.ChatRoom;
import elkar_ekin.app.model.User;
import elkar_ekin.app.repositories.ChatRoomRepository;
import elkar_ekin.app.repositories.UserRepository;

@Service
public class ChatRoomService {

        @Autowired
        private ChatRoomRepository chatRoomRepository;

        @Autowired
        private UserRepository userRepository;

        public String getChatRoomId(String senderId, String recipientId, boolean createNewRoomIfNotExists) {
                // Check if a chat room already exists for the given sender and recipient
                String chatRoomID = null;
                Optional<ChatRoom> chatRoomAux = chatRoomRepository.findBySenderUserIDAndRecipientUserID(
                                Long.parseLong(senderId), Long.parseLong(recipientId));
                // If chat room does not exist and creation is allowed, create a new chat room
                if (chatRoomAux.isEmpty()) {
                        chatRoomID = createChatRoomID(senderId, recipientId);
                } else {
                        // If chat room exists, get the chat room ID
                        ChatRoom chatRoom = chatRoomAux.get();
                        chatRoomID = chatRoom.getChatRoomID();
                }
                return chatRoomID;
        }

        private String createChatRoomID(String senderID, String recipientID) {
                // Create a unique chat room ID
                String chatRoomID = String.format("%s_%s", senderID, recipientID);
                // Retrieve sender and recipient user entities
                User sender = userRepository.findByUserID(Long.parseLong(senderID));
                User recipient = userRepository.findByUserID(Long.parseLong(recipientID));
                // Create chat room entities for both sender-recipient and recipient-sender
                ChatRoom senderRecipient = new ChatRoom(chatRoomID, sender, recipient);
                ChatRoom recipientSender = new ChatRoom(chatRoomID, recipient, sender);
                // Save both chat room entities in the repository
                chatRoomRepository.save(senderRecipient);
                chatRoomRepository.save(recipientSender);
                return chatRoomID;
        }
}
