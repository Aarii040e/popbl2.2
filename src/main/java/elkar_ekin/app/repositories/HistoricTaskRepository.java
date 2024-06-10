package elkar_ekin.app.repositories;

import elkar_ekin.app.model.HistoricTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricTaskRepository extends JpaRepository<HistoricTask, Long> {
    List<HistoricTask> findByClientId(Long clientId);
    List<HistoricTask> findByVolunteerId(Long volunteerId);
}

