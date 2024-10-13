package com.example.napolinights.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * MainController is responsible for handling actions on the main page.
 * This class controls the behavior when the user interacts with the UI elements, such as buttons.
 */
public class MainController {

    // Label element from the FXML file to display a welcome message
    @FXML
    private Label welcomeText;

    /**
     * This method is triggered when the "Hello" button is clicked.
     * It changes the text of the welcomeText label to display a greeting message.
     */
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
