package elkar_ekin.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import elkar_ekin.app.model.User;

@Repository
public interface TaskRepository extends JpaRepository<User, Long> {

}
