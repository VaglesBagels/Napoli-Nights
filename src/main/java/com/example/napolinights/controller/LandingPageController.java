package com.example.napolinights.controller;

import com.example.napolinights.util.StageConstants;  // Import StageUtil class for setting stage size
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller for handling actions on the Landing Page.
 * This includes navigation to other pages like Menu, Order, and Login.
 */
public class LandingPageController {

    @FXML private AnchorPane landingPagePane; // The main pane of the Landing Page

    // UI elements from the FXML file
    @FXML private Button menuButton;
    @FXML private Button orderButton;
    @FXML private Button adminButton;


    /**
     * This method is called after all the FXML elements have been initialized.
     * It sets the minimum size of the stage to prevent resizing below a certain width and height.
     */
    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            Stage stage = (Stage) landingPagePane.getScene().getWindow();
            StageConstants.setStageSize(stage);  // Use utility method for consistency
        });
    }


    /*
     * ===========================
     * Event Handlers for Buttons
     * ===========================
     */


    /**
     * Event handler for the "View Menu" button.
     * Opens the Menu page when clicked.
     */
    @FXML
    private void handleViewMenu() {
        Stage stage = (Stage) menuButton.getScene().getWindow();  // Get the current stage
        StageConstants.openMenuPage(stage);  // Use specific utility method for menu page
    }

    /**
     * Event handler for the "Order Here" button.
     * Opens the Order page when clicked.
     */
    @FXML
    private void handleOrder() {
        Stage stage = (Stage) orderButton.getScene().getWindow();  // Get the current stage
        StageConstants.openOrderPage(stage);  // Use specific utility method for order page
    }

    /**
     * Event handler for the "Staff" button.
     * Opens the Login page when clicked.
     */
    @FXML
    private void handleAdminAccess() {
        Stage stage = (Stage) adminButton.getScene().getWindow();  // Get the current stage
        StageConstants.openLoginPage(stage);  // Use specific utility method for login page
    }
}
