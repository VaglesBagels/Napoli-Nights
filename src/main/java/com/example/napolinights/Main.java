package com.example.napolinights;

import com.example.napolinights.model.*;
import com.example.napolinights.model.UserDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

//public class Main extends Application {
//    @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
//    }

    public class Main {
        public static void main(String[] args) throws SQLException {
            Connection connection = SqliteConnection.getInstance();

            UserDAO userDAO = new UserDAO(connection);
            MenuItemDAO menuItemDAO = new MenuItemDAO(connection);
            OrderDAO orderDAO = new OrderDAO(connection);
            CSVReader csvReader = new CSVReader();

            // Initialise Tables
            userDAO.createUserTable();
            menuItemDAO.createMenuTable();
            orderDAO.createOrdersTable();
            orderDAO.createOrderItemsTable();

            // Read data from CSV
            List<MenuItem> menuItems = csvReader.readMenuFromCSV("menu_data.csv");

            // Insert the Menu data into the database
            for (MenuItem menuItem : menuItems) {
                menuItemDAO.addMenuItem(menuItem);
            }

            // Optionally, retrieve and print all records to verify insertion
            System.out.println("All Menu Items:");
            List<MenuItem> menu = menuItemDAO.fetchAllMenuItems();
            for (MenuItem menuItem : menu) {
                System.out.println(menuItem);
            }
        }
    }
