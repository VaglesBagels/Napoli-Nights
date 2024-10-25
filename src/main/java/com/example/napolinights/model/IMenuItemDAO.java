package com.example.napolinights.model;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface for Data Access Object (DAO) operations related to the {@code menu} table.
 * Provides methods for performing Create, Read, Update, and Delete (CRUD) operations on menu items in the database.
 * <p>
 * Implementations of this interface should manage the interaction with the {@code menu} table in the database,
 * including adding, updating, retrieving, and closing connections.
 * </p>
 */
public interface IMenuItemDAO {

    /**
     * Creates the {@code menu} table in the database if it does not already exist.
     * This method ensures that the table is structured according to the required schema for menu items.
     *
     * @return {@code true} if the table is created successfully; {@code false} if the table already exists or an error occurs.
     */
    boolean createMenuTable();

    /**
     * Adds a new menu item to the {@code menu} table.
     * This method inserts a new {@link MenuItem} into the database using the provided details.
     *
     * @param menuItem The {@link MenuItem} object to be added to the database.
     *                 The object should contain all necessary information, such as category, name, description, price, and image URL.
     */
    void addMenuItem(MenuItem menuItem);

    /**
     * Updates an existing menu item in the {@code menu} table.
     * This method updates the {@link MenuItem} in the database that matches the ID of the provided {@code menuItem} object.
     *
     * @param menuItem The {@link MenuItem} object containing the updated information.
     *                 The method will update the menu item with the corresponding ID in the database with the new details.
     */
    void updateMenuItem(MenuItem menuItem);

    /**
     * Retrieves all menu items from the {@code menu} table.
     * This method returns a list of all {@link MenuItem} objects currently stored in the database.
     *
     * @return A {@code List} of {@link MenuItem} objects representing all menu items in the database.
     */
    List<MenuItem> fetchAllMenuItems() throws SQLException;

    /**
     * Retrieves all menu items from the {@code menu} table that belong to a specific category.
     * This method filters menu items based on the provided {@link Category} and returns a list of items within that category.
     *
     * @param category The {@link Category} representing the category of menu items to retrieve.
     * @return A {@code List} of {@link MenuItem} objects that belong to the specified category.
     */
    List<MenuItem> fetchAllMenuItemsByCategory(Category category);

    /**
     * Retrieves a menu item from the {@code menu} table based on the menu ID.
     * This method looks up the menu item with the provided ID and returns its details as a {@link MenuItem} object.
     *
     * @param id The ID of the menu item to be retrieved.
     * @return A {@link MenuItem} object representing the menu item with the specified ID, or {@code null} if the item is not found.
     */
    MenuItem fetchMenuItemById(int id);

    /**
     * Closes the database connection used by this DAO.
     * This method ensures that any resources associated with the connection are properly released.
     */
    void closeConnection();
}
