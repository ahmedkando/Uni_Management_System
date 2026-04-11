/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.advanced_project;

/**
 *
 * @author VICTUS
 */

import java.util.*;

public class Student extends User implements Enrollable{
    private List<String> enrolledCourses;
    
    public Student(String username,String email){
        super(username, email);
        this.enrolledCourses=new ArrayList<String>();
    }
    
    public void enrollCourse(String courseId){
        if (!enrolledCourses.contains(courseId)){
            enrolledCourses.add(courseId);
        }
    }
    public void removeCourse(String courseId) {
    enrolledCourses.remove(courseId);
}

    
    public List<String>getEnrolledCourses(){
        return enrolledCourses;
    }
    
    public String getInfo(){
        return "Student ID: " +getId()+", Name: "+getUsername()+ 
               ", Email: " +getEmail()+", Courses: "+enrolledCourses.size();
    }
}