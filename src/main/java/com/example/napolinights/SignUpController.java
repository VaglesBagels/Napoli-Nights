package com.example.napolinights;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignUpController {

    @FXML
    private TextField firstNameField;

    @FXML
    private Label firstNameMessageLabel;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField roleField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void onSignUpButtonClick() {
        boolean isValid = validateFields();
        if (!isValid) {
            // signupMessage.setText("* Required field(s) are empty"); // Update a message field or use Alert
        } else {
            //signupMessage.setText(""); // Clear message
            // Proceed with sign-up logic
        }
    }

    private boolean validateFields() {
        boolean isValid = true;
        if (firstNameField.getText().trim().isEmpty()) {
            isValid = false;
            firstNameField.setStyle("-fx-border-color: red;");
            firstNameMessageLabel.setText("* Required field is empty.");
            firstNameMessageLabel.setStyle("-fx-text-fill: red;");
            firstNameMessageLabel.setVisible(true);
        } else {
            firstNameField.setStyle("");
        }
        if (lastNameField.getText().trim().isEmpty()) {
            isValid = false;
            lastNameField.setStyle("-fx-border-color: red;");
        } else {
            lastNameField.setStyle("");
        }
        // Repeat for other fields: employeeIdField, emailField, passwordField

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
}

