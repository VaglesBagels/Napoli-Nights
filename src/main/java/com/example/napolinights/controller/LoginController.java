package com.example.napolinights.controller;

import com.example.napolinights.model.SqliteConnection;
import com.example.napolinights.model.User;
import com.example.napolinights.model.UserDAO;
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
 * Controller class for the Login page. Handles user login, validation,
 * and navigation to the appropriate pages (e.g., sign-up, staff landing page).
 */
public class LoginController {

    // FXML elements from the Login page
    @FXML private TextField emailField; // The user's email
    @FXML private Label lblEmailMessage; // Error message for the email field
    @FXML private TextField passwordField; // The user's password
    @FXML private Label lblPasswordMessage; // Error message for the password field
    @FXML private Hyperlink lnkSignUp; // Hyperlink to the Sign Up page
    @FXML private AnchorPane loginPane; // The main login pane
    @FXML private Button btnLogin; // The login button
    @FXML private Label lblLoginStatusMessage; // Status message for login


    /**
     * Initializes the controller, sets up the table columns, and adjusts the stage size.
     */
    @FXML
    private void initialize() {
        // Set padding for the login pane to provide spacing
        loginPane.setPadding(new Insets(0, 0, 0, 10)); // Top, right, bottom, left padding

        // Ensure that the stage size is adjusted after the scene is loaded
        Platform.runLater(() -> {
            Stage stage = (Stage) loginPane.getScene().getWindow();
            stage.setMinWidth(800);
            stage.setMinHeight(600);
        });
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
            lblLoginStatusMessage.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
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
    private boolean validateFields(String email, String password) {
        boolean isValid = true;

        // Check if email field is empty
        if (email.trim().isEmpty()) {
            emailField.setStyle("-fx-border-color: red;");
            lblEmailMessage.setStyle("-fx-text-fill: red;");
            lblEmailMessage.setVisible(true);
            isValid = false;
        } else {
            emailField.setStyle("");
            lblEmailMessage.setStyle("");
            lblEmailMessage.setVisible(false);
        }

        // Check if password field is empty
        if (password.trim().isEmpty()) {
            passwordField.setStyle("-fx-border-color: red;");
            lblPasswordMessage.setStyle("-fx-text-fill: red;");
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
        UserDAO userDAO = new UserDAO(connection);
        User user = userDAO.verifyUserAccess(email, password);  //VerifyUserAccess Returns a User object when successful.
        if (user != null && user.isUserActive()) {
            lblLoginStatusMessage.setStyle("");
            lblLoginStatusMessage.setText(null);
            lblLoginStatusMessage.setVisible(false);
            openStaffLandingPage(); // Proceed to staff landing page if login is successful
        } else {
            lblLoginStatusMessage.setText("Incorrect Username or Password. Please try again.");
            lblLoginStatusMessage.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            lblLoginStatusMessage.setVisible(true);
            throw new SQLException("Login failed due to incorrect credentials.");
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignUp.fxml"));
            Parent signUpPage = loader.load();

            Stage stage = (Stage) this.lnkSignUp.getScene().getWindow();
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setTitle("Sign Up");

            Scene scene = new Scene(signUpPage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Opens the Staff Landing Page (StaffLandingPage.fxml) after a successful login.
     */
    private void openStaffLandingPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StaffLandingPage.fxml"));
            Parent landingPage = loader.load();

            Stage stage = (Stage) this.btnLogin.getScene().getWindow();
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setTitle("Napoli Nights");

            Scene scene = new Scene(landingPage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
