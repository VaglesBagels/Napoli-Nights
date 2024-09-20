package com.example.napolinights.model.obsolete;

import com.example.napolinights.model.OrderItem;

import java.util.List;

/**
 * Interface for Data Access Object (DAO) for managing OrderItems.
 * Defines methods for CRUD operations on OrderItems.
 */
public interface IOrderItemsDAO {
    /**
     * Creates the OrderItems table if it does not exist.
     */
    void createOrderItemsTable();

    /**
     * Inserts a new OrderItem into the database.
     *
     * @param orderItem The OrderItem object to be inserted.
     */
    void insert(OrderItem orderItem);

    /**
     * Updates an existing OrderItem in the database.
     *
     * @param orderItem The OrderItem object with updated values.
     */
    void update(OrderItem orderItem);

    /**
     * Deletes an OrderItem from the database by its ID.
     *
     * @param id The ID of the OrderItem to be deleted.
     */
    void delete(int id);

    /**
     * Retrieves all OrderItems from the database.
     *
     * @return A list of all OrderItems.
     */
    List<OrderItem> getAll();

    /**
     * Retrieves an OrderItem from the database by its ID.
     *
     * @param id The ID of the OrderItem to be retrieved.
     * @return The OrderItem with the specified ID, or null if not found.
     */
    OrderItem getById(int id);

    /**
     * Closes the database connection.
     */
    void close();
}
