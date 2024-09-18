package com.example.napolinights;

import com.example.napolinights.model.*;
import com.example.napolinights.model.UserDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static javafx.application.Application.launch;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // This method creates the seed data for this application.
        // Uncomment this below function after you have seeded your local database
        // seedDatabase();

        openLandingPage(primaryStage);
    }
    public static void main(String[] args) {
        launch(args);
    }

    private void seedDatabase() {
        Connection connection = SqliteConnection.getInstance();

        UserDAO userDAO = new UserDAO(connection);
        CSVReader csvReader = new CSVReader();

        /*// Initialise Tables
        userDAO.createUserTable();
        // menuDAO.createMenuTable();

        // Read data from CSV
        List<User> users = csvReader.readUserDataFromCSV("user_data.csv");

        // Insert the User data into the database
        for (User user : users) {
            try {
                userDAO.addUser(user);
            } catch (SQLException sqlEx) {
                System.out.println("Error occurred while adding user to database.");
                System.out.println(sqlEx.getMessage());
            }
        }*/

        // List<Menu> menus = csvReader.readMenuFromCSV("menu_data.csv");

        // Insert the Menu data into the database
        /*for (Menu menu : menus) {
            menuDAO.insert(menu);
        }*/

        // Optionally, retrieve and print all records to verify insertion
        /*System.out.println("All Menu Items:");
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
        }*/

        // Retrieve a record by ID
        /*System.out.println("Retrieve by ID 1");
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

        menuDAO.close();*/
    }

    private void openLandingPage(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/napolinights/LandingPage.fxml"));
        primaryStage.setTitle("Napoli Pizza");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
