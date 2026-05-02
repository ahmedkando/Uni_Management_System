package com.mycompany.advanced_project.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mycompany.advanced_project.Classes.*;
import com.mycompany.advanced_project.service.System_controller;

public class EnrollmentDAO {
    

     public static void saveEnrollment(int studentId, int courseId) {
        String sql = "INSERT  INTO enrollments" +
                "(student_id,course_id) VALUES(?,?)";
        try (Connection conn = DBConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("saving Enrollment failed" + e.getMessage());
        }
    }

    /*must be called after loadStudenyt() and loadCourse()so thta both
    objects already exist in memory to be wired together
    */
    public static void loadEnrollments(System_controller system){
        String sql = "SELECT * FROM enrollments";
        try(Connection conn= DBConnection.connect();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        ){
            while(rs.next()){
                int studentId =rs.getInt("student_id");
                int courseId =rs.getInt("course_id");

                Student student = null;
                Course course = null;
                
                for(User u :system.getAllUsers()){
                    if(u.getId() == studentId && u instanceof Student s){
                        student=s;
                        break;
                    }
                }
                for(Course c:system.getAllCourses()){
                    if(c.getId()==courseId){
                        course=c;
                        break;
                    }
                }
                if(student !=null && course !=null){
                    student.enrollCourse(course);
                    course.addStudent(student);
                }

            }

        }catch(SQLException e){
            System.out.println("Loading Enrollments failed: "+e.getMessage());
        }
    };


    public static void deleteEnrollment(int studentId, int courseId) {
        String sql = "DELETE FROM enrollments WHERE student_id=? AND course_id=?";
        try (Connection conn = DBConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Deleting Enrollment failed" + e.getMessage());
        }
    }



   
}
