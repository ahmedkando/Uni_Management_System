package com.mycompany.advanced_project.DB;


import java.sql.*;
import com.mycompany.advanced_project.util.Constant;


public class DBConnection {


    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(
            Constant.DB_URL,
            Constant.DB_USERNAME,
            Constant.DB_PASSWORD
             );
    }
     public static void initDatabase() {
        try (Connection conn = connect()) {

            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS credentials (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "user_id INT NOT NULL, " +
                "username VARCHAR(100) UNIQUE NOT NULL, " +
                "password VARCHAR(64) NOT NULL, " +
                "role VARCHAR(20) NOT NULL)"
            );
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS students (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) NOT NULL)"
            );
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS instructors (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) NOT NULL, " +
                "salary DOUBLE NOT NULL)"
            );
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS courses (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "credits INT NOT NULL, " +
                "type VARCHAR(20) NOT NULL, " +
                "detail VARCHAR(100) NOT NULL)"
            );
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS enrollments (" +
                "student_id VARCHAR(20) NOT NULL, " +
                "course_id VARCHAR(10) NOT NULL, " +
                "PRIMARY KEY (student_id, course_id))"
            );
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS announcements (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "course_id VARCHAR(10) NOT NULL, " +
                "content TEXT NOT NULL, " +
                "timestamp VARCHAR(50) NOT NULL)"
            );
            System.out.println("Database initialization Successfully: " );
        } catch (SQLException e) {
            System.out.println("Database initialization failed: " + e.getMessage());
        }
    }
}  