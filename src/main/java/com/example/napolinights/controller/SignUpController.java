package com.example.napolinights.controller;

import com.example.napolinights.model.SqliteConnection;
import com.example.napolinights.model.UserDAO;
import com.example.napolinights.model.User;

import com.example.napolinights.util.StageConstants;
import com.example.napolinights.util.StyleConstants;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * Controller for handling the user Sign-Up process.
 * This includes validation, saving user data, and navigating to the login page.
 */
public class SignUpController {

    // FXML UI elements
    @FXML private TextField emailField;             // User's email input
    @FXML private Label lblEmailMessage;            // Validation message for email
    @FXML private PasswordField passwordField;      // User's password input
    @FXML private Label lblPasswordMessage;         // Validation message for password
    @FXML private TextField firstNameField;         // User's first name input
    @FXML private Label lblFirstNameMessage;        // Validation message for first name
    @FXML private TextField lastNameField;          // User's last name input
    @FXML private Label lblLastNameMessage;         // Validation message for last name
    @FXML private TextField mobileField;            // User's mobile number input
    @FXML private Label lblMobileMessage;           // Validation message for mobile number
    @FXML private Label lblSignUpStatusMessage;     // Overall sign-up status message
    @FXML private Hyperlink lnkLogin;               // Link to login page
    @FXML private AnchorPane signUpPane;            // Main container for the sign-up page


    /**
     * Initializes the controller, applying padding and setting default stage size.
     */
    @FXML
    private void initialize() {
        signUpPane.setPadding(new Insets(0, 0, 0, 10)); // Left padding for layout

        // Ensure consistent stage sizing using StageConstants
        Platform.runLater(() -> StageConstants.setStageSize((Stage) signUpPane.getScene().getWindow()));
    }


    /**
     * Handles the sign-up process when the user submits the form.
     * Gathers user data from the form, validates fields, and saves user data if valid.
     */
    @FXML
    private void handleSignUp() {
        try {
            // Gather data from the form fields
            String email = emailField.getText();
            String password = passwordField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String mobile = mobileField.getText();

            // Validate the input fields
            boolean isValid = validateFields(email, password, firstName, lastName, mobile);

            // If validation passes, save the user data and navigate to login page
            if (isValid) {
                User userToSave = new User(firstName, lastName, mobile, email, password);
                saveUserData(userToSave);
                openLoginPage();
            } else {
                System.out.println("Sign-up failed due to validation errors.");
            }
        } catch (SQLException sqlEx) {
            if (sqlEx.getMessage().contains("UNIQUE constraint failed")) {
                // Provide a general message without specifying which field caused the constraint violation
                setSignUpStatusMessage("An account with this information already exists.");
            } else {
                setSignUpStatusMessage("An error occurred while saving user data. Please try again.");
            }
        } catch (Exception ex) {
            System.out.println("Sign Up Failed. An error occurred during Sign-Up. Please try again.");
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Validates the input fields (email, password, first name, last name, mobile).
     * Displays validation messages for invalid fields.
     */
    private boolean validateFields(String email, String password, String firstName, String lastName, String mobile) {
        boolean isValid = true;

        isValid &= validateField(emailField, lblEmailMessage, !email.trim().isEmpty(), "Email is required.");
        isValid &= validateField(passwordField, lblPasswordMessage, !password.trim().isEmpty() && password.length() >= StyleConstants.MIN_PASSWORD_LENGTH,
                "Password must be at least " + StyleConstants.MIN_PASSWORD_LENGTH + " characters.");
        isValid &= validateField(firstNameField, lblFirstNameMessage, !firstName.trim().isEmpty(), "First name is required.");
        isValid &= validateField(lastNameField, lblLastNameMessage, !lastName.trim().isEmpty(), "Last name is required.");
        isValid &= validateField(mobileField, lblMobileMessage, !mobile.trim().isEmpty() && mobile.matches("\\d{10}"),
                "Mobile number must be 10 digits.");

        return isValid;
    }

    /**
     * Validates a single input field, applying error styling and setting message if invalid.
     * @param field The input field to validate.
     * @param messageLabel The label to display the validation message.
     * @param condition Condition to validate the field against.
     * @param message Message to display if validation fails.
     * @return true if the field passes validation, false otherwise.
     */
    private boolean validateField(TextField field, Label messageLabel, boolean condition, String message) {
        if (!condition) {
            field.setStyle(StyleConstants.BORDER_ERROR_STYLE);
            messageLabel.setText(message);
            messageLabel.setStyle(StyleConstants.ERROR_STYLE);
            messageLabel.setVisible(true);
            return false;
        } else {
            field.setStyle("");
            messageLabel.setText("");
            messageLabel.setVisible(false);
            return true;
        }
    }

    /**
     * Handles navigation to the login page.
     */
    @FXML
    private void handleLogin() {
        openLoginPage();
    }


    /**
     * Saves user data to the database.
     *
     * @param user The user object to be saved.
     */
    private void saveUserData(User user) throws SQLException {
        Connection connection = SqliteConnection.getInstance();
        try {
            UserDAO userDAO = new UserDAO(connection);
            userDAO.addUser(user);
        } catch (SQLException ex) {
            System.out.println("Error encountered while signing up the user!");
            System.out.println(ex.getMessage());
            throw ex;
        } finally {
            SqliteConnection.closeConnection(); // Close the connection
        }
    }

    /**
     * Opens the login page (Login.fxml) after successful sign-up.
     */
    private void openLoginPage() {
        Stage stage = (Stage) this.lnkLogin.getScene().getWindow();  // Get the current stage
        StageConstants.openLoginPage(stage);  // Use specific utility method for login page
    }

    /**
     * Sets the sign-up status message.
     * Displays error messages when needed.
     * @param errorMessage The error message to display.
     */
    private void setSignUpStatusMessage(String errorMessage) {
        lblSignUpStatusMessage.setText(errorMessage);
        lblSignUpStatusMessage.setStyle(StyleConstants.ERROR_STYLE);
        lblSignUpStatusMessage.setVisible(errorMessage != null && !errorMessage.isEmpty());
    }

}
