package com.example.napolinights.controller;

import com.example.napolinights.CartItem;
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

import java.util.Random;

public class OrderConfirmationController {


    @FXML
    private AnchorPane orderConfirmationPane;  // AnchorPane to hold the order confirmation items

    @FXML
    private TableView<CartItem> orderSummaryTable;
    @FXML
    private TableColumn<CartItem, String> itemNameColumn;
    @FXML
    private TableColumn<CartItem, Integer> quantityColumn;
    @FXML
    private TableColumn<CartItem, Double> unitPriceColumn;
    @FXML
    private TableColumn<CartItem, Double> gstColumn;
    @FXML
    private TableColumn<CartItem, Double> totalColumn;
    @FXML
    private Label totalPriceLabel;  // Declare the totalPriceLabel here
    @FXML
    private Label orderNumberLabel;
    @FXML
    private Button closeButton;

    private ObservableList<CartItem> cartItems = FXCollections.observableArrayList();


    /**
     * Initializes the controller, sets up category toolbar, and displays the table.
     */
    @FXML
    private void initialize() {
        // Set up the table columns
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        gstColumn.setCellValueFactory(new PropertyValueFactory<>("gst"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalInc"));

        // Set padding for the contentAnchorPane to provide spacing
        orderConfirmationPane.setPadding(new Insets(0, 0, 0, 10)); // Top, right, bottom, left padding

        generateAndSetOrderNumber();

        // Ensure that the stage size is adjusted after the scene is loaded
        Platform.runLater(() -> {
            Stage stage = (Stage) orderConfirmationPane.getScene().getWindow();
            stage.setMinWidth(800);
            stage.setMinHeight(600);
        });
    }


    /**
     * Set the total price on the confirmation page.
     */
    private void updateTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalInc();
        }
        totalPriceLabel.setText(String.format("Total (Inc GST): $%.2f", total));
    }


    /**
     * Method to receive data from the CheckoutController and populate the table.
     */
    public void setCartItems(ObservableList<CartItem> items) {
        // Clear the table first
        orderSummaryTable.getItems().clear();

        // Add all the items to the table
        cartItems.addAll(items);
        orderSummaryTable.setItems(cartItems);

        // Calculate and update total price
        updateTotalPrice();
    }

    private void generateAndSetOrderNumber() {
        Random random = new Random();
        int orderNumber = random.nextInt(900000) + 100000;  // Generates a random number between 100000 and 999999
        if (orderNumberLabel != null) {
            orderNumberLabel.setText("Order Number: " + orderNumber);
        } else {
            System.err.println("orderNumberLabel is null, check FXML file for correct fx:id.");
        }
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
