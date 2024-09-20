package com.example.napolinights;

import com.example.napolinights.model.SqliteConnection;
import com.example.napolinights.model.UserDAO;
import com.example.napolinights.model.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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
    private Label lblSignUpStatusMessage;

    @FXML
    private Hyperlink lnkLogin;

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
                User userToSave = new User(0, firstName, lastName, mobile, email, password, "Staff", true);

                // Save to database
                saveUserData(userToSave);
                openLoginPage();
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
    private void handleLogin() {
        openLoginPage();
    }

    @FXML
    private void onHomeButtonClick() {
        // Add navigation to home page logic here
    }

    // Stub method to handle user data saving
    private void saveUserData(User user) {
        System.out.println("User data saved!"); // + user);
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

    private void openLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/napolinights/Login.fxml"));
            Parent menuPage = loader.load();

            Stage stage = (Stage) this.lnkLogin.getScene().getWindow();
            stage.setTitle("Sign Up here");
            Scene scene = new Scene(menuPage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

