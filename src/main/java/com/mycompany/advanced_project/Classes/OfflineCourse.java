
package com.mycompany.advanced_project.Classes;

public class OfflineCourse extends Course{
    private String classroom;
    
    public OfflineCourse(String name, int credits,String classroom){
        super(name,credits);
        this.classroom=classroom;
    }
    
    public String getClassroom(){
        return classroom;
    }
    
    public String getInfo(){
        return "Offline Course: " + getName() + " (" + getId() + "), " +
                "Credits: " + getCredits() + ", Room: " + classroom +", Students: " + getStudentIds().size();
    }
}