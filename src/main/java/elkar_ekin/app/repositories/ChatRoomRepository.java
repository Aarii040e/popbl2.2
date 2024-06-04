package elkar_ekin.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import elkar_ekin.app.model.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    
    Optional<ChatRoom> findBySenderUserIDAndRecipientUserID(Long senderID, Long recipientID);

}
