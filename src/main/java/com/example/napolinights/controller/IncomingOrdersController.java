package com.example.napolinights.controller;

import com.example.napolinights.model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class IncomingOrdersController {
    @FXML public AnchorPane incomingOrdersPane;
    @FXML public VBox headerPane;
    @FXML private Button staffLandingPageButton;

    @FXML private TableView<OrderDisplay> confirmedCartListView;
    @FXML private TableColumn<OrderDisplay, Integer> orderIdColumn;
    @FXML private TableColumn<OrderDisplay, String> orderDetailsColumn;

    private ObservableList<OrderDisplay> displayData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        try {
            // Set cell value factories for TableColumns
            orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
            orderDetailsColumn.setCellValueFactory(new PropertyValueFactory<>("orderDetails"));
            orderDetailsColumn.setPrefWidth(650);  // sets a fixed width of 200 pixels
            orderIdColumn.setPrefWidth(60);


            loadConfirmedOrders();

            // Set padding for the checkout pane to provide spacing
            incomingOrdersPane.setPadding(new Insets(0, 0, 0, 10)); // Top, right, bottom, left padding

            // Ensure that the stage size is adjusted after the scene is loaded
            Platform.runLater(() -> {
                Stage stage = (Stage) incomingOrdersPane.getScene().getWindow();
                stage.setMinWidth(800);
                stage.setMinHeight(600);
            });
        } catch (Exception e) {
            System.out.println("Error in initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadConfirmedOrders() {
        List<Order> confirmedOrders;
        Connection connection = SqliteConnection.getInstance();

        try {
            OrderDAO orderDAO = new OrderDAO(connection);
            confirmedOrders = orderDAO.fetchIncomingOrders();

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

            // Populate the TableView with the list of OrderDisplay objects
            displayData.setAll(orderDisplays);
            confirmedCartListView.setItems(displayData);

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle error, possibly show an alert
        }
    }

    @FXML
    private void handleStaffHomePage() {
        System.out.println("Staff Main Page button clicked");
        openStaffHomePage();
    }

    private void openStaffHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StaffLandingPage.fxml"));
            Parent staffLandingPage = loader.load();
            Stage stage = (Stage) staffLandingPageButton.getScene().getWindow();
            stage.setTitle("Staff Home Page");
            Scene scene = new Scene(staffLandingPage);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Class to hold data for displaying in the TableView.
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
