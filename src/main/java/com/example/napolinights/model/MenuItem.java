package com.example.napolinights.model;

/**
 * Represents a menu item in the restaurant's menu.
 */
public class MenuItem {
    private int menuItemID;
    private Category category;
    private String name;
    private String description;
    private double price;
    private String imageURL;

    /**
     * Constructs a MenuItem without an ID.
     *
     * @param category    the category of the menu item
     * @param name        the name of the menu item
     * @param description a brief description of the menu item
     * @param price       the price of the menu item
     * @param imageURL    the URL of the image representing the menu item
     */
    public MenuItem(Category category, String name, String description, double price, String imageURL) {
        setCategory(category);
        setName(name);
        setDescription(description);
        setPrice(price);
        setImageURL(imageURL);
    }

    /**
     * Constructs a MenuItem with an ID.
     *
     * @param menuItemID  the unique identifier for the menu item
     * @param category    the category of the menu item
     * @param name        the name of the menu item
     * @param description a brief description of the menu item
     * @param price       the price of the menu item
     * @param imageURL    the URL of the image representing the menu item
     */
    public MenuItem(int menuItemID, Category category, String name, String description, double price, String imageURL) {
        this(category, name, description, price, imageURL);
        this.menuItemID = menuItemID;
    }

    /**
     * Returns the unique identifier of the menu item.
     *
     * @return the menu item ID
     */
    public int getMenuItemID() {
        return menuItemID;
    }

    /**
     * Returns the category of the menu item.
     *
     * @return the category of the menu item
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Sets the category of the menu item.
     *
     * @param category the category to set
     * @throws IllegalArgumentException if the category is null
     */
    public void setCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        this.category = category;
    }

    /**
     * Returns the name of the menu item.
     *
     * @return the name of the menu item
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the menu item.
     *
     * @param name the name to set
     * @throws IllegalArgumentException if the name is null or empty
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    /**
     * Returns the description of the menu item.
     *
     * @return the description of the menu item
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the menu item.
     *
     * @param description the description to set
     * @throws IllegalArgumentException if the description is null or empty
     */
    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        this.description = description;
    }

    /**
     * Returns the price of the menu item.
     *
     * @return the price of the menu item
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the menu item.
     *
     * @param price the price to set
     * @throws IllegalArgumentException if the price is negative
     */
    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }

    /**
     * Returns the image URL of the menu item.
     *
     * @return the image URL of the menu item
     */
    public String getImageURL() {
        return imageURL;
    }

    /**
     * Sets the image URL of the menu item.
     *
     * @param imageURL the image URL to set
     * @throws IllegalArgumentException if the image URL is null or empty
     */
    public void setImageURL(String imageURL) {
        if (imageURL == null || imageURL.trim().isEmpty()) {
            throw new IllegalArgumentException("Image URL cannot be null or empty");
        }
        this.imageURL = imageURL;
    }

    /**
     * Returns a string representation of the MenuItem object.
     *
     * @return a string representation of the menu item
     */
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
