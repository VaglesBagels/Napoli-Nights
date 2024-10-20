package com.example.napolinights.controller;

import com.example.napolinights.CartItem;
import com.example.napolinights.ItemSelectionCallback;
import com.example.napolinights.model.Category;
import com.example.napolinights.model.MenuItem;
import com.example.napolinights.model.MenuItemDAO;
import com.example.napolinights.model.SqliteConnection;
import com.example.napolinights.util.StyleConstants;
import com.example.napolinights.util.UIComponentBuilder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
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
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import static com.example.napolinights.util.UIComponentBuilder.getMenuItemImageOrDefault;


/**
 * Controller for handling actions on the Order Page.
 * This includes displaying menu items, handling item selection, and managing the order summary.
 */
public class OrderController {

    @FXML private TilePane menuItemsPane;  // To dynamically load menu items
    @FXML private AnchorPane orderPane;  // AnchorPane to hold the menu items
    @FXML private ToolBar categoryToolBar; // Toolbar for category selection
    @FXML private VBox orderSection;       // Section for the ordered items
    @FXML private Label lblSubtotal;       // Label for displaying subtotal (Excl GST)
    @FXML private Label lblGST;            // Label for displaying GST
    @FXML private Label lblDiscount;       // Label for displaying discount
    @FXML private Label lblTotalIncGST;    // Label for displaying total including GST
    @FXML private Button checkoutButton;   // Button to proceed to checkout
    @FXML private Button homeButton;
    @FXML private VBox headerPane;

    private double totalPrice = 0.00;      // Variable to store total price
    private double subtotal = 0.00;        // Variable to store subtotal (Excl GST)
    private double gst = 0.00;             // Variable to store GST
    private double discount = 0.00;        // Variable to store discount
    private Hyperlink activeTab;           // Keep track of the active tab
    private CheckoutController checkoutController; // Controller for the checkout page


    // List of categories to display in the toolbar
    private List<Category> categories = List.of(
            Category.ENTREE, Category.PIZZA, Category.PASTA, Category.DESSERT, Category.BEVERAGES
    );


    /* ===============================================
     * SECTION 1: Displaying Items on the Menu Page
     * =============================================== */

    /**
     * Initializes the controller, sets up category toolbar, and displays 'ENTREE' category by default.
     */
    @FXML
    private void initialize() {
        // Set padding for the contentAnchorPane to provide spacing
        headerPane.setPadding(new Insets(0, 0, 0, 10)); // Top, right, bottom, left padding

        setupCategoryLinks(); // Set up category hyperlinks in the toolbar
        showCategory(Category.ENTREE); // Show 'ENTREE' items by default
        // Ensure that the stage size is adjusted after the scene is loaded
        Platform.runLater(() -> {
            Stage stage = (Stage) orderPane.getScene().getWindow();
            stage.setMinWidth(800);
            stage.setMinHeight(600);
        });
    }


    /**
     * Sets up category hyperlinks in the toolbar.
     */
    private void setupCategoryLinks() {
        for (Category category : categories) {
            Hyperlink link = new Hyperlink(category.toString());
            setInactiveStyle(link); // Set the default inactive style
            link.setOnAction(event -> {
                setActiveTab(link);  // Highlight the selected category
                showCategory(category);  // Show the selected category items
            });
            categoryToolBar.getItems().add(link);  // Add the link to the toolbar
        }
    }


    /**
     * Updates the active category tab.
     * @param newActiveTab The new active category hyperlink.
     */
    private void setActiveTab(Hyperlink newActiveTab) {
        if (activeTab != null) {
            setInactiveStyle(activeTab); // Reset the previous active tab
        }
        setActiveStyle(newActiveTab); // Set the new tab as active
        activeTab = newActiveTab;  // Update the reference to the active tab
    }


    /**
     * Applies the active style to the currently selected category tab.
     * @param tab The hyperlink to apply the active style to.
     */
    private void setActiveStyle(Hyperlink tab) {
        tab.setStyle("-fx-text-fill: white; -fx-font-weight: bold;"); // Active tab style
    }


