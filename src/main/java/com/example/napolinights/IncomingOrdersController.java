package com.example.napolinights;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.io.IOException;

public class IncomingOrdersController {
    @FXML
    private ListView<String> confirmedCartListView;

    @FXML
    private Button staffLandingPageButton;

    @FXML
    private void handleStaffHomePage(){
        System.out.println("Staff Main Page button clicked");
        openStaffHomePage();
    }

    private void openStaffHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/napolinights/StaffLandingPage.fxml"));
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