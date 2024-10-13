package com.example.napolinights.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Controller for handling actions on the Landing Page.
 * This includes navigation to other pages like Menu, Order, and Login.
 */
public class LandingPageController {

    // UI elements from the FXML file
    @FXML
    private Button menuButton;

    @FXML
    private Button orderButton;

    @FXML
    private ImageView logoImageView;

    @FXML
    private HBox imageBox;

    /**
     * This method is called automatically after the FXML file is loaded.
     * It can be used to initialize UI elements or logic when the page is loaded.
     */
    @FXML
    public void initialize() {
        System.out.println("LandingPageController initialized");
    }

    /**
     * Event handler for the "View Menu" button.
     * Opens the Menu page when clicked.
     */
    @FXML
    private void handleViewMenu() {
        System.out.println("View Menu button clicked");
        openMenuPage();
    }

    /**
     * Event handler for the "Order Here" button.
     * Opens the Order page when clicked.
     */
    @FXML
    private void handleOrder() {
        System.out.println("Order Here button clicked");
        openOrderPage();
    }

    /**
     * Event handler for the "Staff" button.
     * Opens the Login page when clicked.
     */
    @FXML
    private void handleAdminAccess() {
        System.out.println("Staff button clicked");
        openLoginPage();
    }

    /**
     * Opens the Menu page (Menu.fxml).
     * Sets a fixed window size for consistency.
     */
    private void openMenuPage() {
        try {
            // Load the Menu page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Menu.fxml"));
            Parent menuPage = loader.load();

            // Get the current stage (window) and set properties
            Stage stage = (Stage) this.menuButton.getScene().getWindow();
            stage.setMinWidth(800); // Only set the minimum width
            stage.setMinHeight(600); // Only set the minimum height
            stage.setTitle("View Menu");

            // Allow the window to be maximized and resized freely
            stage.setResizable(true);

            // Set the new scene to the stage and display it
            Scene scene = new Scene(menuPage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error occurred while opening the Menu page.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Opens the Order page (Order.fxml).
     * Sets a fixed window size for consistency.
     */
    private void openOrderPage() {
        try {
            // Load the Order page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Order.fxml"));
            Parent orderPage = loader.load();

            // Get the current stage (window) and set properties
            Stage stage = (Stage)this.menuButton.getScene().getWindow();
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setTitle("Order Here");

            // Set the new scene to the stage and display it
            Scene scene = new Scene(orderPage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error occurred while opening the Order page.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Opens the Login page (Login.fxml).
     * Sets a fixed window size for consistency.
     */
    private void openLoginPage() {
        try {
            // Load the Login page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent loginPage = loader.load();

            // Get the current stage (window) and set properties
            Stage stage = (Stage)this.menuButton.getScene().getWindow();
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setTitle("Login");

            // Set the new scene to the stage and display it
            Scene scene = new Scene(loginPage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error occurred while opening the Login page.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}

