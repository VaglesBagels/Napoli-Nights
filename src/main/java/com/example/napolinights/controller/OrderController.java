
package com.example.napolinights.controller;

import com.example.napolinights.CartItem;
import com.example.napolinights.ItemSelectionCallback;
import com.example.napolinights.model.*;
import com.example.napolinights.model.MenuItem;
import com.example.napolinights.util.StyleConstants;
import com.example.napolinights.util.UIComponentBuilder;
import com.example.napolinights.util.StageConstants;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import static com.example.napolinights.util.UIComponentBuilder.getMenuItemImageOrDefault;


/**
 * Controller for handling actions on the Order Page.
 * This includes displaying menu items, handling item selection, and managing the order summary.
 */
public class OrderController {

    @FXML private TilePane menuItemsPane;  // Pane for dynamically loading menu items
    @FXML private ScrollPane menuItemsScrollPane; // ScrollPane to hold the menu items
    @FXML private VBox menuItemsParent;    // Parent VBox for the menu items
    @FXML private AnchorPane orderPane;    // AnchorPane to hold the menu items
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

    private static final double ITEM_BOX_WIDTH = 500.0;
    private static final double ITEM_BOX_HEIGHT = 150.0;
    private static final int ITEM_IMAGE_SIZE = 80;
    private static final double HEADER_PADDING_LEFT = 10.0;

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
        headerPane.setPadding(new Insets(0, 0, 0, HEADER_PADDING_LEFT)); // Left padding for header

        setupCategoryLinks(); // Initialize category hyperlinks
        showCategory(Category.ENTREE); // Show 'ENTREE' items by default

