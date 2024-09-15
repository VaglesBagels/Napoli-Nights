package com.example.napolinights;

@FunctionalInterface
public interface ItemSelectionCallback {
    void onItemSelected(String itemName, String itemPrice, Integer itemQty);
}
