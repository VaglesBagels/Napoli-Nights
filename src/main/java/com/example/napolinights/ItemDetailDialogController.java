package com.example.napolinights;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ItemDetailDialogController {

    @FXML
    private Label dialogTitle;

    @FXML
    private Label dialogDescription;

    @FXML
    private Label dialogPrice;

    @FXML
    private Button btnCancel;

    private int quantity = 1; // Initial quantity

    private ItemSelectionCallback callback;

    @FXML
    private Label lblQuantity;

    @FXML
    private void decrementQuantity() {
        System.out.println("decrementQuantity button clicked");
        if (quantity > 1) {  // Ensure quantity doesn't go below 1
            quantity--;
        }
        updateQuantityLabel();
    }

    @FXML
    private void incrementQuantity() {
        System.out.println("incrementQuantity button clicked");
        quantity++;
        updateQuantityLabel();
    }

    @FXML
    private void handleCancel() {
        System.out.println("handleCancel button clicked");
    }

    @FXML
    private void handleAddToCart() {
        System.out.println("handleAddToCart button clicked");
        if (callback != null) {
            // Pass the selected item details, including quantity, to the callback
            callback.onItemSelected(dialogTitle.getText(), dialogPrice.getText(), quantity);
        }
        // Close dialog after selection
        btnCancel.getScene().getWindow().hide();
    }

    private void updateQuantityLabel() {
        lblQuantity.setText(String.valueOf(quantity));
    }

    public void setItemSelectionCallback(ItemSelectionCallback callback) {
        this.callback = callback;
    }
}
