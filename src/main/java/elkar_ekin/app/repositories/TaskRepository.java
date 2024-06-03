package elkar_ekin.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import elkar_ekin.app.model.Task;
import elkar_ekin.app.model.User;
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    @SuppressWarnings("null")
    List<Task> findAll();
    
    List<Task> findTasksByState(String state);
    
    List<Task> findTasksByVolunteer(User volunteer);

    List<Task> findTasksByClient(User volunteer);

    @Transactional
    void deleteByClient_UserID(Long clientId);

        // List<Task> findAllByOrderByCreatedAtDesc();

}