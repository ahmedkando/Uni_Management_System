package com.mycompany.advanced_project.service;

import java.util.*;

import com.mycompany.advanced_project.Classes.*;
import com.mycompany.advanced_project.DB.*;
import com.mycompany.advanced_project.exceptions.EnrollementException;
import com.mycompany.advanced_project.exceptions.InvalIdUserException;
import com.mycompany.advanced_project.exceptions.InvalidCourseException;
import com.mycompany.advanced_project.exceptions.UserNotFoundException;

import javafx.concurrent.Task;
import com.mycompany.advanced_project.service.AsyncCallBack;

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
        s.setId(id);
        users.put(id, s);
        return s;
    }

    public Instructor loadInstructor(int id, String username, String email, double salary) {
        Instructor i = new Instructor(username, email);
        i.setId(id);
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
        c.setId(id);
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

    public Student addStudent(String username, String email)
            throws InvalIdUserException {

        if (username == null || username.isBlank())
            throw new InvalIdUserException("Username cannot be empty.");
        if (email == null || !email.contains("@"))
            throw new InvalIdUserException("Invalid email address.");

        Student s = new Student(username, email);
        int id = UserDAO.saveStudent(username, email); // now throws RuntimeException on SQL fail
        if (id == -1)
            throw new InvalIdUserException("Failed to save student to database.");
        s.setId(id);
        users.put(id, s);
        return s;
    }

    public void addStudentAsync(String username, String email, AsyncCallBack<Student> callback) {
        Task<Student> task = new Task<>() {
            @Override
            protected Student call() throws Exception {
                return addStudent(username, email);
            }
        };
        task.setOnSucceeded(e -> callback.onSuccess(task.getValue()));
        task.setOnFailed(e -> callback.onFailure(task.getException()));
        new Thread(task).start();
    }

    public Instructor addInstructor(String username, String email, double salary)
            throws InvalIdUserException {

        if (username == null || username.isBlank())
            throw new InvalIdUserException("Username cannot be empty.");
        if (email == null || !email.contains("@"))
            throw new InvalIdUserException("Invalid email address.");
        if (salary < 0)
            throw new InvalIdUserException("Salary cannot be negative.");

        Instructor i = new Instructor(username, email);
        int id = UserDAO.saveInstructor(username, email, salary);
        if (id == -1)
            throw new InvalIdUserException("Failed to save instructor to database.");
        i.setId(id);
        users.put(id, i);
        return i;
    }

    public void addInstructorAsync(String username, String email, double salary, AsyncCallBack<Instructor> callback) {
        Task<Instructor> task = new Task<>() {
            @Override
            protected Instructor call() throws Exception {
                return addInstructor(username, email, salary);
            }
        };
        task.setOnSucceeded(e -> callback.onSuccess(task.getValue()));
        task.setOnFailed(e -> callback.onFailure(task.getException()));
        new Thread(task).start();
    }

    public Course addCourse(String name, int credits, String type, String detail)
            throws InvalidCourseException {

        if (name == null || name.isBlank())
            throw new InvalidCourseException("Course name cannot be empty.");
        if (credits <= 0)
            throw new InvalidCourseException("Credits must be a positive number.");
        if (!type.equalsIgnoreCase("online") && !type.equalsIgnoreCase("offline"))
            throw new InvalidCourseException("Type must be 'online' or 'offline'.");
        if (detail == null || detail.isBlank())
            throw new InvalidCourseException("Platform or room cannot be empty.");

        Course c = type.equalsIgnoreCase("online")
                ? new OnlineCourse(name, credits, detail)
                : new OfflineCourse(name, credits, detail);

        int id = CourseDAO.saveCourse(c);
        if (id == -1)
            throw new InvalidCourseException("Failed to save course to database.");
        c.setId(id);
        courses.put(id, c);
        return c;
    }

    public void addCourseAsync(String name, int credits, String type, String detail, AsyncCallBack<Course> callback) {
        Task<Course> task = new Task<>() {
            @Override
            protected Course call() throws Exception {
                return addCourse(name, credits, type, detail);
            }
        };
        task.setOnSucceeded(e -> callback.onSuccess(task.getValue()));
        task.setOnFailed(e -> callback.onFailure(task.getException()));
        new Thread(task).start();
    }

    public boolean enrollStudent(int studentId, int courseId)
            throws EnrollementException {

        User u = users.get(studentId);
        Course c = courses.get(courseId);

        if (!(u instanceof Student))
            throw new EnrollementException("Student ID " + studentId + " not found.");
        if (c == null)
            throw new EnrollementException("Course ID " + courseId + " not found.");

        Student s = (Student) u;
        if (s.getEnrolledCourses().contains(c))
            throw new EnrollementException("Student is already enrolled in this course.");

        s.enrollCourse(c);
        c.addStudent(s);
        EnrollmentDAO.saveEnrollment(studentId, courseId);
        return true;
    }

    public void enrollStudentAsync(int studentId, int courseId, AsyncCallBack<Boolean> callback) {
        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return enrollStudent(studentId, courseId);
            }
        };
        task.setOnSucceeded(e -> callback.onSuccess(task.getValue()));
        task.setOnFailed(e -> callback.onFailure(task.getException()));
        new Thread(task).start();
    }

    public boolean removeStudent(int studentId)
            throws InvalIdUserException {

        User u = users.get(studentId);
        if (u == null)
            throw new InvalIdUserException("No user found with ID: " + studentId);
        if (!(u instanceof Student))
            throw new InvalIdUserException("User ID " + studentId + " is not a student.");

        Student s = (Student) u;
        for (Course c : new ArrayList<>(s.getEnrolledCourses()))
            c.removeStudent(s);

        users.remove(studentId);
        UserDAO.deleteStudent(studentId);
        return true;
    }

    public void removeStudentAsync(int studentId, AsyncCallBack<Boolean> callback) {
        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return removeStudent(studentId);
            }
        };
        task.setOnSucceeded(e -> callback.onSuccess(task.getValue()));
        task.setOnFailed(e -> callback.onFailure(task.getException()));
        new Thread(task).start();
    }

    public boolean postAnnouncement(int courseId, String text)
            throws InvalidCourseException {

        if (text == null || text.isBlank())
            throw new InvalidCourseException("Announcement text cannot be empty.");
        if (!courses.containsKey(courseId))
            throw new InvalidCourseException("Course ID " + courseId + " not found.");

        Announcement a = new Announcement(text);
        announcements.computeIfAbsent(courseId, k -> new ArrayList<>()).add(a);
        AnnouncementDAO.saveAnnouncement(courseId, a);
        return true;
    }

    public void postAnnouncementAsync(int courseId, String text, AsyncCallBack<Boolean> callback) {
        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return postAnnouncement(courseId, text);
            }
        };
        task.setOnSucceeded(e -> callback.onSuccess(task.getValue()));
        task.setOnFailed(e -> callback.onFailure(task.getException()));
        new Thread(task).start();
    }

    public List<String> getStudentFeed(int studentId) throws UserNotFoundException {

        User u = users.get(studentId);
        if (!(u instanceof Student))
            throw new UserNotFoundException("Student ID " + studentId + " not found.");

        List<String> feed = new ArrayList<>();
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

    public void getStudentFeedAsync(int studentId, AsyncCallBack<List<String>> callback) {
        Task<List<String>> task = new Task<>() {
            @Override
            protected List<String> call() throws Exception {
                return getStudentFeed(studentId);
            }
        };
        task.setOnSucceeded(e -> callback.onSuccess(task.getValue()));
        task.setOnFailed(e -> callback.onFailure(task.getException()));
        new Thread(task).start();
    }

    public boolean sendMessage(int fromId, int toId, String content)
            throws UserNotFoundException {

        User from = users.get(fromId);
        User to = users.get(toId);

        if (from == null)
            throw new UserNotFoundException("Sender ID " + fromId + " not found.");
        if (to == null)
            throw new UserNotFoundException("Recipient ID " + toId + " not found.");
        if (content == null || content.isBlank())
            throw new UserNotFoundException("Message content cannot be empty.");

        to.receiveMessage(new Message(from, to, content));
        return true;
    }

    public void sendMessageAsync(int fromId, int toId, String content, AsyncCallBack<Boolean> callback) {
        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return sendMessage(fromId, toId, content);
            }
        };
        task.setOnSucceeded(e -> callback.onSuccess(task.getValue()));
        task.setOnFailed(e -> callback.onFailure(task.getException()));
        new Thread(task).start();
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
