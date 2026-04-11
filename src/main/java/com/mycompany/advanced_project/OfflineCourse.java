/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.advanced_project;
/**
 *
 * @author VICTUS
 */
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