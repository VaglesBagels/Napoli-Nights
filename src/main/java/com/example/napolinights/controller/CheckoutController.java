package com.example.napolinights.controller;

import com.example.napolinights.model.*;
import com.example.napolinights.util.StageConstants;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
 * Controller for handling actions on the Checkout Page.
 * This includes displaying the order summary, handling navigation, and managing checkout functionality.
 */
public class CheckoutController {

    // FXML elements for the checkout page
    @FXML private TableView<OrderItem> orderSummaryTable;  // Table to display order items
    @FXML private TableColumn<OrderItem, String> itemNameColumn;  // Column for item name
    @FXML private TableColumn<OrderItem, Integer> quantityColumn;  // Column for quantity
    @FXML private TableColumn<OrderItem, Double> unitPriceColumn;  // Column for unit price
    @FXML private TableColumn<OrderItem, Double> gstColumn;  // Column for GST
    @FXML private TableColumn<OrderItem, Double> totalColumn;  // Column for total price including GST
    @FXML private Label totalPriceLabel;  // Label to display the total price including GST
    @FXML private Button backButton;  // Button to go back to the Order page
    @FXML private Button payButton;  // Button to proceed to payment
    @FXML private AnchorPane checkoutPane;  // AnchorPane to hold the checkout items

    private ObservableList<OrderItem> orderItems = FXCollections.observableArrayList();  // List to hold order items

    private int orderID;

    private Order savedOrder;


    /* ===============================================
     * SECTION 1: Initialization
     * =============================================== */

    /**
     * Initializes the controller, sets up the table columns, and adjusts the stage size.
     */
    @FXML
    private void initialize() {
        // Set up the table columns
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("menuName"));  // Updated to use menuName
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
        gstColumn.setCellValueFactory(new PropertyValueFactory<>("gst"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));  // Updated to use totalPrice
        // Set padding for the checkout pane to provide spacing
        checkoutPane.setPadding(new Insets(0, 0, 0, 10)); // Top, right, bottom, left padding

        // Ensure that the stage size is adjusted after the scene is loaded
        Platform.runLater(() -> {
            Stage stage = (Stage) checkoutPane.getScene().getWindow();
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
        StageConstants.openPage("/view/Order.fxml", stage, "Order Page");
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OrderConfirmation.fxml"));
                Parent checkoutPage = loader.load();

                // Get the OrderConfirmationController and pass the order ID
                OrderConfirmationController orderConfirmationController = loader.getController();
                orderConfirmationController.setOrderID(orderID);

                Stage stage = (Stage) this.payButton.getScene().getWindow();
                setupStage(stage, checkoutPage, "Checkout");
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



    /**
     * Sets up the stage for navigation to a new page.
     * @param stage The current stage.
     * @param page The new page to load.
     * @param title The title of the new page.
     */
    private void setupStage(Stage stage, Parent page, String title) {
        stage.setTitle(title);
        Scene scene = new Scene(page);
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.show();
    }
}
