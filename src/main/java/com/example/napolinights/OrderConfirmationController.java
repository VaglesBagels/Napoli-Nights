package com.example.napolinights;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

public class OrderConfirmationController {

    @FXML
    private Label tableLabel;

    @FXML
    private Label OrderConfirmationLabel;

    @FXML
    private VBox OrderDetails;

    @FXML
    private VBox FoodConfirmationDetails;

    @FXML
    private Button LandingPageButton;

    @FXML
    private ImageView logoImageView;

    @FXML
    public void initialize() {
        // Your initialization code here
        System.out.println("OrderConfirmationController initialized");
    }

    @FXML
    private void handleHomePage() {
        System.out.println("Main Page button clicked");
        // Add logic to handle the View Menu button click
        openMenuPage();
    }

    private void openMenuPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/napolinights/LandingPage.fxml"));
            Parent homePage = loader.load();

            Stage stage = (Stage) this.LandingPageButton.getScene().getWindow();
            stage.setTitle("Home Page");
            Scene scene = new Scene(homePage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

