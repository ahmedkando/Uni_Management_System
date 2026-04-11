/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.advanced_project;
/**
 *
 * @author VICTUS
 */
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