
package com.mycompany.advanced_project.Classes;


import java.util.*;
public abstract class Course{
    private String id;
    private String name;
    private int credits;
    private List<String>studentIds;
    private static int counter = 1;
    
    public Course(String name, int credits) {
        this.id=generateId();
        this.name=name;
        this.credits=credits;
        this.studentIds=new ArrayList<String>();
    }
    
    private String generateId(){ 
        return "C" + counter++;
    }
    
    public String getId(){ 
        return id; 
    }
    
    public String getName(){ 
        return name; 
    }
    
    public int getCredits(){ 
        return credits; 
    }
    
    public void addStudent(String studentId){
        if (!studentIds.contains(studentId)){
            studentIds.add(studentId);
        }
    }
    
    public void removeStudent(String studentId){
        studentIds.remove(studentId);
    }
    
    public List<String> getStudentIds() {
        return studentIds;
    }
    
    public abstract String getInfo();
}