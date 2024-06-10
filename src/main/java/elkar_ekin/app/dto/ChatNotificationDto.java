package elkar_ekin.app.dto;


public class ChatNotificationDto {
    
    //Parameters
    private Long notificationID;
    private Long senderID;
    private Long recipientID;
    private String content;

    
    //Constructors
    public ChatNotificationDto(Long notificationID, Long senderID, Long recipientID, String content) {
        this.notificationID = notificationID;
        this.senderID = senderID;
        this.recipientID = recipientID;
        this.content = content;
    }

    public ChatNotificationDto() {
    }

    public Long getNotificationID() {
        return notificationID;
    }

    //Getters and Setters
    public void setNotificationID(Long notificationID) {
        this.notificationID = notificationID;
    }

    public Long getSenderID() {
        return senderID;
    }

    public void setSenderID(Long senderID) {
        this.senderID = senderID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getRecipientID() {
        return recipientID;
    }

    public void setRecipientID(Long recipientID) {
        this.recipientID = recipientID;
    }

}
