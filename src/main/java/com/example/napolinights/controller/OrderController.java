package com.example.napolinights.controller;

import com.example.napolinights.CartItem;
import com.example.napolinights.ItemSelectionCallback;
import com.example.napolinights.model.Category;
import com.example.napolinights.model.MenuItem;
import com.example.napolinights.model.MenuItemDAO;
import com.example.napolinights.model.SqliteConnection;
import com.example.napolinights.util.StyleConstants;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * OrderController is responsible for handling actions on the order page.
 * This class controls the behavior when the user interacts with the UI elements, such as buttons and menu items.
 */
public class OrderController {

    @FXML private TilePane menuItemsPane;  // To dynamically load menu items
    @FXML private ToolBar categoryToolBar; // Toolbar for category selection
    @FXML private VBox orderSection;       // Section for the ordered items
    @FXML private Label lblTotalPrice;     // Label for displaying total price
    @FXML private Button checkoutButton;   // Button to proceed to checkout


    private double totalPrice = 0.00;      // Variable to store total price

    // List of categories to display in the toolbar
    private List<Category> categories = List.of(
            Category.ENTREE, Category.PIZZA, Category.PASTA, Category.DESSERT, Category.BEVERAGES
    );

    /**
     * Initialize the controller, set up categories and their display.
     */
    @FXML
    private void initialize() {
        setupCategoryLinks();
        showCategory(Category.ENTREE);
    }

    /**
     * Setup category links in the toolbar.
     */
    private void setupCategoryLinks() {
        for (Category category : categories) {
            Hyperlink link = new Hyperlink(category.toString());
            link.setOnAction(event -> showCategory(category));
            categoryToolBar.getItems().add(link);
        }
    }


    /**
     * Loads the menu items from the database and displays them by category.
     */
    private void loadMenuItemsByCategory(Category selectedCategory) {
        // Fetch and display the menu items for the selected category
        List<MenuItem> menuItems = fetchMenuItems(selectedCategory);
        displayMenuItems(menuItems);
    }

    /**
     * Fetches menu items for a given category from the database.
     */
    private List<MenuItem> fetchMenuItems(Category selectedCategory) {
        // Get a connection to the database and fetch items by category
        Connection connection = SqliteConnection.getInstance();
        MenuItemDAO menuItemDAO = new MenuItemDAO(connection);
        return menuItemDAO.fetchAllMenuItemsByCategory(selectedCategory);
    }

    /**
     * Shows the menu items based on the selected category.
     */
    private void showCategory(Category category) {
        // Clears the previous menu items
        menuItemsPane.getChildren().clear();
        // Loads and displays items from the selected category
        loadMenuItemsByCategory(category);
    }

    /**
     * Displays the menu items in the UI by creating visual elements for each item.
     */
    private void displayMenuItems(List<MenuItem> menuItems) {
        for (MenuItem menuItem : menuItems) {
            VBox itemBox = createMenuItemBox(menuItem);
            menuItemsPane.getChildren().add(itemBox);
        }
    }

