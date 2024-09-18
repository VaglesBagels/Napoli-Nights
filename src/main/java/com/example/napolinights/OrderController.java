package com.example.napolinights;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;

public class OrderController {

    @FXML
    private Hyperlink lnkEntree;

    @FXML
    private Hyperlink lnkMains;

    @FXML
    private AnchorPane contentAnchorPane;

    @FXML
    private VBox entreeSection;

    @FXML
    private VBox mainsSection;

    @FXML
    private Button btnCancel;

    @FXML
    private VBox orderSection;

    @FXML
    private Label lblTotalPrice;

    private double totalPrice = 0.00;

    private int quantity = 1; // Default quantity

    @FXML
    private Button checkoutButton;

    @FXML
    private void initialize() {
        if (lnkEntree != null && lnkMains != null) {
            System.out.println("lnkEntree and lnkMains are not null");
//            lnkEntree.setOnAction(event -> showSection(entreeSection));
//            lnkMains.setOnAction(event -> showSection(mainsSection));
            System.out.println("Initialized Menu Controller");
        } else {
            System.out.println("One or both amongst lnkEntree and lnkMains are null");
        }

    }

    @FXML
    private void handleMenuOptionMouseClick(MouseEvent event) {
        System.out.println("on menu option mouse clicked");
        System.out.println(event.getSource());
    }

    @FXML
    private void handleMenuOptionClick(ActionEvent event) {
        System.out.println("handleMenuOptionClick for section: " + event.getSource());
        if (event.getSource() == lnkEntree) {
            showSection(entreeSection);
        } else if (event.getSource() == lnkMains) {
            showSection(mainsSection);
        }
        mainsSection.getParent().layout();
        /*switch (section) {
            case "entree":
                showSection(entreeSection);
                break;
            case "mains":
                showSection(mainsSection);
                break;
        }*/
    }

    @FXML
    private void handleMenuItemClick(MouseEvent event) {
        // Get the clicked Vbox
        Object source = event.getSource();
        System.out.println("handleVBoxClick1 Source: " + source);
        if (source instanceof VBox) {
            VBox clickedVBox = (VBox) source;
            System.out.println("Clicked VBox ID: " + clickedVBox.getId());
            HBox hBox = (HBox) clickedVBox.getChildren().get(0);
            System.out.println("Clicked HBox ID: " + hBox.getId());
            Object objVbox = hBox.getChildren().get(0);
            if (objVbox instanceof VBox) {
                System.out.println("OBJVBOX text: " + ((VBox) objVbox).getChildren().get(0));
                VBox controlsVBox = (VBox) objVbox;
                String title = extractTextFromLabel(controlsVBox, 0);
                String description = extractTextFromLabel(controlsVBox, 1);
                String price = extractTextFromLabel(controlsVBox, 2);
                ImageView imageViewCtrl = (ImageView) (hBox.getChildren().get(1));
                System.out.println("ImageViewCtrl Image ID: " + imageViewCtrl.getId());
                String imagePath = ((Image) imageViewCtrl.getImage()).getUrl();
                System.out.println("Image Path: " + imagePath);
                showDetails(
                        title,
                        description,
                        price,
                        imagePath
                );
            }
            else {
                System.out.println("Source is not a VBox, it's a: " + source.getClass().getSimpleName());
            }

        } else {
            System.out.println("Source is not a VBox, it's a: " + source.getClass().getSimpleName());
        }
        //VBox clickedVBox = (VBox) event.getSource();
        //System.out.println("Clicked VBox" + clickedVBox.getId());

        // Extract details from the VBox

    }

    private String extractTextFromLabel(VBox vbox, int index) {
        // Assuming that the Labels are in the first VBox inside the clicked VBox
        Label label = (Label) vbox.getChildren().get(index);
        System.out.println("extractTextFromLabel Label: " + label.getText());
        return label.getText();
    }


    private void showDetails(String title, String details, String price, String imagePath) {
        System.out.println("Show title: " + title + " price: " + price + " imagePath: " + imagePath);
        showDialog(title, details, price, imagePath, ((itemName, itemPrice, itemQty) -> {
            updateSelectedItem(itemName, itemPrice, itemQty);
        }));
    }

