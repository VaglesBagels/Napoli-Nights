package com.example.napolinights.controller;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

/**
 * Controller for the reusable logo component.
 */
public class LogoController {

    // FXML element for the logo image view
    @FXML private ImageView logoImageView;

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        // Any initialization logic for the logo can go here.
        System.out.println("Logo loaded.");
    }
}
