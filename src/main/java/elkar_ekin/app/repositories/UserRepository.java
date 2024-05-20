package elkar_ekin.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import elkar_ekin.app.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
	//This function uses the naming convention, Do NOT change the name!
	User findByUsername (String username);
}
