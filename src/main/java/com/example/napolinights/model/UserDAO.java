package com.example.napolinights.model;

import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Implementation of the IUserDAO interface for managing user data in a database.
 */
public class UserDAO implements IUserDAO {
    private final Connection connection;

    /**
     * Constructs a UserDAO instance with the provided database connection.
     */
    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public void createUserTable() {
        try {
            Statement createUserTable = connection.createStatement();
            createUserTable.execute(
                    "CREATE TABLE IF NOT EXISTS users ( " +
                            "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "user_first_name VARCHAR(50) NOT NULL, " +
                            "user_last_name VARCHAR(50) NOT NULL, " +
                            "mobile VARCHAR(15) NOT NULL UNIQUE, " +
                            "email VARCHAR(100) NOT NULL UNIQUE, " +
                            "password VARCHAR(255) NOT NULL, " +
                            "user_role VARCHAR(20) NOT NULL, " +
                            "user_status BOOLEAN NOT NULL" +
                            ")"
            );

            System.out.println("User table created");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Adds a new user to the database. The user will be added as active by default.
     *
     * @param user The user object containing the details of the user to be added.
     * @throws SQLException If an error occurs while interacting with the database.
     */
    @Override
    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO Users (user_first_name, user_last_name, mobile, email, password, user_role, user_status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUserFirstName());
            statement.setString(2, user.getUserLastName());
            statement.setString(3, user.getMobile());
            statement.setString(4, user.getEmail());
            statement.setString(5, hashPassword(user.getPassword()));
            statement.setString(6, user.getUserRole());
            statement.setBoolean(7, user.isUserActive());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Adding user failed, no rows affected.");
            }

            // Retrieve the generated userID
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getInt(1)); // Assume userID is an int
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }
    }

    /**
     * Verifies the user's access by checking the provided login credentials (email or mobile)
     * and password.
     *
     * @param username The email or mobile number of the user.
     * @param password The plain-text password entered by the user.
     * @return true if the login credentials and password match, false otherwise.
     * @throws SQLException If an error occurs reading from database
     * ror occurs while interacting with the database.
     */
    @Override
    public boolean verifyUserAccess(String username, String password) throws SQLException {
        String sql = "SELECT * FROM Users WHERE (email = ? OR mobile = ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, username);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String storedHashedPassword = rs.getString("password");
                    boolean userStatus = rs.getBoolean("user_status");
                    if (!userStatus) {
                        throw new Exception("User is not active");
                    }
                    return verifyPassword(password, storedHashedPassword);
                } else {
                    return false;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public User getUserDetailsById(int userId) throws SQLException {
        String sql = "SELECT * FROM Users WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId); // Correct parameter index

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    // Retrieve user details and create User object
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("user_first_name"),
                            rs.getString("user_last_name"),
                            rs.getString("mobile"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("user_role"),
                            rs.getBoolean("user_status")
                    );
                } else {
                    throw new SQLException("User not found with ID: " + userId);
                }
            }
        }
    }

    /**
     * Changes the password for the specified user.
     *
     * @param userId The unique ID of the user whose password is to be changed.
     * @param newPassword The new plain-text password to be set for the user.
     * @throws SQLException If an error occurs while interacting with the database.
     */
    @Override
    public void changePassword(int userId, String newPassword) throws SQLException {
        String sql = "UPDATE Users SET password = ? WHERE user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hashPassword(newPassword));
            statement.setInt(2, userId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating password failed, no rows affected.");
            }
        }
    }

    /**
     * Hashes the provided plain-text password using the SHA-256 algorithm.
     *
     * @param password The plain-text password to be hashed.
     * @return The hashed password in hexadecimal format.
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
     * Verifies if the provided plain-text password matches the hashed password.
     *
     * @param password The plain-text password.
     * @param hashedPassword The stored hashed password.
     * @return true if the passwords match, false otherwise.
     */
    private boolean verifyPassword(String password, String hashedPassword) {
        return hashPassword(password).equals(hashedPassword);
    }
}