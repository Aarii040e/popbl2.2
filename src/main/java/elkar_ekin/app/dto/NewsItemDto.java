package elkar_ekin.app.dto;

import elkar_ekin.app.model.User;

public class NewsItemDto {
	
    //Parameters
	private Long newsItemID;
	private String title;
    private String body;
    private User user;

    //Constructors
	public NewsItemDto(){
	}
	
	public String getTitle() {
		return title;
	}
	public NewsItemDto(String title, String body, User user) {
		this.title = title;
		this.body = body;
		this.user = user;
	}

	public NewsItemDto(Long newsItemID, String title, String body, User user) {
		this.newsItemID = newsItemID;
		this.title = title;
		this.body = body;
		this.user = user;
	}

	public NewsItemDto(Long newsItemID2, User user2, String title2, String body2) {
        //TODO Auto-generated constructor stub
    }

    //Getters and Setters
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

	public Long getNewsItemID() {
		return newsItemID;
	}

	public void setNewsItemID(Long newsItemID) {
		this.newsItemID = newsItemID;
	}




}