package com.example.napolinights.controller;

import com.example.napolinights.ItemSelectionCallback;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


/**
 * Controller for handling the Item Detail Dialog.
 * This dialog allows the user to view item details, adjust the quantity, and add the item to the cart.
 */
public class ItemDetailDialogController {

    // UI elements in the FXML for item details and actions
    @FXML private Label dialogId; // Item ID
    @FXML private Label dialogTitle; // Item title
    @FXML private Label dialogDescription; // Item description
    @FXML private Label dialogPrice; // Item price
    @FXML private Button btnCancel; // Cancel button
    @FXML private Label lblQuantity; // Quantity label

    private int quantity = 1; // Initial quantity

    // Callback interface for item selection
    private ItemSelectionCallback callback;


    /**
     * Decrements the quantity of the item.
     * Ensures that quantity does not go below 1.
     */
    @FXML
    private void decrementQuantity() {
        System.out.println("decrementQuantity button clicked");
        if (quantity > 1) {  // Ensure quantity doesn't go below 1
            quantity--;
        }
        updateQuantityLabel();
    }


    /**
     * Increments the quantity of the item.
     */
    @FXML
    private void incrementQuantity() {
        System.out.println("incrementQuantity button clicked");
        quantity++;
        updateQuantityLabel();
    }


    /**
     * Handles the cancel button action.
     * Closes the dialog without adding the item to the cart.
     */
    @FXML
    private void handleCancel() {
        System.out.println("handleCancel button clicked");
    }


    /**
     * Handles the "Add to Cart" button action.
     * Passes the selected item details to the callback and closes the dialog.
     */
    @FXML
    private void handleAddToCart() {
        System.out.println("handleAddToCart button clicked");
        if (callback != null) {
            // Pass the selected item details, including quantity, to the callback
            callback.onItemSelected(dialogId.getText(), dialogTitle.getText(), dialogPrice.getText(), quantity);
        }
        // Close dialog after selection
        btnCancel.getScene().getWindow().hide();
    }


    /**
     * Updates the quantity label in the dialog with the current quantity.
     */
    private void updateQuantityLabel() {

        lblQuantity.setText(String.valueOf(quantity));
    }


    /**
     * Sets the callback to handle item selection.
     * @param callback The callback to set.
     */
    public void setItemSelectionCallback(ItemSelectionCallback callback) {

        this.callback = callback;
    }
}
