package elkar_ekin.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import elkar_ekin.app.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    List<Comment> findByNewsItem_NewsItemIDOrderByCreatedAtDesc(Long newsItemID);

    @Transactional
    void deleteByNewsItem_NewsItemID(Long newsItemID);
}
