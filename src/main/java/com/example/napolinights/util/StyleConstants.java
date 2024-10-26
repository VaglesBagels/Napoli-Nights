package com.example.napolinights.util;

import javafx.stage.StageStyle;

public class StyleConstants {
    public static final String ITEM_NAME_STYLE = "-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;";
    public static final String ITEM_PRICE_STYLE = "-fx-font-size: 16px; -fx-text-fill: white;";
    public static final String ITEM_NAME_STYLE_2 = "-fx-text-fill: white; -fx-font-size: 14px;";
    public static final String ITEM_PRICE_STYLE_2 = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;";

    // Style for the item box that contains each menu item's information
    public static final String ITEM_BOX_STYLE = "-fx-border-color: lightgray; -fx-padding: 10;";

    // Style for item description text in each menu item box
    public static final String ITEM_DESC_STYLE = "-fx-font-size: 14px; -fx-text-fill: white;";

    // Style for active category tab
    public static final String ACTIVE_TAB_STYLE = "-fx-text-fill: white; -fx-font-weight: bold;";

    // Style for inactive category tab
    public static final String INACTIVE_TAB_STYLE = "-fx-text-fill: #00BFFF; -fx-font-weight: normal;";

    // Dialog styling constants
    public static final String DIALOG_TITLE = "Item Details";
    public static final String DIALOG_STYLE_UNDECORATED = StageStyle.UNDECORATED.name();


    // Style constants for labels and buttons in order summary
    public static final String BUTTON_STYLE = "-fx-font-size: 14px; -fx-background-color: white; -fx-font-weight: bold; -fx-text-fill: black;";
    public static final String REMOVE_LINK_STYLE = "-fx-font-size: 14px; -fx-text-fill: #84AE87; -fx-font-weight: bold;";

    // Column widths in GridPane layout
    public static final int GRID_COLUMN_WIDTH_NAME = 200;
    public static final int GRID_COLUMN_WIDTH_PRICE = 60;
    public static final int GRID_COLUMN_WIDTH_BUTTON = 40;
    public static final int GRID_COLUMN_WIDTH_BUTTON_2 = 50;
    public static final int GRID_COLUMN_HEIGHT_BUTTON = 30;
    public static final int GRID_HGAP = 10;
    public static final int GRID_VGAP = 5;


    // FXML file paths
    public static final String ITEM_DETAIL_DIALOG_FXML = "/view/ItemDetailDialog.fxml";
    public static final String LANDING_PAGE_FXML = "/view/LandingPage.fxml";
    public static final String CHECKOUT_PAGE_FXML = "/view/Checkout.fxml";
    public static final String LANDING_PAGE_TITLE = "Landing Page";
    public static final String CHECKOUT_PAGE_TITLE = "Checkout";


}
