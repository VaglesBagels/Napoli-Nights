package com.example.napolinights.controller;

import com.example.napolinights.model.Order;
import com.example.napolinights.model.OrderDAO;
import com.example.napolinights.model.OrderItem;
import com.example.napolinights.model.SqliteConnection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class OrderConfirmationController {


    @FXML private AnchorPane orderConfirmationPane;  // AnchorPane to hold the order confirmation items
    @FXML private TableView<OrderItem> orderSummaryTable;
    @FXML private TableColumn<OrderItem, String> itemNameColumn;
    @FXML private TableColumn<OrderItem, Integer> quantityColumn;
    @FXML private TableColumn<OrderItem, Double> unitPriceColumn;
    @FXML private TableColumn<OrderItem, Double> gstColumn;
    @FXML private TableColumn<OrderItem, Double> totalColumn;
    @FXML private Label totalPriceLabel;  // Declare the totalPriceLabel here
    @FXML private Label orderNumberLabel;
    @FXML private Button closeButton;

    private ObservableList<OrderItem> orderItems = FXCollections.observableArrayList();  // List to hold order items

    private int orderID;

    private Order updatedOrder;


    /**
     * Initializes the controller, sets up category toolbar, and displays the table.
     */
    @FXML
    private void initialize() {
        // Set up the table columns
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("menuName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
        gstColumn.setCellValueFactory(new PropertyValueFactory<>("gst"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        // Set padding for the contentAnchorPane to provide spacing
        orderConfirmationPane.setPadding(new Insets(0, 0, 0, 10)); // Top, right, bottom, left padding

        //generateAndSetOrderNumber();

        // Ensure that the stage size is adjusted after the scene is loaded
        Platform.runLater(() -> {
            Stage stage = (Stage) orderConfirmationPane.getScene().getWindow();
            stage.setMinWidth(800);
            stage.setMinHeight(600);
        });
    }


    /**
     * Sets the order ID for this checkout process.
     * @param orderID The ID of the order to be retrieved.
     */
    public void setOrderID(int orderID) {
        this.orderID = orderID;
        orderNumberLabel.setText("Order Number: " + orderID);
        loadOrderDetails(); // Load order details using the orderID
    }


    /**
     * Loads the order details from the database using the orderID.
     */
    private void loadOrderDetails() {
        Connection connection = SqliteConnection.getInstance(); // Get a connection to the database
        try {
            OrderDAO orderDAO = new OrderDAO(connection);
            updatedOrder = orderDAO.getOrderById(orderID);
            if (updatedOrder != null) {
                List<OrderItem> fetchedOrderItems = orderDAO.getOrderItemsByOrderId(orderID); // Fetch order items
                orderItems.setAll(fetchedOrderItems); // Populate observable list

                // Set the items in the order summary table
                orderSummaryTable.setItems(orderItems);

                // Calculate and update total price
                setTotalPriceText();
                System.out.println("Loaded order with ID: " + orderID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Sets the total price text in the total price label.
     */
    private void setTotalPriceText() {
        // Calculate and set total price
        double totalPrice = orderItems.stream()
                .mapToDouble(item -> item.getItemPrice() * item.getQuantity() * 1.1) // Including 10% GST
                .sum();
        totalPriceLabel.setText(String.format("Total (Inc GST): $%.2f", totalPrice));
    }



    /**
     * Method to handle the "Close" button click event.
     * This closes the Order Confirmation page.
     */
    @FXML
    private void handleCloseButtonClick() {
        // Close the current stage
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
