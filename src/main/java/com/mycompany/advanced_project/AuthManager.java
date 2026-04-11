package com.mycompany.advanced_project;

import com.mycompany.advanced_project.exceptions.*;
import java.security.*;
import com.mycompany.advanced_project.Databasemanager;

public class AuthManager {

    public User login(String username, String password, System_controller system) throws AuthException {
        if (username == null || username.isBlank())
            throw new AuthException("Username cannot be empty.");
        if (password == null || password.isBlank())
            throw new AuthException("Password cannot be empty.");

        String[] cre = Databasemanager.findCredential(username);
        // if cre is null means no account found with that username
        if (cre == null) {
            throw new AuthException("not found for username  :" + username);
        }
        // if hashed password does not match the stored hash
        if (!cre[1].equals(hashPassword(password))) {
            throw new AuthException("Incorrect password.");
        }
        // Find user
        for (User u : system.getAllUsers()) {
            if (u.getId().equals(cre[0])) {
                System.out.println("login successful");
                return u;
            }
        }

        throw new AuthException("");

    }

    public User signup(String registrationNumber, String username, String email, String role, String password,
            System_controller system) throws AuthException {

        // ---> validate the params
        if (username == null || username.isBlank())
            throw new AuthException("Username cannot be empty.");
        if (email == null || !email.contains("@"))
            throw new AuthException("Invalid email address.");
        if (password == null || password.length() < 6)
            throw new AuthException("Password must be at least 6 characters.");
        if (!role.equalsIgnoreCase("student") && !role.equalsIgnoreCase("instructor"))
            throw new AuthException("Role must be 'student' or 'instructor'.");

        if (registrationNumber == null || registrationNumber.isEmpty()) {
            throw new IllegalArgumentException("Registration number cannot be empty");

        }
        if (registrationNumber.length() < 6 || registrationNumber.length() > 9) {
            throw new IllegalArgumentException("Registration number must be between 6 and 9 characters");
        }
        if (registrationNumber.matches("\\d+")) {
            throw new IllegalArgumentException("Registration number must contain only digits");
        }

        // ---> check duplicates
        if (Databasemanager.usernameExists(username))
            throw new AuthException(username + " is already taken.");
        if (Databasemanager.regNumberExists(registrationNumber))
            throw new AuthException(registrationNumber + " is already registered.");

        // ----> Hash the reg.number
        String regHah = RegistrationHashing.hashandValidate(registrationNumber);

        User newUser;
        // ----> create user
     

        // ----> save user
        

        // -----> save credentials

        return newUser;

    }

    private String hashPassword(String password) {

        try {
            MessageDigest digets = MessageDigest.getInstance("SHA-256");
            byte[] hashedbytes = digets.digest(password.getBytes());// convert String to bytes
            StringBuilder toHex = new StringBuilder();
            for (byte b : hashedbytes) {
                toHex.append(String.format("%02x", b));
            }
            return toHex.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found: " + e.getMessage());
        }

    }
}
