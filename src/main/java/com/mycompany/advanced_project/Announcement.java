/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.advanced_project;

/**
 *
 * @author VICTUS
 */
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