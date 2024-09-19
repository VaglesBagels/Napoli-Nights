package com.example.napolinights;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class CheckoutController {

    @FXML
    private Button checkoutBackButton;

    @FXML
    private Button checkoutPayButton;

    @FXML
    private CartItem[] cartItems;

    private double totalPrice = 0.0;

    private int totalQuantity = 0;

    private double tax = 0.0;

    private double subtotalPrice;


    @FXML
    private Text quantityText;
    @FXML
    private Text subtotalPriceText;
    @FXML
    private Text taxText;
    @FXML
    private Text totalPriceText;

    @FXML
    private void handleCheckoutBackButtonClick(MouseEvent event) {
        System.out.println("Checkout back button clicked");
    }

    @FXML
    private void handleCheckoutPayButtonClick(MouseEvent event) {
        System.out.println("Checkout pay button clicked");
        openOrderConfirmationPage();
    }

    public void receiveData(CartItem[] data) {
        cartItems = data;
        for (CartItem item : data) {
            System.out.println(item.getName() + " " + item.getPrice() + " " + item.getQuantity());
            AddItemToListView(item);
        }
        calculatePrices();
        setText();
        System.out.println(totalPrice + " " + totalQuantity);

    }

    private void calculatePrices() {
        for (CartItem item : cartItems) {
            totalPrice += item.getPrice() * item.getQuantity();  // Price * Quantity
            totalQuantity += item.getQuantity();  // Total quantity
        }
        calculateTax();
        calculateSubtotal();
    }

    private void setText() {
        setQuantityText();
        setSubtotalPriceText();
        setTaxText();
        setTotalPriceText();
    }

    private void calculateTax() {
//        10% GST tax
        tax = totalPrice * 0.10;
    }

    private void calculateSubtotal() {
        subtotalPrice = totalPrice - tax;
    }

    private void setQuantityText() {
        quantityText.setText(totalQuantity + " items");
    }

    private void setSubtotalPriceText() {
        subtotalPriceText.setText("$" + subtotalPrice);
    }

    private void setTaxText() {
        taxText.setText("$" + tax);
    }

    private void setTotalPriceText() {
        totalPriceText.setText("$" + totalPrice);
    }

    @FXML
    private ListView<VBox> cartListView;

    public void AddItemToListView(CartItem item) {
        VBox cartItemBox = new VBox();

        HBox itemDetailsBox = new HBox(10);

        // Add product name
        Label nameLabel = new Label(item.getName());

        // Add product price
        Label priceLabel = new Label("Price: $" + item.getPrice());

        // Add product quantity
        Label quantityLabel = new Label("Quantity: " + item.getQuantity());

        // Add all elements to the HBox
        itemDetailsBox.getChildren().addAll(nameLabel, priceLabel, quantityLabel);

        // Add HBox to VBox (so we can extend and add other details if needed)
        cartItemBox.getChildren().add(itemDetailsBox);

        // Add VBox to the ListView
        cartListView.getItems().add(cartItemBox);

    }

    private boolean emptyCart() {
        if (cartItems.length == 0) {
            return true;
        } else {
            return false;
        }
    }

    private void openOrderConfirmationPage() {
        if (emptyCart()) {
            System.out.println("empty cart");
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/napolinights/OrderConfirmation.fxml"));
                Parent landingPage = loader.load();

                Stage stage = (Stage) this.checkoutPayButton.getScene().getWindow();
                stage.setTitle("Napoli Nights");
                Scene scene = new Scene(landingPage);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