    /**
     * Applies the inactive style to non-selected category tabs.
     * @param tab The hyperlink to apply the inactive style to.
     */
    private void setInactiveStyle(Hyperlink tab) {
        tab.setStyle("-fx-text-fill: #00BFFF; -fx-font-weight: normal;"); // Inactive tab style
    }


    /**
     * Loads and displays menu items based on the selected category.
     * @param selectedCategory The selected category to load items for.
     */
    private void loadMenuItemsByCategory(Category selectedCategory) {
        List<MenuItem> menuItems = fetchMenuItems(selectedCategory); // Fetch items from the database
        displayMenuItems(menuItems); // Display them in the UI
    }


    /**
     * Fetches the menu items for the selected category from the database.
     * @param selectedCategory The category to fetch menu items for.
     * @return List of menu items in the selected category.
     */
    private List<MenuItem> fetchMenuItems(Category selectedCategory) {
        Connection connection = SqliteConnection.getInstance(); // Get a connection to the database
        MenuItemDAO menuItemDAO = new MenuItemDAO(connection);  // DAO to fetch menu items
        return menuItemDAO.fetchAllMenuItemsByCategory(selectedCategory);  // Fetch items by category
    }


    /**
     * Displays the items for the selected category in the UI.
     * @param category The category to display items for.
     */
    private void showCategory(Category category) {
        menuItemsPane.getChildren().clear(); // Clear previous items
        loadMenuItemsByCategory(category);  // Load new items for the selected category
    }


    /**
     * Creates and displays visual elements for the menu items.
     * @param menuItems List of menu items to display.
     */
    private void displayMenuItems(List<MenuItem> menuItems) {
        for (MenuItem menuItem : menuItems) {
            VBox itemBox = createMenuItemBox(menuItem);  // Create the visual representation of the item
            menuItemsPane.getChildren().add(itemBox);  // Add it to the menu pane
        }
    }


