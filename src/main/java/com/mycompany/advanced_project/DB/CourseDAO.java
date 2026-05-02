package com.mycompany.advanced_project.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mycompany.advanced_project.Classes.Course;
import com.mycompany.advanced_project.Classes.OfflineCourse;
import com.mycompany.advanced_project.Classes.OnlineCourse;
import com.mycompany.advanced_project.service.System_controller;

public class CourseDAO {


       public static void loadCourses(System_controller system){
        String sql = "SELECT * FROM courses";
        try(Connection conn=DBConnection.connect();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        ){
            while(rs.next()){
                system.loadCourse(
                rs.getInt("id"),
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

    
    // saving cousrses --> the desired course like online or offline so let it be
    // type --> that changes the detail so if condition if is online getplatform ||
    // offline change the type and getclassroom
    public static int saveCourse(Course c) {
        String type = (c instanceof OnlineCourse) ? "online" : "offline";
        
        String detail = (c instanceof OnlineCourse oc)
                ? oc.getPlatform()
                : ((OfflineCourse) c).getClassroom();

        String sql = "INSERT INTO courses" +
                "(id,name,credits,type,detail) VALUES (?,?,?,?,?)";

        try (Connection conn = DBConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setInt(1, c.getId());
            stmt.setString(2, c.getName());
            stmt.setInt(3, c.getCredits());
            stmt.setString(4, type);
            stmt.setString(5, detail);
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } catch (SQLException e) {
            System.out.println("saving Course failed" + e.getMessage());
        }
        return -1;
    }

    
}