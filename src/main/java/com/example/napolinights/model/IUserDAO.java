package com.example.napolinights.model;

import java.sql.SQLException;

/**
 * Interface defining operations for user management in the system.
 */
public interface IUserDAO {
    /**
     * Adds a new user to the database. The user will be added as active by default.
     *
     * @param user The user object containing the details of the user to be added.
     * @throws SQLException If an error occurs while interacting with the database.
     */
    void addUser(User user) throws SQLException;

    /**
     * Verifies the user's access by checking the provided login credentials (email or mobile)
     * and password.
     *
     * @param loginCredential The email or mobile number of the user.
     * @param password The plain-text password entered by the user.
     * @return true if the login credentials and password match, false otherwise.
     * @throws SQLException If an error occurs while interacting with the database.
     */
    boolean verifyUserAccess(String loginCredential, String password) throws SQLException;

    /**
     * Retrieves the details for the specified user.
     *
     * @param userId The unique ID of the user whose password is to be changed.
     * @throws SQLException If an error occurs while interacting with the database.
     */
    User getUserDetails(int userId) throws SQLException;

    /**
     * Changes the password for the specified user.
     *
     * @param userId The unique ID of the user whose password is to be changed.
     * @param newPassword The new plain-text password to be set for the user.
     * @throws SQLException If an error occurs while interacting with the database.
     */
    void changePassword(int userId, String newPassword) throws SQLException;
}
