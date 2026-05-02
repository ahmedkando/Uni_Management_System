
package com.mycompany.advanced_project.Classes;


import java.util.*;

import com.mycompany.advanced_project.interfaces.Enrollable;

public class Student extends User implements Enrollable{
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
    public void removeCourse(String courseId) {
    enrolledCourses.remove(courseId);
}

    
    public List<Course>getEnrolledCourses(){
        return enrolledCourses;
    }
    
    public String getInfo(){
        return "Student ID: " +getId()+", Name: "+getUsername()+ 
               ", Email: " +getEmail()+", Courses: "+enrolledCourses.size();
    }
}