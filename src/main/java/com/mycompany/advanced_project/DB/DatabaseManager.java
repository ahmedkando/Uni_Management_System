package com.mycompany.advanced_project.DB;

import java.net.SocketTimeoutException;
import java.sql.*;

import javax.naming.spi.DirStateFactory.Result;

import com.mycompany.advanced_project.System_controller;
import com.mycompany.advanced_project.Classes.*;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:data/university.db";

    private static Connection connect() throws SQLException {
        new java.io.File("data").mkdirs();
        return DriverManager.getConnection(DB_URL);
    }

    public static void intitDatabase() {
        try (Connection conn = connect()) {

            conn.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS students (" +
                            "reg_hash  TEXT PRIMARY KEY," +
                            "reg_number TEXT UNIQUE NOT NULL," +
                            "username TEXT NOT NULL," +
                            "email TEXT NOT NULL)");
            conn.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS instructors  (" +
                            "reg_hash TEXT PRIMARY KEY," +
                            "reg_number TEXT UNIQUE NOT NULL," +
                            "username TEXT NOT NULL," +
                            "email TEXT NOT NULL," +
                            "salary REAL NOT NULL)");
            conn.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS courses (" +
                            "id TEXT PRIMARY KEY," +
                            "name TEXT NOT NULL," +
                            "credits INTEGER NOT NULL," +
                            "type TEXT NOT NULL," +
                            "detail TEXT NOT NULL)");
            conn.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS announcements (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "course_id TEXT NOT NULL," +
                            "content TEXT NOT NULL," +
                            "timestamp TEXT NOT NULL)");
            conn.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS credentials (" +
                            "reg_hash TEXT PRIMARY KEY," +
                            "username TEXT UNIQUE NOT NULL," +
                            "password TEXT NOT NULL," +
                            "role TEXT NOT NULL)");
            conn.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS enrollments (" +
                            "student_reg_hash TEXT NOT NULL," +
                            "course_id TEXT NOT NULL," +
                            "PRIMARY KEY (student_reg_hash, course_id))");

            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            System.out.println("Initializing database failed: " + e.getMessage());
        }

    }

    // saving students
    public static void saveStudent(String regHash, String regNumber, String username, String email) {
        String sql = "INSERT OR REPLACE INTO students" +
                "(reg_hash, reg_number, username,email) VALUES (?,?,?,?)";
        try (Connection conn = connect();
                PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, regHash);
            stmt.setString(2, regNumber);
            stmt.setString(3, username);
            stmt.setString(4, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            // TODO: handle exception
            System.out.println("saving Student failed" + e.getMessage());
        }
    }

    // save instructor
    public static void saveInstructor(String regHash, String regNumber, String username, String email, double salary) {
        String sql = "INSERT OR REPLACE INTO instructors" +
                "(reg_hash, reg_number, username,email,salary) VALUES (?,?,?,?,?)";
        try (Connection conn = connect();
                PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, regHash);
            stmt.setString(2, regNumber);
            stmt.setString(3, username);
            stmt.setString(4, email);
            stmt.setDouble(5, salary);
            stmt.executeUpdate();
        } catch (SQLException e) {
            // TODO: handle exception
            System.out.println("saving Instructor failed" + e.getMessage());
        }
    }

    // saving cousrses --> the desired course like online or offline so let it be
    // type --> that changes the detail so if condition if is online getplatform ||
    // offline change the type and getclassroom
    public static void saveCourse(Course c) {
        String type = (c instanceof OnlineCourse) ? "online" : "offline";
        String detail = (c instanceof OnlineCourse oc)
                ? oc.getPlatform()
                : ((OfflineCourse) c).getClassroom();
        String sql = "INSERT OR REPLACE INTO Courses" +
                "(id,name,credits,type,detail) VALUES (?,?,?,?,?)";

        try (Connection conn = connect();
                PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, c.getId());
            stmt.setString(2, c.getName());
            stmt.setInt(3, c.getCredits());
            stmt.setString(4, type);
            stmt.setString(5, detail);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("saving Course failed" + e.getMessage());
        }
    }

    // save enrollement using the student's hashed reg and course
    public static void saveEnrollment(String studentRegHash, String courseId) {
        String sql = "INSERT OR IGNORE INTO enrollments" +
                "(student_reg_hash,course_id) VALUES(?,?)";
        try (Connection conn = connect();
                PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, studentRegHash);
            stmt.setString(2, courseId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("saving Enrollment failed" + e.getMessage());
        }
    }

    // save announcemet
    public static void saveAnnouncement(String courseId, Announcement a) {
        String sql = "INSERT INTO announcements" +
                "(course_id,content,timestamp)VALUES(?,?,?)";
        try (Connection conn = connect();
                PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, courseId);
            stmt.setString(2, a.getContent());
            stmt.setString(3, a.getTimestamp());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("saving Announcement failed" + e.getMessage());
        }
    }

    public static void saveCredential(String regHash, String username, String hashedPassword, String role) {

        String sql = "INSERT OR IGNORE INTO credentials" +
                "(reg_hash, username, password, role)Values(?,?,?,?)";
        try (Connection conn = connect();
                PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, regHash);
            stmt.setString(2, username);
            stmt.setString(3, hashedPassword);
            stmt.setString(4, role);
            stmt.executeUpdate();
        }

        catch (SQLException e) {
            System.out.println("saving Credential failed" + e.getMessage());
        }
    }

    // delete student from enrollment , students and credetials tabel using the
    // regHash
    public static void deleteStudent(String regHash) {
        try (Connection conn = connect()) {
            String sql1 = "DELETE FROM students WHERE reg_hash=?";
            PreparedStatement del_student = conn.prepareStatement(sql1);
            del_student.setString(1, regHash);
            del_student.executeUpdate();

            String sql2 = "DELETE FROM enrollments WHERE reg_hash=?";
            PreparedStatement del_enrollments = conn.prepareStatement(sql2);
            del_enrollments.setString(1, regHash);
            del_enrollments.executeUpdate();

            String sql3 = "DELETE FROM credentials WHERE reg_hash=?";
            PreparedStatement del_credentials = conn.prepareStatement(sql3);
            del_credentials.setString(1, regHash);
            del_credentials.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Deleting Student failed" + e.getMessage());
        }
    }

    public static void deleteEnrollment(String studentRegHash, String courseId) {
        String sql = "DELETE FROM enrollments WHERE student_reg_hash=? AND course_id=?";
        try (Connection conn = connect();
                PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, studentRegHash);
            stmt.setString(2, courseId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Deleting Enrollment failed" + e.getMessage());
        }
    }

    // so umm commented the load as we need to modify the system controller to save to the DB and recive IDs
