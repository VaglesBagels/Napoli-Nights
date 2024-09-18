package com.example.napolinights;

import com.example.napolinights.model.SqliteConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.example.napolinights.model.UserDAO;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private Label lblEmailMessage;

    @FXML
    private TextField passwordField;

    @FXML
    private Label lblPasswordMessage;

    @FXML
    private Hyperlink lnkSignUp;

    @FXML
    private Button btnLogin;

    @FXML
    private Label lblLoginStatusMessage;

    @FXML
    private void handleLogin() {
        try {
            String email = emailField.getText();
            String password = passwordField.getText();

            boolean isValid = validateFields(
                    email,
                    password
            );

            if (isValid) {
                // Login processing here
                login(email, password);
            } else {
                System.out.println("Login failed due to validation errors.");
            }

        } catch (Exception ex) {
            System.out.println("Login Failed. An error occured during login. PLease try again.");
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void handleForgotPassword() {
        System.out.println("Handle Forgot password here");
    }

    @FXML
    private void handleSignUp() {
        openSignUpPage();
    }

    private void openSignUpPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/napolinights/SignUp.fxml"));
            Parent menuPage = loader.load();

            Stage stage = (Stage) this.lnkSignUp.getScene().getWindow();
            stage.setTitle("Sign Up here");
            Scene scene = new Scene(menuPage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validateFields(
        String email,
        String password
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

        return isValid;
    }

    private void login(String email, String password) throws SQLException {
        System.out.println("Login here");
        Connection connection = SqliteConnection.getInstance();
        UserDAO userDAO = new UserDAO(connection);
        boolean userHasAccess = userDAO.verifyUserAccess(email, password);
        if (userHasAccess) {
            lblLoginStatusMessage.setStyle("");
            lblLoginStatusMessage.setText(null);
            lblLoginStatusMessage.setVisible(false);
            openStaffLandingPage();
        }
        else {
            lblLoginStatusMessage.setText("Incorrect Username or Password. Please try again.");
            lblLoginStatusMessage.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            lblLoginStatusMessage.setVisible(true);
            throw new SQLException("Login failed due to validation errors.");
        }
    }

    private void openStaffLandingPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/napolinights/StaffLandingPage.fxml"));
            Parent landingPage = loader.load();

            Stage stage = (Stage) this.btnLogin.getScene().getWindow();
            stage.setTitle("Napoli Nights");
            Scene scene = new Scene(landingPage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
