package com.example.napolinights.controller;

import com.example.napolinights.model.OrderDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.io.IOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.napolinights.model.Order;
import com.example.napolinights.model.OrderItem;
import com.example.napolinights.model.MenuItem;
import com.example.napolinights.model.MenuItemDAO;


public class IncomingOrdersController {
    @FXML
    private ListView<String> confirmedCartListView;

    @FXML
    private Button staffLandingPageButton;

    public OrderDAO orderDAO;

    private MenuItemDAO menuItemDAO;

    // Map to cache menu items for quick lookup
    private final Map<Integer, MenuItem> menuItemCache;

    public IncomingOrdersController() {
        menuItemCache = new HashMap<>();

        try {
            Connection connection = DriverManager.getConnection("jdbc:yourdatabaseurl", "username", "password");
            this.orderDAO = new OrderDAO(connection);
            this.menuItemDAO = new MenuItemDAO(connection);

            // Pre-load all menu items into cache
            List<MenuItem> menuItems = menuItemDAO.fetchAllMenuItems();
            for (MenuItem menuItem : menuItems) {
                menuItemCache.put(menuItem.getMenuItemID(), menuItem);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public IncomingOrdersController(OrderDAO orderDAO, Map<Integer, MenuItem> menuItemCache) {
        this.orderDAO = orderDAO;
        this.menuItemCache = menuItemCache;
    }

    @FXML
    private void initialize() {
        loadConfirmedOrders();
    }

    private String loadConfirmedOrders() {
        List<Order> confirmedOrders;

        try {
            confirmedOrders = orderDAO.fetchConfirmedOrders();

            List<String> displayData = new ArrayList<>();
            for (Order order : confirmedOrders) {
                for (OrderItem item : order.getOrderItems()) {
                    String orderDetails = String.format(
                            "Time: %s | Dish: %s | Qty: %d | Notes: %s",
                            order.getOrderDate().toString(),
                            getMenuById(item.getMenuID()),  // You need this method implemented
                            item.getQuantity(),
                            fetchOrderNotes(order.getOrderID())   // You need this method implemented
                    );
                    displayData.add(orderDetails);
                }
            }
            confirmedCartListView.getItems().setAll(displayData);

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle error, possibly show an alert
        }
        return "";
    }

        private String getMenuById ( int menuID){
            MenuItem menuItem = menuItemCache.get(menuID);
            return menuItem != null ? menuItem.getName() : "Unknown Dish";
        }

        private String fetchOrderNotes ( int orderID){
            String sql = "SELECT notes FROM orders WHERE order_id = ?";
            try (PreparedStatement pstmt = orderDAO.connection.prepareStatement(sql)) {
                pstmt.setInt(1, orderID);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("notes");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle error, possibly rethrow or return a default message
            }
            return "No special notes.";
        }

        @FXML
        private void handleStaffHomePage () {
            System.out.println("Staff Main Page button clicked");
            openStaffHomePage();
        }

        private void openStaffHomePage () {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StaffLandingPage.fxml"));
                Parent staffLandingPage = loader.load();
                // Add null check
                Stage stage = (Stage) staffLandingPageButton.getScene().getWindow();
                stage.setTitle("Staff Home Page");
                Scene scene = new Scene(staffLandingPage);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



