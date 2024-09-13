package com.example.napolinights.model;

import java.sql.Timestamp;
import java.util.List;

/**
 * Interface for Data Access Object (DAO) for managing Orders.
 * Defines methods for CRUD operations on Orders.
 */
public interface IOrdersDAO {

    /**
     * Creates the Orders table if it does not exist.
     */
    void createOrdersTable();

    /**
     * Inserts a new Order into the database.
     *
     * @param order The Order object to be inserted.
     */
    void insert(Orders order);

    /**
     * Updates an existing Order in the database.
     *
     * @param order The Order object with updated values.
     */
    void update(Orders order);

    /**
     * Deletes an Order from the database by its ID.
     *
     * @param id The ID of the Order to be deleted.
     */
    void delete(int id);

    /**
     * Retrieves all Orders from the database.
     *
     * @return A list of all Orders.
     */
    List<Orders> getAll();

    /**
     * Retrieves an Order from the database by its ID.
     *
     * @param id The ID of the Order to be retrieved.
     * @return The Order with the specified ID, or null if not found.
     */
    Orders getById(int id);

    /**
     * Closes the database connection.
     */
    void close();
}
