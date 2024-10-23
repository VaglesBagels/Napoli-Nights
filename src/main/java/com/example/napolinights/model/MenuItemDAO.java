package com.example.napolinights.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code MenuItemDAO} class provides methods to interact with the menu items in the database.
 * It implements the {@link IMenuItemDAO} interface to perform CRUD operations on menu items.
 */
public class MenuItemDAO implements IMenuItemDAO {
    private final Connection connection;

    /**
     * Constructs a {@code MenuItemDAO} with the specified database connection.
     *
     * @param connection the database connection to use for operations
     */
    public MenuItemDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Creates the {@code menu} table in the database if it does not already exist.
     *
     * @return {@code true} if the table is created successfully for the first time;
     *         {@code false} if the table already exists or an error occurs during the creation.
     */
    public boolean createMenuTable() {
        String checkTableExistsSQL = "SELECT name FROM sqlite_master WHERE type='table' AND name='menu'";
        String createTableSQL = "CREATE TABLE IF NOT EXISTS menu ("
                + "menuID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "category TEXT NOT NULL, "
                + "name TEXT NOT NULL, "
                + "description TEXT, "
                + "price DOUBLE, "
                + "imageURL TEXT"
                + ")";
        try {
            // Check if the table already exists
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(checkTableExistsSQL);
            if (rs.next()) {
                // Table already exists
                System.out.println("Menu table already exists.");
                return false;
            }

            // Table does not exist, so create it
            stmt.execute(createTableSQL);
            System.out.println("Menu table created.");
            return true;
        } catch (SQLException ex) {
            System.err.println("Error creating menu table: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Adds a new menu item to the database.
     *
     * @param menuItem the {@link MenuItem} object representing the menu item to be added.
     */
    public void addMenuItem(MenuItem menuItem) {
        String sql = "INSERT INTO menu (category, name, description, price, imageURL) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, menuItem.getCategory().name());
            pstmt.setString(2, menuItem.getName());
            pstmt.setString(3, menuItem.getDescription());
            pstmt.setDouble(4, menuItem.getPrice());
            pstmt.setString(5, menuItem.getImageURL());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error adding menu item: " + ex.getMessage());
        }
    }

    /**
     * Updates an existing menu item in the database.
     *
     * @param menuItem the {@link MenuItem} object containing updated information.
     */
    public void updateMenuItem(MenuItem menuItem) {
        String sql = "UPDATE menu SET category = ?, name = ?, description = ?, price = ?, imageURL = ? WHERE menuID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, menuItem.getCategory().name());
            pstmt.setString(2, menuItem.getName());
            pstmt.setString(3, menuItem.getDescription());
            pstmt.setDouble(4, menuItem.getPrice());
            pstmt.setString(5, menuItem.getImageURL());
            pstmt.setInt(6, menuItem.getMenuItemID());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error updating menu item: " + ex.getMessage());
        }
    }

    /**
     * Fetches all menu items from the database.
     *
     * @return a {@code List} of all {@link MenuItem} objects in the database.
     */
    public List<MenuItem> fetchAllMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = "SELECT * FROM menu";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                menuItems.add(new MenuItem(
                        rs.getInt("menuID"),
                        Category.valueOf(rs.getString("category")),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getString("imageURL")
                ));
            }
        } catch (SQLException ex) {
            System.err.println("Error fetching menu items: " + ex.getMessage());
        }
        return menuItems;
    }

    /**
     * Fetches all menu items from the database that belong to the specified category.
     *
     * @param category the {@link Category} to filter the menu items.
     * @return a {@code List} of {@link MenuItem} objects in the specified category.
     */
    @Override
    public List<MenuItem> fetchAllMenuItemsByCategory(Category category) {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = "SELECT * FROM menu WHERE category = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, category.name());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    menuItems.add(new MenuItem(
                            rs.getInt("menuID"),
                            category,
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getDouble("price"),
                            rs.getString("imageURL")
                    ));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error fetching menu items by category: " + ex.getMessage());
        }
        return menuItems;
    }

    /**
     * Fetches a menu item by its ID.
     *
     * @param id the ID of the menu item to be fetched.
     * @return the {@link MenuItem} with the specified ID, or {@code null} if not found.
     */
    public MenuItem fetchMenuItemById(int id) {
        String sql = "SELECT * FROM menu WHERE menuID = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Category category = Category.valueOf(rs.getString("category"));
                    return new MenuItem(
                            rs.getInt("menuID"),
                            category,
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getDouble("price"),
                            rs.getString("imageURL")
                    );
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error fetching menu item by ID: " + ex.getMessage());
        }
        return null;
    }

    /**
     * Closes the database connection used by this DAO.
     */
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.err.println("Error closing connection: " + ex.getMessage());
        }
    }
}
