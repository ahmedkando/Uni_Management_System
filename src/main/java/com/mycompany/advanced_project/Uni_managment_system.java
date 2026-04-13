package com.mycompany.advanced_project;

import com.mycompany.advanced_project.Classes.Course;
import com.mycompany.advanced_project.Classes.Instructor;
import com.mycompany.advanced_project.Classes.Student;
import java.util.*;

public class Uni_managment_system {
    
    public static void runConsole(System_controller system) {
         Student s1 = system.addStudent("john_doe", "john@email.com");
         Instructor i1 = system.addInstructor("dr_smith", "smith@email.com");
         Course c1 = system.addCourse("Java Programming", 3, "Online", "Zoom");
         
         System.out.println("Sample IDs created:");
         System.out.println("Student: " + s1.getId());
         System.out.println("Instructor: " + i1.getId());
         System.out.println("Course: " + c1.getId());
        Scanner input = new Scanner(System.in);
        boolean run = true;

        while (run) {
            printMenu();
            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1 -> createStudent(system, input);
                case 2 -> createInstructor(system, input);
                case 3 -> createCourse(system, input);
                case 4 -> enrollStudent(system, input);
                case 5 -> removeStudent(system, input);
                case 6 -> postAnnouncement(system, input);
                case 7 -> viewFeed(system, input);
                case 8 -> sendMessage(system, input);
                case 9 -> listAll(system);
                case 10 -> run = false;
                default -> System.out.println("Invalid choice!");
            }
        }

        System.out.println("Goodbye!");
        input.close();
    }

    private static void printMenu() {
        System.out.println("\n=============================");
        System.out.println("  University System Menu");
        System.out.println("=============================");
        System.out.println("1. Create Student");
        System.out.println("2. Create Instructor");
        System.out.println("3. Create Course");
        System.out.println("4. Enroll Student");
        System.out.println("5. Remove Student");
        System.out.println("6. Post Announcement");
        System.out.println("7. View Student Feed");
        System.out.println("8. Send Message");
        System.out.println("9. List All");
        System.out.println("10. Exit");
        System.out.print("Choose an option: ");
    }

    private static void createStudent(System_controller system, Scanner input) {
        System.out.print("Enter username: ");
        String username = input.nextLine();
        System.out.print("Enter email: ");
        String email = input.nextLine();
        Student s = system.addStudent(username, email);
        System.out.println("\nCreated: " + s.getInfo());
    }

    private static void createInstructor(System_controller system, Scanner input) {
        System.out.print("Enter username: ");
        String username = input.nextLine();
        System.out.print("Enter email: ");
        String email = input.nextLine();
        Instructor i = system.addInstructor(username, email);
        System.out.println("\nCreated: " + i.getInfo());
    }

    private static void createCourse(System_controller system, Scanner input) {
        System.out.print("Enter course name: ");
        String name = input.nextLine();
        System.out.print("Enter credits: ");
        int credits = input.nextInt();
        input.nextLine();
        System.out.print("Type (Online/Offline): ");
        String type = input.nextLine();
        System.out.print("Enter platform or room: ");
        String detail = input.nextLine();

        Course c = system.addCourse(name, credits, type, detail);
        System.out.println("\nCreated: " + c.getInfo());
    }

    private static void enrollStudent(System_controller system, Scanner input) {
        System.out.print("Enter Student ID: ");
        String sid = input.nextLine();
        System.out.print("Enter Course ID: ");
        String cid = input.nextLine();

        System.out.println(
            system.enrollStudent(sid, cid)
                ? "Enrollment successful!"
                : "Enrollment failed!"
        );
    }

    private static void removeStudent(System_controller system, Scanner input) {
        System.out.print("Enter Student ID: ");
        String id = input.nextLine();

        System.out.println(
            system.removeStudent(id)
                ? "Student removed successfully!"
                : "Student not found!"
        );
    }

    private static void postAnnouncement(System_controller system, Scanner input) {
        System.out.print("Enter Course ID: ");
        String cid = input.nextLine();
        System.out.print("Enter announcement text: ");
        String text = input.nextLine();

        System.out.println(
            system.postAnnouncement(cid, text)
                ? "Announcement posted!"
                : "Course not found!"
        );
    }

    private static void viewFeed(System_controller system, Scanner input) {
        System.out.print("Enter Student ID: ");
        String sid = input.nextLine();

        List<String> feed = system.getStudentFeed(sid);
        System.out.println("\n===== Student Feed =====");

        if (feed.isEmpty()) {
            System.out.println("No announcements found.");
        } else {
            feed.forEach(msg -> System.out.println("• " + msg));
        }
    }

    private static void sendMessage(System_controller system, Scanner input) {
        System.out.print("From User ID: ");
        String from = input.nextLine();
        System.out.print("To User ID: ");
        String to = input.nextLine();
        System.out.print("Message: ");
        String msg = input.nextLine();

        System.out.println(
            system.sendMessage(from, to, msg)
                ? "Message sent!"
                : "Invalid user IDs!"
        );
    }

    private static void listAll(System_controller system) {
        System.out.println("\n========== USERS ==========");
        system.getAllUsers().forEach(u -> System.out.println(u.getInfo()));

        System.out.println("\n========== COURSES ==========");
        system.getAllCourses().forEach(c -> System.out.println(c.getInfo()));
    }
}
