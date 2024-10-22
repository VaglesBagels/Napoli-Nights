package com.example.napolinights.controller;

import com.example.napolinights.model.SqliteConnection;
import com.example.napolinights.model.UserDAO;
import com.example.napolinights.model.User;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Controller for handling the user Sign-Up process.
 * This includes validation, saving user data, and navigating to the login page.
 */
public class SignUpController {

    // FXML UI elements
    @FXML
    private ImageView logoImageView; // ImageView for the logo

    @FXML
    private TextField emailField; // TextField for user's email

    @FXML
    private Label lblEmailMessage; // Label for email validation message

    @FXML
    private PasswordField passwordField; // PasswordField for user's password

    @FXML
    private Label lblPasswordMessage; // Label for password validation message

    @FXML
    private TextField firstNameField; // TextField for user's first name

    @FXML
    private Label lblFirstNameMessage; // Label for first name validation message

    @FXML
    private TextField lastNameField; // TextField for user's last name

    @FXML
    private Label lblLastNameMessage; // Label for last name validation message

    @FXML
    private TextField mobileField; // TextField for user's mobile number

    @FXML
    private Label lblMobileMessage; // Label for mobile validation message

    @FXML
    private Label lblSignUpStatusMessage; // Label for overall sign-up status message

    @FXML
    private Hyperlink lnkLogin; // Hyperlink to navigate to the login page

    @FXML
    private AnchorPane signUpPane;

    /**
     * Initializes the controller, sets up the table columns, and adjusts the stage size.
     */
    @FXML
    private void initialize() {
        // Set padding for the login pane to provide spacing
        signUpPane.setPadding(new Insets(0, 0, 0, 10)); // Top, right, bottom, left padding

        // Ensure that the stage size is adjusted after the scene is loaded
        Platform.runLater(() -> {
            Stage stage = (Stage) signUpPane.getScene().getWindow();
            stage.setMinWidth(800);
            stage.setMinHeight(600);
        });
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

        // Validate email field
        if (email.trim().isEmpty()) {
            emailField.setStyle("-fx-border-color: red;");
            lblEmailMessage.setStyle("-fx-text-fill: red");
            lblEmailMessage.setVisible(true);
            isValid = false;
        } else {
            emailField.setStyle("");
            lblEmailMessage.setStyle("");
            lblEmailMessage.setVisible(false);
        }

        // Validate password field
        if (password.trim().isEmpty()) {
            passwordField.setStyle("-fx-border-color: red;");
            lblPasswordMessage.setStyle("-fx-text-fill: red");
            lblPasswordMessage.setVisible(true);
            isValid = false;
        } else {
            passwordField.setStyle("");
            lblPasswordMessage.setStyle("");
            lblPasswordMessage.setVisible(false);
        }

        // Validate first name field
        if (firstName.trim().isEmpty()) {
            firstNameField.setStyle("-fx-border-color: red;");
            lblFirstNameMessage.setStyle("-fx-text-fill: red");
            lblFirstNameMessage.setVisible(true);
            isValid = false;
        } else {
            firstNameField.setStyle("");
            lblFirstNameMessage.setStyle("");
            lblFirstNameMessage.setVisible(false);
        }

        // Validate last name field
        if (lastName.trim().isEmpty()) {
            lastNameField.setStyle("-fx-border-color: red;");
            lblLastNameMessage.setStyle("-fx-text-fill: red");
            lblLastNameMessage.setVisible(true);
            isValid = false;
        } else {
            lastNameField.setStyle("");
            lblLastNameMessage.setStyle("");
            lblLastNameMessage.setVisible(false);
        }

        // Validate mobile field (must be non-empty and a valid mobile number format)
        if (mobile.trim().isEmpty() || !mobile.matches("\\d{10}")) {
            mobileField.setStyle("-fx-border-color: red;");
            lblMobileMessage.setStyle("-fx-text-fill: red");
            lblMobileMessage.setText("Mobile number must be 10 digits");
            lblMobileMessage.setVisible(true);
            isValid = false;
        } else {
            mobileField.setStyle("");
            lblMobileMessage.setStyle("");
            lblMobileMessage.setVisible(false);
        }

        // Ensure the password is at least 6 characters long
        if (password.trim().length() < 6) {
            passwordField.setStyle("-fx-border-color: red;");
            lblPasswordMessage.setStyle("-fx-text-fill: red");
            lblPasswordMessage.setText("Password must be at least 6 characters");
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
     * Handles navigation to the login page.
     */
    @FXML
    private void handleLogin() {
        openLoginPage();
    }

    /**
     * Navigates to the home page (if the logic is implemented).
     */
    @FXML
    private void onHomeButtonClick() {
        // Add navigation to home page logic here
    }

    /**
     * Saves user data to the database.
     *
     * @param user The user object to be saved.
     */
    private void saveUserData(User user) {
        System.out.println("User data saved!");
        Connection connection = SqliteConnection.getInstance();
        UserDAO userDAO = new UserDAO(connection);

        try {
            userDAO.addUser(user);
            System.out.println("User data saved to the database!");
        } catch (SQLException ex) {
            System.out.println("Error encountered while signing up the user!");
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Opens the login page (Login.fxml) after successful sign-up.
     */
    private void openLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent menuPage = loader.load();
            Stage stage = (Stage) this.lnkLogin.getScene().getWindow();
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setTitle("Sign Up here");
            Scene scene = new Scene(menuPage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the sign-up status message.
     * Displays error messages when needed.
     *
     * @param errorMessage The error message to display.
     */
    private void setSignUpStatusMessage(String errorMessage) {
        if (errorMessage != null) {
            lblSignUpStatusMessage.setText(errorMessage);
            lblSignUpStatusMessage.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            lblSignUpStatusMessage.setVisible(true);
        } else {
            lblSignUpStatusMessage.setStyle("");
            lblSignUpStatusMessage.setText(null);
            lblSignUpStatusMessage.setVisible(false);
        }
    }
}
