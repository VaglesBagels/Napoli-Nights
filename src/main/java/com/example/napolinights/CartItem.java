package com.example.napolinights;

public class CartItem {
    private int id;
    private String name;
    private double unitPrice;
    private int quantity;
    private double gst;
    private double totalInc;

    public CartItem(int id, String name, double unitPrice, int quantity, double gst, double totalInc) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.gst = gst;
        this.totalInc = totalInc;
    }

    public int getId() { return id; }

    public String getName() {
        return name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getGst() {
        return gst;
    }

    public double getTotalInc() {
        return totalInc;
    }
}
