package elkar_ekin.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

}
