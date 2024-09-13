package com.example.napolinights.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Order class using JUnit 5.
 */
public class OrderTest {

    private Order order;
    private OrderItems item1;
    private OrderItems item2;

    @BeforeEach
    public void setUp() {
        // Initialize some test data
        Timestamp orderDate = new Timestamp(System.currentTimeMillis());
        String customerName = "John Doe";
        String customerContact = "123456789";

        // Create an order instance
        order = new Order(orderDate, customerName, customerContact);

        // Create some sample order items
        item1 = new OrderItems(1, 1, 2, 10.5);
        item2 = new OrderItems(2, 1, 1, 5.0);
    }

    @Test
    public void testOrderConstructor() {
        Timestamp orderDate = new Timestamp(System.currentTimeMillis());
        Order testOrder = new Order(orderDate, "Jane Doe", "987654321");

        assertNotNull(testOrder);
        assertEquals("Jane Doe", testOrder.getCustomerName());
        assertEquals("987654321", testOrder.getCustomerContact());
        assertEquals(orderDate, testOrder.getOrderDate());
    }

    @Test
    public void testAddOrderItem() {
        order.addOrderItem(item1);
        order.addOrderItem(item2);

        List<OrderItems> items = order.getOrderItems();
        assertEquals(2, items.size());
        assertTrue(items.contains(item1));
        assertTrue(items.contains(item2));
    }

    @Test
    public void testRemoveOrderItem() {
        order.addOrderItem(item1);
        order.addOrderItem(item2);
        assertEquals(2, order.getOrderItems().size());

        order.removeOrderItem(item1);
        assertEquals(1, order.getOrderItems().size());
        assertFalse(order.getOrderItems().contains(item1));
        assertTrue(order.getOrderItems().contains(item2));
    }

    @Test
    public void testSettersAndGetters() {
        Timestamp newOrderDate = new Timestamp(System.currentTimeMillis() + 1000);
        String newCustomerName = "Alice";
        String newCustomerContact = "111222333";
        List<OrderItems> newItems = new ArrayList<>();
        newItems.add(item1);

        order.setOrderDate(newOrderDate);
        order.setCustomerName(newCustomerName);
        order.setCustomerContact(newCustomerContact);
        order.setOrderItems(newItems);

        assertEquals(newOrderDate, order.getOrderDate());
        assertEquals(newCustomerName, order.getCustomerName());
        assertEquals(newCustomerContact, order.getCustomerContact());
        assertEquals(newItems, order.getOrderItems());
    }

    @Test
    public void testEqualsAndHashCode() {
        Timestamp orderDate = new Timestamp(System.currentTimeMillis());
        Order sameOrder = new Order(orderDate, "John Doe", "123456789");
        Order differentOrder = new Order(orderDate, "Jane Doe", "987654321");

        assertEquals(order, sameOrder);
        assertEquals(order.hashCode(), sameOrder.hashCode());

        assertNotEquals(order, differentOrder);
        assertNotEquals(order.hashCode(), differentOrder.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "Order{orderID=0, orderDate=" + order.getOrderDate() +
                ", customerName='John Doe', customerContact='123456789', orderItems=[]}";
        assertEquals(expected, order.toString());
    }

    // Negative and Edge Case Tests

    @Test
    public void testAddNullOrderItem() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            order.addOrderItem(null);
        });
        assertEquals("Order item cannot be null", exception.getMessage());
    }

    @Test
    public void testRemoveNonExistentOrderItem() {
        order.addOrderItem(item1);
        assertDoesNotThrow(() -> order.removeOrderItem(item2));  // item2 is not in the list
        assertEquals(1, order.getOrderItems().size());  // item1 is still in the list
    }

    @Test
    public void testSetOrderDateToNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            order.setOrderDate(null);
        });
        assertEquals("Order date cannot be null", exception.getMessage());
    }

    @Test
    public void testSetCustomerNameToNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            order.setCustomerName(null);
        });
        assertEquals("Customer name cannot be null", exception.getMessage());
    }

    @Test
    public void testSetCustomerContactToEmptyString() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            order.setCustomerContact("");
        });
        assertEquals("Customer contact cannot be an empty string", exception.getMessage());
    }

    @Test
    public void testSetOrderItemsToNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            order.setOrderItems(null);
        });
        assertEquals("Order items cannot be null", exception.getMessage());
    }

    @Test
    public void testSetOrderItemsWithEmptyList() {
        List<OrderItems> emptyList = new ArrayList<>();
        order.setOrderItems(emptyList);
        assertEquals(0, order.getOrderItems().size());
    }
}
