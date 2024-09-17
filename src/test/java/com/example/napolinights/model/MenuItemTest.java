package com.example.napolinights.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuItemTest {

    @Test
    void testConstructor() {
        MenuItem menuItem = new MenuItem(Category.PIZZA, "Margherita", "Classic Pizza", 9.99, "image.jpg");

        assertEquals(Category.PIZZA, menuItem.getCategory());
        assertEquals("Margherita", menuItem.getName());
        assertEquals("Classic Pizza", menuItem.getDescription());
        assertEquals(9.99, menuItem.getPrice());
        assertEquals("image.jpg", menuItem.getImageURL());
    }

    @Test
    void testConstructorWithMenuItemID() {
        MenuItem menuItem = new MenuItem(1, Category.PIZZA, "Margherita", "Classic Pizza", 9.99, "image.jpg");

        assertEquals(1, menuItem.getMenuItemID());
        assertEquals(Category.PIZZA, menuItem.getCategory());
        assertEquals("Margherita", menuItem.getName());
        assertEquals("Classic Pizza", menuItem.getDescription());
        assertEquals(9.99, menuItem.getPrice());
        assertEquals("image.jpg", menuItem.getImageURL());
    }

    @Test
    void testSetNameValid() {
        MenuItem menuItem = new MenuItem(Category.PIZZA, "Margherita", "Classic Pizza", 9.99, "image.jpg");
        menuItem.setName("Pepperoni");

        assertEquals("Pepperoni", menuItem.getName());
    }

    @Test
    void testSetNameInvalid() {
        MenuItem menuItem = new MenuItem(Category.PIZZA, "Margherita", "Classic Pizza", 9.99, "image.jpg");

        assertThrows(IllegalArgumentException.class, () -> menuItem.setName(null));
        assertThrows(IllegalArgumentException.class, () -> menuItem.setName(""));
    }

    @Test
    void testSetPriceValid() {
        MenuItem menuItem = new MenuItem(Category.PIZZA, "Margherita", "Classic Pizza", 9.99, "image.jpg");
        menuItem.setPrice(12.99);

        assertEquals(12.99, menuItem.getPrice());
    }

    @Test
    void testSetPriceInvalid() {
        MenuItem menuItem = new MenuItem(Category.PIZZA, "Margherita", "Classic Pizza", 9.99, "image.jpg");

        assertThrows(IllegalArgumentException.class, () -> menuItem.setPrice(-1));
    }

    @Test
    void testSetCategoryValid() {
        MenuItem menuItem = new MenuItem(Category.PIZZA, "Margherita", "Classic Pizza", 9.99, "image.jpg");
        menuItem.setCategory(Category.DESSERT);

        assertEquals(Category.DESSERT, menuItem.getCategory());
    }

    @Test
    void testSetCategoryInvalid() {
        MenuItem menuItem = new MenuItem(Category.PIZZA, "Margherita", "Classic Pizza", 9.99, "image.jpg");

        assertThrows(IllegalArgumentException.class, () -> menuItem.setCategory(null));
    }

    @Test
    void testToString() {
        MenuItem menuItem = new MenuItem(1, Category.PIZZA, "Margherita", "Classic Pizza", 9.99, "image.jpg");
        String expected = "MenuItem{menuItemID=1, category=PIZZA, name='Margherita', description='Classic Pizza', price=9.99, imageURL='image.jpg'}";

        assertEquals(expected, menuItem.toString());
    }
}
