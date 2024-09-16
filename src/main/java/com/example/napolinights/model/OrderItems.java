package com.example.napolinights.model;

import java.util.Objects;

public class OrderItems {
    private int orderItemID;
    private static int orderID;
    private static int menuID;
    private int quantity;
    private static double itemPrice;

    public OrderItems(int orderID, int menuID, int quantity, double itemPrice) {
        this.orderID = orderID;
        this.menuID = menuID;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
    }

    public OrderItems(int orderItemID, int orderID, int menuID, int quantity, double itemPrice) {
        this(orderID, menuID, quantity, itemPrice);
        this.orderItemID = orderItemID;
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
// OrderItemID is determined by the DB.
//    public void setOrderItemID(int orderItemID) {
//        this.orderItemID = orderItemID;
//    }
//
//    OrderID is determined during instancing and should not be changed.
//    public void setOrderID(int orderID) {
//        this.orderID = orderID;
//    }
//
//
//    public void setMenuID(int menuID) {
//        this.menuID = menuID;
//    }

    public void setQuantity(int quantity) {
        if (quantity < 0 ) {
            throw new IllegalArgumentException("Item quantity cannot be negative");
        }
        this.quantity = quantity;
    }
// Price should not be changed after instancing.
//    public void setItemPrice(double itemPrice) {
//        if (itemPrice < 0 ) {
//            throw new IllegalArgumentException("Item Price cannot be negative");
//        }
//        this.itemPrice = itemPrice;
//    }

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