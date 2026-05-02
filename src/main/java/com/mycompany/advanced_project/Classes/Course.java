
package com.mycompany.advanced_project.Classes;


import java.util.*;
public abstract class Course{
    private int id;
    private String name;
    private int credits;
    private List<Student>students;
    private static int counter = 1;
    
    public Course(String name, int credits) {
        this.name=name;
        this.credits=credits;
        this.students=new ArrayList<>();
    }

    public int getId(){ 
        return id; 
    }
    
    public String getName(){ 
        return name; 
    }
    
    public int getCredits(){ 
        return credits; 
    }
    
    public void addStudent(Student student){
        if (!students.contains(student)){
            students.add(student);
        }
    }
    
    public void removeStudent(Student student){
        students.remove(student);
    }
    
    public List<Student> getStudentIds() {
        return students;
    }
    
    public abstract String getInfo();
}