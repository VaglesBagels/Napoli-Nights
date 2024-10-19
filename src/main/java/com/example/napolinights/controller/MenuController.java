package com.example.napolinights.controller;

import com.example.napolinights.model.Category;
import com.example.napolinights.model.MenuItem;
import com.example.napolinights.model.MenuItemDAO;
import com.example.napolinights.model.SqliteConnection;
import com.example.napolinights.util.StageConstants;
import com.example.napolinights.util.StageConstants;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller for the Menu Page. Handles the display of menu items
 * categorized by type, and ensures the user interface adapts to the
 * specified layout with scrolling enabled.
 */
public class MenuController {

    // ScrollPane to hold the VBox for scrolling through the menu items
    @FXML private ScrollPane menuScrollPane;

    // VBox to dynamically add categories and their menu items inside the ScrollPane
    @FXML private VBox contentAnchorPane;

    /**
     * Initializes the MenuController. Sets up the ScrollPane and loads the menu items.
     */
    public void initialize() {
        // Set padding for the contentAnchorPane to provide spacing
        contentAnchorPane.setPadding(new Insets(10, 50, 10, 50)); // Top, right, bottom, left padding

        // Ensure the ScrollPane fits the width of its content
        menuScrollPane.setFitToWidth(true);

        // Load the menu items from the database
        loadMenuItems();

        // Ensure that the stage size is adjusted after the scene is loaded
        Platform.runLater(() -> {
            Stage stage = (Stage) menuScrollPane.getScene().getWindow();
            StageConstants.setStageSize(stage);  // Use utility method to set stage size
        });
    }

    /**
     * Loads the menu items from the database and groups them by category.
     * Uses a try-with-resources block to automatically close the database connection after use.
     */
    private void loadMenuItems() {
        try (Connection connection = SqliteConnection.getInstance()) {
            // Create a MenuItemDAO to access the database
            MenuItemDAO menuItemDAO = new MenuItemDAO(connection);

            // Fetch all menu items and group them by their category
            List<MenuItem> menuItems = menuItemDAO.fetchAllMenuItems();
            Map<Category, List<MenuItem>> groupedMenuItems = menuItems.stream()
                    .collect(Collectors.groupingBy(MenuItem::getCategory));

            // Display the grouped menu items on the UI
            displayMenuItems(groupedMenuItems);
        } catch (SQLException e) {
            // Handle any SQL exceptions that occur during database access
            e.printStackTrace();
        }
    }

    /**
     * Displays the menu items grouped by their categories. Each category is displayed
     * with a label, and its items are displayed in a tile layout.
     *
     * @param groupedMenuItems A map of categories and their respective menu items.
     */
    private void displayMenuItems(Map<Category, List<MenuItem>> groupedMenuItems) {
        // Define the desired order of the categories
        List<Category> preferredCategoryOrder = List.of(
                Category.ENTREE,
                Category.PIZZA,
                Category.PASTA,
                Category.DESSERT,
                Category.BEVERAGES
        );

        // Sort the menu items based on the preferred category order
        Map<Category, List<MenuItem>> sortedMenuItems = new LinkedHashMap<>();
        preferredCategoryOrder.forEach(category -> {
            if (groupedMenuItems.containsKey(category)) {
                sortedMenuItems.put(category, groupedMenuItems.get(category));
            }
        });

        // Iterate through the sorted categories and items
        sortedMenuItems.forEach((category, items) -> {
            // Create a Label for the category and style it
            Label categoryLabel = new Label(category.name());
            categoryLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 20 0 10 0; -fx-text-fill: white;");
            contentAnchorPane.getChildren().add(categoryLabel); // Add the category label to the VBox

            // Create a TilePane for the items in each category (automatically handles layout and wrapping)
            TilePane tilePane = new TilePane();
            tilePane.setHgap(40);  // Set the horizontal gap between grid columns
            tilePane.setVgap(20);  // Set the vertical gap between grid rows
            tilePane.setPrefColumns(3);  // Number of items per row

            // Add the menu items to the TilePane
            for (MenuItem item : items) {
                VBox itemBox = createMenuItemBox(item);  // Create a VBox for each menu item
                tilePane.getChildren().add(itemBox);  // Add the itemBox to the TilePane
            }

            // Add the TilePane to the VBox (contentAnchorPane)
            contentAnchorPane.getChildren().add(tilePane);
        });
    }

    /**
     * Creates a VBox containing the details of a menu item.
     *
     * @param item The menu item to display.
     * @return A VBox with the item's name, description, and price.
     */
    private VBox createMenuItemBox(MenuItem item) {
        // Create a VBox to hold the item's information
        VBox itemBox = new VBox(5);  // Set vertical spacing between elements
        itemBox.setAlignment(Pos.TOP_LEFT);

        // Add the item name to the VBox
        Label nameLabel = new Label(item.getName());
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
        itemBox.getChildren().add(nameLabel);

        // Add the item description to the VBox
        Label descLabel = new Label(item.getDescription());
        descLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        itemBox.getChildren().add(descLabel);

        // Add the item price to the VBox
        Label priceLabel = new Label(String.format("$%.2f", item.getPrice()));
        priceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
        itemBox.getChildren().add(priceLabel);

        return itemBox;  // Return the fully constructed VBox
    }
}
