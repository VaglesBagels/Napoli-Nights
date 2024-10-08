package com.example.napolinights.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOTest {

    private OrderDAO orderDAO;
    private Connection mockConnection;
    private PreparedStatement mockOrderStmt;
    private PreparedStatement mockOrderItemStmt;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockOrderStmt = mock(PreparedStatement.class);
        mockOrderItemStmt = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(ArgumentMatchers.anyString(), ArgumentMatchers.eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(mockOrderStmt);
        when(mockConnection.prepareStatement(ArgumentMatchers.anyString())).thenReturn(mockOrderItemStmt);
        when(mockOrderStmt.getGeneratedKeys()).thenReturn(mockResultSet);

        orderDAO = new OrderDAO(mockConnection);
    }

    @Test
    public void testAddOrderSuccess() throws SQLException {
        // Arrange
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Order order = new Order(now, "John Doe", "123-456-7890");
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(123, 1, 2, 19.99));
        order.setOrderItems(items);

        // Mock ResultSet to return generated keys
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(123);

        // Mock execution results for orderStmt
        when(mockOrderStmt.executeUpdate()).thenReturn(1); // Simulate successful order row update

        // Mock execution of order items batch
        when(mockOrderItemStmt.executeBatch()).thenReturn(new int[]{1}); // Simulate successful batch insert

        // Act
        orderDAO.addOrder(order);

        // Assert for order insertion
        verify(mockOrderStmt).setTimestamp(1, now);
        verify(mockOrderStmt).setString(2, "John Doe");
        verify(mockOrderStmt).setString(3, "123-456-7890");
        verify(mockOrderStmt).executeUpdate();
        verify(mockResultSet).next();

        // Assert for order item insertion
        verify(mockOrderItemStmt).setInt(1, 123);
        verify(mockOrderItemStmt).setInt(2, 1);
        verify(mockOrderItemStmt).setInt(3, 2);
        verify(mockOrderItemStmt).setDouble(4, 19.99);
        verify(mockOrderItemStmt).addBatch();
        verify(mockOrderItemStmt).executeBatch();

        // Assert for transaction commit
        verify(mockConnection).commit();
    }


    @Test
    public void testAddOrderInsertOrderFailure() throws SQLException {
        // Arrange
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Order order = new Order(now, "John Doe", "123-456-7890");
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(1, 2, 3, 19.99));
        order.setOrderItems(items);

        // Mock failure to insert order
        when(mockOrderStmt.executeUpdate()).thenReturn(0);

        // Act & Assert
        SQLException thrown = assertThrows(SQLException.class, () -> orderDAO.addOrder(order));
        assertEquals("Creating order failed, no rows affected.", thrown.getMessage());
    }

    @Test
    public void testAddOrderRetrieveIDFailure() throws SQLException {
        // Arrange
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Order order = new Order(now, "John Doe", "123-456-7890");
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(1, 2, 3, 19.99));
        order.setOrderItems(items);

        // Mock successful order insert but failure to retrieve ID
        when(mockOrderStmt.executeUpdate()).thenReturn(1);
        when(mockResultSet.next()).thenReturn(false);

        // Act & Assert
        SQLException thrown = assertThrows(SQLException.class, () -> orderDAO.addOrder(order));
        assertEquals("Creating order failed, no ID obtained.", thrown.getMessage());
    }

    @Test
    public void testAddOrderBatchInsertFailure() throws SQLException {
        // Arrange
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Order order = new Order(now, "John Doe", "123-456-7890");
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(1, 2, 3, 19.99));
        order.setOrderItems(items);

        // Mock successful order insert and ID retrieval
        when(mockOrderStmt.executeUpdate()).thenReturn(1);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString(1)).thenReturn("order123");

        // Mock batch insert to throw SQLException
        doThrow(new SQLException("Invalid order data")).when(mockOrderItemStmt).executeBatch();

        // Act & Assert
        SQLException thrown = assertThrows(SQLException.class, () -> orderDAO.addOrder(order));
        assertEquals("Invalid order data", thrown.getMessage());
    }

    @Test
    public void testValidateOrderNullOrder() {
        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> orderDAO.addOrder(null));
        assertEquals("Order cannot be null.", thrown.getMessage());
    }

    @Test
    public void testAddOrderInvalidCustomerName() {
        // Arrange
        Order order = new Order(new Timestamp(System.currentTimeMillis()), "", "123-456-7890");

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> orderDAO.addOrder(order));
        assertEquals("Customer name cannot be null or empty.", thrown.getMessage());
    }

    @Test
    public void testAddOrderInvalidCustomerContact() {
        // Arrange
        Order order = new Order(new Timestamp(System.currentTimeMillis()), "John Doe", "");

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> orderDAO.addOrder(order));
        assertEquals("Customer contact cannot be null or empty.", thrown.getMessage());
    }

    @Test
    public void testAddOrderNoItems() {
        // Arrange
        Order order = new Order(new Timestamp(System.currentTimeMillis()), "John Doe", "123-456-7890");
        order.setOrderItems(new ArrayList<>());

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> orderDAO.addOrder(order));
        assertEquals("Order must have at least one order item.", thrown.getMessage());
    }
}
