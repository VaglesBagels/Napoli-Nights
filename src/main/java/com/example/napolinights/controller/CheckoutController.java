package com.example.napolinights.controller;

import com.example.napolinights.model.*;
import com.example.napolinights.util.StageConstants;
import com.example.napolinights.util.StyleConstants;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller for handling actions on the Checkout Page, including
 * displaying the order summary, managing checkout functionality,
 * and navigating to the order confirmation or other pages.
 */
public class CheckoutController {

    // FXML-linked components for UI interaction
    @FXML private AnchorPane checkoutPane;                // Root pane for checkout page
    @FXML private TableView<OrderItem> orderSummaryTable; // Table to display order items
    @FXML private TableColumn<OrderItem, String> itemNameColumn;  // Column for item name
    @FXML private TableColumn<OrderItem, Integer> quantityColumn; // Column for item quantity
    @FXML private TableColumn<OrderItem, Double> unitPriceColumn; // Column for unit price
    @FXML private TableColumn<OrderItem, Double> gstColumn;       // Column for GST
    @FXML private TableColumn<OrderItem, Double> totalColumn;     // Column for total price including GST
    @FXML private Label totalPriceLabel;                  // Label to display total price including GST
    @FXML private Button backButton;                      // Button to navigate back to the order page
    @FXML private Button payButton;                       // Button to confirm payment

    private ObservableList<OrderItem> orderItems = FXCollections.observableArrayList(); // Observable list of order items
    private int orderID;                                  // ID of the current order
    private Order savedOrder;                             // Order data retrieved from the database


    /* ===============================================
     * SECTION 1: Initialization
     * =============================================== */

    /**
     * Initializes the checkout page controller by setting up table columns,
     * loading order data, and applying consistent stage size settings.
     */
    @FXML
    private void initialize() {
        setupTableColumns(); // Set up the columns in the order summary table
        checkoutPane.setPadding(new Insets(0, 0, 0, StyleConstants.GRID_HGAP)); // Apply padding to the checkout pane
        // Ensure the stage has consistent dimensions using StageConstants utility
        Platform.runLater(() -> StageConstants.setStageSize((Stage) checkoutPane.getScene().getWindow()));
    }

    /**
     * Sets up table columns to bind with OrderItem properties for display in the order summary.
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
        loadOrderDetails(); // Load order details using the orderID
    }

    /**
     * Loads the order details from the database using the orderID.
     */
    private void loadOrderDetails() {
        Connection connection = SqliteConnection.getInstance(); // Get a connection to the database
        try {
            OrderDAO orderDAO = new OrderDAO(connection);
            savedOrder = orderDAO.getOrderById(orderID);
            if (savedOrder != null) {
                List<OrderItem> fetchedOrderItems = orderDAO.getOrderItemsByOrderId(orderID); // Fetch order items
                orderItems.setAll(fetchedOrderItems); // Populate observable list

                // Set the items in the order summary table
                orderSummaryTable.setItems(orderItems);

                // Calculate and update total price
                updateTotalPrice();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqliteConnection.closeConnection(); // Close the database connection
        }
    }


    /* ===============================================
     * SECTION 2: Displaying Cart Data
     * =============================================== */

    /**
     * Calculates and updates the total price label.
     */
    private void updateTotalPrice() {
        double total = 0;
        for (OrderItem item : orderItems) {
            double itemTotal = item.getItemPrice() * item.getQuantity();
            double itemGST = itemTotal * StageConstants.GST; // Assuming GST is 10%

            total += itemTotal + itemGST;
        }
        totalPriceLabel.setText(String.format("Total (Inc GST): $%.2f", total));
    }


    /* ===============================================
     * SECTION 3: Navigation and Button Handlers
     * =============================================== */

    /**
     * Handles the "Back" button click to navigate to the Order Page.
     */
    @FXML
    private void handleBackButtonClick() {
        // Use the StageConstants utility to open the Order Page
        Stage stage = (Stage) backButton.getScene().getWindow();
        StageConstants.openOrderPage(stage);
    }

    /**
     * Handles the "Pay" button click to proceed to the Order Confirmation Page.
     */
    @FXML
    private void handlePayButtonClick() {
        updateOrderToDatabase(orderID);
        openOrderConfirmationPage();
    }

    /**
     * Updates the order in the database to mark it as paid.
     *
     * @param orderID The ID of the order to be updated.
     */
    private void updateOrderToDatabase(int orderID) {
        Connection connection = SqliteConnection.getInstance(); // Get a connection to the database
        try {
            OrderDAO orderDAO = new OrderDAO(connection);
            Timestamp paidDate = new Timestamp(System.currentTimeMillis());
            Order orderToBeUpdated = new Order(savedOrder.getOrderID(), savedOrder.getOrderDate(), savedOrder.getCustomerName(), savedOrder.getCustomerContact(), savedOrder.getOrderItems(), paidDate);

            if (orderToBeUpdated != null) {
                orderDAO.updateOrder(orderToBeUpdated);
                System.out.println("Order with ID: " + orderID + " has been updated as paid.");
            } else {
                System.err.println("Order with ID: " + orderID + " not found in the database.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to update the order in the database.");
        } finally {
            SqliteConnection.closeConnection(); // Close the database connection
        }
    }


    /* ===============================================
     * SECTION 4: Order Confirmation Process
     * =============================================== */

    /**
     * Opens the Order Confirmation Page if the cart is not empty.
     */
    private void openOrderConfirmationPage() {
        if (emptyCart()) {
            showEmptyCartError();
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(StyleConstants.ORDER_CONFIRMATION_PAGE_FXML));
                Parent confirmationPage = loader.load();

                // Get the OrderConfirmationController and pass the order ID
                OrderConfirmationController orderConfirmationController = loader.getController();
                orderConfirmationController.setOrderID(orderID);

                Stage stage = (Stage) this.payButton.getScene().getWindow();
                StageConstants.configureStage(stage, confirmationPage, StyleConstants.CHECKOUT_PAGE_TITLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks if the cart is empty.
     * @return True if the cart is empty, false otherwise.
     */
    private boolean emptyCart() {
        return orderItems.isEmpty();
    }

    /**
     * Displays an error message indicating that the cart is empty.
     */
    private void showEmptyCartError() {
        System.out.println("Cart is empty! Cannot proceed to payment.");
    }

}
