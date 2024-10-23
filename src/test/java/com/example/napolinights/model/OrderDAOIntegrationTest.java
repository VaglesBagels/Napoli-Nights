package com.example.napolinights.model;

import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for OrderDAO, using an in-memory H2 database.
 */
public class OrderDAOIntegrationTest {

    private Connection connection;
    private OrderDAO orderDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        // Set up an in-memory SQLite database
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");  // Use the instance-level variable here
        orderDAO = new OrderDAO(connection);

        // Create the orders and order_items tables
        orderDAO.createOrdersTable();
        orderDAO.createOrderItemsTable();

        // Ensure tables exist before continuing
        assertTrue(doesTableExist("orders"), "Orders table should be created.");
        assertTrue(doesTableExist("order_items"), "OrderItems table should be created.");
    }

    public boolean doesTableExist(String tableName) throws SQLException {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'")) {
            return rs.next(); // Returns true if the table exists
        }
    }


    @Test
    public void testTableCreation() throws SQLException {
        assertTrue(doesTableExist("orders"));
        assertTrue(doesTableExist("order_items"));
    }


    @Test
    public void testAddOrderAndFetchById() throws SQLException {
        // Arrange: Create an order and add at least one order item to it
        Order order = new Order(new Timestamp(System.currentTimeMillis()), "John Doe", "123-456-7890");

        // Add an order item to satisfy the validation
        OrderItem orderItem = new OrderItem(1, 2, 19.99); // Dummy menuID = 1, quantity = 2, price = 19.99
        order.addOrderItem(orderItem); // Add the order item to the order

        // Act: Add the order to the database and fetch it by ID
        int orderId = orderDAO.addOrder(order);
        Order fetchedOrder = orderDAO.getOrderById(orderId);

        // Assert: Verify that the fetched order matches the original one
        assertNotNull(fetchedOrder);
        assertEquals("John Doe", fetchedOrder.getCustomerName());
        assertEquals("123-456-7890", fetchedOrder.getCustomerContact());
    }

    @Test
    public void testAddOrderItem() throws SQLException {
        // Arrange: Create an order and add it
        Order order = new Order(new Timestamp(System.currentTimeMillis()), "Jane Smith", "987-654-3210");

        // Add an order item to this order
        OrderItem orderItem = new OrderItem(1, 3, 10.99);  // menuID = 1, quantity = 3, price = 10.99
        orderItem.setSpecialInstructions("Extra cheese");
        order.addOrderItem(orderItem);
        int orderId = orderDAO.addOrder(order);

        String insertOrderItemSQL = "INSERT INTO order_items (order_id, menu_id, quantity, item_price, special_instructions) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertOrderItemSQL)) {
            stmt.setInt(1, orderItem.getOrderID());
            stmt.setInt(2, orderItem.getMenuID());
            stmt.setInt(3, orderItem.getQuantity());
            stmt.setDouble(4, orderItem.getItemPrice());
            stmt.setString(5, orderItem.getSpecialInstructions());
            stmt.executeUpdate();
        }

        // Act: Query the database to ensure the order item was added
        String querySQL = "SELECT * FROM order_items WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(querySQL)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(1, rs.getInt("menu_id"));
                assertEquals(3, rs.getInt("quantity"));
                assertEquals(10.99, rs.getDouble("item_price"), 0.01);
                assertEquals("Extra cheese", rs.getString("special_instructions"));
            }
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Close the database connection after each test
        connection.close();
    }
}
