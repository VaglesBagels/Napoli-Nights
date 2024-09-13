package com.example.napolinights.model;

public class Menu {
    private int menuID;
    private Category category;
    private String name;
    private String description;
    private double price;
    private String imageURL;

    public Menu(Category category, String name, String description, double price, String imageURL) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageURL = imageURL;
    }

    public Menu(int menuID, Category category, String name, String description, double price, String imageURL) {
        this.menuID = menuID;
        this.category = category;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageURL = imageURL;
    }

    // GET functions for DB
    public int getMenuID() {
        return menuID;
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getImageURL() {
        return imageURL;
    }

    // SET functions for DB
    public void setCategory(Category category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "menuID=" + menuID +
                ", category=" + category +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
