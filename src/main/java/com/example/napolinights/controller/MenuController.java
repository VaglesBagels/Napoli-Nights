package com.example.napolinights.controller;

import com.example.napolinights.model.Category;
import com.example.napolinights.model.MenuItem;
import com.example.napolinights.model.MenuItemDAO;
import com.example.napolinights.model.SqliteConnection;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MenuController {

    @FXML
    private VBox contentAnchorPane; // VBox to hold all categories and items

    public void initialize() {
        // Load the categories and their items
        loadMenuItems();
        /*addCategory("Starters & Salads", Arrays.asList(
                new MenuItem(Category.ENTREE.toString(), "Freshly cooked with mozzarella", 18.00, "path_to_image1"),
                new MenuItem("Fried Green Olives", "Lightly crumbed and served with truffle mayo", 24.00, "path_to_image2")
        ));

        addCategory("WOODFIRED PIZZA", Arrays.asList(
                new MenuItem("Margherita", "Traditional pizza with fresh tomato and mozzarella", 20.00, "path_to_image3"),
                new MenuItem("Pepperoni", "Topped with spicy pepperoni and mozzarella", 22.00, "path_to_image4")
        ));*/
    }

    private void loadMenuItems() {
        Connection connection = SqliteConnection.getInstance();
        MenuItemDAO menuItemDAO = new MenuItemDAO(connection);
        List<MenuItem> menuItems = menuItemDAO.fetchAllMenuItems();
        Map<Category, List<MenuItem>> groupedMenuItems = menuItems.stream()
                .collect(Collectors.groupingBy(MenuItem::getCategory));
        displayMenuItems(groupedMenuItems);
    }

    private void displayMenuItems(Map<Category, List<MenuItem>> groupedMenuItems) {
        groupedMenuItems.forEach((category, items) -> {
            // Create a Label for each category
            Label categoryLabel = new Label(category.name());
            categoryLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

            // Add the category label to the content anchor pane
            contentAnchorPane.getChildren().add(categoryLabel);

            // Create a TilePane for each category's items
            TilePane tilePane = new TilePane();
            tilePane.setHgap(20);
            tilePane.setVgap(20);
            tilePane.setAlignment(Pos.CENTER);
            tilePane.setPrefColumns(4); // Customize column number

            for (MenuItem item : items) {
                // Create a VBox for each menu item
                VBox itemBox = new VBox(5);
                itemBox.setStyle("-fx-border-color: lightgray; -fx-padding: 10;");

                // Add the item name, description, and price
                String itemName = item.getName();
                Label nameLabel = new Label(itemName);
                nameLabel.setStyle("-fx-font-size: 18px;");
                String itemDescription = item.getDescription();
                Label descLabel = new Label(itemDescription);
                descLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");
                String itemPrice = String.format("$%.2f", item.getPrice());
                Label priceLabel = new Label(itemPrice);
                priceLabel.setStyle("-fx-font-size: 16px;");
                // System.out.println("Item Name: " + itemName + " Item Description: " + itemDescription + " Item Price: " + itemPrice);
                // Add image
                ImageView imageView = new ImageView();
                imageView.setFitHeight(200);
                imageView.setFitWidth(200);
                imageView.setPreserveRatio(true);

                /*String imageUrl = item.getImageURL();

                System.out.println("Image Url " + imageUrl);
                if (imageUrl == null || imageUrl.trim().isEmpty()) {
                    itemImageUrl = getClass().getResource("/com/example/napolinights/images/menu_images/no_image_found.jpg").toExternalForm();
                } else {
                    itemImageUrl = getClass().getResource("/com/example/napolinights/images/menu_images/" + item.getImageURL()).toExternalForm();
                }
                */
                URL imageUrl = getClass().getResource("/com/example/napolinights/images/menu_images/no_image_found.png");
                if (imageUrl != null) {
                    System.out.println("Image URL: " + imageUrl);
                } else {
                    System.out.println("Resource URL is null");
                }

                Image img = new Image(imageUrl.toExternalForm());
                imageView.setImage(img);
                if (img.isError()) {
                    System.out.println("Error loading image: " + img.getException());
                } else {
                    System.out.println("Image successfully loaded: " + img.getWidth() + "x" + img.getHeight());
                }

                InputStream imageStream = getClass().getResourceAsStream("/com/example/napolinights/images/menu_images/no_image_found.png");
                if (imageStream != null) {
                 imageView.setImage(new Image(imageStream));
                } else {
                    System.out.println("Resource URL is null");
                }

                // Add all elements to the VBox
                itemBox.getChildren().addAll(nameLabel, descLabel, priceLabel, imageView);
                //itemBox.getChildren().addAll(nameLabel, descLabel, priceLabel);

                // Add the VBox to the TilePane
                tilePane.getChildren().add(itemBox);
            }

            // Add the TilePane to the content anchor pane
            contentAnchorPane.getChildren().add(tilePane);
        });
    }

    /*private void addCategory(String categoryName, List<MenuItem> items) {
        // Create a VBox for the category
        VBox categoryBox = new VBox(10);
        categoryBox.setPadding(new Insets(10));

        // Add category title
        Label categoryLabel = new Label(categoryName);
        categoryLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        categoryBox.getChildren().add(categoryLabel);

        // Add items under this category
        TilePane itemsPane = new TilePane(20, 20);
        itemsPane.setPrefColumns(2); // Adjust as per your layout
        for (MenuItem item : items) {
            VBox itemBox = new VBox(5);
            itemBox.setStyle("-fx-border-color: lightgray; -fx-padding: 10;");

            Label nameLabel = new Label(item.getName());
            nameLabel.setStyle("-fx-font-size: 18px;");

            Label descriptionLabel = new Label(item.getDescription());
            descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");

            Label priceLabel = new Label(String.format("$%.2f", item.getPrice()));
            priceLabel.setStyle("-fx-font-size: 16px;");

            ImageView imageView = new ImageView(new Image(item.getImageURL()));
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);

            itemBox.getChildren().addAll(nameLabel, descriptionLabel, priceLabel, imageView);
            itemsPane.getChildren().add(itemBox);
        }

        categoryBox.getChildren().add(itemsPane);
        categorySection.getChildren().add(categoryBox);
    }*/

}


