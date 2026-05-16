package com.mycompany.advanced_project.service;

import com.mycompany.advanced_project.DB.*;
import com.mycompany.advanced_project.Classes.User;
import com.mycompany.advanced_project.exceptions.*;

import javafx.concurrent.Task;


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
        // if  password does not match the stored password
        if (!cre[1].equals(password)) {
            throw new AuthException("Incorrect password.");
        }
        // Find user
        for (User u : system.getAllUsers()) {
            if (u.getId() == Integer.parseInt(cre[0])) {
                System.out.println("login successful");
                return u;
            }
        }

        throw new AuthException("");

    }

    public void loginAsync(String username, String password, System_controller system, AsyncCallBack<User> callback) {
        Task<User> loginTask = new Task<>() {
            @Override
            protected User call() throws Exception {
                return login(username, password, system);
            }
        };

        loginTask.setOnSucceeded(e -> callback.onSuccess(loginTask.getValue()));
        loginTask.setOnFailed(e -> callback.onFailure(loginTask.getException()));

        new Thread(loginTask).start();
    }

      public User signup( String username, String email, String role, String password,
            System_controller system) throws AuthException, InvalIdUserException {

        // ---> validate the params
        if (username == null || username.isBlank())
            throw new AuthException("Username cannot be empty.");
        if (email == null || !email.contains("@"))
            throw new AuthException("Invalid email address.");
        if (password == null || password.length() < 6)
            throw new AuthException("Password must be at least 6 characters.");
        if (!role.equalsIgnoreCase("student") && !role.equalsIgnoreCase("instructor"))
            throw new AuthException("Role must be 'student' or 'instructor'.");
        if(CredentialDAO.usernameExists(username))
        throw new AuthException("Username already exists.");

        //Create user
        User newUser;
        if(role.equalsIgnoreCase("student")){
            newUser=system.addStudent(username,email);
        }else{
            newUser=system.addInstructor(username, email, 7000.0);
        }

        if(newUser==null){
            throw new AuthException("Failed to create user");
        }
        CredentialDAO.saveCredential(newUser.getId(),
        username,
        password,
        role
        );
        
        System.out.println("Signup successful" + username + " " + role);
        return newUser;

    }
    public void signupAsync(String username, String email, String role, String password,
                        System_controller system, AsyncCallBack<User> callback) {
    Task<User> task = new Task<>() {
        @Override
        protected User call() throws Exception {
            return signup(username, email, role, password, system);
        }
    };
    task.setOnSucceeded(e -> callback.onSuccess(task.getValue()));
    task.setOnFailed(e -> callback.onFailure(task.getException()));
    new Thread(task).start();
}
}
