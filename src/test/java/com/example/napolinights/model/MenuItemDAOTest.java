package com.example.napolinights.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;

public class MenuItemDAOTest {

    private Connection mockConnection;
    private MenuItemDAO menuItemDAO;
    private PreparedStatement mockPreparedStatement;
    private Statement mockStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockStatement = mock(Statement.class);
        mockResultSet = mock(ResultSet.class);

        menuItemDAO = new MenuItemDAO(mockConnection);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @Test
    public void testCreateMenuTable() throws SQLException {
        // Mock the ResultSet to simulate table existence check
        ResultSet mockResultSet = mock(ResultSet.class);

        // When the result set is queried, simulate that the table doesn't exist
        when(mockResultSet.next()).thenReturn(false); // Simulate table doesn't exist

        // Mock the Statement
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

        // Call the method being tested
        boolean result = menuItemDAO.createMenuTable();

        // Verify that the table creation statement was executed since the table doesn't exist
        verify(mockStatement, times(1)).execute(anyString());

        // Assert that the result is true, meaning the table was created
        assertTrue(result);
    }

    @Test
    public void testCreateMenuTableWhenTableExists() throws SQLException {
        // Mock the ResultSet to simulate table existence check
        ResultSet mockResultSet = mock(ResultSet.class);

        // Simulate the table already exists
        when(mockResultSet.next()).thenReturn(true);

        // Mock the Statement
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

        // Call the method being tested
        boolean result = menuItemDAO.createMenuTable();

        // Verify that the creation SQL statement was not executed since the table already exists
        verify(mockStatement, times(0)).execute(anyString());

        // Assert that the result is false, meaning the table already existed
        assertFalse(result);
    }

    @Test
    public void testAddMenuItem() throws Exception {
        MenuItem menuItem = new MenuItem(1, Category.PIZZA, "Margherita", "Classic pizza", 9.99, "url");
        menuItemDAO.addMenuItem(menuItem);

        verify(mockPreparedStatement, times(1)).setString(1, "PIZZA");
        verify(mockPreparedStatement, times(1)).setString(2, "Margherita");
        verify(mockPreparedStatement, times(1)).setString(3, "Classic pizza");
        verify(mockPreparedStatement, times(1)).setDouble(4, 9.99);
        verify(mockPreparedStatement, times(1)).setString(5, "url");
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testUpdateMenuItem() throws Exception {
        MenuItem menuItem = new MenuItem(1, Category.PIZZA, "Margherita", "Classic pizza", 9.99, "url");
        menuItemDAO.updateMenuItem(menuItem);

        verify(mockPreparedStatement, times(1)).setString(1, "PIZZA");
        verify(mockPreparedStatement, times(1)).setString(2, "Margherita");
        verify(mockPreparedStatement, times(1)).setString(3, "Classic pizza");
        verify(mockPreparedStatement, times(1)).setDouble(4, 9.99);
        verify(mockPreparedStatement, times(1)).setString(5, "url");
        verify(mockPreparedStatement, times(1)).setInt(6, 1);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testFetchAllMenuItems() throws Exception {
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false); // Simulate one item in result set
        when(mockResultSet.getInt("menuID")).thenReturn(1);
        when(mockResultSet.getString("category")).thenReturn("PIZZA");
        when(mockResultSet.getString("name")).thenReturn("Margherita");
        when(mockResultSet.getString("description")).thenReturn("Classic pizza");
        when(mockResultSet.getDouble("price")).thenReturn(9.99);
        when(mockResultSet.getString("imageURL")).thenReturn("url");

        List<MenuItem> menuItems = menuItemDAO.fetchAllMenuItems();
        assertEquals(1, menuItems.size());
        assertEquals("Margherita", menuItems.get(0).getName());
    }

    @Test
    void testFetchMenuItemById() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("menuID")).thenReturn(1);
        when(mockResultSet.getString("category")).thenReturn("PIZZA");
        when(mockResultSet.getString("name")).thenReturn("Margherita");
        when(mockResultSet.getString("description")).thenReturn("Classic pizza");
        when(mockResultSet.getDouble("price")).thenReturn(9.99);
        when(mockResultSet.getString("imageURL")).thenReturn("url");

        MenuItem menuItem = menuItemDAO.fetchMenuItemById(1);
        assertNotNull(menuItem);
        assertEquals("Margherita", menuItem.getName());
    }

    @Test
    public void testFetchMenuItemByIdNotFound() throws Exception {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // simulate no rows found

        MenuItem item = menuItemDAO.fetchMenuItemById(1);

        assertNull(item);
    }

    @Test
    void testFetchAllMenuItemsByCategory() throws SQLException {
        Category category = Category.PIZZA;
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false); // Simulate one item in result set
        when(mockResultSet.getInt("menuID")).thenReturn(1);
        when(mockResultSet.getString("category")).thenReturn(category.name());
        when(mockResultSet.getString("name")).thenReturn("Margherita");
        when(mockResultSet.getString("description")).thenReturn("Classic pizza");
        when(mockResultSet.getDouble("price")).thenReturn(9.99);
        when(mockResultSet.getString("imageURL")).thenReturn("url");

        List<MenuItem> menuItems = menuItemDAO.fetchAllMenuItemsByCategory(category);
        assertEquals(1, menuItems.size());
        assertEquals("Margherita", menuItems.get(0).getName());
    }

    @Test
    void testFetchAllMenuItemsByCategory_NoResults() throws SQLException {
        Category category = Category.PIZZA;
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // Simulate no items in result set

        List<MenuItem> menuItems = menuItemDAO.fetchAllMenuItemsByCategory(category);
        assertTrue(menuItems.isEmpty());
    }

    @Test
    void testFetchAllMenuItemsByCategory_SQLException() throws SQLException {
        Category category = Category.PIZZA;
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Database error"));

        List<MenuItem> menuItems = menuItemDAO.fetchAllMenuItemsByCategory(category);
        assertTrue(menuItems.isEmpty()); // Expected to return an empty list on SQL exception
        // You can also check if the exception is logged if you have a logger setup in your DAO
    }

    private void mockResultSetForSingleMenuItem(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false); // simulate one row
        when(resultSet.getInt("menuID")).thenReturn(1);
        when(resultSet.getString("category")).thenReturn("PIZZA");
        when(resultSet.getString("name")).thenReturn("Margherita");
        when(resultSet.getString("description")).thenReturn("Classic cheese pizza");
        when(resultSet.getDouble("price")).thenReturn(8.99);
        when(resultSet.getString("imageURL")).thenReturn("imageURL");
    }


    private void assertMenuItemEquals(MenuItem item, int expectedId, String expectedCategory,
                                      String expectedName, String expectedDescription,
                                      double expectedPrice, String expectedImageURL) {
        assertEquals(expectedId, item.getMenuItemID());
        assertEquals(Category.valueOf(expectedCategory), item.getCategory());
        assertEquals(expectedName, item.getName());
        assertEquals(expectedDescription, item.getDescription());
        assertEquals(expectedPrice, item.getPrice());
        assertEquals(expectedImageURL, item.getImageURL());
    }
}
