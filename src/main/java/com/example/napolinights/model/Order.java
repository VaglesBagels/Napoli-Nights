package com.example.napolinights.model;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents an Order in the Napoli Nights application.
 * Contains details about the order including the order date, customer information, and the list of items in the order.
 */
public class Order {
    private int orderID;
    private Timestamp orderDate;
    private String customerName;
    private String customerContact;
    private List<OrderItem> orderItems;
    private boolean isPaid;
    private Timestamp paidDate;

    /**
     * Constructs a new Order with the specified date, customer name, and contact.
     * Typically used when creating a new order that has not yet been stored in the database.
     *
     * @param orderDate       The timestamp when the order was placed. Must not be null.
     * @param customerName    The name of the customer placing the order. Must not be null.
     * @param customerContact The contact information of the customer. Must not be null.
     * @throws NullPointerException if any of the parameters are null.
     */
    public Order (Timestamp orderDate, String customerName, String customerContact) {
        this.orderDate = Objects.requireNonNull(orderDate, "Order date cannot be null");
        setCustomerName(customerName);
        setCustomerContact(customerContact);
        this.orderItems = new ArrayList<>(); // Initialise orderItems
        this.isPaid = false;
    }

    /**
     * Constructs an Order with the specified ID, date, customer name, contact, and list of order items.
     * Typically used when retrieving an order from the database.
     *
     * @param orderID         The unique identifier of the order.
     * @param orderDate       The timestamp when the order was placed. Must not be null.
     * @param customerName    The name of the customer placing the order. Must not be null.
     * @param customerContact The contact information of the customer. Must not be null.
     * @param orderItems      The list of items associated with this order.
     * @throws NullPointerException if any of the required parameters are null.
     */
    public Order(int orderID, Timestamp orderDate, String customerName, String customerContact, List<OrderItem> orderItems, Timestamp paidDate) {
        this(orderDate, customerName, customerContact);
        setOrderID(orderID);
        setOrderItems(orderItems);
        setPaidDate(paidDate);
    }

    /**
     * Sets the paid date of the order and marks the order as paid.
     * Ensures that the paid date is not in the future.
     *
     * @param paidDate The timestamp when the order was paid. Can be null to unset the payment status.
     * @throws IllegalArgumentException if paidDate is in the future.
     */
    void setPaidDate(Timestamp paidDate) {
        if (paidDate != null) {
            if (paidDate.after(Timestamp.from(Instant.now()))) {
                throw new IllegalArgumentException("Paid date cannot be in the future.");
            }
            this.isPaid = true;
            this.paidDate = paidDate;
        } else {
            this.isPaid = false;
            this.paidDate = null;
        }
    }

    /**
     * Gets the paid status of the order.
     *
     * @return true if the order is paid; false otherwise.
     */
    public boolean isPaid() {
        return isPaid;
    }

    /**
     * Gets the paid date of the order.
     *
     * @return The timestamp when the order was paid, or null if the order is unpaid.
     */
    public Timestamp getPaidDate() {
        return paidDate;
    }

    /**
     * Gets the unique identifier of the order.
     *
     * @return The order ID.
     */
    public Integer getOrderID() {
        return orderID;
    }

    /**
     * Sets the unique identifier of the order.
     *
     * @param orderID The order ID to set.
     */
    public void setOrderID(int orderID) {
        if (this.orderID > 0) {
            throw new IllegalArgumentException("Order ID cannot be changed after it's been set.");
        }
        if (orderID == 0) {
            throw new IllegalArgumentException("Order ID must be greater than 0.");
        }
        this.orderID = orderID;
    }

    /**
     * Gets the date and time when the order was placed.
     *
     * @return The order date as a {@link Timestamp}.
     */
    public Timestamp getOrderDate() {
        return orderDate;
    }

    /**
     * Sets the date and time when the order was placed.
     *
     * @param orderDate The order date to set. Must not be null.
     * @throws NullPointerException if orderDate is null.
     */
    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = Objects.requireNonNull(orderDate, "Order date cannot be null");
    }

    /**
     * Gets the name of the customer who placed the order.
     *
     * @return The customer's name.
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the name of the customer who placed the order.
     *
     * @param customerName The customer's name to set. Must not be null.
     * @throws NullPointerException if customerName is null.
     */
    public void setCustomerName(String customerName) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty.");
        }
        this.customerName = customerName;
    }

    /**
     * Gets the contact information of the customer.
     *
     * @return The customer's contact information.
     */
    public String getCustomerContact() {
        return customerContact;
    }

    /**
     * Sets the contact information of the customer.
     *
     * @param customerContact The customer's contact information to set. Must not be null.
     * @throws NullPointerException if customerContact is null.
     */
    public void setCustomerContact(String customerContact) {
        if (customerContact == null || customerContact.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer contact cannot be null or empty.");
        }
        this.customerContact = customerContact;
    }

    /**
     * Gets the list of items associated with the order.
     *
     * @return A list of {@link OrderItem} associated with the order.
     */
    public List<OrderItem> getOrderItems() {
        return new ArrayList<>(orderItems); // Defensive copy
    }

    /**
     * Sets the list of items associated with the order.
     *
     * @param orderItems The list of {@link OrderItem} to set.
     */
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = new ArrayList<>(Objects.requireNonNull(orderItems, "Order items cannot be null")); // Defensive copy
    }

    /**
     * Adds an item to the order.
     *
     * @param item The {@link OrderItem} to add.
     */
    public void addOrderItem(OrderItem item) {
        orderItems.add(Objects.requireNonNull(item, "Order item cannot be null"));
    }

    /**
     * Removes an item from the order.
     *
     * @param item The {@link OrderItem} to remove.
     */
    public void removeOrderItem(OrderItem item) {
        orderItems.remove(item);
    }

    /**
     * Returns a string representation of the order, including its ID, date, customer information, and items.
     *
     * @return A string representation of the order.
     */
    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", orderDate=" + orderDate +
                ", customerName='" + customerName + '\'' +
                ", customerContact='" + customerContact + '\'' +
                ", orderItems=" + orderItems +
                ", isPaid=" + isPaid +
                ", paidDate=" + paidDate +
                '}';
    }

    /**
     * Checks if this order is equal to another object.
     * Two orders are considered equal if they have the same ID, date, customer name, and contact.
     *
     * @param o The object to compare.
     * @return true if the orders are equal; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderID == order.orderID && orderDate.equals(order.orderDate) &&
                customerName.equals(order.customerName) && customerContact.equals(order.customerContact);
    }

    /**
     * Returns the hash code value for the order.
     * The hash code is based on the order's ID, date, customer name, and contact.
     *
     * @return The hash code value for this order.
     */
    @Override
    public int hashCode() {
        return Objects.hash(orderID, orderDate, customerName, customerContact);
    }
}
