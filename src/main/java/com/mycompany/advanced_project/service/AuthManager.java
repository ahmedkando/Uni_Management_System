package com.mycompany.advanced_project.service;

import com.mycompany.advanced_project.DB.*;
import com.mycompany.advanced_project.Classes.User;
import com.mycompany.advanced_project.exceptions.*;
import java.security.*;

public class AuthManager {

    public User login(String username, String password, System_controller system) throws AuthException {
        if (username == null || username.isBlank())
            throw new AuthException("Username cannot be empty.");
        if (password == null || password.isBlank())
            throw new AuthException("Password cannot be empty.");

        String[] cre = CredentialDAO.findCredential(username);
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

 /*     public User signup(String registrationNumber, String username, String email, String role, String password,
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
        if (DatabaseManager.usernameExists(username))
            throw new AuthException(username + " is already taken.");
        if (DatabaseManager.regNumberExists(registrationNumber))
            throw new AuthException(registrationNumber + " is already registered.");

        // ----> Hash the reg.number
        String regHah = RegistrationHashing.hashandValidate(registrationNumber);

        User newUser;
        // ----> create user
        try {
            if(role.equalsIgnoreCase("student")){
                newUser =system.addStudent( registrationNumber,
                                                  username, email);
            }
            else{
                newUser =system.addInstructor( registrationNumber,
                                                  username, email,90000.0);
            }
            
        } catch (Exception e) {
            // TODO: handle exception
            throw new AuthException("Failed to create user: " + e.getMessage());
        }

     

        // ----> save user
        if(newUser instanceof Student){
            DatabaseManager.saveStudent(regHah, registrationNumber, username, email);
        }
        else{
            DatabaseManager.saveInstructor(regHah, registrationNumber, username, email, 0);
        }

        // -----> save credentials
        DatabaseManager.saveCredential(regHah, username, hashPassword(password), role);

        System.out.println("Signup successful for " + username + " with role " + role);


        return newUser;

    }
        */
        

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
