
package com.mycompany.advanced_project.Classes;


import java.util.*;


public class Student extends User{
    private List<Course> enrolledCourses;
    
    public Student(String username,String email){
        super(username, email);
        this.enrolledCourses=new ArrayList<>();
    }
    
    public void enrollCourse(Course course){
        if (!enrolledCourses.contains(course)){
            enrolledCourses.add(course);
        }
    }
    public void removeCourse(Course course) {
    enrolledCourses.remove(course);
}

    
    public List<Course>getEnrolledCourses(){
        return enrolledCourses;
    }
    
    public String getInfo(){
        return "Student ID: " +getId()+", Name: "+getUsername()+ 
               ", Email: " +getEmail()+", Courses: "+enrolledCourses.size();
    }
}
