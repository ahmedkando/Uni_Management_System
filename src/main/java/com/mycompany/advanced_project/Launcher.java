package com.mycompany.advanced_project;
import javax.swing.*;

import com.mycompany.advanced_project.Databasemanager;

import java.util.Scanner;

/**
 *
 * @author VICTUS
 */
public class Launcher {

    public static void main(String[] args) {
        Databasemanager.intitDatabase();
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
