
package com.mycompany.advanced_project.Classes;


public class Announcement extends Sendable {

    public Announcement(String content) {
        super(content,java.time.LocalDateTime.now().toString());
    }


    public String getInfo() {
        return "Announcement: " + getContent() + " (Posted: " + getTimestamp() + ")";
    }
}
