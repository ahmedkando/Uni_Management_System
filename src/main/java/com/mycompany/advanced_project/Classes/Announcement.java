
package com.mycompany.advanced_project.Classes;


public class Announcement {
    private String content;
    private String timestamp;

    public Announcement(String content) {
        this.content = content;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }

    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getInfo() {
        return "Announcement: " + content + " (Posted: " + timestamp + ")";
    }
}