package com.mycompany.advanced_project;

import java.sql.*;
import java.util.*;

public class Databasemanager {

        private static final String DB_URL = "jdbc:sqlite:university.db";

        private static Connection connect() throws SQLException {
            return DriverManager.getConnection(DB_URL);
        }

        public static void intitDatabase() {

            try (Connection conn = connect()) {

                conn.createStatement().execute(
                        "CREATE TABLE IF NOT EXISTS students (" +
                                "req_hash TEXT PRIMARY KEY," +
                                "reg_number TEXT UNIQUE NOT NULL," +
                                "username TEXT NOT NULL," +
                                "email TEXT NOT NULL)"
                                );
                conn.createStatement().execute(
                        "CREATE TABLE IF NOT EXISTS instructors  (" +
                                "req_hash TEXT PRIMARY KEY," +
                                "reg_number TEXT UNIQUE NOT NULL," +
                                "username TEXT NOT NULL," +
                                "email TEXT NOT NULL," +
                                "salary REAL NOT NULL)"
                                );
                conn.createStatement().execute(
                        "CREATE TABLE IF NOT EXISTS courses (" +
                                "id TEXT PRIMARY KEY," +
                                "name TEXT NOT NULL," +
                                "credits INTEGER NOT NULL," +
                                "type TEXT NOT NULL," +
                                "detail TEXT NOT NULL)"
                                );
                conn.createStatement().execute(
                        "CREATE TABLE IF NOT EXISTS announcements (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "course_id TEXT NOT NULL," +
                                "content TEXT NOT NULL," +
                                "timestamp TEXT NOT NULL)"
                                );
                conn.createStatement().execute(
                        "CREATE TABLE IF NOT EXISTS credentials (" +
                                "reg_hash TEXT PRIMARY KEY," +
                                "username TEXT UNIQUE NOT NULL," +
                                "password TEXT NOT NULL," +
                                "role TEXT NOT NULL)"
                                );
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

        // cred[0] = reg_hash cred[1] = hashedPassword cred[2] = role
        public static String[] findCredential(String username) {
        String sql = "SELECT reg_hash, password, role FROM credentials WHERE username=?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new String[]{
                    rs.getString("reg_hash"),
                    rs.getString("password"),
                    rs.getString("role")
                };
            }
        } catch (SQLException e) {
            System.err.println("[DB] findCredential failed: " + e.getMessage());
        }
        return null;
    }

    
    public static boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM credentials WHERE username=?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("[DB] usernameExists failed: " + e.getMessage());
        }
        return false;
    }

    
    public static boolean regNumberExists(String regNumber) {
        String sql = "SELECT 1 FROM students WHERE reg_number=? "+
        "UNION SELECT 1 FROM instructors WHERE reg_number=?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, regNumber);
            stmt.setString(2, regNumber);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("[DB] regNumberExists failed: " + e.getMessage());
        }
        return false;
    }

}
