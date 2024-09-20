package com.example.napolinights.model;

public class OrderItem {
    private int orderItemID;
    private final int orderID;
    private final int menuID;
    private int quantity;
    private final double itemPrice;

    public OrderItem(int orderID, int menuID, int quantity, double itemPrice) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Item quantity must be greater than 0.");
        }
        if (itemPrice <= 0) {
            throw new IllegalArgumentException("Item price must be greater than 0.");
        }
        this.orderID = orderID;
        this.menuID = menuID;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
    }

    public OrderItem(int orderItemID, int orderID, int menuID, int quantity, double itemPrice) {
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
        if (itemPrice <= 0) {
            throw new IllegalArgumentException("Item price must be greater than 0.");
        }
        return itemPrice;
    }

    // Setter for quantity with validation
    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Item quantity must be greater than 0.");
        }
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemID=" + orderItemID +
                ", orderID=" + orderID +
                ", menuID=" + menuID +
                ", quantity=" + quantity +
                ", itemPrice=" + itemPrice +
                '}';
    }
}
