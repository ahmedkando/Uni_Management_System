package com.mycompany.advanced_project;

import javax.swing.*;
import com.mycompany.advanced_project.DB.DatabaseManager;
import com.mycompany.advanced_project.UI.UniversityGUI;
import com.mycompany.advanced_project.service.System_controller;
import java.util.Scanner;

public class Launcher {

    public static void main(String[] args) {
        DatabaseManager.intitDatabase();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver works!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System_controller system = new System_controller();

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
