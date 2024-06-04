package elkar_ekin.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "notifications")
public class ChatNotification {

    @Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Horrek primary keya automatikoki sortzen du
    private Long notificationID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senderID", nullable = false)
    private User sender;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipientID", nullable = false)
    private User recipient;

    @Column(columnDefinition = "TEXT")
    private String content;


    /*Constructors */
    
    public ChatNotification() {
    }
    
    public ChatNotification(Long notificationID, User sender, User recipient, String content) {
        this.notificationID = notificationID;
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
    }
    /*Getters y setters */


    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(Long notificationID) {
        this.notificationID = notificationID;
    }

}