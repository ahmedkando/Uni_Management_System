package com.mycompany.advanced_project.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mycompany.advanced_project.Classes.Course;
import com.mycompany.advanced_project.Classes.Student;
import com.mycompany.advanced_project.Classes.User;
import com.mycompany.advanced_project.service.System_controller;

public class EnrollmentDAO {
    

    // need to execute the sql command normally but to assign every student id "hashed "and courseid loop on both and like connect them and before that should get them from the database 
    public static void loadEnrollments(System_controller system){
        String sql = "SELECT * FROM enrollments";
        try(Connection conn= DBConnection.connect();
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


       public static void deleteEnrollment(String studentRegHash, String courseId) {
        String sql = "DELETE FROM enrollments WHERE student_reg_hash=? AND course_id=?";
        try (Connection conn = DBConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, studentRegHash);
            stmt.setString(2, courseId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Deleting Enrollment failed" + e.getMessage());
        }
    }



        // save enrollement using the student's hashed reg and course
    public static void saveEnrollment(String studentRegHash, String courseId) {
        String sql = "INSERT  INTO enrollments" +
                "(student_reg_hash,course_id) VALUES(?,?)";
        try (Connection conn = DBConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, studentRegHash);
            stmt.setString(2, courseId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("saving Enrollment failed" + e.getMessage());
        }
    }

}
