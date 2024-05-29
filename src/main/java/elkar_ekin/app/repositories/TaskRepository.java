package elkar_ekin.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import elkar_ekin.app.model.Task;
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

        // List<Task> findAllByOrderByCreatedAtDesc();

}