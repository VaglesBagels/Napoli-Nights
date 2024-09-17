package com.example.napolinights;

public class CartItem{
    private String name;
    private double price;
    private int quantity;

    public CartItem(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
    public int getQuantity() {
        return quantity;
    }
}
