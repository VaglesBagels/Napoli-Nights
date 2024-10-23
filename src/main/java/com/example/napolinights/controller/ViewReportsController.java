package com.example.napolinights.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ViewReportsController {

    @FXML
    public AnchorPane reportsPane;

    @FXML
    private TableView<Report> reportsTable;

    @FXML
    private Button backToStaffLandingPageButton;

    @FXML
    public void initialize() {
        // Initialize columns
        TableColumn<Report, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Report, Integer> orderCountColumn = new TableColumn<>("Order Count");
        orderCountColumn.setCellValueFactory(new PropertyValueFactory<>("orderCount"));

        TableColumn<Report, Double> revenueColumn = new TableColumn<>("Revenue");
        revenueColumn.setCellValueFactory(new PropertyValueFactory<>("revenue"));

        reportsTable.getColumns().addAll(dateColumn, orderCountColumn, revenueColumn);

        List<Report> reports = fetchReportsFromDatabase();
        reportsTable.getItems().setAll(reports);

        // Set padding for the reports pane to provide spacing
        reportsPane.setPadding(new Insets(0, 0, 0, 10)); // Top, right, bottom, left padding

        // Ensure that the stage size is adjusted after the scene is loaded
        Platform.runLater(() -> {
            Stage stage = (Stage) reportsPane.getScene().getWindow();
            stage.setMinWidth(800);
            stage.setMinHeight(600);
        });
    }

    private List<Report> fetchReportsFromDatabase() {
        // Implement database query here
        // Sample return
        return List.of(
                new Report(LocalDate.now(), 10, 200.0),
                new Report(LocalDate.now().minusDays(1), 12, 240.0)
        );
    }

    public static class Report {
        private LocalDate date;
        private int orderCount;
        private double revenue;

        public Report(LocalDate date, int orderCount, double revenue) {
            this.date = date;
            this.orderCount = orderCount;
            this.revenue = revenue;
        }

        public LocalDate getDate() {
            return date;
        }

        public int getOrderCount() {
            return orderCount;
        }

        public double getRevenue() {
            return revenue;
        }
    }

    @FXML
    private void handleBackToStaffLandingPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StaffLandingPage.fxml"));
            Parent staffLandingPage = loader.load();
            Stage stage = (Stage) backToStaffLandingPageButton.getScene().getWindow();
            stage.setTitle("Staff Landing Page");
            Scene scene = new Scene(staffLandingPage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