        // Ensure consistent stage sizing using StageConstants
        Platform.runLater(() -> StageConstants.setStageSize((Stage) orderPane.getScene().getWindow()));
    }


    /**
     * Sets up category hyperlinks in the toolbar.
     */
    private void setupCategoryLinks() {
        for (Category category : categories) {
            Hyperlink link = new Hyperlink(category.toString());
            setInactiveStyle(link); // Apply inactive style by default
            link.setOnAction(event -> {
                setActiveTab(link);  // Highlight the selected category
                showCategory(category);  // Display the selected category items
            });
            categoryToolBar.getItems().add(link);  // Add each category link to the toolbar
        }
    }


    /**
     * Updates the active category tab to highlight the selected category.
     * @param newActiveTab The newly selected category hyperlink.
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
        tab.setStyle(StyleConstants.ACTIVE_TAB_STYLE); // Active tab style
    }


    /**
     * Applies the inactive style to non-selected category tabs.
     * @param tab The hyperlink to apply the inactive style to.
     */
    private void setInactiveStyle(Hyperlink tab) {
        tab.setStyle(StyleConstants.INACTIVE_TAB_STYLE); // Inactive tab style
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
        try (Connection connection = SqliteConnection.getInstance()) {
            MenuItemDAO menuItemDAO = new MenuItemDAO(connection);  // DAO to fetch menu items
            return menuItemDAO.fetchAllMenuItemsByCategory(selectedCategory);  // Fetch items by category
        } catch (SQLException e) {
            System.err.println("Failed to fetch menu items: " + e.getMessage());
            return List.of();
        }
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
        itemBox.setStyle(StyleConstants.ITEM_BOX_STYLE);  // Styling constant for item box

        // Set consistent size for all item boxes
        itemBox.setPrefWidth(ITEM_BOX_WIDTH);
        itemBox.setPrefHeight(ITEM_BOX_HEIGHT);

        // Store the MenuItem in the VBox for later retrieval
        itemBox.setUserData(menuItem);

        // Set an event handler for item click
        itemBox.setOnMouseClicked(this::handleMenuItemClick);


        // Create labels and image for the item
        String menuId = Integer.toString(menuItem.getMenuItemID());
        Label itemId = UIComponentBuilder.createLabel(menuId, "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
        itemId.setVisible(false);

        Label itemTitle = UIComponentBuilder.createLabel(
                menuItem.getName(), StyleConstants.ITEM_NAME_STYLE);  // Item name styling

        Label itemDescription = UIComponentBuilder.createLabel(
                menuItem.getDescription(), StyleConstants.ITEM_DESC_STYLE);  // Item description styling
        itemDescription.setWrapText(true);  // Enable text wrapping for long descriptions


        Label itemPrice = UIComponentBuilder.createLabel(
                String.format("$%.2f", menuItem.getPrice()), StyleConstants.ITEM_PRICE_STYLE);  // Price styling

        ImageView itemImage = UIComponentBuilder.createImageView(menuItem.getImageURL());
        itemImage.setFitHeight(ITEM_IMAGE_SIZE);
        itemImage.setFitWidth(ITEM_IMAGE_SIZE);
        // Add components to the VBox
        itemBox.getChildren().addAll(itemImage, itemTitle, itemDescription, itemPrice);
        return itemBox;
    }



    /* ===============================================
     * SECTION 2: Pop-up Dialog for Item Details
     * =============================================== */

    /**
     * Handles the click event on a menu item and displays its details in a dialog.
     * Retrieves the MenuItem from the clicked VBox and passes the data to the showDialog method.
     *
     * @param event The click event triggered by the user.
     */
    private void handleMenuItemClick(MouseEvent event) {
        VBox clickedBox = (VBox) event.getSource();  // Get the clicked VBox
        MenuItem menuItem = (MenuItem) clickedBox.getUserData();  // Retrieve the associated MenuItem

        if (menuItem != null) {
            try {
                String menuId = Integer.toString(menuItem.getMenuItemID());
                // Display dialog with menu item details
                showDialog(menuId, menuItem.getName(), menuItem.getDescription(),
                        String.format("$%.2f", menuItem.getPrice()), menuItem.getImageURL(), this::updateSelectedItem);
            } catch (Exception e) {
                System.err.println("Error handling menu item click: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    /**
     * Displays a dialog with item details when a menu item is clicked.
     * This method sets up the dialog's UI components and functionality.
     *
     * @param id The ID of the menu item.
     * @param title The name of the menu item.
     * @param description The description of the menu item.
     * @param price The price of the menu item.
     * @param imagePath The image URL of the menu item.
     * @param callback Callback to handle the selection of the item.
     */
    private void showDialog(String id, String title, String description, String price, String imagePath, ItemSelectionCallback callback) {
        try {
            // Load the dialog FXML layout
            FXMLLoader loader = new FXMLLoader(getClass().getResource(StyleConstants.ITEM_DETAIL_DIALOG_FXML));
            VBox dialogVBox = loader.load();

            // Access and set dialog components via controller
            ItemDetailDialogController itemDetailDialogController = loader.getController();
            itemDetailDialogController.setItemSelectionCallback(callback);  // Set callback for item selection

            // Retrieve components from the loaded FXML
            Label dialogId = (Label) loader.getNamespace().get("dialogId");
            Label dialogTitle = (Label) loader.getNamespace().get("dialogTitle");
            Label dialogDescription = (Label) loader.getNamespace().get("dialogDescription");
            Label dialogPrice = (Label) loader.getNamespace().get("dialogPrice");
            ImageView dialogImageView = (ImageView) loader.getNamespace().get("dialogImageView");
            Button cancelButton = (Button) loader.getNamespace().get("btnCancel");

            // Set component values
            dialogId.setText(id);
            dialogId.setVisible(false);  // Hide ID label for user-facing dialog
            dialogTitle.setText(title);
            dialogDescription.setText(description);
            dialogPrice.setText(price);

            // Load and display item image with fallback to default
            String menuImagePath = getMenuItemImageOrDefault(imagePath);
            dialogImageView.setImage(new Image(menuImagePath));

            // Set up the dialog stage with constant properties
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle(StyleConstants.DIALOG_TITLE);

            Scene dialogScene = new Scene(dialogVBox);
            dialogStage.setScene(dialogScene);
            dialogStage.initStyle(StageStyle.valueOf(StyleConstants.DIALOG_STYLE_UNDECORATED));

            // Close the dialog when cancel button is clicked
            cancelButton.setOnAction(e -> dialogStage.close());

            // Display the dialog and wait for user action
            dialogStage.showAndWait();

        } catch (IOException e) {
            System.err.println("Error displaying item details dialog: " + e.getMessage());
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource(StyleConstants.LANDING_PAGE_FXML));
            Parent landingPage = loader.load();
            Stage stage = (Stage) homeButton.getScene().getWindow();
            setupStage(stage, landingPage, "Landing Page");
        } catch (IOException e) {
            System.err.println("Error loading Landing Page: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Updates the selected item and adds it to the checkout section.
     * @param itemId The ID of the menu item.
     * @param itemName The name of the menu item.
     * @param itemPrice The price of the menu item.
     * @param itemQty The quantity of the menu item.
     */
    public void updateSelectedItem(String itemId, String itemName, String itemPrice, Integer itemQty) {
        // Label for item name with wrap and style
        Label nameLabel = new Label(itemName);
        nameLabel.setMaxWidth(StyleConstants.GRID_COLUMN_WIDTH_NAME);
        nameLabel.setMinWidth(StyleConstants.GRID_COLUMN_WIDTH_NAME);
        nameLabel.setWrapText(true);
        nameLabel.setStyle(StyleConstants.ITEM_NAME_STYLE_2);

        // Label for price, with calculated total for the quantity
        Label priceLabel = new Label(String.format("$%.2f", Double.parseDouble(itemPrice.replace("$", "")) * itemQty));
        priceLabel.setMinWidth(StyleConstants.GRID_COLUMN_WIDTH_PRICE);
        priceLabel.setStyle(StyleConstants.ITEM_PRICE_STYLE_2);

        // Hidden labels for item ID and unit price (used for internal reference)
        Label idLabel = new Label(itemId);
        idLabel.setVisible(false);

        Label unitPriceLabel = new Label(itemPrice);
        unitPriceLabel.setVisible(false);

        nameLabel.setStyle(StyleConstants.ITEM_NAME_STYLE);
        priceLabel.setStyle(StyleConstants.ITEM_PRICE_STYLE);

        // Increment and decrement buttons for quantity management
        Button incrementButton = new Button("+");
        incrementButton.setStyle(StyleConstants.BUTTON_STYLE);
        incrementButton.setPrefWidth(StyleConstants.GRID_COLUMN_WIDTH_BUTTON);
        incrementButton.setPrefHeight(StyleConstants.GRID_COLUMN_HEIGHT_BUTTON);

        Button decrementButton = new Button("-");
        decrementButton.setStyle(StyleConstants.BUTTON_STYLE);
        decrementButton.setPrefWidth(StyleConstants.GRID_COLUMN_WIDTH_BUTTON);
        decrementButton.setPrefHeight(StyleConstants.GRID_COLUMN_HEIGHT_BUTTON);

        // Label to display current quantity
        Label quantityLabel = new Label(String.valueOf(itemQty));
        quantityLabel.setStyle(StyleConstants.ITEM_NAME_STYLE_2);
        GridPane.setHalignment(quantityLabel, HPos.CENTER);

        // Action handlers for quantity adjustment
        incrementButton.setOnAction(e -> incrementQuantity(itemPrice, priceLabel, quantityLabel));
        decrementButton.setOnAction(e -> decrementQuantity(itemPrice, priceLabel, quantityLabel));

        // GridPane layout to organize item controls
        GridPane itemGrid = new GridPane();
        itemGrid.setHgap(StyleConstants.GRID_HGAP);
        itemGrid.setVgap(StyleConstants.GRID_VGAP);

        // Define and set up column widths for consistent alignment
        ColumnConstraints column1 = new ColumnConstraints(StyleConstants.GRID_COLUMN_WIDTH_BUTTON_2); // Decrement button
        ColumnConstraints column2 = new ColumnConstraints(StyleConstants.GRID_COLUMN_WIDTH_BUTTON_2); // Quantity label
        ColumnConstraints column3 = new ColumnConstraints(StyleConstants.GRID_COLUMN_WIDTH_BUTTON_2); // Increment button
        ColumnConstraints column4 = new ColumnConstraints(StyleConstants.GRID_COLUMN_WIDTH_NAME);   // Name label
        ColumnConstraints column5 = new ColumnConstraints(StyleConstants.GRID_COLUMN_WIDTH_PRICE);  // Price label
        column4.setHgrow(Priority.ALWAYS);
        itemGrid.getColumnConstraints().addAll(column1, column2, column3, column4, column5);

        // Add elements to the grid (column, row)
        itemGrid.add(decrementButton, 0, 0);  // Column 0: Decrement button
        itemGrid.add(quantityLabel, 1, 0);    // Column 1: Quantity label
        itemGrid.add(incrementButton, 2, 0);  // Column 2: Increment button
        itemGrid.add(nameLabel, 3, 0);        // Column 3: Name label
        itemGrid.add(priceLabel, 4, 0);       // Column 4: Price label
        itemGrid.add(unitPriceLabel, 5, 0);   // Column 5: Hidden unit price label
        itemGrid.add(idLabel, 6, 0);          // Column 6: Hidden item ID label

        // Remove link with styling, adds removal action
        Hyperlink removeLink = new Hyperlink("Remove");
        removeLink.setStyle(StyleConstants.REMOVE_LINK_STYLE);
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
     * Increases the item quantity and updates the display price.
     * @param itemPrice The price of the item.
     * @param priceLabel The label showing the updated price.
     * @param quantityLabel The label showing the updated quantity.
     */
    private void incrementQuantity(String itemPrice, Label priceLabel, Label quantityLabel) {
        int currentQuantity = Integer.parseInt(quantityLabel.getText());
        currentQuantity++;
        quantityLabel.setText(String.valueOf(currentQuantity));
        updateItemPrice(itemPrice, currentQuantity, priceLabel);
        updateTotalPrice();
    }


    /**
     * Decreases the item quantity (minimum 1) and updates the display price.
     * @param itemPrice The price of the item.
     * @param priceLabel The label showing the updated price.
     * @param quantityLabel The label showing the updated quantity.
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
     * Updates the total price displayed based on all items in the order.
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
            gst += (unitPrice * 0.10) * quantity;
        }

        totalPrice = subtotal + gst - discount;
        lblSubtotal.setText(String.format("$%.2f", subtotal));
        lblGST.setText(String.format("$%.2f", gst));
        lblDiscount.setText(String.format("$%.2f", discount));
        lblTotalIncGST.setText(String.format("$%.2f", totalPrice));
    }


    /**
     * Updates the display price of a single item based on its quantity.
     * @param itemPrice The unit price of the item.
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
     * Handles the checkout button click to gather order details
     * and navigate to the checkout page.
     */
    @FXML
    private void handleCheckoutButton() {
        int cartLength = orderSection.getChildren().size();
        CartItem[] items = new CartItem[cartLength];

        // Loop through items in the order section and populate CartItem array
        for (int i = 0; i < cartLength; i++) {
            GridPane itemBox = (GridPane) orderSection.getChildren().get(i);

            // Retrieve item details from GridPane cells
            Label nameLabel = (Label) itemBox.getChildren().get(3);
            Label priceLabel = (Label) itemBox.getChildren().get(4);
            Label quantityLabel = (Label) itemBox.getChildren().get(1);
            Label unitPriceLabel = (Label) itemBox.getChildren().get(5);
            Label idLabel = (Label) itemBox.getChildren().get(6);

            String name = nameLabel.getText();
            int id = parseItemId(idLabel.getText());
            double unitPrice = parseItemPrice(unitPriceLabel.getText());
            int quantity = parseItemQuantity(quantityLabel.getText());

            // Round GST and total price to two decimal places
            double gst = Math.round((unitPrice * 0.10) * 100.0) / 100.0;
            double totalInc = Math.round(((unitPrice * quantity) + (gst * quantity)) * 100.0) / 100.0;

            items[i] = new CartItem(id, name, unitPrice, quantity, gst, totalInc);

        }


        // Save order details in the database and get the generated order ID
        int savedOrderId = saveOrderToDatabase(items);

        // Load and navigate to the Checkout page, passing the order ID
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(StyleConstants.CHECKOUT_PAGE_FXML));
            Parent checkoutPage = loader.load();

            // Pass the order ID to the CheckoutController
            CheckoutController checkoutController = loader.getController();
            checkoutController.setOrderID(savedOrderId);

            Stage stage = (Stage) this.checkoutButton.getScene().getWindow();
            setupStage(stage, checkoutPage, StyleConstants.CHECKOUT_PAGE_TITLE);
        } catch (IOException e) {
            System.err.println("Error loading Checkout page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Saves the order and items to the database.
     * @param cartItems Array of CartItem objects containing item details.
     * @return The ID of the saved order.
     */
    private int saveOrderToDatabase(CartItem[] cartItems) {
        // Create an Order object with current timestamp and customer details
        Order order = new Order(new Timestamp(System.currentTimeMillis()), "Customer Name", "Customer Contact");

        // Populate OrderItems based on cart items
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem(cartItem.getId(), cartItem.getQuantity(), cartItem.getUnitPrice());
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);

        int orderID = 0;
        // Save the order using OrderDAO
        Connection connection = SqliteConnection.getInstance(); // Get a connection to the database
        try {
            OrderDAO orderDAO = new OrderDAO(connection); // Use a method to get the connection
            orderID = orderDAO.addOrder(order); // Save the order and get the generated order ID
            System.out.println("Order saved to the database with ID: " + orderID);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to save the order to the database.");
        } finally {
            SqliteConnection.closeConnection(); // Close the connection
        }
        return orderID;
    }


    /**
     * Parses a price string to a double.
     * @param priceStr The price string to parse.
     * @return The parsed price as a double.
     */
    private double parseItemPrice(String priceStr) {
        return Double.parseDouble(priceStr.replace("$", ""));
    }

    /**
     * Parses an ID string to an integer.
     * @param id The ID string to parse.
     * @return The parsed ID as an integer.
     */
    private int parseItemId(String id) {
        return Integer.parseInt(id);
    }


    /**
     * Parses a quantity string to an integer.
     * @param qtyStr The quantity string to parse.
     * @return The parsed quantity as an integer.
     */
    private int parseItemQuantity(String qtyStr) {
        return Integer.parseInt(qtyStr);
    }

    /**
     * Sets up the stage with specified scene and title for navigation.
     * @param stage The stage to set up.
     * @param page The new page to load.
     * @param title The title of the new page.
     */
    private void setupStage(Stage stage, Parent page, String title) {
        stage.setTitle(title);
        Scene scene = new Scene(page);
        stage.setScene(scene);
        stage.setMinWidth(StageConstants.DEFAULT_MIN_WIDTH);
        stage.setMinHeight(StageConstants.DEFAULT_MIN_HEIGHT);
        stage.show();
    }

}
