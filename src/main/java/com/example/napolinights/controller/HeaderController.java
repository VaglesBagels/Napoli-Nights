package com.example.napolinights.controller;

import com.example.napolinights.util.StageConstants;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class HeaderController {

    @FXML private Button homeButton;

    /**
     * Handles the Home button click event.
     * Navigates back to the main landing page.
     */
    @FXML
    private void handleHomeButtonClick() {
        Stage stage = (Stage) homeButton.getScene().getWindow();
        StageConstants.openPage("/view/LandingPage.fxml", stage, "Landing Page");
    }
}
