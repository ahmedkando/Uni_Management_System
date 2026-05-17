package com.mycompany.advanced_project;

import com.mycompany.advanced_project.Classes.*;
import com.mycompany.advanced_project.DB.*;
import com.mycompany.advanced_project.exceptions.*;
import com.mycompany.advanced_project.service.*;

import java.util.List;

/**
 * Full integration test — tests DB + memory + exception handling.
 * Run main() and check console. Each test prints PASS or FAIL.
 */
public class FullSystemTest {

    static int passed = 0;
    static int failed = 0;

    static int studentId = -1;
    static int instructorId = -1;
    static int courseId = -1;

    public static void main(String[] args) {
        System.out.println("========== FULL SYSTEM INTEGRATION TEST ==========\n");

        DBConnection.initDatabase();
        System_controller system = new System_controller();
        system.loadAll();
        AuthManager auth = new AuthManager();

        testIdPlumbing();

        testInputValidation(system, auth);

        System.out.println("\n--- DB: Add Users & Course ---");
        try {
            Student student = system.addStudent("testuser_ahmed", "ahmed_test@uni.com");
            studentId = student.getId();
            check("addStudent: real DB id", studentId > 0, "id=" + studentId);
            check("addStudent: in memory", system.getUserById(studentId) == student, "not in memory");
        } catch (Exception e) {
            check("addStudent: no exception", false, e.getMessage());
        }

        try {
            Instructor instructor = system.addInstructor("testuser_sara", "sara_test@uni.com", 7000.0);
            instructorId = instructor.getId();
            check("addInstructor: real DB id", instructorId > 0, "id=" + instructorId);
        } catch (Exception e) {
            check("addInstructor: no exception", false, e.getMessage());
        }

        try {
            Course course = system.addCourse("Test_OOP", 3, "offline", "Room 202");
            courseId = course.getId();
            check("addCourse: real DB id", courseId > 0, "id=" + courseId);
            check("addCourse: in memory", system.getCourseById(courseId) == course, "not in memory");
        } catch (Exception e) {
            check("addCourse: no exception", false, e.getMessage());
        }

        System.out.println("\n--- DB: Signup & Login ---");
        String testUsername = "test_login_user_xyz";
        User signedUp = null;
        try {
            signedUp = auth.signup(testUsername, "test@uni.com", "student", "password123", system);
            check("signup: success", signedUp != null, "returned null");
            check("signup: id > 0", signedUp.getId() > 0, "id=" + signedUp.getId());
        } catch (Exception e) {
            check("signup: no exception", false, e.getMessage());
        }

        if (signedUp != null) {
            try {
                User loggedIn = auth.login(testUsername, "password123", system);
                check("login: correct user returned", loggedIn.getId() == signedUp.getId(),
                        "expected " + signedUp.getId() + " got " + loggedIn.getId());
                check("getRole: returns 'student'",
                        "student".equalsIgnoreCase(CredentialDAO.getRole(loggedIn.getId())),
                        "role=" + CredentialDAO.getRole(loggedIn.getId()));
            } catch (Exception e) {
                check("login: no exception on correct credentials", false, e.getMessage());
            }

            try {
                auth.login(testUsername, "wrongpassword", system);
                check("login: rejects wrong password", false, "should have thrown AuthException");
            } catch (AuthException e) {
                check("login: rejects wrong password", true, "");
            } catch (Exception e) {
                check("login: throws AuthException (not generic)", false,
                        "threw " + e.getClass().getSimpleName());
            }

            try {
                auth.login("nonexistent_user_xyz", "password123", system);
                check("login: rejects unknown username", false, "should have thrown AuthException");
            } catch (AuthException e) {
                check("login: rejects unknown username", true, "");
            } catch (Exception e) {
                check("login: throws AuthException for unknown user", false,
                        "threw " + e.getClass().getSimpleName());
            }

            try {
                auth.signup(testUsername, "other@uni.com", "student", "password123", system);
                check("signup: rejects duplicate username", false, "should have thrown AuthException");
            } catch (AuthException e) {
                check("signup: rejects duplicate username", true, "");
            } catch (Exception e) {
                check("signup: throws AuthException for duplicate", false,
                        "threw " + e.getClass().getSimpleName());
            }
        }

        System.out.println("\n--- Enroll ---");
        if (studentId > 0 && courseId > 0) {
            // happy path
            try {
                boolean ok = system.enrollStudent(studentId, courseId);
                check("enrollStudent: returns true", ok, "returned false");
                Student s = (Student) system.getUserById(studentId);
                check("enrollStudent: student has course",
                        s.getEnrolledCourses().contains(system.getCourseById(courseId)), "not in list");
                check("enrollStudent: course has student",
                        system.getCourseById(courseId).getStudentIds().contains(s), "not in list");
            } catch (Exception e) {
                check("enrollStudent: no exception on valid data", false, e.getMessage());
            }

            try {
                system.enrollStudent(studentId, courseId);
                check("enrollStudent: rejects duplicate", false, "should have thrown EnrollementException");
            } catch (EnrollementException e) {
                check("enrollStudent: rejects duplicate", true, "");
            } catch (Exception e) {
                check("enrollStudent: throws EnrollementException for duplicate", false,
                        "threw " + e.getClass().getSimpleName());
            }

            try {
                system.enrollStudent(99999, courseId);
                check("enrollStudent: rejects invalid student id", false, "should have thrown EnrollementException");
            } catch (EnrollementException e) {
                check("enrollStudent: rejects invalid student id", true, "");
            } catch (Exception e) {
                check("enrollStudent: throws EnrollementException for bad student", false,
                        "threw " + e.getClass().getSimpleName());
            }

            try {
                system.enrollStudent(studentId, 99999);
                check("enrollStudent: rejects invalid course id", false, "should have thrown EnrollementException");
            } catch (EnrollementException e) {
                check("enrollStudent: rejects invalid course id", true, "");
            } catch (Exception e) {
                check("enrollStudent: throws EnrollementException for bad course", false,
                        "threw " + e.getClass().getSimpleName());
            }
        }

        System.out.println("\n--- Announcement ---");
        if (courseId > 0) {
            // happy path
            try {
                boolean ok = system.postAnnouncement(courseId, "Test announcement content");
                check("postAnnouncement: returns true", ok, "returned false");
            } catch (Exception e) {
                check("postAnnouncement: no exception on valid data", false, e.getMessage());
            }

            try {
                system.postAnnouncement(courseId, "");
                check("postAnnouncement: rejects blank text", false, "should have thrown InvalidCourseException");
            } catch (InvalidCourseException e) {
                check("postAnnouncement: rejects blank text", true, "");
            } catch (Exception e) {
                check("postAnnouncement: throws InvalidCourseException for blank", false,
                        "threw " + e.getClass().getSimpleName());
            }

            try {
                system.postAnnouncement(99999, "some text");
                check("postAnnouncement: rejects invalid course id", false,
                        "should have thrown InvalidCourseException");
            } catch (InvalidCourseException e) {
                check("postAnnouncement: rejects invalid course id", true, "");
            } catch (Exception e) {
                check("postAnnouncement: throws InvalidCourseException for bad id", false,
                        "threw " + e.getClass().getSimpleName());
            }
        }

        System.out.println("\n--- Student Feed ---");
        if (studentId > 0) {
            try {
                List<String> feed = system.getStudentFeed(studentId);
                check("getStudentFeed: non-empty", !feed.isEmpty(), "feed is empty");
                check("getStudentFeed: contains course name",
                        !feed.isEmpty() && feed.get(0).contains("Test_OOP"),
                        feed.isEmpty() ? "empty" : feed.get(0));
            } catch (Exception e) {
                check("getStudentFeed: no exception", false, e.getMessage());
            }

            try {
                system.getStudentFeed(99999);
                check("getStudentFeed: rejects invalid id", false, "should have thrown UserNotFoundException");
            } catch (UserNotFoundException e) {
                check("getStudentFeed: rejects invalid id", true, "");
            } catch (Exception e) {
                check("getStudentFeed: throws UserNotFoundException", false,
                        "threw " + e.getClass().getSimpleName());
            }
        }

        System.out.println("\n--- Message ---");
        if (studentId > 0 && instructorId > 0) {
            // happy path
            try {
                boolean ok = system.sendMessage(studentId, instructorId, "Hello instructor!");
                check("sendMessage: returns true", ok, "returned false");
                check("sendMessage: delivered to inbox",
                        !system.getUserById(instructorId).getInbox().isEmpty(), "inbox empty");
            } catch (Exception e) {
                check("sendMessage: no exception on valid data", false, e.getMessage());
            }

            try {
                system.sendMessage(studentId, 99999, "hello");
                check("sendMessage: rejects invalid recipient", false, "should have thrown UserNotFoundException");
            } catch (UserNotFoundException e) {
                check("sendMessage: rejects invalid recipient", true, "");
            } catch (Exception e) {
                check("sendMessage: throws UserNotFoundException for bad recipient", false,
                        "threw " + e.getClass().getSimpleName());
            }

            try {
                system.sendMessage(studentId, instructorId, "");
                check("sendMessage: rejects blank content", false, "should have thrown UserNotFoundException");
            } catch (UserNotFoundException e) {
                check("sendMessage: rejects blank content", true, "");
            } catch (Exception e) {
                check("sendMessage: throws exception for blank content", false,
                        "threw " + e.getClass().getSimpleName());
            }
        }

        System.out.println("\n--- Reload from DB (persistence) ---");
        System_controller system2 = new System_controller();
        system2.loadAll();

        if (studentId > 0) {
            User reloaded = system2.getUserById(studentId);
            check("student persisted in DB", reloaded instanceof Student, "not found after reload");
            check("student id correct after reload",
                    reloaded != null && reloaded.getId() == studentId, "id mismatch");
        }
        if (courseId > 0) {
            check("course persisted in DB",
                    system2.getCourseById(courseId) != null, "not found after reload");
        }
        if (studentId > 0 && courseId > 0) {
            Student s2 = (Student) system2.getUserById(studentId);
            Course c2 = system2.getCourseById(courseId);
            check("enrollment persisted and reloaded",
                    s2 != null && c2 != null && s2.getEnrolledCourses().contains(c2),
                    "enrollment not found after reload");
        }

        System.out.println("\n--- Remove Student ---");
        if (studentId > 0) {
            // invalid id → must throw InvalIdUserException
            try {
                system.removeStudent(99999);
                check("removeStudent: rejects invalid id", false, "should have thrown InvalIdUserException");
            } catch (InvalIdUserException e) {
                check("removeStudent: rejects invalid id", true, "");
            } catch (Exception e) {
                check("removeStudent: throws InvalIdUserException for bad id", false,
                        "threw " + e.getClass().getSimpleName());
            }

            try {
                boolean ok = system.removeStudent(studentId);
                check("removeStudent: returns true", ok, "returned false");
                check("removeStudent: gone from memory",
                        system.getUserById(studentId) == null, "still in memory");
            } catch (Exception e) {
                check("removeStudent: no exception on valid id", false, e.getMessage());
            }
        }

        System.out.println("\n--- Cleanup ---");
        cleanupTestData(instructorId, courseId, testUsername);
        System.out.println("  Test data cleaned up.");

        System.out.println("\n==================================================");
        System.out.printf("  RESULTS: %d passed, %d failed%n", passed, failed);
        System.out.println("==================================================");
        System.out.println(failed == 0
                ? "  ✅ All tests passed! Your system is working."
                : "  ❌ Some tests failed. Check the reasons above.");
    }

