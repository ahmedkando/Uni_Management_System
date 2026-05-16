package com.mycompany.advanced_project;

import javax.swing.*;

import com.mycompany.advanced_project.DB.*;
import com.mycompany.advanced_project.service.System_controller;
import com.mycompany.advanced_project.UI.UniversityGUI;

import java.sql.*;

public class Launcher {

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver works!");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver failed: " + e.getMessage());
        }

        try (Connection conn = DBConnection.connect()) {
            System.out.println("Database connected successfully!");

        } catch (SQLException e) {
            System.out.println("Cannot connect to database: " + e.getMessage());
        }

        DBConnection.initDatabase();

        System_controller system = new System_controller();

        system.loadAll();

        UniversityGUI.launchGUI(system);
    }
}
