package com.mycompany.advanced_project.DB;

import java.sql.*;

public class CredentialDAO {



    public static void saveCredential(int userId, String username, String password, String role) {

        String sql = "INSERT  INTO credentials" +
                "(user_id username, password, role) Values(?,?,?,?)";
        try (Connection conn = DBConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setInt(1, userId);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, role);
            stmt.executeUpdate();
        }

        catch (SQLException e) {
            System.out.println("saving Credential failed" + e.getMessage());
        }
    }

    /**
      returns [userId, password, role] for the given username,
      or null if no account exists.
     */
    public static String[] findCredential(String username) {
        String sql = "SELECT user_id, password, role FROM credentials WHERE username=?";
        try (Connection conn = DBConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new String[] {
                        String.valueOf(rs.getInt("user_id")),
                        rs.getString("password"),
                        rs.getString("role")
                };
            }
        } catch (SQLException e) {
            System.err.println("finding Credential failed: " + e.getMessage());
        }
        return null;
    }

    public static boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM credentials WHERE username=?";
        try (Connection conn = DBConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("username Exists failed : " + e.getMessage());
        }
        return false;
    }




}
