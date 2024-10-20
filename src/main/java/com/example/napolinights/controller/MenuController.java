package com.example.napolinights.controller;

import com.example.napolinights.model.Category;
import com.example.napolinights.model.MenuItem;
import com.example.napolinights.model.MenuItemDAO;
import com.example.napolinights.model.SqliteConnection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
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
    @FXML
    private ScrollPane menuScrollPane;

    // VBox to dynamically add categories and their menu items inside the ScrollPane
    @FXML
    private VBox contentAnchorPane;

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
            stage.setMinWidth(800);
            stage.setMinHeight(600);
        });
    }

    /**
     * Loads the menu items from the database and groups them by category.
     */
    private void loadMenuItems() {
        // Get a connection to the database
        Connection connection = SqliteConnection.getInstance();
        MenuItemDAO menuItemDAO = new MenuItemDAO(connection);

        // Fetch all menu items and group them by their category
        List<MenuItem> menuItems = menuItemDAO.fetchAllMenuItems();
        Map<Category, List<MenuItem>> groupedMenuItems = menuItems.stream()
                .collect(Collectors.groupingBy(MenuItem::getCategory));

        // Display the grouped menu items on the UI
        displayMenuItems(groupedMenuItems);
    }

    /**
     * Displays the menu items grouped by their categories. Each category is displayed
     * with a label, and its items are displayed in a grid.
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

            // Create a GridPane for the items in each category
            GridPane gridPane = new GridPane();
            gridPane.setHgap(40);  // Set the horizontal gap between grid columns
            gridPane.setVgap(20);  // Set the vertical gap between grid rows
            gridPane.setPadding(new Insets(10, 0, 20, 0));  // Set padding around the grid

            // Add constraints for the columns to ensure uniform widths
            for (int i = 0; i < 3; i++) {
                gridPane.getColumnConstraints().add(new ColumnConstraints(500));  // Fixed width for each column
            }

            // Display menu items in the GridPane
            int row = 0; // Track the current row
            int col = 0; // Track the current column
            int maxColumns = 3;  // Number of items per row

            for (MenuItem item : items) {
                // Create a VBox for each menu item (contains name, description, and price)
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

                // Add the VBox to the grid at the current row and column
                gridPane.add(itemBox, col, row);

                // Increment the column, if maxColumns is reached, move to the next row
                col++;
                if (col == maxColumns) {
                    col = 0;
                    row++;
                }
            }

            // Add the GridPane to the VBox (contentAnchorPane)
            contentAnchorPane.getChildren().add(gridPane);
        });
    }
}