package com.mycompany.advanced_project.service;

import java.util.*;

import com.mycompany.advanced_project.Classes.*;
import com.mycompany.advanced_project.DB.*;

public class System_controller {

    private Map<Integer, User> users;
    private Map<Integer, Course> courses;
    private Map<Integer, List<Announcement>> announcements;

    public System_controller() {
        this.users = new HashMap<>();
        this.courses = new HashMap<>();
        this.announcements = new HashMap<>();
    }

    // call once at startup (after initDatabase)
    public void loadAll() {
        UserDAO.loadStudents(this);
        UserDAO.loadInstructors(this);
        CourseDAO.loadCourses(this);
        EnrollmentDAO.loadEnrollments(this);
        AnnouncementDAO.loadAnnouncements(this);
    }

    // called only by DAOs, no DB write

    public Student loadStudent(int id, String username, String email) {
        Student s = new Student(username, email);
        // s.setId(id);
        users.put(id, s);
        return s;
    }

    public Instructor loadInstructor(int id, String username, String email, double salary) {
        Instructor i = new Instructor(username, email);
        // i.setId(id);
        users.put(id, i);
        return i;
    }

    public Course loadCourse(int id, String name, int credits, String type, String detail) {
        Course c;
        if (type.equalsIgnoreCase("online")) {
            c = new OnlineCourse(name, credits, detail);
        } else {
            c = new OfflineCourse(name, credits, detail);
        }
        // c.setId(id);
        courses.put(id, c);
        return c;

    }

    public void loadAnnouncement(int courseId, String content, String timestamp) {
        if (!courses.containsKey(courseId))
            return;
        Announcement a = new Announcement(content);
        announcements.computeIfAbsent(courseId, k -> new ArrayList<>()).add(a);
    }

    /* used by GUI each method saves to DB */

    public Student addStudent(String username, String email) {
        Student s = new Student(username, email);
        int id = UserDAO.saveStudent(username, email);
        if (id == -1)
            return null;
        // s.setId(id);
        users.put(id, s);
        return s;
    }

    public Instructor addInstructor(String username, String email, double salary) {
        Instructor i = new Instructor(username, email);
        int id = UserDAO.saveInstructor(username, email, salary);
        if (id == -1)
            return null;
        // i.setId(id);
        users.put(id, i);
        return i;
    }

    public Course addCourse(String name, int credits, String type, String detail) {
        Course c;
        if (type.equalsIgnoreCase("online")) {
            c = new OnlineCourse(name, credits, detail);
        } else {
            c = new OfflineCourse(name, credits, detail);
        }
        int id = CourseDAO.saveCourse(c);
        if (id == -1)
            return null;
        // c.setId(id);
        courses.put(id, c);
        return c;

    }

    public boolean enrollStudent(int studentId, int courseId) {
        User u = users.get(studentId);
        Course c = courses.get(courseId);
        if (u instanceof Student s && c != null) {
            s.enrollCourse(c);
            c.addStudent(s);
            EnrollmentDAO.saveEnrollment(studentId, courseId);
            return true;
        }
        return false;

    }

    public boolean removeStudent(int studentId) {
        User u = users.get(studentId);

        if (!(u instanceof Student s)) {
            return false;
        }
        for (Course c : new ArrayList<>(s.getEnrolledCourses())) {
            c.removeStudent(s);
        }
        users.remove(studentId);
        UserDAO.deleteStudent(studentId);
        return true;
    }

    public boolean postAnnouncement(int courseId, String text) {
        if (!courses.containsKey(courseId))
            return false;
        Announcement a = new Announcement(text);
        announcements.computeIfAbsent(courseId, k -> new ArrayList<>()).add(a);
        AnnouncementDAO.saveAnnouncement(courseId, a);
        return true;

    }

    public List<String> getStudentFeed(int studentId) {
        List<String> feed = new ArrayList<>();
        User u = users.get(studentId);
        if (u instanceof Student s) {
            for (Course c : s.getEnrolledCourses()) {
                List<Announcement> anns = announcements.get(c.getId());
                if (anns != null) {
                    for (Announcement a : anns) {
                        feed.add("[" + c.getName() + "] " + a.getContent());
                    }
                }
            }
        }
        return feed;
    }

    public boolean sendMessage(int fromId, int toId, String content) {
        User from = users.get(fromId);
        User to = users.get(toId);
        if (from != null && to != null) {
            to.receiveMessage(new Message(from, to, content));
            return true;
        }
        return false;
    }

    public User getUserById(int id) {
        return users.get(id);
    }

    public Course getCourseById(int id) {
        return courses.get(id);
    }
    
    public Collection<User> getAllUsers() {
        return users.values();
    }

    public Collection<Course> getAllCourses() {
        return courses.values();
    }
}
