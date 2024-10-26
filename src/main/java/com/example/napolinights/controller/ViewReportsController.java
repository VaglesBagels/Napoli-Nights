package com.example.napolinights.controller;

import com.example.napolinights.util.StageConstants;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller for the 'View Reports' page, which displays a table
 * of daily reports including order counts and revenue totals.
 */
public class ViewReportsController {
    // FXML-linked UI components for interaction
    @FXML public AnchorPane reportsPane;
    @FXML private TableView<Report> reportsTable;
    @FXML private Button backToStaffLandingPageButton;

    /**
     * Initializes the controller, setting up the reports table with columns and
     * fetching data to populate it. Also configures layout padding and stage dimensions.
     */
    @FXML
    public void initialize() {
        // Initialize columns
        TableColumn<Report, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Report, Integer> orderCountColumn = new TableColumn<>("Order Count");
        orderCountColumn.setCellValueFactory(new PropertyValueFactory<>("orderCount"));

        TableColumn<Report, Double> revenueColumn = new TableColumn<>("Revenue");
        revenueColumn.setCellValueFactory(new PropertyValueFactory<>("revenue"));

        // Add columns to the table
        reportsTable.getColumns().addAll(dateColumn, orderCountColumn, revenueColumn);

        // Fetch reports data and populate the table
        List<Report> reports = fetchReportsFromDatabase();
        reportsTable.getItems().setAll(reports);

        // Set padding for the reports pane for layout consistency
        reportsPane.setPadding(new Insets(0, 0, 0, 10)); // Top, right, bottom, left padding

        // Set stage size for consistent dimensions
        Platform.runLater(() -> StageConstants.setStageSize((Stage) reportsPane.getScene().getWindow()));
    }

    /**
     * Fetching report data.
     *
     * @return A list of report data.
     */
    private List<Report> fetchReportsFromDatabase() {
        // Implement database query here
        // Sample return
        return List.of(
                new Report(LocalDate.now(), 10, 200.0),
                new Report(LocalDate.now().minusDays(1), 12, 240.0)
        );
    }

    /**
     * Inner class representing a report record with date, order count, and revenue.
     * Used to populate rows in the reportsTable.
     */
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

    /**
     * Handles the action of returning to the Staff Landing Page when the 'Back' button is clicked.
     */
    @FXML
    private void handleBackToStaffLandingPage() {
        Stage stage = (Stage) backToStaffLandingPageButton.getScene().getWindow();  // Retrieve the current stage
        StageConstants.openStaffLandingPage(stage);  // Navigate to Staff Landing page using utility
    }
}
