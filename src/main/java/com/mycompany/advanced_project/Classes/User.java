
package com.mycompany.advanced_project.Classes;



import java.util.*;

public abstract class User {
    private String id;
    private String username;
    private String email;
    private List<Message> inbox;
    private static int counter = 1;
    
    public User(String username, String email) {
        this.id = generateId();
        this.username = username;
        this.email = email;
        this.inbox = new ArrayList<Message>();
    }
    
    private String generateId() {
        return "U" + counter++;
      
    }
    
    public String getId() { 
        return id; 
    }
    
    public String getUsername() { 
        return username; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public void receiveMessage(Message m) {
        inbox.add(m);
    }
    
    public List<Message> getInbox() {
        return inbox;
    }
    
    public abstract String getInfo();
}