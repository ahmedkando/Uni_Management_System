package com.mycompany.advanced_project.Classes;


public abstract class Sendable {
    private String content;
    private String timestamp;
   
    public Sendable(String content, String timestamp) {
        this.content=content;
        this.timestamp=timestamp;
    }

    public String getContent(){ 
        return content; 
    }
    
    public String getTimestamp(){ 
        return timestamp; 
    }
    
    abstract public String getInfo();

}
