package com.example.napolinights.controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;


/**
 * Controller class for handling the "Empty Cart Error" dialog.
 * Provides functionality to close the error dialog.
 */
public class EmptyCartErrorController {

    @FXML private Button closeButton;


    /**
     * Event handler for the "Close" button click.
     * Closes the dialog window when the button is pressed.
     */
    @FXML
    private void handleClose() {
        // Close the stage when the close button is pressed
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}