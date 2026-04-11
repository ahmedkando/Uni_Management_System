/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.advanced_project;
/**
 *
 * @author VICTUS
 */
public class OnlineCourse extends Course {
    private String platform;
    
    public OnlineCourse(String name, int credits, String platform) {
        super(name, credits);
        this.platform = platform;
    }
    
    public String getPlatform() {
        return platform;
    }
    
    public String getInfo() {
        return "Online Course: " + getName() + " (" + getId() + "), " +
               "Credits: " + getCredits() + ", Platform: " + platform + 
               ", Students: " + getStudentIds().size();
    }
}