package elkar_ekin.app.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "chatRooms")
public class ChatRoom {

    @Id
    @Column(nullable = false, updatable = false)
    private String chatRoomID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "senderID", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipientID", nullable = false)
    private User recipient;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatMessage> messages = new HashSet<>();


    public ChatRoom() {
    }

    public ChatRoom(String chatRoomID, User sender, User recipient) {
        this.chatRoomID = chatRoomID;
        this.sender = sender;
        this.recipient = recipient;
    }

    /* Getters and setters */
    

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

    public String getChatRoomID() {
        return chatRoomID;
    }

    public void setChatRoomID(String chatRoomID) {
        this.chatRoomID = chatRoomID;
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        message.setChatRoom(this);
    }

    public void removeMessage(ChatMessage message) {
        messages.remove(message);
        message.setChatRoom(null);
    }

    public void clearMessages() {
        for (ChatMessage message : new HashSet<>(messages)) {
            removeMessage(message);
        }
    }
}
