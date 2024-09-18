package com.example.napolinights;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

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
    }

    public void receiveData(CartItem[] data) {
        cartItems = data;
        for (CartItem item : data) {
            System.out.println(item.getName() + " " + item.getPrice() + " " + item.getQuantity());
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

}
