package com.example.napolinights.model;
// TODO: Need to refactor this class with respects to the password validation and security.
//  Hashing the password should belong to this class (or create a Password class) and not UserDAO.
//  Passwords should not be stored in plain text at all even when creating a new class.

/**
 * Class representing a user in the system.
 */
public class User {
    private int userId;
    private String userFirstName;
    private String userLastName;
    private String mobile;
    private String email;
    private String password;
    private String userRole;
    private boolean userStatus;

    /**
     * Constructs a User instance for creating new user accounts.
     *
     * @param userFirstName The first name of the user.
     * @param userLastName The last name of the user.
     * @param mobile The mobile phone number of the user.
     * @param email The email address of the user.
     * @param password The plain-text password of the user.
     */
    public User(String userFirstName, String userLastName, String mobile, String email, String password) {
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.mobile = mobile;
        this.email = email;
        this.password = password != null ? password : ""; // Handle null password.
        this.userRole = "staff";  // Default role
        this.userStatus = true; // Default status
    }

    /**
     * Constructs a User instance for existing users that have already been created in a database.
     *
     * @param userId The ID of the user in the database.
     * @param userFirstName The first name of the user.
     * @param userLastName The last name of the user.
     * @param mobile The mobile phone number of the user.
     * @param email The email address of the user.
     * @param password The plain-text password of the user.
     * @param userRole The role assigned to the user (e.g., admin, staff).
     * @param userStatus Indicates whether the user account is active or not.
     */
    public User(int userId, String userFirstName, String userLastName, String mobile, String email, String password, String userRole, boolean userStatus) {
        this(userFirstName, userLastName, mobile, email, password);
        this.userId = userId;
        this.userRole = userRole;
        this.userStatus = userStatus;
        this.password = password;  // When reading from DB password is already hashed.
    }

    public int getUserId() { return userId; }

    public void setUserId(int userId) {
        if (this.userId > 0) {
            throw new IllegalArgumentException("User ID cannot be changed after it's been set.");
        }
        if (userId == 0) {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
        this.userId = userId;
    }

    public String getUserFirstName() { return userFirstName; }

    public void setUserFirstName(String userFirstName) { this.userFirstName = userFirstName; }

    public String getUserLastName() { return userLastName; }

    public void setUserLastName(String userLastName) { this.userLastName = userLastName; }

    public String getMobile() { return mobile; }

    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    // This function should be removed as there is no need to get the hashed password.
    public String getPassword() { return password; }

    public void setPassword(String password) {
        this.password = password != null ? password : ""; // Handle null password
    }

    public String getUserRole() { return userRole; }

    public void setUserRole(String userRole) { this.userRole = userRole; }

    public boolean isUserActive() { return userStatus; }

    public void setUserStatus(boolean userStatus) { this.userStatus = userStatus; }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userFirstName + '\'' +
                ", userLastName='" + userLastName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", userRole='" + userRole + '\'' +
                ", userStatus=" + userStatus +
                '}';
    }
}
