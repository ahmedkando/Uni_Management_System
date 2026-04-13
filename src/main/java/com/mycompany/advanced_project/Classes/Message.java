
package com.mycompany.advanced_project.Classes;

public class Message {
    private String fromUserId;
    private String toUserId;
    private String content;
    private String timestamp;
    
    public Message(String fromUserId, String toUserId, String content) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.content = content;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }
    
    public String getFromUserId() {
        return fromUserId;
    }
    
    public String getToUserId() {
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