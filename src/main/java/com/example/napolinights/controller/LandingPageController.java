package com.example.napolinights.controller;

import com.example.napolinights.util.StageConstants;  // Import StageUtil class for setting stage size
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
    @FXML private Button menuButton;
    @FXML private Button orderButton;
    @FXML private ImageView logoImageView;
    @FXML private HBox imageBox;

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
        openPage("/view/Menu.fxml", "View Menu");
    }

    /**
     * Event handler for the "Order Here" button.
     * Opens the Order page when clicked.
     */
    @FXML
    private void handleOrder() {
        openPage("/view/Order.fxml", "Order Here");
    }

    /**
     * Event handler for the "Staff" button.
     * Opens the Login page when clicked.
     */
    @FXML
    private void handleAdminAccess() {
        openPage("/view/Login.fxml", "Login");
    }

    /**
     * Opens a page based on the specified FXML file and sets the stage title.
     * Uses the StageUtil utility to ensure consistent stage size settings.
     *
     * @param fxmlPath Path to the FXML file.
     * @param title Title of the window.
     */
    private void openPage(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent page = loader.load();
            Stage stage = (Stage) menuButton.getScene().getWindow();
            stage.setTitle(title);

            // Use the StageUtil to set the stage size
            StageConstants.setStageSize(stage);

            stage.setResizable(true);
            stage.setScene(new Scene(page));
            stage.show();
        } catch (IOException e) {
            System.out.println("Error occurred while opening the " + title + " page.");
            e.printStackTrace();
        }
    }
}

