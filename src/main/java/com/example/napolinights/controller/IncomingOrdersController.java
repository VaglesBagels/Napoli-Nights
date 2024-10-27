package com.example.napolinights.controller;

import com.example.napolinights.model.*;
import com.example.napolinights.util.StageConstants;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for displaying incoming orders in the 'Incoming Orders' page.
 * This includes fetching order data from the database, displaying it in a TableView,
 * and providing navigation to the staff's main page.
 */
public class IncomingOrdersController {

    @FXML public AnchorPane incomingOrdersPane; // Root pane for the incoming orders page
    @FXML public VBox headerPane; // Header section for page layout
    @FXML private Button staffLandingPageButton; // Button to navigate to Staff Landing Page

    // TableView and columns to display confirmed orders
    @FXML private TableView<OrderDisplay> confirmedCartListView;
    @FXML private TableColumn<OrderDisplay, Integer> orderIdColumn;
    @FXML private TableColumn<OrderDisplay, String> orderDetailsColumn;

    // Observable list to hold order display data for the TableView
    private ObservableList<OrderDisplay> displayData = FXCollections.observableArrayList();

    /**
     * Initializes the Incoming Orders page by setting up table columns, loading order data,
     * and adjusting the stage dimensions and layout.
     */
    @FXML
    private void initialize() {
        try {
            // Configure table columns to display order details
            orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
            orderDetailsColumn.setCellValueFactory(new PropertyValueFactory<>("orderDetails"));

            // Load and display confirmed orders
            loadConfirmedOrders();

            // Apply padding for page layout consistency
            incomingOrdersPane.setPadding(new Insets(0, 0, 0, 10)); // Top, right, bottom, left padding

            // Set consistent stage size using StageConstants utility
            Platform.runLater(() -> StageConstants.setStageSize((Stage) incomingOrdersPane.getScene().getWindow()));

        } catch (Exception e) {
            System.out.println("Error in initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Fetches confirmed orders from the database, formats order details for display,
     * and populates the TableView.
     */
    private void loadConfirmedOrders() {
        List<Order> confirmedOrders;
        Connection connection = SqliteConnection.getInstance();

        try {
            // Retrieve confirmed orders from database
            OrderDAO orderDAO = new OrderDAO(connection);
            confirmedOrders = orderDAO.fetchIncomingOrders();

            // Convert each Order into an OrderDisplay for TableView compatibility
            List<OrderDisplay> orderDisplays = new ArrayList<>();
            for (Order order : confirmedOrders) {
                StringBuilder orderDetails = new StringBuilder();
                for (OrderItem item : order.getOrderItems()) {
                    orderDetails.append(String.format("Dish: %s | Qty: %d | Notes: %s%n",
                            item.getMenuName(),
                            item.getQuantity(),
                            item.getSpecialInstructions()));
                }

                orderDisplays.add(new OrderDisplay(order.getOrderID(), orderDetails.toString()));
            }

            // Update TableView with formatted order display data
            displayData.setAll(orderDisplays);
            confirmedCartListView.setItems(displayData);

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle error, possibly show an alert
        } finally {
            SqliteConnection.closeConnection(); // Close the connection
        }
    }

    /**
     * Event handler for the button that navigates back to the Staff Landing Page.
     */
    @FXML
    private void handleStaffHomePage() {
        System.out.println("Staff Main Page button clicked");
        openStaffHomePage();
    }

    /**
     * Navigates to the Staff Landing Page using the utility method in StageConstants.
     */
    private void openStaffHomePage() {
        Stage stage = (Stage) staffLandingPageButton.getScene().getWindow();  // Retrieve the current stage
        StageConstants.openStaffLandingPage(stage);  // Navigate to Staff Landing page using utility
    }

    /**
     * Class representing data structure for displaying order details in the TableView.
     */
    public static class OrderDisplay {
        private final Integer orderId;
        private final String orderDetails;

        public OrderDisplay(Integer orderId, String orderDetails) {
            this.orderId = orderId;
            this.orderDetails = orderDetails;
        }

        public Integer getOrderId() {
            return orderId;
        }

        public String getOrderDetails() {
            return orderDetails;
        }
    }
}
