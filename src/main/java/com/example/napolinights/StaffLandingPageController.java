package com.example.napolinights;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class StaffLandingPageController {

    @FXML
    private Button viewOrdersButton;

    @FXML
    private Button viewReportsButton;

    @FXML
    private void initialize() {
        viewOrdersButton.setOnAction(event -> openIncomingOrdersPage());
        viewReportsButton.setOnAction(event -> openReportsPage());
    }

    private void openIncomingOrdersPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/napolinights/IncomingOrders.fxml"));
            Parent ordersPage = loader.load();

            Stage stage = (Stage) viewOrdersButton.getScene().getWindow();
            stage.setTitle("Incoming Orders");
            Scene scene = new Scene(ordersPage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openReportsPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/napolinights/Reports.fxml"));
            Parent reportsPage = loader.load();

            Stage stage = (Stage) viewReportsButton.getScene().getWindow();
            stage.setTitle("Reports");
            Scene scene = new Scene(reportsPage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}