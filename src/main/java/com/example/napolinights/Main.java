package com.example.napolinights;

import com.example.napolinights.model.*;

import java.sql.Connection;
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
        public static void main(String[] args) {
            Connection connection = DatabaseConnection.getInstance();

            MenuDAO menuDAO = new MenuDAO();
            CSVReader csvReader = new CSVReader();

            menuDAO.createMenuTable();

            // Read data from CSV
            List<Menu> menus = csvReader.readMenuFromCSV("menu_data.csv");

            // Insert the Menu data into the database
            for (Menu menu : menus) {
                menuDAO.insert(menu);
            }

            // Optionally, retrieve and print all records to verify insertion
            System.out.println("All Menu Items:");
            List<Menu> allMenus = menuDAO.getAll();
            for (Menu menu : allMenus) {
                System.out.println(menu);
            }

            // Insert new records
            menuDAO.insert(new Menu(Category.PIZZA, "Margherita", "Tomato, mozzarella, grana padano parmesan, cherry tomato, basil, garlic", 24.00, "margherita.jpg"));

            // Retrieve all records
            System.out.println("List after using menuDAO.insert (inserting new entry)");
            List<Menu> currentMenus = menuDAO.getAll();
            for (Menu menu : currentMenus) {
                System.out.println(menu);
            }

            // Retrieve a record by ID
            System.out.println("Retrieve by ID 1");
            Menu menu = menuDAO.getById(1);
            if (menu != null) {
                System.out.println(menu);
            } else {
                System.out.println("Menu item not found.");
            }

            // Update the record
            if (menu != null) {
                menu.setPrice(27.50);
                menu.setDescription("Updated description");
                menuDAO.update(menu);
                System.out.println("After update:");
                System.out.println(menuDAO.getById(1));
            } else {
                System.out.println("Menu item not found.");
            }

            // Delete a record
            menuDAO.delete(1);

            System.out.println("After deleting record with menuID = 1:");
            menus = menuDAO.getAll();
            for (Menu remainMenu : menus) {
                System.out.println(remainMenu);
            }

            menuDAO.close();
        }
    }