package com.example.napolinights.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class MenuItemDAOTest {

    private Connection connection;
    private MenuItemDAO menuItemDAO;

    @BeforeEach
    public void setUp() {
        connection = mock(Connection.class);
        menuItemDAO = new MenuItemDAO(connection);
    }

    @Test
    public void testCreateTableIfNotExists() throws Exception {
        Statement statement = mock(Statement.class);
        when(connection.createStatement()).thenReturn(statement);

        menuItemDAO.createMenuTable();

        verify(statement).execute("CREATE TABLE IF NOT EXISTS menu ("
                + "menuID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "category TEXT NOT NULL, "
                + "name TEXT NOT NULL, "
                + "description TEXT, "
                + "price DOUBLE, "
                + "imageURL TEXT"
                + ")");
    }

    @Test
    public void testAddMenuItem() throws Exception {
        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(connection.prepareStatement(any(String.class))).thenReturn(pstmt);

        MenuItem menuItem = new MenuItem(1, Category.PIZZA, "Margherita", "Classic cheese pizza", 8.99, "imageURL");
        menuItemDAO.addMenuItem(menuItem);

        verify(pstmt).setString(1, "PIZZA");
        verify(pstmt).setString(2, "Margherita");
        verify(pstmt).setString(3, "Classic cheese pizza");
        verify(pstmt).setDouble(4, 8.99);
        verify(pstmt).setString(5, "imageURL");
        verify(pstmt).executeUpdate();
    }

    @Test
    public void testUpdateMenuItem() throws Exception {
        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(connection.prepareStatement(any(String.class))).thenReturn(pstmt);

        MenuItem menuItem = new MenuItem(1, Category.PIZZA, "Margherita", "Updated description", 9.99, "newImageURL");
        menuItemDAO.updateMenuItem(menuItem);

        verify(pstmt).setString(1, "PIZZA");
        verify(pstmt).setString(2, "Margherita");
        verify(pstmt).setString(3, "Updated description");
        verify(pstmt).setDouble(4, 9.99);
        verify(pstmt).setString(5, "newImageURL");
        verify(pstmt).setInt(6, 1);
        verify(pstmt).executeUpdate();
    }

    @Test
    public void testRemoveMenuItemById() throws Exception {
        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(connection.prepareStatement(any(String.class))).thenReturn(pstmt);

        menuItemDAO.removeMenuItemById(1);

        verify(pstmt).setInt(1, 1);
        verify(pstmt).executeUpdate();
    }

    @Test
    public void testFetchAllMenuItems() throws Exception {
        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery("SELECT * FROM menu")).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true).thenReturn(false); // simulate one row
        when(resultSet.getInt("menuID")).thenReturn(1);
        when(resultSet.getString("category")).thenReturn("PIZZA");
        when(resultSet.getString("name")).thenReturn("Margherita");
        when(resultSet.getString("description")).thenReturn("Classic cheese pizza");
        when(resultSet.getDouble("price")).thenReturn(8.99);
        when(resultSet.getString("imageURL")).thenReturn("imageURL");

        List<MenuItem> items = menuItemDAO.fetchAllMenuItems();

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(1, items.get(0).getMenuItemID());
        assertEquals(Category.PIZZA, items.get(0).getCategory());
        assertEquals("Margherita", items.get(0).getName());
        assertEquals("Classic cheese pizza", items.get(0).getDescription());
        assertEquals(8.99, items.get(0).getPrice());
        assertEquals("imageURL", items.get(0).getImageURL());
    }

    @Test
    public void testFetchMenuItemById() throws Exception {
        PreparedStatement pstmt = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(connection.prepareStatement(any(String.class))).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true).thenReturn(false); // simulate one row
        when(resultSet.getInt("menuID")).thenReturn(1);
        when(resultSet.getString("category")).thenReturn("PIZZA");
        when(resultSet.getString("name")).thenReturn("Margherita");
        when(resultSet.getString("description")).thenReturn("Classic cheese pizza");
        when(resultSet.getDouble("price")).thenReturn(8.99);
        when(resultSet.getString("imageURL")).thenReturn("imageURL");

        MenuItem item = menuItemDAO.fetchMenuItemById(1);

        assertNotNull(item);
        assertEquals(1, item.getMenuItemID());
        assertEquals(Category.PIZZA, item.getCategory());
        assertEquals("Margherita", item.getName());
        assertEquals("Classic cheese pizza", item.getDescription());
        assertEquals(8.99, item.getPrice());
        assertEquals("imageURL", item.getImageURL());
    }

    @Test
    public void testFetchMenuItemByIdNotFound() throws Exception {
        PreparedStatement pstmt = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(connection.prepareStatement(any(String.class))).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(false); // simulate no rows found

        MenuItem item = menuItemDAO.fetchMenuItemById(1);

        assertNull(item);
    }
}
