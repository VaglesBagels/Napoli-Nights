package com.example.napolinights.model;

public class MenuItem {
    private int menuItemID;
    private Category category;
    private String name;
    private String description;
    private double price;
    private String imageURL;

    // Constructor without menuItemID
    public MenuItem(Category category, String name, String description, double price, String imageURL) {
        setCategory(category);
        setName(name);
        setDescription(description);
        setPrice(price);
        setImageURL(imageURL);
    }

    // Constructor with menuItemID
    public MenuItem(int menuItemID, Category category, String name, String description, double price, String imageURL) {
        this(category, name, description, price, imageURL);
        this.menuItemID = menuItemID;
    }

    // Getters
    public int getMenuItemID() {
        return menuItemID;
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

    // Setters with validation
    public void setCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        this.category = category;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        this.description = description;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }

    public void setImageURL(String imageURL) {
        if (imageURL == null || imageURL.trim().isEmpty()) {
            throw new IllegalArgumentException("Image URL cannot be null or empty");
        }
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "menuItemID=" + menuItemID +
                ", category=" + category +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
