package com.example.napolinights.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import java.util.Random;
import java.io.IOException;
import com.example.napolinights.model.Order;
import com.example.napolinights.controller.OrderService;
import javafx.scene.control.Label;

public class OrderConfirmationController {

    // Fields from Maria's Code
    @FXML
    private Text totalPriceText;  // Added from Maria

    @FXML
    private Label orderNumberLabel;  // Added from Maria

    @FXML
    private ListView<GridPane> confirmedCartListView;  // Added from Maria

    // Fields from Ben's Code
    @FXML
    private Label tableLabel;

    @FXML
    private Label OrderConfirmationLabel;

    @FXML
    private VBox OrderDetails;

    @FXML
    private VBox FoodConfirmationDetails;

    @FXML
    private Button LandingPageButton;

    @FXML
    private ImageView logoImageView;

    @FXML
    private Label orderDetailsLabel;

    private Order confirmedOrder;

    public void initialize(Order order) {
        this.confirmedOrder = order;
        orderDetailsLabel.setText(order.toString());
        OrderService.setConfirmedOrder(order);
    }

    // Initialization method (Merged Logic)
    @FXML
    public void initialize() {
        System.out.println("OrderConfirmationController initialized");
        generateAndSetOrderNumber();  // Maria's logic for generating order number
    }

    // Method to navigate to the home page (Shared logic)
    @FXML
    private void handleHomePage() {
        System.out.println("Main Page button clicked");
        openMenuPage();
    }

    // Method to open the menu page (Shared logic)
    private void openMenuPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LandingPage.fxml"));
            Parent homePage = loader.load();

            Stage stage = (Stage) this.LandingPageButton.getScene().getWindow();
            stage.setTitle("Home Page");
            Scene scene = new Scene(homePage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Maria's Method: Set Total Price
    public void setTotalPrice(double totalPrice) {
        if (totalPriceText != null) {
            totalPriceText.setText(String.format("$%.2f", totalPrice));  // Properly format to two decimal places
        } else {
            System.err.println("totalPriceText is null, check FXML file for correct fx:id.");
        }
    }

    // Maria's Method: Set Cart Items into ListView
    public void setCartItems(ObservableList<VBox> cartItems) {
        confirmedCartListView.getItems().clear();  // Clear existing items

        for (VBox item : cartItems) {
            HBox itemDetailsBox = (HBox) item.getChildren().get(0);  // Get the HBox

            // Extract labels from the HBox
            Label nameLabel = (Label) itemDetailsBox.getChildren().get(0);  // Product name
            Label priceLabel = (Label) itemDetailsBox.getChildren().get(1);  // Price
            Label quantityLabel = (Label) itemDetailsBox.getChildren().get(2);  // Quantity

            // Remove unnecessary text from price and quantity
            String priceString = priceLabel.getText().replace("Price: $", "").trim();  // Fix the string extraction
            double price = Double.parseDouble(priceString);  // Extract numeric price
            int quantity = Integer.parseInt(quantityLabel.getText().replace("Quantity: ", "").trim());  // Extract numeric quantity

            // Calculate total for this item
            double total = price * quantity;
            Label totalLabel = new Label(String.format("$%.1f", total));  // Format the total price

            // Create GridPane to align the fields
            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);  // Set horizontal gap between columns
            gridPane.setVgap(5);   // Set vertical gap between rows

            // Set the column constraints to align the fields correctly
            ColumnConstraints col1 = new ColumnConstraints(362); // Product name width
            ColumnConstraints col2 = new ColumnConstraints(90);  // Price width
            ColumnConstraints col3 = new ColumnConstraints(71);  // Quantity width
            ColumnConstraints col4 = new ColumnConstraints(33.52);  // Total width
            gridPane.getColumnConstraints().addAll(col1, col2, col3, col4);  // Apply constraints to grid

            // Add the labels to the correct columns
            gridPane.add(new Label(nameLabel.getText()), 0, 0);  // Product name in column 0
            gridPane.add(new Label("$" + price), 1, 0);  // Price in column 1
            gridPane.add(new Label(String.valueOf(quantity)), 2, 0);  // Quantity in column 2
            gridPane.add(totalLabel, 3, 0);  // Total in column 3

            // Add the GridPane to the ListView
            confirmedCartListView.getItems().add(gridPane);
        }
    }

    // Maria's Method: Generate and Set Order Number
    private void generateAndSetOrderNumber() {
        Random random = new Random();
        int orderNumber = random.nextInt(900000) + 100000;  // Generates a random number between 100000 and 999999
        if (orderNumberLabel != null) {
            orderNumberLabel.setText(" " + orderNumber);
        } else {
            System.err.println("orderNumberLabel is null, check FXML file for correct fx:id.");
        }
    }
}