package elkar_ekin.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import elkar_ekin.app.model.Task;
import elkar_ekin.app.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
	//This function uses the naming convention, Do NOT change the name!
	User findByUsername (String username);
	
	boolean existsByUsername(String username);

	List<User> getUsersByRole(String role);

    User findByUserID(Long id);

	@Query("SELECT u FROM User u WHERE u.id <> :userId")
    List<User> findAllExcludingUser(@Param("userId") Long userId);

	@Query("SELECT u.savedTasks FROM User u WHERE u.username = ?1")
    List<Task> findSavedTasksByUsername(String username);

	@Query("SELECT u FROM User u WHERE u.role = 'A' OR u.userID IN (SELECT t.client.userID FROM Task t WHERE t.volunteer.userID = ?1)")
    List<User> findVolunteersContacts(Long volunteerId);

    // @Query("SELECT u FROM User u WHERE u.role = 'A' OR u.userID IN (SELECT t.volunteerID FROM Task t WHERE t.clientID = ?1)")
	@Query("SELECT u FROM User u WHERE u.role = 'A' OR u.userID IN (SELECT t.volunteer.userID FROM Task t WHERE t.client.userID = ?1)")
    List<User> findClientsContacts(Long clientId);

}
