package com.mycompany.advanced_project.DB;

import java.sql.*;

public class CredentialDAO {


     public static void saveCredential(String regHash, String username, String hashedPassword, String role) {

        String sql = "INSERT OR IGNORE INTO credentials" +
                "(reg_hash, username, password, role)Values(?,?,?,?)";
        try (Connection conn = DBConnection.connect();
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
    
     // cred[0] = reg_hash cred[1] = hashedPassword cred[2] = role
    public static String[] findCredential(String username) {
        String sql ="SELECT reg_hash, password, role FROM credentials WHERE username=?";
        try (Connection conn =DBConnection.connect();
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
        try (Connection conn = DBConnection.connect();
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
                "UNION "+"SELECT 1 FROM instructors WHERE reg_number=?";


        try (Connection conn = DBConnection.connect();
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

    