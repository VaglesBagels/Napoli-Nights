package com.example.napolinights.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.io.IOException;
import com.example.napolinights.model.Order;
import com.example.napolinights.controller.OrderService;
import javafx.scene.control.Label;

public class IncomingOrdersController {
    @FXML
    private ListView<String> confirmedCartListView;

    @FXML
    private Button staffLandingPageButton;

    @FXML
    private Label incomingOrderLabel;

    public void initialize() {
        Order confirmedOrder = OrderService.getConfirmedOrder();
        if (confirmedOrder != null) {
            incomingOrderLabel.setText(confirmedOrder.toString());
        }
    }

    @FXML
    private void handleStaffHomePage(){
        System.out.println("Staff Main Page button clicked");
        openStaffHomePage();
    }

    private void openStaffHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StaffLandingPage.fxml"));
            Parent staffLandingPage = loader.load();
            // Add null check
                Stage stage = (Stage) staffLandingPageButton.getScene().getWindow();
                stage.setTitle("Staff Home Page");
                Scene scene = new Scene(staffLandingPage);
                stage.setScene(scene);
                stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}