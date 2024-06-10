package elkar_ekin.app.dto;

public class ChatMessageDto {
    
    //Parameters
    Long chatMessageID;
    String chatRoomID;
    Long senderID;
    Long recipientID;
    String content;

    //Constructors
    public ChatMessageDto(Long chatMessageID, String chatRoomID, Long senderID, Long recipientID, String content) {
        this.chatMessageID = chatMessageID;
        this.chatRoomID = chatRoomID;
        this.senderID = senderID;
        this.recipientID = recipientID;
        this.content = content;
    }

    public ChatMessageDto(Long senderID, Long recipientID, String content) {
        this.senderID = senderID;
        this.recipientID = recipientID;
        this.content = content;
    }

    public ChatMessageDto() {
    }
    
    //Getters and Setters
    public Long getChatMessageID() {
        return chatMessageID;
    }


    public void setChatMessageID(Long chatMessageID) {
        this.chatMessageID = chatMessageID;
    }


    public String getChatRoomID() {
        return chatRoomID;
    }


    public void setChatRoomID(String chatRoomID) {
        this.chatRoomID = chatRoomID;
    }


    public Long getSenderID() {
        return senderID;
    }


    public void setSenderID(Long senderID) {
        this.senderID = senderID;
    }


    public Long getRecipientID() {
        return recipientID;
    }


    public void setRecipientID(Long recipientID) {
        this.recipientID = recipientID;
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }
    
}
