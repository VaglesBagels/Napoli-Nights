package com.example.napolinights;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;

import java.time.LocalDate;

public class SignUpController {

    @FXML
    private ImageView logoImageView;

    @FXML
    private TextField emailField;

    @FXML
    private Label lblEmailMessage;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label lblPasswordMessage;

    @FXML
    private TextField firstNameField;

    @FXML
    private Label lblFirstNameMessage;

    @FXML
    private TextField lastNameField;

    @FXML
    private Label lblLastNameMessage;

    @FXML
    private TextField mobileField;

    @FXML
    private void handleSignUp() {
        try {
            // Gather data from the form
            String email = emailField.getText();
            String password = passwordField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String mobile = mobileField.getText();

            boolean isValid = validateFields(
                    email,
                    password,
                    firstName,
                    lastName
            );

            if (isValid) {
                // Save to database
                saveUserData();
                // showAlert(AlertType.INFORMATION, "Signup Successful", "You have successfully signed up!");
            } else {
                System.out.println("Login failed due to validation errors.");
            }

        } catch (Exception ex) {
            System.out.println("Sign Up Failed. An error occurred during Sign-Up. Please try again.");
            System.out.println(ex.getMessage());
            // showAlert(AlertType.ERROR, "Signup Failed", "An error occurred during Sign-Up. Please try again.");
        }

    }

    private boolean validateFields(
        String email,
        String password,
        String firstName,
        String lastName
    ) {
        boolean isValid = true;
        // Basic validation check (add more as needed)
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

    @FXML
    private void onLoginButtonClick() {
        // Add navigation to login page logic here
    }

    @FXML
    private void onHomeButtonClick() {
        // Add navigation to home page logic here
    }

    // Stub method to handle user data saving
    private void saveUserData() { //User user) {
        System.out.println("User data saved!"); // + user);
    }
}

