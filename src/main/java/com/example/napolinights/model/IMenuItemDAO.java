package com.example.napolinights.model;

import java.util.List;

/**
 * Interface for Data Access Object (DAO) operations related to the `Menu` table.
 * Defines methods for creating, reading, updating, and deleting (CRUD) menu items.
 */
public interface IMenuItemDAO {

    /**
     * Creates the `menu` table in the database if it does not already exist.
     */
    void createMenuTable();

    /**
     * Adds a new menu item to the `menu` table.
     *
     * @param menuItem The `MenuItem` object to be added to the database.
     */
    void addMenuItem(MenuItem menuItem);

    /**
     * Updates an existing menu item in the `menu` table.
     *
     * @param menuItem The `MenuItem` object containing updated information.
     */
    void updateMenuItem(MenuItem menuItem);

    /**
     * Removes a menu item from the `menu` table based on the menu ID.
     *
     * @param id The ID of the menu item to be removed.
     */
    void removeMenuItemById(int id);

    /**
     * Retrieves all menu items from the `menu` table.
     *
     * @return A list of `MenuItem` objects representing all menu items in the database.
     */
    List<MenuItem> fetchAllMenuItems();

    /**
     * Retrieves a menu item from the `menu` table based on the menu ID.
     *
     * @param id The ID of the menu item to be retrieved.
     * @return A `MenuItem` object representing the menu item with the specified ID, or `null` if not found.
     */
    MenuItem fetchMenuItemById(int id);

    /**
     * Closes the database connection.
     */
    void closeConnection();
}
