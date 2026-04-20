package com.mycompany.advanced_project.DB;

import java.sql.*;
import com.mycompany.advanced_project.service.System_controller;

public class UserDAO {

    public static void saveStudent(String regHash, String regNumber, String username, String email) {
        String sql = "INSERT INTO students" +
                "(reg_hash, reg_number, username,email) VALUES (?,?,?,?)";
        try (Connection conn = DBConnection.connect();
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
        String sql = "INSERT  INTO instructors" +
                "(reg_hash, reg_number, username,email,salary) VALUES (?,?,?,?,?)";
        try (Connection conn = DBConnection.connect();
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
    
      // delete student from enrollment , students and credetials tabel using the
    // regHash
    public static void deleteStudent(String regHash) {
        try (Connection conn = DBConnection.connect()) {
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
/* 
      public static void loadStudents(System_controller system){
        String sql = "SELECT * FROM students";
        try(Connection conn=DBConnection.connect();
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
*/
}