    static void testIdPlumbing() {
        System.out.println("--- Unit: ID plumbing ---");
        Student s = new Student("unit_test", "u@u.com");
        s.setId(99);
        check("Student setId/getId", s.getId() == 99, "getId()=" + s.getId());

        Instructor i = new Instructor("unit_inst", "i@i.com");
        i.setId(88);
        check("Instructor setId/getId", i.getId() == 88, "getId()=" + i.getId());

        Course c = new OfflineCourse("unit_course", 3, "Room1");
        c.setId(77);
        check("Course setId/getId", c.getId() == 77, "getId()=" + c.getId());
    }

    static void testInputValidation(System_controller system, AuthManager auth) {
        System.out.println("\n--- Unit: Input Validation (exceptions) ---");

        try {
            system.addStudent("", "a@b.com");
            check("addStudent: rejects blank username", false, "no exception thrown");
        } catch (InvalIdUserException e) {
            check("addStudent: rejects blank username", true, "");
        } catch (Exception e) {
            check("addStudent: throws InvalIdUserException", false, "threw " + e.getClass().getSimpleName());
        }

        try {
            system.addStudent("validname", "notanemail");
            check("addStudent: rejects invalid email", false, "no exception thrown");
        } catch (InvalIdUserException e) {
            check("addStudent: rejects invalid email", true, "");
        } catch (Exception e) {
            check("addStudent: throws InvalIdUserException for email", false, "threw " + e.getClass().getSimpleName());
        }

        try {
            system.addCourse("Math", 3, "hybrid", "Room 1");
            check("addCourse: rejects invalid type", false, "no exception thrown");
        } catch (InvalidCourseException e) {
            check("addCourse: rejects invalid type", true, "");
        } catch (Exception e) {
            check("addCourse: throws InvalidCourseException for type", false, "threw " + e.getClass().getSimpleName());
        }

        try {
            system.addCourse("Math", 0, "online", "Zoom");
            check("addCourse: rejects zero credits", false, "no exception thrown");
        } catch (InvalidCourseException e) {
            check("addCourse: rejects zero credits", true, "");
        } catch (Exception e) {
            check("addCourse: throws InvalidCourseException for credits", false,
                    "threw " + e.getClass().getSimpleName());
        }

        try {
            auth.signup("newuser", "a@b.com", "student", "123", system);
            check("signup: rejects short password", false, "no exception thrown");
        } catch (AuthException e) {
            check("signup: rejects short password", true, "");
        } catch (Exception e) {
            check("signup: throws AuthException for short password", false, "threw " + e.getClass().getSimpleName());
        }

        try {
            auth.signup("newuser2", "a@b.com", "admin", "password123", system);
            check("signup: rejects invalid role", false, "no exception thrown");
        } catch (AuthException e) {
            check("signup: rejects invalid role", true, "");
        } catch (Exception e) {
            check("signup: throws AuthException for invalid role", false, "threw " + e.getClass().getSimpleName());
        }
    }

