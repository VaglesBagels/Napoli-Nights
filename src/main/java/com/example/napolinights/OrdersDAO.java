package com.example.napolinights;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdersDAO {
    private Connection connection;

    public OrdersDAO() {
        connection = DatabaseConnection.getInstance();
    }

    public void createOrdersTable() {
        try {
            Statement createMenuTable = connection.createStatement();
            createMenuTable.execute(
                    "CREATE TABLE IF NOT EXISTS Orders ("
                            + "orderID INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "orderDate TIMESTAMP NOT NULL, "
                            + "customerName TEXT NOT NULL, "
                            + "customerContact TEXT NOT NULL"
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void insert(Orders order) {
        String sql = "INSERT INTO Orders (orderDate, customerName, customerContact) VALUES (?, ?, ?)";
        try (PreparedStatement insertOrder = connection.prepareStatement(sql)) {
            // Set the values for the placeholders
            insertOrder.setTimestamp(1, order.getOrderDate());
            insertOrder.setString(2, order.getCustomerName());
            insertOrder.setString(3, order.getCustomerContact());

            insertOrder.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void update(Orders order) {
        String sql = "UPDATE Orders SET orderDate = ?, customerName = ?, customerContact = ? WHERE orderID = ?";
        try (PreparedStatement updateOrder = connection.prepareStatement(sql)) {
            // Set the values for the placeholders
            updateOrder.setTimestamp(1, order.getOrderDate());
            updateOrder.setString(2, order.getCustomerName());
            updateOrder.setString(3, order.getCustomerContact());
            updateOrder.setInt(4, order.getOrderID());

            updateOrder.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void delete(int id) {
        try {
            String sql = "DELETE FROM Orders WHERE orderID = ?";
            PreparedStatement deleteOrder = connection.prepareStatement(sql);
            deleteOrder.setInt(1, id);
            deleteOrder.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public List<Orders> getAll() {
        List<Orders> ordersList = new ArrayList<>();
        try {
            Statement getAll = connection.createStatement();
            String sql = "SELECT * FROM Orders";
            ResultSet rs = getAll.executeQuery(sql);

            while (rs.next()) {
                ordersList.add(
                        new Orders(
                                rs.getInt("orderID"),
                                rs.getTimestamp("orderDate"),
                                rs.getString("customerName"),
                                rs.getString("customerContact")
                        )
                );
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return ordersList;
    }

    public Orders getById(int id) {
        try {
            String sql = "SELECT * FROM Orders WHERE orderID = ?";
            PreparedStatement getOrder = connection.prepareStatement(sql);
            getOrder.setInt(1, id);
            ResultSet rs = getOrder.executeQuery();

            if (rs.next()) {
                return new Orders(
                        rs.getInt("orderID"),
                        rs.getTimestamp("orderDate"),
                        rs.getString("customerName"),
                        rs.getString("customerContact")
                );
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