package elkar_ekin.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import elkar_ekin.app.model.ChatRoom;
import elkar_ekin.app.model.User;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    
    Optional<ChatRoom> findBySenderUserIDAndRecipientUserID(Long senderID, Long recipientID);

    // Fetch all chat rooms where the user is the recipient
    List<ChatRoom> findAllByRecipient(User recipient);

    // Fetch all chat rooms where the user is the sender
    List<ChatRoom> findAllBySender(User sender);

    // Delete all chat rooms where the user is the recipient
    void deleteAllByRecipient(User recipient);

    // Delete all chat rooms where the user is the sender
    void deleteAllBySender(User sender);
}
