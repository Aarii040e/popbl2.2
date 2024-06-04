package elkar_ekin.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import elkar_ekin.app.model.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    Optional<ChatMessage> findById(Long id);

    List<ChatMessage> findByChatRoomChatRoomID(String chatRoomID);



}
