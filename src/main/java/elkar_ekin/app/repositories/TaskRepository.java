package elkar_ekin.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import elkar_ekin.app.model.Task;
import elkar_ekin.app.model.User;
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    @SuppressWarnings("null")
    List<Task> findAll();
    
    List<Task> findTasksByState(String state);
    
    List<Task> findTasksByVolunteer(User volunteer);

    List<Task> findTasksByClient(User volunteer);

    long countByVolunteer(User volunteer);

    long countByClient(User volunteer);

    // @Query("SELECT COUNT(t) FROM Task t WHERE t.volunteer = :volunteer AND t.defaultTask.category.name = :category")
    // long countByVolunteerAndCategory(@Param("volunteer") User volunteer, @Param("category") String category);

    // List<Task> findAllByOrderByCreatedAtDesc();

}