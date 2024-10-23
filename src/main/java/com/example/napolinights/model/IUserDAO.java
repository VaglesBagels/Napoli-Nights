package com.example.napolinights.model;

import java.sql.SQLException;

/**
 * Interface defining operations for user management in the system.
 * <p>
 * This interface provides methods for interacting with the user table in the database, allowing
 * for the addition of new users, verification of login credentials, retrieval of user details,
 * and password management.
 * </p>
 */
public interface IUserDAO {

    /**
     * Adds a new user to the database. The user will be added as active by default.
     * <p>
     * The {@code User} object should contain all necessary details, such as first name, last name,
     * mobile number, email, password, role, and status.
     * </p>
     *
     * @param user The {@link User} object containing the details of the user to be added.
     * @throws SQLException If an error occurs while interacting with the database, such as a constraint violation.
     */
    void addUser(User user) throws SQLException;

    /**
     * Verifies the user's access by checking the provided login credentials (email or mobile number)
     * and the password. If both the credentials and password match, the method returns the corresponding
     * {@link User} object.
     * <p>
     * This method uses either the email or mobile number for identification and verifies the
     * provided plain-text password after hashing.
     * </p>
     *
     * @param loginCredential The email or mobile number of the user attempting to log in.
     * @param password        The plain-text password entered by the user.
     * @return The {@link User} object if the login credentials and password match;
     *         throws {@code SQLException} if no match is found or if an error occurs.
     * @throws SQLException If an error occurs while interacting with the database or if login credentials are invalid.
     */
    User verifyUserAccess(String loginCredential, String password) throws SQLException;

    /**
     * Retrieves the details of the specified user based on the provided user ID.
     * <p>
     * The method returns a {@link User} object that contains the user's information, including their
     * first name, last name, contact details, and other relevant attributes.
     * </p>
     *
     * @param userId The unique ID of the user whose details are to be retrieved.
     * @return The {@link User} object representing the user with the specified ID.
     * @throws SQLException If an error occurs while interacting with the database, or if the user is not found.
     */
    User getUserDetailsById(int userId) throws SQLException;

    /**
     * Changes the password for the specified user.
     * <p>
     * The method takes the user's ID and the new plain-text password, hashes it, and updates the
     * password field in the database.
     * </p>
     *
     * @param userId      The unique ID of the user whose password is to be changed.
     * @param newPassword The new plain-text password to be set for the user.
     * @throws SQLException If an error occurs while interacting with the database, or if the update fails.
     */
    void changePassword(int userId, String newPassword) throws SQLException;
}
