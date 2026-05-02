package com.mycompany.advanced_project.util;

import java.security.*;

public class validate {



    public static void Validate(String registrationNumber) throws IllegalArgumentException {

        if (registrationNumber ==null||registrationNumber.isEmpty()) {
            throw new IllegalArgumentException("Registration number cannot be empty");

        }
        if (registrationNumber.length() < 6 || registrationNumber.length() > 9) {
            throw new IllegalArgumentException("Registration number must be between 6 and 9 characters");
        }
        if (!registrationNumber.matches("\\d+")) {
            throw new IllegalArgumentException("Registration number must contain only digits");
        }

    }
}