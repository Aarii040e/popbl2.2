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
                String chatRoomID = null;
                Optional<ChatRoom> chatRoomAux = chatRoomRepository.findBySenderUserIDAndRecipientUserID(
                                Long.parseLong(senderId), Long.parseLong(recipientId));
                if (chatRoomAux.isEmpty()) {
                        chatRoomID = createChatRoomID(senderId, recipientId);
                } else {
                        ChatRoom chatRoom = chatRoomAux.get();
                        chatRoomID = chatRoom.getChatRoomID();
                }
                return chatRoomID;
        }

        private String createChatRoomID(String senderID, String recipientID) {
                String chatRoomID = String.format("%s_%s", senderID, recipientID);
                User sender = userRepository.findByUserID(Long.parseLong(senderID));
                User recipient = userRepository.findByUserID(Long.parseLong(recipientID));
                ChatRoom senderRecipient = new ChatRoom(chatRoomID, sender, recipient);
                ChatRoom recipientSender = new ChatRoom(chatRoomID, recipient, sender);
                chatRoomRepository.save(senderRecipient);
                chatRoomRepository.save(recipientSender);
                return chatRoomID;
        }
}
