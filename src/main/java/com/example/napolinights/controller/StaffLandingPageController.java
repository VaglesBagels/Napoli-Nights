package com.example.napolinights.controller;

import com.example.napolinights.util.StageConstants;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Controller for the Staff Landing Page, providing options to view
 * incoming orders and reports.
 */
public class StaffLandingPageController {

    // FXML-linked components for UI interaction
    @FXML private AnchorPane staffLandingPane;  // Root pane for staff landing page
    @FXML private Button viewOrdersButton;      // Button to open incoming orders page
    @FXML private Button viewReportsButton;     // Button to open view reports page

    /**
     * Initializes the controller by setting button actions and applying
     * consistent stage size settings.
     */
    @FXML
    private void initialize() {
        // Set actions for the buttons to open respective pages
        viewOrdersButton.setOnAction(event -> openIncomingOrdersPage());
        viewReportsButton.setOnAction(event -> openReportsPage());

        // Ensure the stage has consistent dimensions using StageConstants utility
        Platform.runLater(() -> StageConstants.setStageSize((Stage) staffLandingPane.getScene().getWindow()));
    }

    /**
     * Opens the Incoming Orders page when the 'View Orders' button is clicked.
     * This uses the StageConstants utility for consistent navigation.
     */
    private void openIncomingOrdersPage() {
        Stage stage = (Stage) viewOrdersButton.getScene().getWindow();  // Retrieve the current stage
        StageConstants.openIncomingOrdersPage(stage);  // Navigate to incoming orders page using utility
    }

    /**
     * Opens the View Reports page when the 'View Reports' button is clicked.
     * This uses the StageConstants utility for consistent navigation.
     */
    private void openReportsPage() {
        Stage stage = (Stage) viewReportsButton.getScene().getWindow();  // Retrieve the current stage
        StageConstants.openViewReportsPage(stage);  // Navigate to view reports page using utility
    }
}
