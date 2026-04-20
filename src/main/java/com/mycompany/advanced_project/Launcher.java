package com.mycompany.advanced_project;

import javax.swing.*;

import com.mycompany.advanced_project.DB.*;
import com.mycompany.advanced_project.service.System_controller;
import com.mycompany.advanced_project.UI.UniversityGUI;

import java.sql.*;
import java.util.Scanner;

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
/* 
        CourseDAO.loadCourses(system);
        UserDAO.loadStudents(system);
        UserDAO.loadInstructors(system);
        EnrollmentDAO.loadEnrollments(system);
        AnnouncementDAO.loadAnnouncements(system);
*/

        System.out.println("Choose mode:");
        System.out.println("1. Console");
        System.out.println("2. GUI");

        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {
            Uni_managment_system.runConsole(system);
        } else {
            SwingUtilities.invokeLater(() -> new UniversityGUI(system));
        }
    }
}


