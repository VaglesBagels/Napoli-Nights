package com.example.napolinights.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for handling database operations related to Orders.
 */
public class OrderDAO implements IOrderDAO{
    public final Connection connection;

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
                            "    customer_name TEXT NOT NULL," +
                            "    customer_contact TEXT NOT NULL," +
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
                            "    order_id INTEGER NOT NULL," +
                            "    menu_id INTEGER NOT NULL," +
                            "    quantity INTEGER NOT NULL," +
                            "    item_price DOUBLE NOT NULL," +
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
            List<OrderItem> orderItemList = order.getOrderItems();
            for (OrderItem item : orderItemList) {
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

    /**
     * Updates an existing order in the database.
     * The method updates the customer information, payment status, and payment timestamp based on the provided order.
     *
     * @param order The order object containing updated order details.
     * @throws SQLException If an SQL error occurs during the update operation.
     */
    @Override
    public void updateOrder(Order order) throws SQLException {
        String updateOrderSQL = "UPDATE orders SET customer_name = ?, customer_contact = ?, order_paid = ?, paid_timestamp = ? WHERE order_id = ?";
        try (PreparedStatement orderStmt = connection.prepareStatement(updateOrderSQL)) {
            orderStmt.setString(1, order.getCustomerName());
            orderStmt.setString(2, order.getCustomerContact());
            orderStmt.setBoolean(3, order.isPaid());
            orderStmt.setTimestamp(4, order.getPaidDate());
            orderStmt.setInt(5, order.getOrderID());

            int affectedRows = orderStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating order failed, no rows affected.");
            }
        }
    }

    /**
     * Fetches all orders from the database.
     * This method retrieves all orders, including their timestamps, customer information, and payment status.
     *
     * @return A list of all orders in the database.
     * @throws SQLException If an SQL error occurs during the fetch operation.
     */
    @Override
    public List<Order> fetchAllOrders() throws SQLException {
        String fetchAllOrdersSQL = "SELECT * FROM orders";
        List<Order> orders = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(fetchAllOrdersSQL)) {

            while (rs.next()) {
                Order order = new Order(
                        rs.getTimestamp("created_timestamp"),
                        rs.getString("customer_name"),
                        rs.getString("customer_contact")
                );
                order.setOrderID(rs.getInt("order_id"));
                order.setPaidDate(rs.getTimestamp("paid_timestamp"));
                orders.add(order);
            }
        }
        return orders;
    }

    /**
     * Fetches all orders from the database that were created on a specific date.
     * This method retrieves orders where the `created_timestamp` matches the specified date.
     *
     * @param date The date (as a {@link Timestamp}) to filter orders by. Only orders created on this date will be returned.
     * @return A list of orders created on the specified date.
     * @throws SQLException If an SQL error occurs during the fetch operation.
     */
    public List<Order> fetchAllOrdersByDate(Timestamp date) throws SQLException {
        if (date == null) {
            throw new SQLException("The SQL statement cannot be prepared with a null date");
        }

        String fetchOrdersByDateSQL = "SELECT * FROM orders WHERE DATE(created_timestamp) = DATE(?)";
        List<Order> orders = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(fetchOrdersByDateSQL)) {
            stmt.setTimestamp(1, date);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order(
                            rs.getTimestamp("created_timestamp"),
                            rs.getString("customer_name"),
                            rs.getString("customer_contact")
                    );
                    order.setOrderID(rs.getInt("order_id"));
                    order.setPaidDate(rs.getTimestamp("paid_timestamp"));
                    orders.add(order);
                }
            }
        }

        return orders;
    }

    public List<Order> fetchConfirmedOrders() throws SQLException {
        List<Order> confirmedOrders = new ArrayList<>();
        String query = "SELECT * FROM Orders WHERE isPaid = 1"; // Assuming 'isPaid' indicates order confirmation

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                // Retrieve order details from the result set
                Order order = new Order(
                        rs.getInt("orderID"),
                        rs.getTimestamp("orderDate"),
                        rs.getString("customerName"),
                        rs.getString("customerContact"),
                        fetchOrderItems(rs.getInt("orderID")),
                        rs.getTimestamp("paidDate")
                );
                confirmedOrders.add(order);
            }
        }
        return confirmedOrders;
    }

    /**
     * Fetches a specific order by its ID.
     * The method retrieves the order details from the database based on the provided order ID.
     *
     * @param id The ID of the order to retrieve.
     * @return The order object if found, or null if no order exists with the given ID.
     * @throws SQLException If an SQL error occurs during the fetch operation.
     */
    private List<OrderItem> fetchOrderItems(int orderId) throws SQLException {
        List<OrderItem> orderItems = new ArrayList<>();
        String query = "SELECT * FROM OrderItems WHERE orderID = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    orderItems.add(new OrderItem(
                            rs.getInt("orderItemID"),
                            rs.getInt("orderID"),
                            rs.getInt("menuID"),
                            rs.getInt("quantity"),
                            rs.getDouble("itemPrice")
                    ));
                }
            }
        }
        return orderItems;
    }

    @Override
    public Order getOrderById(int id) throws SQLException {
        String getOrderByIdSQL = "SELECT * FROM orders WHERE order_id = ?";
        Order order = null;

        try (PreparedStatement stmt = connection.prepareStatement(getOrderByIdSQL)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    order = new Order(
                            rs.getTimestamp("created_timestamp"),
                            rs.getString("customer_name"),
                            rs.getString("customer_contact")
                    );
                    order.setOrderID(rs.getInt("order_id"));
                    order.setPaidDate(rs.getTimestamp("paid_timestamp"));
                }
            }
        }
        return order;
    }

    @Override
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to close connection: " + e.getMessage());
        }
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

        for (OrderItem item : order.getOrderItems()) {
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

    public String fetchOrderNotes(int orderID) {
        String sql = "SELECT notes FROM orders WHERE order_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
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

}
