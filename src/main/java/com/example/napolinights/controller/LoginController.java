package com.example.napolinights.controller;

import com.example.napolinights.model.SqliteConnection;
import com.example.napolinights.model.User;
import com.example.napolinights.model.UserDAO;
import com.example.napolinights.util.StageConstants;
import com.example.napolinights.util.StyleConstants;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * Controller class for handling login functionality, including
 * user validation and navigation to other pages (e.g., sign-up and staff landing).
 */
public class LoginController {

    // FXML-linked UI elements
    @FXML private TextField emailField;              // User's email input field
    @FXML private Label lblEmailMessage;             // Error message for the email field
    @FXML private TextField passwordField;           // User's password input field
    @FXML private Label lblPasswordMessage;          // Error message for the password field
    @FXML private Hyperlink lnkSignUp;               // Link to the sign-up page
    @FXML private AnchorPane loginPane;              // Main container for the login page
    @FXML private Button btnLogin;                   // Button to submit login
    @FXML private Label lblLoginStatusMessage;       // Status message for login


    /**
     * Initializes the controller by setting padding for layout
     * and ensuring consistent stage dimensions.
     */
    @FXML
    private void initialize() {
        loginPane.setPadding(new Insets(0, 0, 0, 10)); // Padding on left side of login pane

        // Ensure consistent stage sizing using StageConstants
        Platform.runLater(() -> StageConstants.setStageSize((Stage) loginPane.getScene().getWindow()));
    }


    /**
     * Event handler for the login button click.
     * Validates the input fields and attempts to log in the user.
     */
    @FXML
    private void handleLogin() {
        try {
            String email = emailField.getText();
            String password = passwordField.getText();

            // Validate the email and password fields
            boolean isValid = validateFields(email, password);

            // Proceed with login if validation is successful
            if (isValid) {
                login(email, password);
            } else {
                System.out.println("Login failed due to validation errors.");
            }

        } catch (SQLException sqlEx) {
            System.out.println("SqlException occured. Please try again.");
            System.out.println(sqlEx.getMessage());
            lblLoginStatusMessage.setText(sqlEx.getMessage());
            lblLoginStatusMessage.setStyle(StyleConstants.ERROR_STYLE);
            lblLoginStatusMessage.setVisible(true);
        } catch (Exception ex) {
            System.out.println("Login Failed. An error occurred during login. Please try again.");
            System.out.println(ex.getMessage());
        }
    }


    /**
     * Validates the email and password input fields.
     * @param email The email entered by the user.
     * @param password The password entered by the user.
     * @return true if both fields are valid, false otherwise.
     */
    /**
     * Validates the email and password input fields.
     * @param email The email entered by the user.
     * @param password The password entered by the user.
     * @return true if both fields are valid, false otherwise.
     */
    private boolean validateFields(String email, String password) {
        boolean isValid = true;

        if (email.trim().isEmpty()) {
            emailField.setStyle(StyleConstants.BORDER_ERROR_STYLE);
            lblEmailMessage.setStyle(StyleConstants.ERROR_STYLE);
            lblEmailMessage.setVisible(true);
            isValid = false;
        } else {
            emailField.setStyle("");
            lblEmailMessage.setStyle("");
            lblEmailMessage.setVisible(false);
        }

        // Check if password field is empty
        if (password.trim().isEmpty()) {
            passwordField.setStyle(StyleConstants.BORDER_ERROR_STYLE);
            lblPasswordMessage.setStyle(StyleConstants.ERROR_STYLE);
            lblPasswordMessage.setVisible(true);
            isValid = false;
        } else {
            passwordField.setStyle("");
            lblPasswordMessage.setStyle("");
            lblPasswordMessage.setVisible(false);
        }

        return isValid;
    }


    /**
     * Attempts to log in the user with the provided email and password.
     * @param email The email entered by the user.
     * @param password The password entered by the user.
     * @throws SQLException if login fails due to validation errors.
     */
    private void login(String email, String password) throws SQLException {
        System.out.println("Attempting login");
        Connection connection = SqliteConnection.getInstance();
        try {
            UserDAO userDAO = new UserDAO(connection);
            User user = userDAO.verifyUserAccess(email, password);  //VerifyUserAccess Returns a User object when successful.
            if (user != null && user.isUserActive()) {
                lblLoginStatusMessage.setStyle("");
                lblLoginStatusMessage.setText(null);
                lblLoginStatusMessage.setVisible(false);
                openStaffLandingPage(); // Proceed to staff landing page if login is successful
            } else {
                lblLoginStatusMessage.setText("Incorrect Username or Password. Please try again.");
                lblLoginStatusMessage.setStyle(StyleConstants.ERROR_STYLE);
                lblLoginStatusMessage.setVisible(true);
                throw new SQLException("Login failed due to incorrect credentials.");
            }
        } catch (SQLException sqlEx) {
            System.out.println(sqlEx.getMessage());
            throw sqlEx;
        } finally {
            SqliteConnection.closeConnection(); // Close the connection
        }
    }


    /**
     * Event handler for the "Forgot Password" hyperlink.
     * Currently just prints a message to the console.
     */
    @FXML
    private void handleForgotPassword() {
        System.out.println("Handle Forgot Password here");
    }


    /**
     * Event handler for the "Sign Up" hyperlink.
     * Opens the Sign Up page when clicked.
     */
    @FXML
    private void handleSignUp() {
        openSignUpPage();
    }


    /**
     * Opens the Sign Up page (SignUp.fxml).
     */
    private void openSignUpPage() {
        Stage stage = (Stage) this.lnkSignUp.getScene().getWindow();  // Get the current stage
        StageConstants.openSignUpPage(stage);  // Use specific utility method for login page
    }


    /**
     * Opens the Staff Landing Page (StaffLandingPage.fxml) after a successful login.
     */
    private void openStaffLandingPage() {
        Stage stage = (Stage) this.btnLogin.getScene().getWindow();  // Get the current stage
        StageConstants.openStaffLandingPage(stage);  // Use specific utility method for login page
    }
}
