package com.example.napolinights.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDAO implements IMenuItemDAO {
    private final Connection connection;

    public MenuItemDAO(Connection connection) {
        this.connection = connection;
    }

    public void createMenuTable() {
        String sql = "CREATE TABLE IF NOT EXISTS menu ("
                + "menuID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "category TEXT NOT NULL, "
                + "name TEXT NOT NULL, "
                + "description TEXT, "
                + "price DOUBLE, "
                + "imageURL TEXT"
                + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("Menu table created");
        } catch (SQLException ex) {
            System.err.println("Error creating menu table: " + ex.getMessage());
        }
    }

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

    public List<MenuItem> fetchAllMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = "SELECT * FROM menu";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Category category = Category.valueOf(rs.getString("category"));
                menuItems.add(new MenuItem(
                        rs.getInt("menuID"),
                        category,
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

    @Override
    public List<MenuItem> fetchAllMenuItemsByCategory(Category category) {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = "SELECT * FROM menu WHERE category = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
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


    public MenuItem fetchMenuItemById(int id) {
        String sql = "SELECT * FROM menu WHERE menuID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
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

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.err.println("Error closing connection: " + ex.getMessage());
        }
    }
}