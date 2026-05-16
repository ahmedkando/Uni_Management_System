package com.mycompany.advanced_project.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mycompany.advanced_project.Classes.Announcement;
import com.mycompany.advanced_project.service.System_controller;

public class AnnouncementDAO {
    
        /* must be called after loadCourses()*/
        public static void loadAnnouncements(System_controller system){
        String sql = "SELECT * FROM announcements ORDER BY id";
        try(Connection conn=DBConnection.connect();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        ){
            while(rs.next()){
                system.loadAnnouncement(
                rs.getInt("course_id"),
                rs.getString("content"),
                rs.getString("timestamp"));
            }
        }
        catch (SQLException e){
            System.err.println(" Loading Announcements failed: "+e.getMessage());
        }
    };


      // save announcemet
    public static void saveAnnouncement(int courseId, Announcement a) {
        String sql = "INSERT INTO announcements" +
                "(course_id,content,timestamp)VALUES(?,?,?)";
        try (Connection conn = DBConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            stmt.setString(2, a.getContent());
            stmt.setString(3, a.getTimestamp());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save announcement: " + e.getMessage(), e);
        }
    }
}
