package com.example.napolinights;

import com.example.napolinights.model.*;
import com.example.napolinights.model.alt.UserDAO;

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
            MenuDAO menuDAO = new MenuDAO();
            CSVReader csvReader = new CSVReader();

            userDAO.createUserTable();
            menuDAO.createMenuTable();


            // Read data from CSV
            List<MenuItem> menuItems = csvReader.readMenuFromCSV("menu_data.csv");

            // Insert the Menu data into the database
            for (MenuItem menuItem : menuItems) {
                menuDAO.insert(menuItem);
            }

            // Optionally, retrieve and print all records to verify insertion
            System.out.println("All Menu Items:");
            List<MenuItem> allMenuItems = menuDAO.getAll();
            for (MenuItem menuItem : allMenuItems) {
                System.out.println(menuItem);
            }

            // Insert new records
            menuDAO.insert(new MenuItem(Category.PIZZA, "Margherita", "Tomato, mozzarella, grana padano parmesan, cherry tomato, basil, garlic", 24.00, "margherita.jpg"));

            // Retrieve all records
            System.out.println("List after using menuDAO.insert (inserting new entry)");
            List<MenuItem> currentMenuItems = menuDAO.getAll();
            for (MenuItem menuItem : currentMenuItems) {
                System.out.println(menuItem);
            }

            // Retrieve a record by ID
            System.out.println("Retrieve by ID 1");
            MenuItem menuItem = menuDAO.getById(1);
            if (menuItem != null) {
                System.out.println(menuItem);
            } else {
                System.out.println("Menu item not found.");
            }

            // Update the record
            if (menuItem != null) {
                menuItem.setPrice(27.50);
                menuItem.setDescription("Updated description");
                menuDAO.update(menuItem);
                System.out.println("After update:");
                System.out.println(menuDAO.getById(1));
            } else {
                System.out.println("Menu item not found.");
            }

            // Delete a record
            menuDAO.delete(1);

            System.out.println("After deleting record with menuID = 1:");
            menuItems = menuDAO.getAll();
            for (MenuItem remainMenuItem : menuItems) {
                System.out.println(remainMenuItem);
            }

            menuDAO.close();
        }
    }