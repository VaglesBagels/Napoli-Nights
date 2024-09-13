package com.example.napolinights.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Data Access Object (DAO) class for handling database operations related to the `Menu` table.
 * This class provides methods to create, read, update, and delete (CRUD) menu items in the database.
 */
public class MenuDAO implements IMenuDAO {
    private final Connection connection;

    public MenuDAO() {
        connection = DatabaseConnection.getInstance();
    }

    public void createMenuTable() {
        try {
            Statement createMenuTable = connection.createStatement();
            createMenuTable.execute(
                    "CREATE TABLE IF NOT EXISTS menu ("
                            + "menuID INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "category TEXT NOT NULL, "
                            + "name TEXT NOT NULL, "
                            + "description TEXT, "
                            + "price DOUBLE, "
                            + "imageURL TEXT"
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void insert(MenuItem menuItem) {
        try {
            String sql = "INSERT INTO menu (category, name, description, price, imageURL) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertMenu = connection.prepareStatement(sql);

            insertMenu.setString(1, menuItem.getCategory().name());
            insertMenu.setString(2, menuItem.getName());
            insertMenu.setString(3, menuItem.getDescription());
            insertMenu.setDouble(4, menuItem.getPrice());
            insertMenu.setString(5, menuItem.getImageURL());
            insertMenu.execute();

        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void update(MenuItem menuItem) {
        try {
            String sql = "UPDATE menu SET category = ?, name = ?, description = ?, price = ?, imageURL = ? WHERE menuID = ?";
            PreparedStatement updateMenu = connection.prepareStatement(sql);

            updateMenu.setString(1, menuItem.getCategory().name());
            updateMenu.setString(2, menuItem.getName());
            updateMenu.setString(3, menuItem.getDescription());
            updateMenu.setDouble(4, menuItem.getPrice());
            updateMenu.setString(5, menuItem.getImageURL());
            updateMenu.setInt(6, menuItem.getMenuItemID());
            updateMenu.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void delete(int id) {
        try {
            String sql = "DELETE FROM menu WHERE menuID = ?";
            PreparedStatement deleteMenu = connection.prepareStatement(sql);
            deleteMenu.setInt(1, id);
            deleteMenu.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public List<MenuItem> getAll() {
        List<MenuItem> menuItems = new ArrayList<>();
        try {
            Statement getAll = connection.createStatement();
            String sql = "SELECT * from menu";
            ResultSet rs = getAll.executeQuery(sql);

            while (rs.next()) {
                Category category = Category.valueOf(rs.getString("category"));
                menuItems.add(
                        new MenuItem(
                                rs.getInt("menuID"),
                                category,
                                rs.getString("name"),
                                rs.getString("description"),
                                rs.getDouble("price"),
                                rs.getString("imageURL")
                        )
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return menuItems;
    }

    public MenuItem getById(int id) {
        try {
            String sql = "SELECT * FROM menu WHERE menuID = ?";
            PreparedStatement getMenu = connection.prepareStatement(sql);
            getMenu.setInt(1, id);
            ResultSet rs = getMenu.executeQuery();

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
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
}