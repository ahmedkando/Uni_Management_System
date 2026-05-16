package com.mycompany.advanced_project;

import com.mycompany.advanced_project.Classes.*;
import com.mycompany.advanced_project.DB.*;
import com.mycompany.advanced_project.service.*;

import java.util.List;

/**
 * Full integration test — tests DB + memory together.
 * Run main() and check console. Each test prints PASS or FAIL.
 *
 * BEFORE RUNNING:
 *   - Make sure DB is running and credentials in Constant.java are correct
 *   - Tables must exist (DBConnection.initDatabase() is called at the top)
 */
/* public class FullSystemTest {

    static int passed = 0;
    static int failed = 0;

    // ── track created IDs so we can clean up at the end ──────────────────
    static int studentId    = -1;
    static int instructorId = -1;
    static int courseId     = -1;

    public static void main(String[] args) {
        System.out.println("========== FULL SYSTEM INTEGRATION TEST ==========\n");

        // ── SETUP ──────────────────────────────────────────────────────────
        DBConnection.initDatabase();
        System_controller system = new System_controller();
        system.loadAll();    // load whatever is already in DB
        AuthManager auth = new AuthManager();

        // ── UNIT: ID PLUMBING ──────────────────────────────────────────────
        testIdPlumbing();

        // ── DB: ADD STUDENT ────────────────────────────────────────────────
        System.out.println("\n--- DB: Users ---");
        Student student = system.addStudent("testuser_ahmed", "ahmed_test@uni.com");
        check("addStudent returns non-null", student != null, "returned null — saveStudent failed");
        if (student != null) {
            studentId = student.getId();
            check("addStudent: getId() is real DB id (not 0)",
                  studentId > 0,
                  "getId()=" + studentId + " — setId() not called or insert failed");
            check("addStudent: object in memory",
                  system.getUserById(studentId) == student,
                  "getUserById returned wrong object");
        }

        // ── DB: ADD INSTRUCTOR ─────────────────────────────────────────────
        Instructor instructor = system.addInstructor("testuser_sara", "sara_test@uni.com", 7000.0);
        check("addInstructor returns non-null", instructor != null, "returned null — saveInstructor failed");
        if (instructor != null) {
            instructorId = instructor.getId();
            check("addInstructor: getId() is real DB id (not 0)",
                  instructorId > 0,
                  "getId()=" + instructorId);
        }

        // ── DB: ADD COURSE ─────────────────────────────────────────────────
        System.out.println("\n--- DB: Courses ---");
        Course course = system.addCourse("Test_OOP", 3, "offline", "Room 202");
        check("addCourse returns non-null", course != null, "returned null — saveCourse failed (check SQL placeholders + RETURN_GENERATED_KEYS)");
        if (course != null) {
            courseId = course.getId();
            check("addCourse: getId() is real DB id (not 0)",
                  courseId > 0,
                  "getId()=" + courseId + " — RETURN_GENERATED_KEYS missing or SQL wrong");
            check("addCourse: object in memory",
                  system.getCourseById(courseId) == course,
                  "getCourseById returned wrong object");
        }

        // ── DB: CREDENTIAL (signup flow) ───────────────────────────────────
        System.out.println("\n--- DB: Credentials ---");
        String testUsername = "test_login_user_xyz";
        User signedUp = null;
        try {
            signedUp = auth.signup(testUsername, "test@uni.com", "student", "password123", system);
            check("signup returns non-null user", signedUp != null, "returned null");
            if (signedUp != null) {
                check("signup: getId() > 0",
                      signedUp.getId() > 0,
                      "getId()=" + signedUp.getId());
            }
        } catch (Exception e) {
            check("signup throws no exception", false, e.getMessage());
        }

        // ── DB: LOGIN ──────────────────────────────────────────────────────
        System.out.println("\n--- DB: Login ---");
        if (signedUp != null) {
            try {
                User loggedIn = auth.login(testUsername, "password123", system);
                check("login returns non-null user", loggedIn != null, "returned null");
                check("login: correct user id",
                      loggedIn != null && loggedIn.getId() == signedUp.getId(),
                      "expected id=" + signedUp.getId() + " got " + (loggedIn != null ? loggedIn.getId() : "null"));

                String role = CredentialDAO.getRole(loggedIn.getId());
                check("getRole returns 'student'",
                      "student".equalsIgnoreCase(role),
                      "role=" + role);
            } catch (Exception e) {
                check("login throws no exception", false, e.getMessage());
            }

            // wrong password
            try {
                auth.login(testUsername, "wrongpassword", system);
                check("login rejects wrong password", false, "should have thrown AuthException");
            } catch (Exception e) {
                check("login rejects wrong password", true, "");
            }
        }

        // ── ENROLL ─────────────────────────────────────────────────────────
        System.out.println("\n--- Enroll ---");
        if (studentId > 0 && courseId > 0) {
            boolean enrolled = system.enrollStudent(studentId, courseId);
            check("enrollStudent returns true",
                  enrolled,
                  "returned false — getUserById(" + studentId + ") or getCourseById(" + courseId + ") returned null");

            Student s = (Student) system.getUserById(studentId);
            check("student has course in memory",
                  s != null && s.getEnrolledCourses().contains(system.getCourseById(courseId)),
                  "enrolled course not found in student's list");

            Course c = system.getCourseById(courseId);
            check("course has student in memory",
                  c != null && c.getStudentIds().contains(s),
                  "student not found in course's list");

            // duplicate enroll should not add twice
            system.enrollStudent(studentId, courseId);
            check("no duplicate enroll",
                  s.getEnrolledCourses().size() == 1,
                  "enrolled " + s.getEnrolledCourses().size() + " times");
        }

        // ── ANNOUNCEMENT ───────────────────────────────────────────────────
        System.out.println("\n--- Announcement ---");
        if (courseId > 0) {
            boolean posted = system.postAnnouncement(courseId, "Test announcement content");
            check("postAnnouncement returns true", posted,
                  "returned false — courseId=" + courseId + " not found in memory");
        }

        // ── STUDENT FEED ───────────────────────────────────────────────────
        System.out.println("\n--- Student Feed ---");
        if (studentId > 0) {
            List<String> feed = system.getStudentFeed(studentId);
            check("getStudentFeed returns non-empty list",
                  !feed.isEmpty(),
                  "feed is empty — enrollment or announcement may have failed");
            if (!feed.isEmpty()) {
                check("feed contains course name",
                      feed.get(0).contains("Test_OOP"),
                      "feed entry: " + feed.get(0));
            }
        }

        // ── MESSAGE ────────────────────────────────────────────────────────
        System.out.println("\n--- Message ---");
        if (studentId > 0 && instructorId > 0) {
            boolean sent = system.sendMessage(studentId, instructorId, "Hello instructor!");
            check("sendMessage returns true", sent,
                  "returned false — one of the users not found");

            User instr = system.getUserById(instructorId);
            check("instructor received message",
                  instr != null && !instr.getInbox().isEmpty(),
                  "inbox is empty");
        }

        // ── RELOAD FROM DB ─────────────────────────────────────────────────
        System.out.println("\n--- Reload from DB (persistence check) ---");
        System_controller system2 = new System_controller();
        system2.loadAll();

        if (studentId > 0) {
            User reloaded = system2.getUserById(studentId);
            check("student persisted in DB after reload",
                  reloaded instanceof Student,
                  "getUserById(" + studentId + ") returned " + reloaded);
            check("reloaded student has correct id",
                  reloaded != null && reloaded.getId() == studentId,
                  "id=" + (reloaded != null ? reloaded.getId() : "null"));
        }
        if (courseId > 0) {
            Course reloaded = system2.getCourseById(courseId);
            check("course persisted in DB after reload",
                  reloaded != null,
                  "getCourseById(" + courseId + ") returned null");
        }
        if (studentId > 0 && courseId > 0) {
            Student s2 = (Student) system2.getUserById(studentId);
            Course  c2 = system2.getCourseById(courseId);
            check("enrollment persisted and reloaded",
                  s2 != null && c2 != null && s2.getEnrolledCourses().contains(c2),
                  "enrollment not found after reload");
        }

        // ── REMOVE STUDENT ─────────────────────────────────────────────────
        System.out.println("\n--- Remove Student ---");
        if (studentId > 0) {
            boolean removed = system.removeStudent(studentId);
            check("removeStudent returns true", removed, "returned false");
            check("student gone from memory",
                  system.getUserById(studentId) == null,
                  "still in memory after remove");
        }

        // ── CLEANUP: remove test data ──────────────────────────────────────
        System.out.println("\n--- Cleanup ---");
        cleanupTestData(instructorId, courseId, testUsername);
        System.out.println("  Test data cleaned up.");

        // ── RESULTS ────────────────────────────────────────────────────────
        System.out.println("\n==================================================");
        System.out.printf("  RESULTS: %d passed, %d failed%n", passed, failed);
        System.out.println("==================================================");

        if (failed == 0) {
            System.out.println("  ✅ All tests passed! Your system is working.");
        } else {
            System.out.println("  ❌ Some tests failed. Check the reasons above.");
        }
    }

    // ── UNIT: ID PLUMBING ──────────────────────────────────────────────────
    static void testIdPlumbing() {
        System.out.println("--- Unit: ID plumbing ---");

        Student s = new Student("unit_test", "u@u.com");
        s.setId(99);
        check("Student setId/getId", s.getId() == 99,
              "getId()=" + s.getId() + " — duplicate id field still in Student or setId missing from User");

        Instructor i = new Instructor("unit_inst", "i@i.com");
        i.setId(88);
        check("Instructor setId/getId", i.getId() == 88,
              "getId()=" + i.getId() + " — duplicate id field still in Instructor");

        Course c = new OfflineCourse("unit_course", 3, "Room1");
        c.setId(77);
        check("Course setId/getId", c.getId() == 77,
              "getId()=" + c.getId());
    }

    // ── CLEANUP ────────────────────────────────────────────────────────────
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

    // ── HELPER ─────────────────────────────────────────────────────────────
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

*/