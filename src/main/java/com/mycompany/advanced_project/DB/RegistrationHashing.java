package com.mycompany.advanced_project.DB;

import java.security.*;

public class RegistrationHashing {

    public static String hash(String registrationNumber) {
        try {
            MessageDigest digets = MessageDigest.getInstance("SHA-256");
            byte[] hashedbytes = digets.digest(registrationNumber.getBytes());// convert String to bytes
            StringBuilder toHex = new StringBuilder();
            for (byte b : hashedbytes) {
                toHex.append(String.format("%02x", b));
            }
            return toHex.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found: " + e.getMessage());
        }
    }

    public static String hashandValidate(String registrationNumber) throws IllegalArgumentException {

        if (registrationNumber ==null||registrationNumber.isEmpty()) {
            throw new IllegalArgumentException("Registration number cannot be empty");

        }
        if (registrationNumber.length() < 6 || registrationNumber.length() > 9) {
            throw new IllegalArgumentException("Registration number must be between 6 and 9 characters");
        }
        if (!registrationNumber.matches("\\d+")) {
            throw new IllegalArgumentException("Registration number must contain only digits");
        }
        return hash(registrationNumber);

    }
}