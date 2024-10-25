package com.example.napolinights.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class StaffLandingPageController {

    @FXML private Button viewOrdersButton;

    @FXML private Button viewReportsButton;

    @FXML
    private void initialize() {
        viewOrdersButton.setOnAction(event -> openIncomingOrdersPage());
        viewReportsButton.setOnAction(event -> openReportsPage());
    }

    private void openIncomingOrdersPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/IncomingOrders.fxml"));
            Parent ordersPage = loader.load();
            Stage stage = (Stage) viewOrdersButton.getScene().getWindow();
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setTitle("Incoming Orders");
            Scene scene = new Scene(ordersPage);
            stage.setScene(scene);
            stage.show();

            //IncomingOrdersController controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openReportsPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ViewReports.fxml"));
            Parent reportsPage = loader.load();
            Stage stage = (Stage) viewReportsButton.getScene().getWindow();
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setTitle("Reports");
            Scene scene = new Scene(reportsPage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

