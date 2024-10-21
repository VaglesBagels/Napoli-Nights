package com.example.napolinights.controller;

import com.example.napolinights.model.Order;

public class OrderService {
    private static Order confirmedOrder;

    public static void setConfirmedOrder(Order order) {
        confirmedOrder = order;
    }

    public static Order getConfirmedOrder() {
        return confirmedOrder;
    }

}
