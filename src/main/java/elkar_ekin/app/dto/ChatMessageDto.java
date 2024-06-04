package elkar_ekin.app.dto;

public class ChatMessageDto {
    
    Long chatMessageID;
    String chatRoomID;
    Long senderID;
    Long recipientID;
    String content;

    
    public ChatMessageDto(Long chatMessageID, String chatRoomID, Long senderID, Long recipientID, String content) {
        this.chatMessageID = chatMessageID;
        this.chatRoomID = chatRoomID;
        this.senderID = senderID;
        this.recipientID = recipientID;
        this.content = content;
    }


    //Este se utiliza para crear el message antes de mandarlo desde javascript
    public ChatMessageDto(Long senderID, Long recipientID, String content) {
        this.senderID = senderID;
        this.recipientID = recipientID;
        this.content = content;
    }

    public ChatMessageDto() {
    }
    
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
