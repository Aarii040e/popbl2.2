package elkar_ekin.app.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "newsItems")
public class NewsItem {
	
	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Horrek primary keya automatikoki sortzen du
	private Long newsItemID;

	private String title;

	@Column(columnDefinition = "TEXT")
    private String body;
   
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID", nullable = false)
    private User user;
	
	@CreationTimestamp
    @Column(name = "createdAt", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "newsItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

	public Long getNewsItemID() {
		return newsItemID;
	}

	public NewsItem() {
	}

	public NewsItem(String title, String body, User user) {
		this.title = title;
		this.body = body;
		this.user = user;
	}

	public void setNewsItemID(Long newsItemID) {
		this.newsItemID = newsItemID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
}