package com.example.napolinights;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class LandingPageController {

    @FXML
    private Button menuButton;

    @FXML
    private Button orderButton;

    @FXML
    private void handleViewMenu() {
        System.out.println("View Menu button clicked");
        // Add logic to handle the View Menu button click
        openMenuPage();
    }

    private void openMenuPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/napolinights/Menu.fxml"));
            Parent menuPage = loader.load();

            Stage stage = (Stage) this.menuButton.getScene().getWindow();
            stage.setTitle("View Menu");
            Scene scene = new Scene(menuPage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOrder() {
        System.out.println("Order button clicked");
        // Add logic to handle the Order button click
        openOrderPage();
    }

    @FXML
    private void handleViewCart() {
        System.out.println("View Cart button clicked");
        // Add logic to handle the View Cart button click
    }

    @FXML
    private void handleAdminAccess() {
        System.out.println("Admin button clicked");
        // Add logic to handle the Admin button click
        openLoginPage();
    }

    private void openOrderPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/napolinights/Order.fxml"));
            Parent orderPage = loader.load();

            Stage stage = (Stage)this.menuButton.getScene().getWindow();
            stage.setTitle("Order Here");
            Scene scene = new Scene(orderPage);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/napolinights/Login.fxml"));
            Parent menuPage = loader.load();

            Stage stage = (Stage)this.menuButton.getScene().getWindow();
            stage.setTitle("Login");
            Scene scene = new Scene(menuPage);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}




