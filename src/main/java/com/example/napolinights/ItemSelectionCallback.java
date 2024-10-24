package com.example.napolinights;

@FunctionalInterface
public interface ItemSelectionCallback {
    void onItemSelected(String itemId, String itemName, String itemPrice, Integer itemQty);
}
