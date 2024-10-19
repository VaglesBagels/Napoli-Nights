package com.example.napolinights.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class representing a user in the system.
 * This class encapsulates user-related information and provides methods for managing user data,
 * including authentication and account details.
 */
public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String mobile;
    private String email;
    private String password;
    private String role;
    private boolean status;

    /**
     * Constructs a User instance for creating new user accounts.
     *
     * @param firstName The first name of the user.
     * @param lastName  The last name of the user.
     * @param mobile    The mobile phone number of the user.
     * @param email     The email address of the user.
     * @param password  The plain-text password of the user.
     */
    public User(String firstName, String lastName, String mobile, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        setMobile(mobile);
        setEmail(email);
        setPassword(password);
        this.role = "staff"; // Default role
        this.status = true; // Default status
    }

    /**
     * Constructs a User instance for existing users that have already been created in a database.
     *
     * @param id           The ID of the user in the database.
     * @param firstName    The first name of the user.
     * @param lastName     The last name of the user.
     * @param mobile       The mobile phone number of the user.
     * @param email        The email address of the user.
     * @param hashedPassword The hashed password of the user.
     * @param role         The role assigned to the user (e.g., admin, staff).
     * @param status       Indicates whether the user account is active or not.
     */
    public User(int id, String firstName, String lastName, String mobile, String email, String hashedPassword, String role, boolean status) {
        this(firstName, lastName, mobile, email, hashedPassword);
        this.id = id;
        this.role = role;
        this.status = status;
    }

    /**
     * Hashes the given password using SHA-256.
     *
     * @param password The plain-text password to hash.
     * @return The hashed password as a hexadecimal string.
     * @throws RuntimeException If the hashing algorithm is not found.
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Checks if the given password is already hashed.
     *
     * @param password The password to check.
     * @return True if the password is hashed, false otherwise.
     */
    static boolean isPasswordHashed(String password) {
        return password != null && password.length() == 64 && password.matches("^[a-f0-9]{64}$");
    }

    /**
     * Gets the unique ID of the user.
     *
     * @return The user ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of the user.
     *
     * @param id The user ID to set.
     * @throws IllegalArgumentException If trying to change the ID after it's been set or if the ID is zero.
     */
    public void setId(int id) {
        if (this.id > 0) {
            throw new IllegalArgumentException("User ID cannot be changed after it's been set.");
        }
        if (id == 0) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
        this.id = id;
    }

    /**
     * Gets the first name of the user.
     *
     * @return The first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName The first name to set.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the user.
     *
     * @return The last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName The last name to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the mobile number of the user.
     *
     * @return The mobile number.
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets the mobile number of the user.
     *
     * @param mobile The mobile number to set.
     * @throws IllegalArgumentException If the mobile number is invalid (not exactly 10 digits).
     */
    public void setMobile(String mobile) {
        if (mobile == null || !mobile.matches("^\\d{10}$")) {
            throw new IllegalArgumentException("Mobile number must be exactly 10 digits.");
        }
        this.mobile = mobile;
    }

    /**
     * Gets the email address of the user.
     *
     * @return The email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email The email address to set.
     * @throws IllegalArgumentException If the email address is invalid.
     */

    public void setEmail(String email) {
        if (email == null || !email.matches("^(?=.{1,64}@)[A-Za-z0-9+_-]+(\\.[A-Za-z0-9+_-]+)*@[^-][A-Za-z0-9+-]+(\\.[A-Za-z0-9+-]+)*(\\.[A-Za-z]{2,})$")) {
            throw new IllegalArgumentException("Invalid email address.");
        }
        this.email = email;
    }

    /**
     * Gets the password of the user.
     *
     * @return The password (hashed if set correctly).
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the user. If the password is not already hashed, it will be hashed before storing.
     *
     * @param password The password to set.
     * @throws IllegalArgumentException If the password is null or empty.
     */
    public void setPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }
        this.password = isPasswordHashed(password) ? password : hashPassword(password);
    }

    /**
     * Gets the role of the user.
     *
     * @return The user's role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     *
     * @param role The role to set.
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Checks if the user account is active.
     *
     * @return True if the user account is active, false otherwise.
     */
    public boolean isUserActive() {
        return status;
    }

    /**
     * Sets the active status of the user account.
     *
     * @param status The active status to set.
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * Returns a string representation of the User object, including user details.
     *
     * @return A string containing user details.
     */
    @Override
    public String toString() {
        return "User{" +
                "userId='" + id + '\'' +
                ", userName='" + firstName + '\'' +
                ", userLastName='" + lastName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", userRole='" + role + '\'' +
                ", userStatus=" + status +
                '}';
    }
}
