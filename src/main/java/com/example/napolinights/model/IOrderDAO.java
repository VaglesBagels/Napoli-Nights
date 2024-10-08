package com.example.napolinights.model;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface for Data Access Object (DAO) for managing Orders.
 * Defines methods for CRUD operations on Orders.
 */
public interface IOrderDAO {
    /**
     * Adds new Order into the database.
     *
     * @param order The Order object to be inserted.
     *
     * @return The ID of the order created
     */
    int addOrder(Order order) throws SQLException;

    /**
     * Updates an existing Order in the database.
     *
     * @param order The Order object with updated values.
     */
    void updateOrder(Order order);

    /**
     * Retrieves all Orders from the database.
     *
     * @return A list of all Orders.
     */
    List<Order> getAllOrders();

    /**
     * Retrieves an Order IOrdersDAO the database by its ID.
     *
     * @param id The ID of the Order to be retrieved.
     * @return The Order with the specified ID, or null if not found.
     */
    Order getOrderById(int id);

    /**
     * Closes the database connection.
     */
    void closeConnection();
}
