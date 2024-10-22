package com.example.napolinights.controller;

import com.example.napolinights.model.Category;
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
import com.example.napolinights.CartItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller for handling actions on the Checkout Page.
 * This includes displaying the order summary, handling navigation, and managing checkout functionality.
 */
public class CheckoutController {

    // FXML elements for the checkout page
    @FXML private TableView<CartItem> orderSummaryTable;  // Table to display cart items
    @FXML private TableColumn<CartItem, String> itemNameColumn;  // Column for item name
    @FXML private TableColumn<CartItem, Integer> quantityColumn;  // Column for quantity
    @FXML private TableColumn<CartItem, Double> unitPriceColumn;  // Column for unit price
    @FXML private TableColumn<CartItem, Double> gstColumn;  // Column for GST
    @FXML private TableColumn<CartItem, Double> totalColumn;  // Column for total price including GST
    @FXML private Label totalPriceLabel;  // Label to display the total price including GST
    @FXML private Button backButton;  // Button to go back to the Order page
    @FXML private Button payButton;  // Button to proceed to payment
    @FXML private AnchorPane checkoutPane;  // AnchorPane to hold the checkout items

    private ObservableList<CartItem> cartItems = FXCollections.observableArrayList();  // List to hold cart items

    /* ===============================================
     * SECTION 1: Initialization
     * =============================================== */

    /**
     * Initializes the controller, sets up the table columns, and adjusts the stage size.
     */
    @FXML
    private void initialize() {
        // Set up the table columns
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        gstColumn.setCellValueFactory(new PropertyValueFactory<>("gst"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalInc"));

        // Set padding for the checkout pane to provide spacing
        checkoutPane.setPadding(new Insets(0, 0, 0, 10)); // Top, right, bottom, left padding

        // Ensure that the stage size is adjusted after the scene is loaded
        Platform.runLater(() -> {
            Stage stage = (Stage) checkoutPane.getScene().getWindow();
            stage.setMinWidth(800);
            stage.setMinHeight(600);
        });
    }

    /* ===============================================
     * SECTION 2: Receiving and Displaying Cart Data
     * =============================================== */

    /**
     * Receives data from the OrderController and populates the order summary table.
     * @param items The array of cart items to be displayed in the order summary.
     */
    public void receiveData(CartItem[] items) {
        // Clear the table first
        orderSummaryTable.getItems().clear();

        // Add all the items to the table
        cartItems.addAll(items);
        orderSummaryTable.setItems(cartItems);

        // Calculate and update total price
        updateTotalPrice();
    }

    /**
     * Calculates and updates the total price label.
     */
    private void updateTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalInc();
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
        System.out.println("Checkout pay button clicked");
        openOrderConfirmationPage();
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
            setTotalPriceText();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OrderConfirmation.fxml"));
                Parent orderConfirmationPage = loader.load();
                OrderConfirmationController orderConfirmationController = loader.getController();
                transferCartItems(orderConfirmationController);

                Stage stage = (Stage) payButton.getScene().getWindow();
                stage.setTitle("Order Confirmation");
                Scene scene = new Scene(orderConfirmationPage);
                stage.setScene(scene);
                stage.show();
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
        return cartItems.isEmpty();
    }

    /**
     * Displays an error message indicating that the cart is empty.
     */
    private void showEmptyCartError() {
        System.out.println("Cart is empty! Cannot proceed to payment.");
    }

    /**
     * Sets the total price text in the total price label.
     */
    private void setTotalPriceText() {
        // Calculate and set total price
        double totalPrice = cartItems.stream().mapToDouble(CartItem::getTotalInc).sum();
        totalPriceLabel.setText(String.format("Total (Inc GST): $%.2f", totalPrice));
    }

    /**
     * Transfers cart items to the OrderConfirmationController.
     * @param orderConfirmationController The controller for the Order Confirmation page.
     */
    private void transferCartItems(OrderConfirmationController orderConfirmationController) {
        // Transfer cart items to the confirmation page
        orderConfirmationController.setCartItems(cartItems);
    }
}
