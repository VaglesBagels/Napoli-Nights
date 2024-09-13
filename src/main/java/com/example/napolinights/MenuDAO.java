package com.example.napolinights;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {
    private Connection connection;

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

    public void insert(Menu menu) {
        try {
            String sql = "INSERT INTO menu (category, name, description, price, imageURL) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertMenu = connection.prepareStatement(sql);

            insertMenu.setString(1, menu.getCategory().name());
            insertMenu.setString(2, menu.getName());
            insertMenu.setString(3, menu.getDescription());
            insertMenu.setDouble(4, menu.getPrice());
            insertMenu.setString(5, menu.getImageURL());
            insertMenu.execute();

        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void update(Menu menu) {
        try {
            String sql = "UPDATE menu SET category = ?, name = ?, description = ?, price = ?, imageURL = ? WHERE menuID = ?";
            PreparedStatement updateMenu = connection.prepareStatement(sql);

            updateMenu.setString(1, menu.getCategory().name());
            updateMenu.setString(2, menu.getName());
            updateMenu.setString(3, menu.getDescription());
            updateMenu.setDouble(4, menu.getPrice());
            updateMenu.setString(5, menu.getImageURL());
            updateMenu.setInt(6, menu.getMenuID());
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

    public List<Menu> getAll() {
        List<Menu> menus = new ArrayList<>();
        try {
            Statement getAll = connection.createStatement();
            String sql = "SELECT * from menu";
            ResultSet rs = getAll.executeQuery(sql);

            while (rs.next()) {
                Category category = Category.valueOf(rs.getString("category"));
                menus.add(
                        new Menu(
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
            System.err.println(ex);
        }
        return menus;
    }

    public Menu getById(int id) {
        try {
            String sql = "SELECT * FROM menu WHERE menuID = ?";
            PreparedStatement getMenu = connection.prepareStatement(sql);
            getMenu.setInt(1, id);
            ResultSet rs = getMenu.executeQuery();

            if (rs.next()) {
                Category category = Category.valueOf(rs.getString("category"));
                return new Menu(
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