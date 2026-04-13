
package com.mycompany.advanced_project.Classes;


import java.util.*;

import com.mycompany.advanced_project.interfaces.Enrollable;

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