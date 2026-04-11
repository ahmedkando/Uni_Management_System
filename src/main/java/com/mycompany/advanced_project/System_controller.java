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

public class System_controller{

    private Map<String, User> users;
    private Map<String, Course> courses;
    private Map<String, List<Announcement>> announcements;

    public System_controller() {
        this.users = new HashMap<String, User>();
        this.courses = new HashMap<String, Course>();
        this.announcements = new HashMap<String, List<Announcement>>();
    }

    public Student addStudent(String username, String email) {
        Student s = new Student(username, email);
        users.put(s.getId(), s);
        return s;
    }

    public Instructor addInstructor(String username, String email) {
        Instructor i = new Instructor(username, email);
        users.put(i.getId(), i);
        return i;
    }

    public Course addCourse(String name, int credits, String type, String detail) {
        Course c;
        if (type.equalsIgnoreCase("online")) {
            c = new OnlineCourse(name, credits, detail);
        } else {
            c = new OfflineCourse(name, credits, detail);
        }
        courses.put(c.getId(), c);
        return c;

    }

    public boolean enrollStudent(String studentId, String courseId) {
        User u=users.get(studentId);
        Course c=courses.get(courseId);
        if (u != null && c != null && u instanceof Student) {
            Student s = (Student) u;
            s.enrollCourse(courseId);
            c.addStudent(studentId);
            return true;
        }
        return false;

    }

    public boolean removeStudent(String studentId) {
        User u=users.get(studentId);

        if (u==null || !(u instanceof Student)) {
            return false;
        }
        Student s=(Student) u;

        for (String courseId : s.getEnrolledCourses()) {
            Course c=courses.get(courseId);
            if (c!=null){
                c.removeStudent(studentId);
            }
        }

        users.remove(studentId);
        return true;
    }

    public boolean postAnnouncement(String courseId, String text) {
        if (courses.containsKey(courseId)) {
            Announcement a = new Announcement(text);
            if (!announcements.containsKey(courseId)) {
                announcements.put(courseId, new ArrayList<Announcement>());
            }
            announcements.get(courseId).add(a);
            return true;
        }
        return false;

    }

    public List<String> getStudentFeed(String studentId) {
        List<String> feed = new ArrayList<String>();
        User u = users.get(studentId);
        if (u != null && u instanceof Student) {
            Student s = (Student) u;
            for (String courseId : s.getEnrolledCourses()) {
                Course c = courses.get(courseId);
                List<Announcement> anns = announcements.get(courseId);
                if (anns != null) {
                    for (Announcement a : anns) {
                        feed.add("[" + c.getName() + "] " + a.getContent());
                    }
                }
            }
        }
        return feed;
    }

    public boolean sendMessage(String fromId, String toId, String content) {
        User from = users.get(fromId);
        User to = users.get(toId);
        if (from != null && to != null) {
            Message m = new Message(fromId, toId, content);
            to.receiveMessage(m);
            return true;
        }
        return false;
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public Collection<Course> getAllCourses() {
        return courses.values();
    }
}
