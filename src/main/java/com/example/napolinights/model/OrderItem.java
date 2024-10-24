package com.example.napolinights.model;

public class OrderItem {
    private int orderItemID;
    private int orderID;
    private final int menuID;
    private int quantity;
    private String specialInstructions;
    private final double itemPrice;

    // New fields for additional menu item details
    private String menuName;  // Menu item name
    private double menuPrice; // Menu item original price
    private double gst;  // GST for the item

    public OrderItem(int menuID, int quantity, double itemPrice) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Item quantity must be greater than 0.");
        }
        if (itemPrice <= 0) {
            throw new IllegalArgumentException("Item price must be greater than 0.");
        }
        this.menuID = menuID;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
        this.specialInstructions = "";
        this.gst = gst;
    }

    public OrderItem(int orderItemID, int orderID, int menuID, int quantity, double itemPrice) {
        this(menuID, quantity, itemPrice);
        this.orderID = orderID;
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
        if (quantity <= 0) {
            throw new IllegalArgumentException("Item quantity must be greater than 0.");
        }
        this.quantity = quantity;
    }

    // Getter for special instructions
    public String getSpecialInstructions() {
        return specialInstructions;
    }

    // Setter for special instructions
    public void setSpecialInstructions(String specialInstructions) {
        if (specialInstructions != null) {
            this.specialInstructions = specialInstructions.trim();
        } else {
            this.specialInstructions = "";
        }
    }

    // Getters and setters for new fields
    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public double getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(double menuPrice) {
        this.menuPrice = menuPrice;
    }


    // Method to calculate GST
    public double getGst() {
        return Math.round(itemPrice * quantity * 0.10 * 100.0) / 100.0; // Assuming GST is 10%, rounding to 2 decimal places
    }


    // Method to calculate total price including GST
    public double getTotalPrice() {
        return (itemPrice * quantity) + getGst();
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemID=" + orderItemID +
                ", orderID=" + orderID +
                ", menuID=" + menuID +
                ", quantity=" + quantity +
                ", itemPrice=" + itemPrice +
                ", specialInstructions='" + specialInstructions + '\'' +
                ", menuName='" + menuName + '\'' +
                ", menuPrice=" + menuPrice +
                '}';
    }
}
