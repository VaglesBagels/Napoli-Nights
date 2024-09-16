package com.example.napolinights.model;

public class OrderItems {
    private int orderItemID;
    private final int orderID;
    private final int menuID;
    private int quantity;
    private final double itemPrice;

    public OrderItems(int orderID, int menuID, int quantity, double itemPrice) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Item quantity cannot be negative");
        }
        if (itemPrice < 0) {
            throw new IllegalArgumentException("Item price cannot be negative");
        }
        this.orderID = orderID;
        this.menuID = menuID;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
    }

    public OrderItems(int orderItemID, int orderID, int menuID, int quantity, double itemPrice) {
        this(orderID, menuID, quantity, itemPrice);
        this.orderItemID = orderItemID;
    }

    // Getter methods
    public int getOrderItemID() {
        return orderItemID;
    }

    public int getOrderID() {
        return orderID;
    }

    public int getMenuID() {
        return menuID;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    // Setter for quantity with validation
    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Item quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderItems{" +
                "orderItemID=" + orderItemID +
                ", orderID=" + orderID +
                ", menuID=" + menuID +
                ", quantity=" + quantity +
                ", itemPrice=" + itemPrice +
                '}';
    }
}
