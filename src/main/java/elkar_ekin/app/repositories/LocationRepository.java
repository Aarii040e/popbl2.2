package elkar_ekin.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import elkar_ekin.app.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    
	
}
