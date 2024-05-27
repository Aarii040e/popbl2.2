package elkar_ekin.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import elkar_ekin.app.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
    
    //This function uses the naming convention, Do NOT change the name!
    Category findByName (String name);
}