    private void showDialog(String title, String description, String price, String imagePath, ItemSelectionCallback callback) {
        try {
            // Load the FXML template
            FXMLLoader loader = new FXMLLoader(getClass().getResource("itemDetailDialog.fxml"));
            VBox dialogVBox = loader.load();

            System.out.println("Template loaded");

            ItemDetailDialogController itemDetailDialogController = loader.getController();
            itemDetailDialogController.setItemSelectionCallback(callback);

            // Access the controls in the dialog
            Label dialogTitle = (Label) loader.getNamespace().get("dialogTitle");
            Label dialogDescription = (Label) loader.getNamespace().get("dialogDescription");
            Label dialogPrice = (Label) loader.getNamespace().get("dialogPrice");
            ImageView dialogImageView = (ImageView) loader.getNamespace().get("dialogImageView");
            Button cancelButton = (Button) loader.getNamespace().get("btnCancel");

            // Set the values dynamically
            dialogTitle.setText(title);
            dialogDescription.setText(description);
            dialogPrice.setText(price);
            dialogImageView.setImage(new Image(imagePath));

            // Create a new Stage for the dialog
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle(title);

            // Set up the scene and add the content
            Scene dialogScene = new Scene(dialogVBox);
            dialogStage.setScene(dialogScene);

            // Disable the close (X) button by setting the stage's style
            dialogStage.initStyle(StageStyle.UNDECORATED);

            // Handle the "Cancel" button to close the dialog
            cancelButton.setOnAction(e -> dialogStage.close());

            dialogStage.showAndWait(); // showAndWait will block until the dialog is closed
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showSection(VBox sectionToShow) {
        System.out.println("Show Section");
        System.out.println(sectionToShow.getId());
        // Hide all sections
        entreeSection.setVisible(false);
        mainsSection.setVisible(false);

        // Show selected section
        sectionToShow.setVisible(true);
    }

    public void updateSelectedItem(String itemName, String itemPrice, Integer itemQty) {
        System.out.println("Adding item: " + itemName + " Price: " + itemPrice + " Qty: " + itemQty);

        // Create labels for item name and price
        Label nameLabel = new Label(itemName);
        Label priceLabel = new Label(String.format("$%.2f", Double.parseDouble(itemPrice.replace("$", "")) * itemQty));  // Adjust for initial quantity

        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        priceLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");

        // Create buttons for quantity control
        Button incrementButton = new Button("+");
        Button decrementButton = new Button("-");
        Label quantityLabel = new Label(String.valueOf(itemQty));  // Display initial quantity passed from the dialog

        incrementButton.setOnAction(e -> incrementQuantity(itemPrice, priceLabel, quantityLabel));
        decrementButton.setOnAction(e -> decrementQuantity(itemPrice, priceLabel, quantityLabel));

        // Create an HBox to hold the buttons and labels
        HBox itemBox = new HBox(10); // 10 is spacing between elements
        itemBox.getChildren().addAll(decrementButton, quantityLabel, incrementButton, nameLabel, priceLabel);
        itemBox.setAlignment(Pos.CENTER);

        // Add the HBox to the orderSection VBox
        orderSection.getChildren().add(itemBox);

        // Update the total price
        updateTotalPrice();
    }


    private void incrementQuantity(String itemPrice, Label priceLabel, Label quantityLabel) {
        int currentQuantity = Integer.parseInt(quantityLabel.getText());  // Get current quantity
        currentQuantity++;  // Increment quantity
        quantityLabel.setText(String.valueOf(currentQuantity));  // Update quantity label
        updateItemPrice(itemPrice, currentQuantity, priceLabel);  // Update price display for this item
        updateTotalPrice();  // Update the total price for the entire order
    }

    private void decrementQuantity(String itemPrice, Label priceLabel, Label quantityLabel) {
        int currentQuantity = Integer.parseInt(quantityLabel.getText());  // Get current quantity
        if (currentQuantity > 1) {
            currentQuantity--;  // Decrement quantity if greater than 1
            quantityLabel.setText(String.valueOf(currentQuantity));  // Update quantity label
            updateItemPrice(itemPrice, currentQuantity, priceLabel);  // Update price display for this item
            updateTotalPrice();  // Update the total price for the entire order
        }
    }


    private void updateTotalPrice() {
        totalPrice = 0.0;

        // Iterate over all items in the orderSection to calculate total price
        for (int i = 0; i < orderSection.getChildren().size(); i++) {
            HBox itemBox = (HBox) orderSection.getChildren().get(i);
            Label nameLabel = (Label) itemBox.getChildren().get(4);  // Price label is the 5th element (index 4)

            double itemPrice = Double.parseDouble(nameLabel.getText().replace("$", ""));
            totalPrice += itemPrice;
        }

        // Update the total price label
        lblTotalPrice.setText(String.format("$%.2f", totalPrice));
    }



    private void updateItemPrice(String itemPrice, int quantity, Label priceLabel) {
        double price = Double.parseDouble(itemPrice.replace("$", ""));
        double updatedPrice = price * quantity;
        priceLabel.setText(String.format("$%.2f", updatedPrice));
    }


    private CheckoutController checkoutController;

    @FXML
    private void HandleCheckoutButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/napolinights/Checkout.fxml"));
            Parent checkoutPage = loader.load();
            checkoutController = loader.getController();

            passCartData();

            Stage stage = (Stage)this.checkoutButton.getScene().getWindow();
            stage.setTitle("Checkout");
            Scene scene = new Scene(checkoutPage);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void passCartData() {

        int cartLength = orderSection.getChildren().size();

        CartItem[] item = new CartItem[cartLength];
        for (int i = 0; i < cartLength; i++) {
            HBox itemBox = (HBox) orderSection.getChildren().get(i);
            Label nameLabel = (Label) itemBox.getChildren().get(3);  // Name label is the 4th element (index 3)
            Label priceLabel = (Label) itemBox.getChildren().get(4);  // Price label is the 5th element (index 4)
            Label quantityLabel = (Label) itemBox.getChildren().get(1);  // Quantity label is the 2nd element (index 1)

            String name = nameLabel.getText();
            double price = parseItemPrice(priceLabel.getText());
            int quantity = parseItemQuantity(quantityLabel.getText());
            item[i] = new CartItem(name, price,quantity);

            System.out.println(name + ", " +  price + ", " + quantity);

        }
        checkoutController.receiveData(item);
    }

    private double parseItemPrice(String string){
        double itemPrice = Double.parseDouble(string.replace("$", ""));
        return itemPrice;
    }

    private int parseItemQuantity(String string) {
        int quantity = Integer.parseInt(string);
        return quantity;
    }


}

