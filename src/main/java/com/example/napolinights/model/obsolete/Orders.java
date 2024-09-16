package com.example.napolinights.model.obsolete;

import java.sql.Timestamp;

public class Orders {
    private int orderID;
    private Timestamp orderDate;
    private String customerName;
    private String customerContact;

    public Orders(int orderID, Timestamp orderDate, String customerName, String customerContact) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.customerName = customerName;
        this.customerContact = customerContact;
    }

    public Orders(Timestamp orderDate, String customerName, String customerContact) {
        this.orderDate = orderDate;
        this.customerName = customerName;
        this.customerContact = customerContact;
    }

    // GET functions for DB
    public int getOrderID() {
        return orderID;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    // SET functions for DB
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "orderID=" + orderID +
                ", orderDate=" + orderDate +
                ", customerName='" + customerName + '\'' +
                ", customerContact='" + customerContact + '\'' +
                '}';
    }
}
