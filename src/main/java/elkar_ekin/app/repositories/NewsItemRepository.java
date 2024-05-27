package elkar_ekin.app.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import elkar_ekin.app.model.NewsItem;

@Repository
public interface NewsItemRepository extends JpaRepository<NewsItem, Long> {
    
    List<NewsItem> findAllByOrderByCreatedAtDesc();
	
    List<NewsItem> findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase(String titleKeyword, String bodyKeyword);

}
