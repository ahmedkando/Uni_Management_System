
package com.mycompany.advanced_project.Classes;

public class Message {
    private User fromUserId;
    private User toUserId;
    private String content;
    private String timestamp;
    
    public Message(User fromUserId, User toUserId, String content) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.content = content;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }
    
    public User getFromUserId() {
        return fromUserId;
    }
    
    public User getToUserId() {
        return toUserId;
    }
    
    public String getContent() {
        return content;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public String getInfo() {
        return "From: " + fromUserId + " -> To: " + toUserId + 
               " | Message: " + content;
    }
}