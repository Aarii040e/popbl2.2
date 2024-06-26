package elkar_ekin.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import elkar_ekin.app.model.DefaultTask;

@Repository
public interface DefaultTaskRepository extends JpaRepository<DefaultTask, Long> {

    DefaultTask findByName(String name);
}