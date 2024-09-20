package com.example.napolinights;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class EmptyCartErrorController {

    @FXML
    private Button closeButton;

    // Method to close the dialog
    @FXML
    private void handleClose() {
        // Close the stage when the close button is pressed
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}