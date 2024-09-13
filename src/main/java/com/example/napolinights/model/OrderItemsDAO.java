package com.example.napolinights.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemsDAO implements IOrderItemsDAO {
    private final Connection connection;

    public OrderItemsDAO() {
        connection = SqliteConnection.getInstance();
    }

    public void createOrderItemsTable() {
        try {
            Statement createOrderItemsTable = connection.createStatement();
            createOrderItemsTable.execute(
                    "CREATE TABLE IF NOT EXISTS OrderItems ("
                            + "orderItemID INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "orderID INTEGER NOT NULL, "
                            + "menuID INTEGER NOT NULL, "
                            + "quantity INTEGER NOT NULL, "
                            + "itemPrice DOUBLE NOT NULL, "
                            + "FOREIGN KEY (orderID) REFERENCES Orders(orderID), "
                            + "FOREIGN KEY (menuID) REFERENCES menu(menuID)"
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void insert(OrderItems orderItem) {
        try {
            String sql = "INSERT INTO OrderItems (orderID, menuID, quantity, itemPrice) VALUES (?, ?, ?, ?)";
            PreparedStatement insertOrderItem = connection.prepareStatement(sql);

            insertOrderItem.setInt(1, orderItem.getOrderID());
            insertOrderItem.setInt(2, orderItem.getMenuID());
            insertOrderItem.setInt(3, orderItem.getQuantity());
            insertOrderItem.setDouble(4, orderItem.getItemPrice());
            insertOrderItem.execute();

        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void update(OrderItems orderItem) {
        try {
            String sql = "UPDATE OrderItems SET orderID = ?, menuID = ?, quantity = ?, itemPrice = ? WHERE orderItemID = ?";
            PreparedStatement updateOrderItem = connection.prepareStatement(sql);

            updateOrderItem.setInt(1, orderItem.getOrderID());
            updateOrderItem.setInt(2, orderItem.getMenuID());
            updateOrderItem.setInt(3, orderItem.getQuantity());
            updateOrderItem.setDouble(4, orderItem.getItemPrice());
            updateOrderItem.setInt(5, orderItem.getOrderItemID());
            updateOrderItem.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void delete(int id) {
        try {
            String sql = "DELETE FROM OrderItems WHERE orderItemID = ?";
            PreparedStatement deleteOrderItem = connection.prepareStatement(sql);

            deleteOrderItem.setInt(1, id);

            // Execute the deletion
            deleteOrderItem.execute();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public List<OrderItems> getAll() {
        List<OrderItems> orderItemsList = new ArrayList<>();
        try {
            Statement getAll = connection.createStatement();
            String sql = "SELECT * FROM OrderItems";
            ResultSet rs = getAll.executeQuery(sql);

            while (rs.next()) {
                int orderItemID = rs.getInt("orderItemID");
                int orderID = rs.getInt("orderID");
                int menuID = rs.getInt("menuID");
                int quantity = rs.getInt("quantity");
                double itemPrice = rs.getDouble("itemPrice");

                orderItemsList.add(
                        new OrderItems(
                                orderItemID,
                                orderID,
                                menuID,
                                quantity,
                                itemPrice
                        )
                );
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return orderItemsList;
    }

    public OrderItems getById(int id) {
        try {
            String sql = "SELECT * FROM OrderItems WHERE orderItemID = ?";
            PreparedStatement getOrderItem = connection.prepareStatement(sql);
            getOrderItem.setInt(1, id);
            ResultSet rs = getOrderItem.executeQuery();

            if (rs.next()) {
                int orderItemID = rs.getInt("orderItemID");
                int orderID = rs.getInt("orderID");
                int menuID = rs.getInt("menuID");
                int quantity = rs.getInt("quantity");
                double itemPrice = rs.getDouble("itemPrice");

                return new OrderItems(orderItemID, orderID, menuID, quantity, itemPrice);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
}