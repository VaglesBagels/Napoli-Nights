package com.example.napolinights.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    void testConstructor() {
        OrderItem orderItem = new OrderItem(2, 3, 10.5);

        assertEquals(2, orderItem.getMenuID());
        assertEquals(3, orderItem.getQuantity());
        assertEquals(10.5, orderItem.getItemPrice());
        assertEquals("", orderItem.getSpecialInstructions());
    }

    @Test
    void testConstructorWithOrderItemID() {
        OrderItem orderItem = new OrderItem(10, 1, 2, 3, 10.5);

        assertEquals(10, orderItem.getOrderItemID());
        assertEquals(1, orderItem.getOrderID());
        assertEquals(2, orderItem.getMenuID());
        assertEquals(3, orderItem.getQuantity());
        assertEquals(10.5, orderItem.getItemPrice());
        assertEquals("", orderItem.getSpecialInstructions());
    }

    @Test
    void testSetQuantityValid() {
        OrderItem orderItems = new OrderItem(2, 3, 10.5);
        orderItems.setQuantity(5);

        assertEquals(5, orderItems.getQuantity());
    }

    @Test
    void testSetQuantityInvalid() {
        OrderItem orderItems = new OrderItem(2, 3, 10.5);

        assertThrows(IllegalArgumentException.class, () -> orderItems.setQuantity(-1));
    }

    @Test
    void testConstructorWithInvalidQuantity() {
        assertThrows(IllegalArgumentException.class, () -> new OrderItem(2, -3, 10.5));
    }

    @Test
    void testConstructorWithInvalidItemPrice() {
        assertThrows(IllegalArgumentException.class, () -> new OrderItem(12, 3, -10.5));
    }

    @Test
    void testToString() {
        OrderItem orderItems = new OrderItem(10, 1, 2, 3, 10.5);
        String expected = "OrderItem{orderItemID=10, orderID=1, menuID=2, quantity=3, itemPrice=10.5, specialInstructions=''}";

        assertEquals(expected, orderItems.toString());
    }

    // Negative Tests for Constructor

    @Test
    void testConstructorWithZeroQuantity() {
        assertThrows(IllegalArgumentException.class, () -> new OrderItem(2, 0, 10.5),
                "Expected to throw IllegalArgumentException for zero quantity");
    }

    @Test
    void testConstructorWithZeroPrice() {
        assertThrows(IllegalArgumentException.class, () -> new OrderItem(2, 3, 0.0),
                "Expected to throw IllegalArgumentException for zero price.");
    }

    // Negative Test for Invalid Quantity Setting

    @Test
    void testSetQuantityZero() {
        OrderItem orderItem = new OrderItem(2, 3, 10.5);
        assertThrows(IllegalArgumentException.class, () -> orderItem.setQuantity(0),
                "Expected to throw IllegalArgumentException for setting zero quantity");
    }

    // Negative Test for Large Quantity or Price

    @Test
    void testConstructorWithLargeQuantity() {
        int largeQuantity = Integer.MAX_VALUE;
        assertDoesNotThrow(() -> new OrderItem(2, largeQuantity, 10.5),
                "The constructor should handle large quantities without throwing an exception");
    }

    @Test
    void testConstructorWithLargePrice() {
        double largePrice = Double.MAX_VALUE;
        assertDoesNotThrow(() -> new OrderItem(2, 3, largePrice),
                "The constructor should handle large prices without throwing an exception");
    }

    // Negative Test for String Representation Edge Case

    @Test
    void testToStringWithEdgeValues() {
        OrderItem orderItem = new OrderItem(0, 0, 0, Integer.MAX_VALUE, Double.MAX_VALUE);
        String expected = "OrderItem{orderItemID=0, orderID=0, menuID=0, quantity=" + Integer.MAX_VALUE + ", itemPrice=" + Double.MAX_VALUE + ", specialInstructions=''}";

        assertEquals(expected, orderItem.toString(),
                "The toString method should handle edge values correctly in its string representation");
    }
}
