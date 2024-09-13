package com.example.napolinights.model;

import java.util.List;

/**
 * Interface for Data Access Object (DAO) operations related to the `Menu` table.
 * Defines methods for creating, reading, updating, and deleting (CRUD) menu items.
 */
public interface IMenuDAO {

    /**
     * Creates the `menu` table in the database if it does not already exist.
     */
    void createMenuTable();

    /**
     * Inserts a new menu item into the `menu` table.
     *
     * @param menuItem The `Menu` object to be inserted into the database.
     */
    void insert(MenuItem menuItem);

    /**
     * Updates an existing menu item in the `menu` table.
     *
     * @param menuItem The `Menu` object containing updated information.
     */
    void update(MenuItem menuItem);

    /**
     * Deletes a menu item from the `menu` table based on the menu ID.
     *
     * @param id The ID of the menu item to be deleted.
     */
    void delete(int id);

    /**
     * Retrieves all menu items from the `menu` table.
     *
     * @return A list of `Menu` objects representing all menu items in the database.
     */
    List<MenuItem> getAll();

    /**
     * Retrieves a menu item from the `menu` table based on the menu ID.
     *
     * @param id The ID of the menu item to be retrieved.
     * @return A `Menu` object representing the menu item with the specified ID, or `null` if not found.
     */
    MenuItem getById(int id);

    /**
     * Closes the database connection.
     */
    void close();
}
