package com.example.napolinights;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class CheckoutController {

    @FXML
    private Button checkoutBackButton;

    @FXML
    private Button checkoutPayButton;

    @FXML
    private CartItem[] cartItems;

    private double totalPrice = 0.0;

    @FXML
    private int totalQuantity = 0;

    @FXML
    private void handleCheckoutBackButtonClick(MouseEvent event) {
        System.out.println("Checkout back button clicked");
    }

    @FXML
    private void handleCheckoutPayButtonClick(MouseEvent event) {
        System.out.println("Checkout pay button clicked");
    }

    public void receiveData(CartItem[] data) {
        cartItems = data;
        for (CartItem item : data) {
            System.out.println(item.getName() + " " + item.getPrice() + " " + item.getQuantity());
        }
        calculateTotal();
        System.out.println(totalPrice + " " + totalQuantity);
    }

    private void calculateTotal() {
        for (CartItem item : cartItems) {
            totalPrice += item.getPrice() * item.getQuantity();  // Price * Quantity
            totalQuantity += item.getQuantity();  // Total quantity
        }
    }
}
