
package com.mycompany.advanced_project.Classes;


import java.util.*;

import com.mycompany.advanced_project.interfaces.Payable;

public class Instructor extends User implements Payable{
    private List<String>teachingCourses;
    private double salary;
    
    public Instructor(String username,String email){
        super(username,email); 
        this.teachingCourses=new ArrayList<>();
        this.salary=3000.0;
    }
    
    public void assignCourse(String courseId){
        if (!teachingCourses.contains(courseId)){
            teachingCourses.add(courseId);
        }
    }
    
    public List<String>getTeachingCourses(){
        return teachingCourses;
    }
    
    public double calculatePayment(){
        return salary+(teachingCourses.size()*200);
    }
    
    public String getInfo(){
        return "Instructor ID: "+getId()+", Name: "+getUsername()+", Email: "+getEmail()+", Salary: $"+calculatePayment();
    }
    
}