    /**
     * Creates a visual display (VBox) for a single menu item.
     */
    private VBox createMenuItemBox(MenuItem menuItem) {
        VBox itemBox = new VBox(10);
        itemBox.setAlignment(Pos.TOP_LEFT);
        itemBox.setStyle("-fx-border-color: lightgray; -fx-padding: 10;");

        // Attaches the click event to the VBox
        itemBox.setOnMouseClicked(event -> handleMenuItemClick(menuItem));

        // Creates the labels and image for the menu item
        Label itemTitle = createLabel(menuItem.getName(), "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label itemDescription = createLabel(menuItem.getDescription(), "-fx-font-size: 14px; -fx-text-fill: white;");
        Label itemPrice = createLabel(String.format("$%.2f", menuItem.getPrice()), "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
        ImageView itemImage = createImageView(menuItem.getImageURL());

        // Adds the labels and image to the VBox
        itemBox.getChildren().addAll(itemImage, itemTitle, itemDescription, itemPrice);

        return itemBox;
    }

    /**
     * Creates a styled label.
     */
    private Label createLabel(String text, String style) {
        Label label = new Label(text);
        label.setStyle(style);
        return label;
    }

    /**
     * Creates an ImageView for a menu item, using a default image if none is provided.
     */
    private ImageView createImageView(String imageUrl) {
        String imagePath = getMenuItemImageOrDefault(imageUrl);
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        return imageView;
    }

    /**
     * Returns the image URL or a default image if none is available.
     */
    private String getMenuItemImageOrDefault(String imageUrl) {
        if (imageUrl != null && !imageUrl.trim().isEmpty() && !imageUrl.equalsIgnoreCase("null")) {
            URL menuItemImageUrl = getClass().getResource("/com/example/napolinights/images/menu_images/" + imageUrl);
            if (menuItemImageUrl != null) {
                return menuItemImageUrl.toExternalForm();
            }
        }
        return getClass().getResource("/com/example/napolinights/images/menu_images/no_image_found.png").toExternalForm();
    }

    /**
     * Handles a menu item click event to display its details.
     */
    private void handleMenuItemClick(MenuItem menuItem) {
        try {
            showDialog(menuItem.getName(), menuItem.getDescription(), String.format("$%.2f", menuItem.getPrice()), menuItem.getImageURL(), this::updateSelectedItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showDialog(String title, String description, String price, String imagePath, ItemSelectionCallback callback) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ItemDetailDialog.fxml"));
            VBox dialogVBox = loader.load();

            System.out.println("Image Path: " + imagePath);
            ItemDetailDialogController itemDetailDialogController = loader.getController();
            itemDetailDialogController.setItemSelectionCallback(callback);
            Label dialogTitle = (Label) loader.getNamespace().get("dialogTitle");
            Label dialogDescription = (Label) loader.getNamespace().get("dialogDescription");
            Label dialogPrice = (Label) loader.getNamespace().get("dialogPrice");
            ImageView dialogImageView = (ImageView) loader.getNamespace().get("dialogImageView");
            Button cancelButton = (Button) loader.getNamespace().get("btnCancel");
            dialogTitle.setText(title);
            dialogDescription.setText(description);
            dialogPrice.setText(price);
            String menuImagePath = getMenuItemImageOrDefault(imagePath);
            dialogImageView.setImage(new Image(menuImagePath));
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle(title);
            Scene dialogScene = new Scene(dialogVBox);
            dialogStage.setScene(dialogScene);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            cancelButton.setOnAction(e -> dialogStage.close());
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateSelectedItem(String itemName, String itemPrice, Integer itemQty) {
        Label nameLabel = new Label(itemName);
        Label priceLabel = new Label(String.format("$%.2f", Double.parseDouble(itemPrice.replace("$", "")) * itemQty));
        // Hidden unit price label
        Label unitPriceLabel = new Label(itemPrice);
        unitPriceLabel.setVisible(false); // This makes it invisible in the UI

        nameLabel.setStyle(StyleConstants.ITEM_NAME_STYLE);
        priceLabel.setStyle(StyleConstants.ITEM_PRICE_STYLE);

        Button incrementButton = new Button("+");
        Button decrementButton = new Button("-");

        Label quantityLabel = new Label(String.valueOf(itemQty));

        incrementButton.setOnAction(e -> incrementQuantity(itemPrice, priceLabel, quantityLabel));
        decrementButton.setOnAction(e -> decrementQuantity(itemPrice, priceLabel, quantityLabel));

        HBox itemBox = new HBox(10);
        itemBox.getChildren().addAll(decrementButton, quantityLabel, incrementButton, nameLabel, priceLabel, unitPriceLabel);
        itemBox.setAlignment(Pos.CENTER);

        orderSection.getChildren().add(itemBox);

        updateTotalPrice();
    }

    private void incrementQuantity(String itemPrice, Label priceLabel, Label quantityLabel) {
        int currentQuantity = Integer.parseInt(quantityLabel.getText());
        currentQuantity++;
        quantityLabel.setText(String.valueOf(currentQuantity));
        updateItemPrice(itemPrice, currentQuantity, priceLabel);
        updateTotalPrice();
    }

    private void decrementQuantity(String itemPrice, Label priceLabel, Label quantityLabel) {
        int currentQuantity = Integer.parseInt(quantityLabel.getText());
        if (currentQuantity > 1) {
            currentQuantity--;
            quantityLabel.setText(String.valueOf(currentQuantity));
            updateItemPrice(itemPrice, currentQuantity, priceLabel);
            updateTotalPrice();
        }
    }

    private void updateTotalPrice() {
        totalPrice = 0.0;
        for (int i = 0; i < orderSection.getChildren().size(); i++) {
            HBox itemBox = (HBox) orderSection.getChildren().get(i);
            Label priceLabel = (Label) itemBox.getChildren().get(4);

            double itemPrice = Double.parseDouble(priceLabel.getText().replace("$", ""));
            totalPrice += itemPrice;
        }
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Checkout.fxml"));
            Parent checkoutPage = loader.load();
            checkoutController = loader.getController();

            passCartData();

            Stage stage = (Stage) this.checkoutButton.getScene().getWindow();
            setupStage(stage, checkoutPage, "Checkout");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void passCartData() {
        int cartLength = orderSection.getChildren().size();
        CartItem[] item = new CartItem[cartLength];
        for (int i = 0; i < cartLength; i++) {
            HBox itemBox = (HBox) orderSection.getChildren().get(i);
            Label nameLabel = (Label) itemBox.getChildren().get(3);
            Label priceLabel = (Label) itemBox.getChildren().get(4);
            Label quantityLabel = (Label) itemBox.getChildren().get(1);
            // Retrieving the hidden unit price label
            Label unitPriceLabel = (Label) itemBox.getChildren().get(5);

            String name = nameLabel.getText();
            double price = parseItemPrice(priceLabel.getText());
            int quantity = parseItemQuantity(quantityLabel.getText());
            double unitPrice = parseItemPrice(unitPriceLabel.getText());
            item[i] = new CartItem(name, unitPrice, quantity);
        }
        System.out.println("passing item to cart: " + item);
        checkoutController.receiveData(item);
    }

    private double parseItemPrice(String string) {
        return Double.parseDouble(string.replace("$", ""));
    }

    private int parseItemQuantity(String string) {
        return Integer.parseInt(string);
    }

    private void setupStage(Stage stage, Parent page, String title) {
        stage.setTitle(title);
        Scene scene = new Scene(page);
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.show();
    }
}

