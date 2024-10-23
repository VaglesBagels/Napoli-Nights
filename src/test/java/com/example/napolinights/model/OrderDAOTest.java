package com.example.napolinights.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the OrderDAO class.
 * This test suite focuses on testing the database interactions defined in the OrderDAO class.
 */
public class OrderDAOTest {

    private OrderDAO orderDAO;
    private Connection mockConnection;
    private PreparedStatement mockOrderStmt;
    private Statement mockStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockOrderStmt = mock(PreparedStatement.class);
        mockStatement = mock(Statement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(ArgumentMatchers.anyString())).thenReturn(mockOrderStmt);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockOrderStmt.executeQuery()).thenReturn(mockResultSet);

        orderDAO = new OrderDAO(mockConnection);
    }

    @Test
    public void testAddOrderSuccess() throws SQLException {
        // Arrange
        Order order = new Order(new Timestamp(System.currentTimeMillis()), "John Doe", "123-456-7890");
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(0, 1, 2, 19.99)); // Adding a dummy item
        order.setOrderItems(items);

        // Mocking PreparedStatement and ResultSet for generated keys
        PreparedStatement mockOrderStmt = mock(PreparedStatement.class);
        ResultSet mockGeneratedKeys = mock(ResultSet.class);

        // Mocking behavior
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockOrderStmt);
        when(mockOrderStmt.executeUpdate()).thenReturn(1); // Simulate successful order insertion
        when(mockOrderStmt.getGeneratedKeys()).thenReturn(mockGeneratedKeys);
        when(mockGeneratedKeys.next()).thenReturn(true);
        when(mockGeneratedKeys.getInt(1)).thenReturn(123); // Assume order ID 123 is generated

        // Act
        int generatedOrderId = orderDAO.addOrder(order);

        // Assert
        assertEquals(123, generatedOrderId); // Check that the generated order ID matches
        verify(mockOrderStmt).setTimestamp(eq(1), any(Timestamp.class));
        verify(mockOrderStmt).setString(eq(2), eq("John Doe"));
        verify(mockOrderStmt).setString(eq(3), eq("123-456-7890"));
        verify(mockOrderStmt).executeUpdate();
        verify(mockOrderStmt).getGeneratedKeys();
    }


    @Test
    public void testUpdateOrderSuccess() throws SQLException {
        Order order = new Order(new Timestamp(System.currentTimeMillis()), "John Doe", "123-456-7890");
        order.setOrderID(1);
        order.setPaidDate(new Timestamp(System.currentTimeMillis()));

        when(mockOrderStmt.executeUpdate()).thenReturn(1);

        orderDAO.updateOrder(order);

        verify(mockOrderStmt).setString(1, "John Doe");
        verify(mockOrderStmt).setString(2, "123-456-7890");
        verify(mockOrderStmt).setBoolean(3, true);
        verify(mockOrderStmt).setTimestamp(4, order.getPaidDate());
        verify(mockOrderStmt).setInt(5, 1);
        verify(mockOrderStmt).executeUpdate();
    }

    @Test
    public void testUpdateOrderFailure() throws SQLException {
        Order order = new Order(new Timestamp(System.currentTimeMillis()), "John Doe", "123-456-7890");
        order.setOrderID(1);
        order.setPaidDate(new Timestamp(System.currentTimeMillis()));

        when(mockOrderStmt.executeUpdate()).thenReturn(0);

        SQLException thrown = assertThrows(SQLException.class, () -> orderDAO.updateOrder(order));
        assertEquals("Updating order failed, no rows affected.", thrown.getMessage());
    }

    @Test
    public void testFetchAllOrdersSuccess() throws SQLException {
        when(mockStatement.executeQuery(ArgumentMatchers.anyString())).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getTimestamp("created_timestamp")).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(mockResultSet.getString("customer_name")).thenReturn("John Doe");
        when(mockResultSet.getString("customer_contact")).thenReturn("123-456-7890");
        when(mockResultSet.getInt("order_id")).thenReturn(1);

        List<Order> orders = orderDAO.fetchAllOrders();

        assertEquals(1, orders.size());
        Order order = orders.get(0);
        assertEquals("John Doe", order.getCustomerName());
        assertEquals("123-456-7890", order.getCustomerContact());
        assertFalse(order.isPaid());
    }

    @Test
    public void testFetchAllOrdersFailure() throws SQLException {
        when(mockStatement.executeQuery(ArgumentMatchers.anyString())).thenThrow(new SQLException("Fetch failed"));

        SQLException thrown = assertThrows(SQLException.class, () -> orderDAO.fetchAllOrders());
        assertEquals("Fetch failed", thrown.getMessage());
    }

    @Test
    public void testGetOrderByIdSuccess() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getTimestamp("created_timestamp")).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(mockResultSet.getString("customer_name")).thenReturn("John Doe");
        when(mockResultSet.getString("customer_contact")).thenReturn("123-456-7890");
        when(mockResultSet.getInt("order_id")).thenReturn(1);

        Order order = orderDAO.getOrderById(1);

        assertNotNull(order);
        assertEquals("John Doe", order.getCustomerName());
        assertEquals("123-456-7890", order.getCustomerContact());
    }

    @Test
    public void testGetOrderByIdNotFound() throws SQLException {
        when(mockResultSet.next()).thenReturn(false);

        Order order = orderDAO.getOrderById(1);

        assertNull(order);
    }

    @Test
    public void testCloseConnection() throws SQLException {
        orderDAO.closeConnection();

        verify(mockConnection).close();
    }

    @Test
    public void testCloseConnectionWhenAlreadyClosed() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(true);

        orderDAO.closeConnection();

        verify(mockConnection, never()).close();
    }

    @Test
    public void testFetchAllOrdersByDateSuccess() throws SQLException {
        Timestamp date = Timestamp.valueOf("2024-10-09 00:00:00");
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getTimestamp("created_timestamp")).thenReturn(date);
        when(mockResultSet.getString("customer_name")).thenReturn("John Doe");
        when(mockResultSet.getString("customer_contact")).thenReturn("123-456-7890");
        when(mockResultSet.getInt("order_id")).thenReturn(1);

        List<Order> orders = orderDAO.fetchAllOrdersByDate(date);

        assertEquals(1, orders.size());
        Order order = orders.get(0);
        assertEquals("John Doe", order.getCustomerName());
        assertEquals("123-456-7890", order.getCustomerContact());
        assertEquals(1, order.getOrderID());
        assertEquals(date, order.getOrderDate());
    }

    @Test
    public void testFetchAllOrdersByDateNoOrders() throws SQLException {
        Timestamp date = Timestamp.valueOf("2024-10-09 00:00:00");
        when(mockResultSet.next()).thenReturn(false);

        List<Order> orders = orderDAO.fetchAllOrdersByDate(date);

        assertTrue(orders.isEmpty());
    }

    @Test
    public void testFetchAllOrdersByDateSQLFailure() throws SQLException {
        Timestamp date = Timestamp.valueOf("2024-10-09 00:00:00");
        when(mockOrderStmt.executeQuery()).thenThrow(new SQLException("Database error"));

        SQLException thrown = assertThrows(SQLException.class, () -> orderDAO.fetchAllOrdersByDate(date));
        assertEquals("Database error", thrown.getMessage());
    }

    @Test
    public void testFetchAllOrdersByDateInvalidDate() throws SQLException {
        // Arrange
        Timestamp invalidDate = null; // Passing a null timestamp to simulate an invalid date

        // Act & Assert
        SQLException thrown = assertThrows(SQLException.class, () -> orderDAO.fetchAllOrdersByDate(invalidDate));
        assertEquals("The SQL statement cannot be prepared with a null date", thrown.getMessage());
    }
}
