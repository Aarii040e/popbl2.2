package elkar_ekin.app.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "comments")
public class Comment {
	
	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Horrek primary keya automatikoki sortzen du
	private Long commentID;

	@Column(columnDefinition = "TEXT")
    private String content;
   
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID", nullable = false)
    private User user;

	@CreationTimestamp
    @Column(name = "createdAt", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createdAt;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "newsItemID", nullable = false)
    private NewsItem newsItem;


	public Comment() {
	}


	public Comment(String content, User user, NewsItem newsItem) {
		this.content = content;
		this.user = user;
		this.newsItem = newsItem;
	}


	public Long getCommentID() {
		return commentID;
	}


	public void setCommentID(Long commentID) {
		this.commentID = commentID;
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