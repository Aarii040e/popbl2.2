package elkar_ekin.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import elkar_ekin.app.dto.CommentDto;
import elkar_ekin.app.dto.NewsItemDto;
import elkar_ekin.app.model.Comment;
import elkar_ekin.app.model.NewsItem;
import elkar_ekin.app.repositories.CommentRepository;
import elkar_ekin.app.repositories.NewsItemRepository;

@Service
public class NewsItemServiceImpl implements NewsItemService {

	@Autowired
	private NewsItemRepository newsItemRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Override
	public NewsItem save(NewsItemDto newsItemDto) {
		NewsItem newsItem = new NewsItem(newsItemDto.getTitle(), newsItemDto.getBody(), newsItemDto.getUser());
		return newsItemRepository.save(newsItem);
	}

	@Override
	public List<NewsItem> getAllNewsItems() {
        // List<NewsItem> newsItems = newsItemRepository.findAll();
        List<NewsItem> newsItems = newsItemRepository.findAllByOrderByCreatedAtDesc();

        // Si necesitas transformar los datos de alguna manera, puedes hacerlo aquí
        return newsItems.stream().map(item -> {
            NewsItem newsItem = new NewsItem();
            newsItem.setNewsItemID(item.getNewsItemID());
            newsItem.setTitle(item.getTitle());
            newsItem.setBody(item.getBody());
            return newsItem;
        }).collect(Collectors.toList());
    }

    @Override
    public String deleteNewsItem(Long newsItemID) {
        Optional<NewsItem> newsItemOptional = newsItemRepository.findById(newsItemID);
		NewsItem newsItem = null;
		if(newsItemOptional.isPresent()) {
			newsItem = newsItemOptional.get();
		}
		newsItemRepository.delete(newsItem);
		return "DELETED";
    }

    @Override
	public NewsItem getNewsItemByID(Long newsItemID) {
		Optional<NewsItem> newsItemOptional = newsItemRepository.findById(newsItemID);
		NewsItem newsItem = new NewsItem();
		if (newsItemOptional.isPresent()) {
            newsItem = newsItemOptional.get();
		}
		return newsItem;
	}

    @Override
    public void editNewsItem(Long newsItemID, NewsItemDto newsItemDto) {
        NewsItem existingNewsItem = newsItemRepository.findById(newsItemID).orElseThrow(() -> new IllegalArgumentException("Invalid newsItem ID"));
        existingNewsItem.setTitle(newsItemDto.getTitle());
        existingNewsItem.setBody(newsItemDto.getBody());
        newsItemRepository.save(existingNewsItem);
    }

	@Override
	public List<Comment> getAllCommentsByNewsItemId(NewsItem newsItem) {
		List<Comment> allCommentByNewsItemId = commentRepository.findByNewsItem_NewsItemIDOrderByCreatedAtDesc(newsItem.getNewsItemID());
		return allCommentByNewsItemId;
	}
	
	@Override
	public Comment saveComment(CommentDto commentDto) {
		Comment comment = new Comment();
		comment.setContent(commentDto.getContent());
		comment.setNewsItem(commentDto.getNewsItem());
		comment.setUser(commentDto.getUser());
        return commentRepository.save(comment);
    }

	@Override
	public List<NewsItem> searchNewsItems(String keyword) {
        return newsItemRepository.findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase(keyword, keyword);
    }

	// @Override
	// public boolean addComment(CommentDto commentRequest) {
		
	// }

    // @Override
	// public String editNewsItem(NewsItemDto newsItemDto) {
	// 	Optional<NewsItem> userData = newsItemRepository.findById((Long) httpSession.getAttribute(AppUtils.USER_ID));

	// 	NewsItem newsItem = new NewsItem();
	// 	newsItem.setTitle(newsItemDto.getTitle());
	// 	newsItem.setBody(newsItemDto.getBody());
	// 	newsItemRepository.save(newsItem);
	// 	return AppUtils.POST_ADDED_SUCCESS;
	// }
	

}