    /**
     * Creates a VBox for each menu item to display its details.
     * @param menuItem The menu item to create a visual box for.
     * @return A VBox containing the visual representation of the menu item.
     */
    private VBox createMenuItemBox(MenuItem menuItem) {
        VBox itemBox = new VBox(10);  // Create a VBox with 10px spacing
        itemBox.setAlignment(Pos.TOP_LEFT);  // Align content to top left
        itemBox.setStyle("-fx-border-color: lightgray; -fx-padding: 10;");  // Styling

        // Set consistent size for all item boxes
        itemBox.setPrefWidth(500);
        itemBox.setPrefHeight(150);

        // Set the MenuItem as user data for later retrieval
        itemBox.setUserData(menuItem);

        // Set an event handler for item click
        itemBox.setOnMouseClicked(this::handleMenuItemClick);

        // Create labels and image for the item
        Label itemTitle = UIComponentBuilder.createLabel(menuItem.getName(),"-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label itemDescription = UIComponentBuilder.createLabel(menuItem.getDescription(), "-fx-font-size: 14px; -fx-text-fill: white;");
        itemDescription.setWrapText(true);  // Enable text wrapping for long descriptions
        Label itemPrice = UIComponentBuilder.createLabel(String.format("$%.2f", menuItem.getPrice()), "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
        ImageView itemImage = UIComponentBuilder.createImageView(menuItem.getImageURL());
        itemImage.setFitHeight(80);
        itemImage.setFitWidth(80);

        // Add components to the VBox
        itemBox.getChildren().addAll(itemImage, itemTitle, itemDescription, itemPrice);
        return itemBox;
    }



    /* ===============================================
     * SECTION 2: Pop-up Dialog for Item Details
     * =============================================== */

    /**
     * Handles the click event on a menu item and displays its details in a dialog.
     * @param event The click event triggered by the user.
     */
    private void handleMenuItemClick(MouseEvent event) {
        VBox clickedBox = (VBox) event.getSource(); // Get the clicked VBox
        MenuItem menuItem = (MenuItem) clickedBox.getUserData();  // Retrieve the associated MenuItem

        if (menuItem != null) {
            try {
                showDialog(menuItem.getName(), menuItem.getDescription(),
                        String.format("$%.2f", menuItem.getPrice()), menuItem.getImageURL(), this::updateSelectedItem);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Displays a dialog with item details when a menu item is clicked.
     * @param title The name of the menu item.
     * @param description The description of the menu item.
     * @param price The price of the menu item.
     * @param imagePath The image URL of the menu item.
     * @param callback Callback to handle the selection of the item.
     */
    private void showDialog(String title, String description, String price, String imagePath, ItemSelectionCallback callback) {
        try {
            // Load the FXML for the dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ItemDetailDialog.fxml"));
            VBox dialogVBox = loader.load();

            // Set dialog content
            ItemDetailDialogController itemDetailDialogController = loader.getController();
            itemDetailDialogController.setItemSelectionCallback(callback);

            // Set dialog labels and image
            Label dialogTitle = (Label) loader.getNamespace().get("dialogTitle");
            Label dialogDescription = (Label) loader.getNamespace().get("dialogDescription");
            Label dialogPrice = (Label) loader.getNamespace().get("dialogPrice");
            ImageView dialogImageView = (ImageView) loader.getNamespace().get("dialogImageView");
            Button cancelButton = (Button) loader.getNamespace().get("btnCancel");

            dialogTitle.setText(title);
            dialogDescription.setText(description);
            dialogPrice.setText(price);

            // Load and set image
            String menuImagePath = getMenuItemImageOrDefault(imagePath);
            dialogImageView.setImage(new Image(menuImagePath));

            // Set up the dialog stage
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);  // Make it modal
            dialogStage.setTitle(title);

            Scene dialogScene = new Scene(dialogVBox);
            dialogStage.setScene(dialogScene);
            dialogStage.initStyle(StageStyle.UNDECORATED);  // No window decorations

            // Close dialog when cancel button is clicked
            cancelButton.setOnAction(e -> dialogStage.close());

            dialogStage.showAndWait();  // Show dialog and wait for user action

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /* ===============================================
     * SECTION 3: Right-hand Side Checkout (Order Summary)
     * =============================================== */

    /**
     * Event handler for the "Home" button click.
     * Navigates back to the Landing Page.
     */

    @FXML
    private void handleHomeButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LandingPage.fxml"));
            Parent landingPage = loader.load();
            Stage stage = (Stage) homeButton.getScene().getWindow();
            setupStage(stage, landingPage, "Landing Page");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Updates the selected item and adds it to the checkout section.
     * @param itemName The name of the menu item.
     * @param itemPrice The price of the menu item.
     * @param itemQty The quantity of the menu item.
     */
    public void updateSelectedItem(String itemName, String itemPrice, Integer itemQty) {
        // Create Label for item name and enable text wrapping
        Label nameLabel = new Label(itemName);
        nameLabel.setMaxWidth(200);  // Set maximum width for the label to fit in the fixed column size
        nameLabel.setMinWidth(200);  // Set minimum width for consistent alignment
        nameLabel.setWrapText(true);  // Enable text wrapping
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");  // Set text color to white

        // Create Label for price
        Label priceLabel = new Label(String.format("$%.2f", Double.parseDouble(itemPrice.replace("$", "")) * itemQty));
        priceLabel.setMinWidth(60);  // Set minimum width for consistent alignment
        priceLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");

        // Hidden unit price label
        Label unitPriceLabel = new Label(itemPrice);
        unitPriceLabel.setVisible(false); // This makes it invisible in the UI

        nameLabel.setStyle(StyleConstants.ITEM_NAME_STYLE);
        priceLabel.setStyle(StyleConstants.ITEM_PRICE_STYLE);

        // Increment and Decrement buttons with fixed size
        Button incrementButton = new Button("+");
        incrementButton.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-font-weight: bold; -fx-text-fill: black;");
        incrementButton.setPrefWidth(40);
        incrementButton.setPrefHeight(30);

        Button decrementButton = new Button("-");
        decrementButton.setStyle("-fx-font-size: 14px; -fx-background-color: white; -fx-font-weight: bold; -fx-text-fill: black;");
        decrementButton.setPrefWidth(40);
        decrementButton.setPrefHeight(30);

        // Label for quantity
        Label quantityLabel = new Label(String.valueOf(itemQty));
        quantityLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        GridPane.setHalignment(quantityLabel, HPos.CENTER); // Center align the quantity label within its column

        incrementButton.setOnAction(e -> incrementQuantity(itemPrice, priceLabel, quantityLabel));
        decrementButton.setOnAction(e -> decrementQuantity(itemPrice, priceLabel, quantityLabel));

        // GridPane for layout
        GridPane itemGrid = new GridPane();
        itemGrid.setHgap(10);  // Horizontal gap between columns
        itemGrid.setVgap(5);   // Vertical gap between rows

        // Set fixed column widths for alignment
        ColumnConstraints column1 = new ColumnConstraints(50);  // Decrement button
        ColumnConstraints column2 = new ColumnConstraints(50);  // Quantity label
        ColumnConstraints column3 = new ColumnConstraints(50);  // Increment button
        ColumnConstraints column4 = new ColumnConstraints(200); // Name label
        ColumnConstraints column5 = new ColumnConstraints(60);  // Price label
        column4.setHgrow(Priority.ALWAYS);  // Allow the name label column to grow if needed
        itemGrid.getColumnConstraints().addAll(column1, column2, column3, column4, column5);

        // Add elements to the grid (column, row)
        itemGrid.add(decrementButton, 0, 0);  // Column 0: Decrement button
        itemGrid.add(quantityLabel, 1, 0);    // Column 1: Quantity label
        itemGrid.add(incrementButton, 2, 0);  // Column 2: Increment button
        itemGrid.add(nameLabel, 3, 0);        // Column 3: Name label
        itemGrid.add(priceLabel, 4, 0);       // Column 4: Price label
        itemGrid.add(unitPriceLabel, 5, 0);   // Column 5: Hidden unit price label

        // Remove link
        Hyperlink removeLink = new Hyperlink("Remove");
        removeLink.setStyle("-fx-font-size: 14px; -fx-text-fill: #84AE87; -fx-font-weight: bold");
        removeLink.setOnAction(e -> {
            orderSection.getChildren().remove(itemGrid);
            updateTotalPrice();
        });
        itemGrid.add(removeLink, 3, 1, 2, 1); // Column 3-4: Remove link, spanning 2 columns

        itemGrid.setAlignment(Pos.CENTER_LEFT); // Align grid to the left for consistent layout

        orderSection.getChildren().add(itemGrid);

        updateTotalPrice();
    }


    /**
     * Increments the quantity of a selected item and updates the price.
     * @param itemPrice The price of the item.
     * @param priceLabel The label showing the price.
     * @param quantityLabel The label showing the quantity.
     */
    private void incrementQuantity(String itemPrice, Label priceLabel, Label quantityLabel) {
        int currentQuantity = Integer.parseInt(quantityLabel.getText());
        currentQuantity++;
        quantityLabel.setText(String.valueOf(currentQuantity));
        updateItemPrice(itemPrice, currentQuantity, priceLabel);
        updateTotalPrice();
    }


    /**
     * Decrements the quantity of a selected item, ensuring the quantity stays above 1.
     * @param itemPrice The price of the item.
     * @param priceLabel The label showing the price.
     * @param quantityLabel The label showing the quantity.
     */
    private void decrementQuantity(String itemPrice, Label priceLabel, Label quantityLabel) {
        int currentQuantity = Integer.parseInt(quantityLabel.getText());
        if (currentQuantity > 1) {
            currentQuantity--;
            quantityLabel.setText(String.valueOf(currentQuantity));
            updateItemPrice(itemPrice, currentQuantity, priceLabel);
            updateTotalPrice();
        }
    }


    /**
     * Updates the total price of all items in the order section.
     */
    private void updateTotalPrice() {
        subtotal = 0.0;
        gst = 0.0;
        for (int i = 0; i < orderSection.getChildren().size(); i++) {
            GridPane itemBox = (GridPane) orderSection.getChildren().get(i);
            Label priceLabel = (Label) itemBox.getChildren().get(4);
            Label quantityLabel = (Label) itemBox.getChildren().get(1);
            double unitPrice = Double.parseDouble(priceLabel.getText().replace("$", "")) / Integer.parseInt(quantityLabel.getText());
            int quantity = Integer.parseInt(quantityLabel.getText());
            subtotal += unitPrice * quantity;
            gst += (unitPrice * 0.10) * quantity; // GST is calculated per item and then multiplied by quantity
        }
        totalPrice = subtotal + gst - discount;

        lblSubtotal.setText(String.format("$%.2f", subtotal));
        lblGST.setText(String.format("$%.2f", gst));
        lblDiscount.setText(String.format("$%.2f", discount));
        lblTotalIncGST.setText(String.format("$%.2f", totalPrice));
    }


    /**
     * Updates the price of a single item based on its quantity.
     * @param itemPrice The price of the item.
     * @param quantity The quantity of the item.
     * @param priceLabel The label showing the updated price.
     */
    private void updateItemPrice(String itemPrice, int quantity, Label priceLabel) {
        double price = Double.parseDouble(itemPrice.replace("$", ""));
        priceLabel.setText(String.format("$%.2f", price * quantity));
    }



    /* ===============================================
     * SECTION 4: Checkout Process
     * =============================================== */

    /**
     * Handles the checkout button click to navigate to the checkout page.
     */
    @FXML
    private void HandleCheckoutButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Checkout.fxml"));
            Parent checkoutPage = loader.load();
            checkoutController = loader.getController();

            //passCartData();  // Pass cart data to the checkout page

            Stage stage = (Stage) this.checkoutButton.getScene().getWindow();
            setupStage(stage, checkoutPage, "Checkout");  // Setup and show the checkout stage
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Passes the cart data (items, quantities, prices) to the checkout page.
     */

    /**
    @FXML
    public void passCartData() {
        int cartLength = orderSection.getChildren().size();
        CartItem[] items = new CartItem[cartLength];
        for (int i = 0; i < cartLength; i++) {
            GridPane itemBox = (GridPane) orderSection.getChildren().get(i);
            Label nameLabel = (Label) itemBox.getChildren().get(3);
            Label priceLabel = (Label) itemBox.getChildren().get(4);
            Label quantityLabel = (Label) itemBox.getChildren().get(1);
            Label unitPriceLabel = (Label) itemBox.getChildren().get(5);

            String name = nameLabel.getText();
            double price = parseItemPrice(priceLabel.getText());
            int quantity = parseItemQuantity(quantityLabel.getText());
            double unitPrice = parseItemPrice(unitPriceLabel.getText());
            items[i] = new CartItem(name, unitPrice, quantity);
        }
         checkoutController.receiveData(items);
    }
     */



    /**
     * Parses the price string to a double.
     * @param string The price string to parse.
     * @return The price as a double.
     */
    private double parseItemPrice(String string) {
        return Double.parseDouble(string.replace("$", ""));
    }


    /**
     * Parses the quantity string to an integer.
     * @param string The quantity string to parse.
     * @return The quantity as an integer.
     */
    private int parseItemQuantity(String string) {
        return Integer.parseInt(string);
    }


    /**
     * Sets up the stage for navigation to a new page.
     * @param stage The current stage.
     * @param page The new page to load.
     * @param title The title of the new page.
     */
    private void setupStage(Stage stage, Parent page, String title) {
        stage.setTitle(title);
        Scene scene = new Scene(page);
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.show();
    }

}




