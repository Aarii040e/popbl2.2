package elkar_ekin.app.dto;

import elkar_ekin.app.model.NewsItem;
import elkar_ekin.app.model.User;

public class CommentDto {

    private String content;
    private User user;
    private NewsItem newsItem;
	
	public CommentDto() {
	}

	public CommentDto(String content, User user, NewsItem newsItem) {
		this.content = content;
		this.user = user;
		this.newsItem = newsItem;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public NewsItem getNewsItem() {
		return newsItem;
	}

	public void setNewsItem(NewsItem newsItem) {
		this.newsItem = newsItem;
	}

	
	
}