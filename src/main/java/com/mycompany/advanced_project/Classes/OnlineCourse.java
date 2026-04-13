
package com.mycompany.advanced_project.Classes;

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