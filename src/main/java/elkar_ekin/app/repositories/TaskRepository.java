package elkar_ekin.app.repositories;

import java.time.LocalTime; // Add this import statement

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import elkar_ekin.app.model.Task;
import elkar_ekin.app.model.User;
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    List<Task> findAll();
    
    @Query("SELECT t FROM Task t WHERE t.state = 'active' OR t.state IS NULL")
    List<Task> findActiveTasks();

    
    List<Task> findTasksByVolunteer(User volunteer);

    List<Task> findTasksByClient(User volunteer);

    @Query("SELECT t FROM Task t WHERE (t.state = 'active' OR t.state IS NULL) AND (:client IS NULL OR t.client = :client) AND t.endTime > :currentTime")
    List<Task> findTasksByClientAndCurrentTimeAfter(@Param("client") User client, @Param("currentTime") LocalTime currentTime);
    
    
    long countByVolunteer(User volunteer);

    long countByClient(User volunteer);
  
    @Transactional
    void deleteByClient_UserID(Long clientId);
}