/* 
    // load Data to start with the no dependecies tabels then enrollment and announcmentes

    public static void loadData(System_controller system){
        loadCourses(system);
        loadStudents(system);
        loadInstructors(system);
        loadEnrollments(system);
        loadAnnouncements(system);
        System.out.println("Data loaded successfully.");
    };

    public static void loadCourses(System_controller system){
        String sql = "SELECT * FROM courses";
        try(Connection conn=connect();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        ){
            while(rs.next()){
                system.addCourse(
                rs.getString("id"),
                rs.getString("name"), 
                rs.getInt("credits"),
                rs.getString("type"),
                rs.getString("detail")
                 );
            }
        }catch (SQLException e){
            System.err.println(" Loading Courses failed: "+e.getMessage());
        }
    };

    public static void loadStudents(System_controller system){
        String sql = "SELECT * FROM students";
        try(Connection conn=connect();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        ){
            while(rs.next()){
                system.addStudent(
                rs.getString("reg_hash"),
                rs.getString("reg_number"),
                rs.getString("username"),
                rs.getString("email")
                 );
            }
        }catch (SQLException e){
            System.err.println(" Loading Students failed: "+e.getMessage());
        }
    };

    public static void loadInstructors(System_controller system){
        String sql = "SELECT * FROM instructors";
        try(Connection conn=connect();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        ){
            while(rs.next()){
                system.addInstructor(
                rs.getString("reg_hash"),
                rs.getString("reg_number"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getDouble("salary")
                 );
            }
        }catch (SQLException e){
            System.err.println(" Loading Instructors failed: "+e.getMessage());
        }
    };

// need to execute the sql command normally but to assign every student id "hashed "and courseid loop on both and like connect them and before that should get them from the database 
    public static void loadEnrollments(System_controller system){
        String sql = "SELECT * FROM enrollments";
        try(Connection conn=connect();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        ){
            while(rs.next()){
                String regHash =rs.getString("student_reg_hash");
                String courseId =rs.getString("course_id");
                
                for(User u :system.getAllUsers()){
                    if(u.getId().equals(regHash)&&u instanceof Student s){
                        s.enrollCourse(courseId);
                    }
                }
                for(Course c:system.getAllCourses()){
                    if(c.getId().equals(courseId)){
                        c.addStudent(regHash);
                    }
                }

            }

        }catch(SQLException e){
            System.out.println("Loading Enrollments failed: "+e.getMessage());
        }
        

    };

    public static void loadAnnouncements(System_controller system){
        String sql = "SELECT * FROM announcements ORDER BY id";
        try(Connection conn=connect();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        ){
            while(rs.next()){
                system.postAnnouncement(
                rs.getString("course_id"),
                rs.getString("content"),
                rs.getString("timestamp")
                 );
            }
        }catch (SQLException e){
            System.err.println(" Loading Announcements failed: "+e.getMessage());
        }
    };

    */

    // cred[0] = reg_hash cred[1] = hashedPassword cred[2] = role
    public static String[] findCredential(String username) {
        String sql ="SELECT reg_hash, password, role FROM credentials WHERE username=?";
        try (Connection conn =connect();
                PreparedStatement stmt =conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs =stmt.executeQuery();
            if (rs.next()) {
                return new String[] {
                        rs.getString("reg_hash"),
                        rs.getString("password"),
                        rs.getString("role")
                };
            }
        } catch (SQLException e) {
            System.err.println("findCredential failed: " + e.getMessage());
        }
        return null;
    }

    public static boolean usernameExists(String username) {
        String sql ="SELECT 1 FROM credentials WHERE username=?";
        try (Connection conn =connect();
                PreparedStatement stmt =conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("username Exists : " + e.getMessage());
        }
        return false;
    }

    public static boolean regNumberExists(String regNumber) {
        String sql = "SELECT 1 FROM students WHERE reg_number=? " +
                "UNION SELECT 1 FROM instructors WHERE reg_number=?";
        try (Connection conn =connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, regNumber);
            stmt.setString(2, regNumber);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("Registartion number exists : " + e.getMessage());
        }
        return false;
    }

}
