
package com.mycompany.advanced_project.Classes;

public class Message extends Sendable{
    private User fromUserId;
    private User toUserId;
    
    public Message(User fromUserId, User toUserId, String content) {
        super(content,java.time.LocalDateTime.now().toString());
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
    }
    
    public User getFromUserId() {
        return fromUserId;
    }
    
    public User getToUserId() {
        return toUserId;
    }
    
    public String getInfo() {
        return "From: " + fromUserId + " -> To: " + toUserId + 
               " | Message: " + getContent();
    }
}
