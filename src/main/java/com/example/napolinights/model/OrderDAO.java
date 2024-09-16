package com.example.napolinights.model;

import java.sql.*;
import java.util.List;

/**
 * DAO class for handling database operations related to Orders.
 */
public class OrderDAO implements IOrderDAO{
    private final Connection connection;

    /**
     * Constructs an OrderDAO with the specified database connection.
     *
     * @param connection The database connection to be used by this DAO.
     */
    public OrderDAO(Connection connection) {
        this.connection = connection;
    }

    public void createOrdersTable() {
        try {
            Statement createOrdersTable = connection.createStatement();
            createOrdersTable.execute(
                    "CREATE TABLE IF NOT EXISTS orders (" +
                            "    order_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "    created_timestamp DATETIME NOT NULL," +
                            "    order_paid BOOLEAN NOT NULL," +
                            "    paid_timestamp DATETIME" +
                            ")"
            );

            System.out.println("Orders table created");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void createOrderItemsTable() {
        try {
            Statement createOrderItemsTable = connection.createStatement();
            createOrderItemsTable.execute(
                    "CREATE TABLE IF NOT EXISTS order_items (" +
                            "    order_item_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "    order_id VARCHAR(20) NOT NULL," +
                            "    menu_id VARCHAR(10) NOT NULL," +
                            "    special_instructions TEXT," +
                            "    FOREIGN KEY (order_id) REFERENCES orders(order_id)," +
                            "    FOREIGN KEY (menu_id) REFERENCES menu_items(menu_id)" +
                            ")"
            );

            System.out.println("OrderItems table created");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


    /**
     * Adds an order and its associated order items to the database.
     *
     * @param order The order to be added.
     */
    public int addOrder(Order order) throws SQLException {
        // Validate input
        validateOrder(order);

        // Prepare SQL statements
        String insertOrderSQL = "INSERT INTO Orders (orderDate, customerName, customerContact) VALUES (?, ?, ?)";
        String insertOrderItemSQL = "INSERT INTO OrderItems (orderID, menuID, quantity, itemPrice) VALUES (?, ?, ?, ?)";

        // Use try-with-resources for transaction management
        try (PreparedStatement orderStmt = connection.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement orderItemStmt = connection.prepareStatement(insertOrderItemSQL)) {

            // Start transaction
            connection.setAutoCommit(false);

            // Insert Order into Orders table
            orderStmt.setTimestamp(1, order.getOrderDate());
            orderStmt.setString(2, order.getCustomerName());
            orderStmt.setString(3, order.getCustomerContact());
            int affectedRows = orderStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            // Retrieve the generated orderID
            try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setOrderID(generatedKeys.getInt(1)); // Assume orderID is an int
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }

            // Insert OrderItems into OrderItems table
            List<OrderItems> orderItemsList = order.getOrderItems();
            for (OrderItems item : orderItemsList) {
                orderItemStmt.setInt(1, order.getOrderID());
                orderItemStmt.setInt(2, item.getMenuID());
                orderItemStmt.setInt(3, item.getQuantity());
                orderItemStmt.setDouble(4, item.getItemPrice());
                orderItemStmt.addBatch();
            }

            // Execute batch insert for order items
            orderItemStmt.executeBatch();

            // Commit transaction
            connection.commit();
            return order.getOrderID();

        } catch (SQLException e) {
            // Rollback transaction in case of error
            connection.rollback();
            throw e;
        } catch (IllegalArgumentException e) {
            // Convert IllegalArgumentException to SQLException for consistency with expected exceptions
            throw new SQLException("Invalid order data", e);
        } finally {
            // Restore auto-commit mode
            connection.setAutoCommit(true);
        }

    }

    @Override
    public void updateOrder(Order order) {

    }

    @Override
    public List<Order> getAllOrders() {
        return List.of();
    }

    @Override
    public Order getOrderById(int id) {
        return null;
    }

    @Override
    public void closeConnection() {

    }

    /**
     * Validates the provided order and its items.
     *
     * @param order The order to be validated.
     * @throws IllegalArgumentException if the order or its items are invalid.
     */
    private void validateOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }

        if (order.getCustomerName() == null || order.getCustomerName().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty.");
        }

        if (order.getCustomerContact() == null || order.getCustomerContact().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer contact cannot be null or empty.");
        }

        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one order item.");
        }

        for (OrderItems item : order.getOrderItems()) {
            if (item == null) {
                throw new IllegalArgumentException("Order item cannot be null.");
            }
            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Order item quantity must be greater than 0.");
            }
            if (item.getItemPrice() <= 0) {
                throw new IllegalArgumentException("Order item price must be greater than 0.");
            }
        }
    }
}
