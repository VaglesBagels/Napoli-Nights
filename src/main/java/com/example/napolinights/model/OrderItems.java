package com.example.napolinights.model;

public class OrderItems {
    private int orderItemID;
    private int orderID;
    private int menuID;
    private int quantity;
    private double itemPrice;

    public OrderItems(int orderItemID, int orderID, int menuID, int quantity, double itemPrice) {
        this.orderItemID = orderItemID;
        this.orderID = orderID;
        this.menuID = menuID;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
    }

    public OrderItems(int orderID, int menuID, int quantity, double itemPrice) {
        this.orderID = orderID;
        this.menuID = menuID;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
    }

    // GET functions for DB
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

    // SET functions for DB
    public void setOrderItemID(int orderItemID) {
        this.orderItemID = orderItemID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public void setMenuID(int menuID) {
        this.menuID = menuID;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
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