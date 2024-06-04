package elkar_ekin.app.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import elkar_ekin.app.dto.CommentDto;
import elkar_ekin.app.dto.NewsItemDto;
import elkar_ekin.app.model.Comment;
import elkar_ekin.app.model.NewsItem;


public interface NewsItemService {
	
	public NewsItem save (NewsItemDto NewsItemDto);

	public List<NewsItem> getAllNewsItems();

	public List<NewsItem> getLastFiveNewsItems();

    public void editNewsItem(Long newsItemID, NewsItemDto newsItemDto);

	@Transactional
	public String deleteNewsItem(Long newsItemID);

	public NewsItem getNewsItemByID(Long newsItemID);

	public List<Comment> getAllCommentsByNewsItemId(NewsItem newsItem);

	public Comment saveComment(CommentDto comment);

	public List<NewsItem> searchNewsItems(String keyword);
}
