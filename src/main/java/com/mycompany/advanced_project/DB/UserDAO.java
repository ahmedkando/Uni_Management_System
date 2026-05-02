package com.mycompany.advanced_project.DB;

import java.sql.*;
import com.mycompany.advanced_project.service.System_controller;

public class UserDAO {

    public static int saveStudent(String username, String email) {
        String sql = "INSERT INTO students" +
                "(username,email) VALUES (?,?)";
        try (Connection conn = DBConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } 
        catch (SQLException e) {
            // TODO: handle exception
            System.out.println("saving Student failed" + e.getMessage());
        }
        return -1;
    }

    // save instructor
    public static int saveInstructor(String username, String email, double salary) {
        String sql = "INSERT  INTO instructors" +
                "(username,email,salary) VALUES (?,?,?)";
        try (Connection conn = DBConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setDouble(3, salary);
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } catch (SQLException e) {
            // TODO: handle exception
            System.out.println("saving Instructor failed" + e.getMessage());
        }
        return -1;
    }
    
    public static void loadStudents(System_controller system){
        String sql = "SELECT * FROM students";
        try(Connection conn=DBConnection.connect();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        ){
            while(rs.next()){
                system.loadStudent(
                rs.getInt("id"),
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
        try(Connection conn=DBConnection.connect();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        ){
            while(rs.next()){
                system.loadInstructor(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getDouble("salary")
                 );
            }
        }catch (SQLException e){
            System.err.println(" Loading Instructors failed: "+e.getMessage());
        }
    };
    
      // delete student from enrollment , students and credetials tabel using the
    // regHash
    public static void deleteStudent(int studentId) {
        try (Connection conn = DBConnection.connect()) {
            String sql1 = "DELETE FROM students WHERE id=?";
            PreparedStatement del_student = conn.prepareStatement(sql1);
            del_student.setInt(1, studentId);
            del_student.executeUpdate();
    
            String sql2 = "DELETE FROM enrollments WHERE student_id=?";
            PreparedStatement del_enrollments = conn.prepareStatement(sql2);
            del_enrollments.setInt(1, studentId);
            del_enrollments.executeUpdate();
    
            String sql3 = "DELETE FROM credentials WHERE user_id=?";
            PreparedStatement del_credentials = conn.prepareStatement(sql3);
            del_credentials.setInt(1, studentId);
            del_credentials.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Deleting Student failed" + e.getMessage());
        }
    };
}