    static void cleanupTestData(int instructorId, int courseId, String testUsername) {
        try (java.sql.Connection conn = DBConnection.connect()) {
            if (instructorId > 0) {
                conn.prepareStatement("DELETE FROM instructors WHERE id=" + instructorId).executeUpdate();
                conn.prepareStatement("DELETE FROM credentials WHERE user_id=" + instructorId).executeUpdate();
            }
            if (courseId > 0) {
                conn.prepareStatement("DELETE FROM courses WHERE id=" + courseId).executeUpdate();
                conn.prepareStatement("DELETE FROM enrollments WHERE course_id=" + courseId).executeUpdate();
                conn.prepareStatement("DELETE FROM announcements WHERE course_id=" + courseId).executeUpdate();
            }
            if (testUsername != null) {
                conn.prepareStatement("DELETE FROM credentials WHERE username='" + testUsername + "'").executeUpdate();
                conn.prepareStatement("DELETE FROM students WHERE username='" + testUsername + "'").executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("  Cleanup error (safe to ignore): " + e.getMessage());
        }
    }

    static void check(String name, boolean condition, String failReason) {
        if (condition) {
            System.out.println("  ✅ PASS  " + name);
            passed++;
        } else {
            System.out.println("  ❌ FAIL  " + name);
            System.out.println("           → " + failReason);
            failed++;
        }
    }
}