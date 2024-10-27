package com.example.napolinights.controller;

import com.example.napolinights.model.Order;
import com.example.napolinights.model.OrderDAO;
import com.example.napolinights.model.OrderItem;
import com.example.napolinights.model.SqliteConnection;
import com.example.napolinights.util.StageConstants;
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


/**
 * Controller for handling actions on the Order Confirmation Page.
 * This includes displaying the order summary, total price, and
 * order details based on the provided order ID.
 */
public class OrderConfirmationController {
    // FXML-linked UI components
    @FXML private AnchorPane orderConfirmationPane;                // Root pane for order confirmation page
    @FXML private TableView<OrderItem> orderSummaryTable;          // Table displaying order items
    @FXML private TableColumn<OrderItem, String> itemNameColumn;   // Column for item names
    @FXML private TableColumn<OrderItem, Integer> quantityColumn;  // Column for item quantities
    @FXML private TableColumn<OrderItem, Double> unitPriceColumn;  // Column for item unit prices
    @FXML private TableColumn<OrderItem, Double> gstColumn;        // Column for GST of items
    @FXML private TableColumn<OrderItem, Double> totalColumn;      // Column for total item prices including GST
    @FXML private Label totalPriceLabel;                           // Label displaying total price including GST
    @FXML private Label orderNumberLabel;                          // Label displaying order number
    @FXML private Button closeButton;                              // Button to close the confirmation page

    private ObservableList<OrderItem> orderItems = FXCollections.observableArrayList();  // Observable list for order items
    private int orderID;                                           // ID of the current order
    private Order updatedOrder;                                    // Order object with fetched data


    /**
     * Initializes the controller by setting up the table columns,
     * configuring padding, and ensuring consistent stage dimensions.
     */
    @FXML
    private void initialize() {
        setupTableColumns();  // Set up table column bindings
        orderConfirmationPane.setPadding(new Insets(0, 0, 0, 10));  // Apply padding to the confirmation pane

        // Ensure stage dimensions using constants for default size
        Platform.runLater(() -> StageConstants.setStageSize((Stage) orderConfirmationPane.getScene().getWindow()));
    }


    /**
     * Configures table columns to bind with OrderItem properties for displaying the order summary.
     */
    private void setupTableColumns() {
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("menuName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
        gstColumn.setCellValueFactory(new PropertyValueFactory<>("gst"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
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
        } finally {
            SqliteConnection.closeConnection(); // Close the connection
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
