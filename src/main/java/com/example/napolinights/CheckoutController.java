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
    private ArrayList<Object[]> cartData;

    @FXML
    private void handleCheckoutBackButtonClick(MouseEvent event) {
        System.out.println("Checkout back button clicked");
    }

    @FXML
    private void handleCheckoutPayButtonClick(MouseEvent event) {
        System.out.println("Checkout pay button clicked");
    }

    public void receiveData(ArrayList<Object[]> data) {
//        System.out.println(data);
    }
}
