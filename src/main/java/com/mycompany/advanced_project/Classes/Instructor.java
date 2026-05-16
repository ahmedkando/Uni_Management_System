
package com.mycompany.advanced_project.Classes;


import java.util.*;


public class Instructor extends User{
    private List<Course>teachingCourses;
    private double salary;

    
    public Instructor(String username,String email){
        super(username,email); 
        this.teachingCourses=new ArrayList<>();
        this.salary=3000.0;
    }
    
    public void assignCourse(Course course){
        if (!teachingCourses.contains(course)){
            teachingCourses.add(course);
        }
    }
    public List<Course>getTeachingCourses(){
        return teachingCourses;
    }
    
    public double calculatePayment(){
        return salary+(teachingCourses.size()*200);
    }
    
    public String getInfo(){
        return "Instructor ID: "+getId()+", Name: "+getUsername()+", Email: "+getEmail()+", Salary: $"+calculatePayment();
    }
    
}
