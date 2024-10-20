package com.example.napolinights.controller;

import com.example.napolinights.util.StageConstants;  // Import StageUtil class for setting stage size
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller for handling actions on the Landing Page.
 * This includes navigation to other pages like Menu, Order, and Login.
 */
public class LandingPageController {

    // UI elements from the FXML file
    @FXML private Button menuButton;
    @FXML private Button orderButton;
    @FXML private Button adminButton;


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
        Stage stage = (Stage) menuButton.getScene().getWindow();  // Get the current stage
        StageConstants.openPage("/view/Menu.fxml", stage, "View Menu");  // Use the utility method
    }

    /**
     * Event handler for the "Order Here" button.
     * Opens the Order page when clicked.
     */
    @FXML
    private void handleOrder() {
        Stage stage = (Stage) orderButton.getScene().getWindow();  // Get the current stage
        StageConstants.openPage("/view/Order.fxml", stage, "Order Here");  // Use the utility method
    }

    /**
     * Event handler for the "Staff" button.
     * Opens the Login page when clicked.
     */
    @FXML
    private void handleAdminAccess() {
        Stage stage = (Stage) adminButton.getScene().getWindow();  // Get the current stage
        StageConstants.openPage("/view/Login.fxml", stage, "Login");  // Use the utility method
    }
}
