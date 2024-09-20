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
        seedDatabase();

        openLandingPage(primaryStage);
    }
    public static void main(String[] args) {
        launch(args);
    }

    private void seedDatabase() {
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
        List<User> users = csvReader.readUserDataFromCSV("user_data.csv");

        // Insert the User data into the database
        for (User user : users) {
            try {
                userDAO.addUser(user);
            } catch (SQLException sqlEx) {
                System.out.println("Error occurred while adding user to database.");
                System.out.println(sqlEx.getMessage());
            }
        }


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


    private void openLandingPage(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/napolinights/LandingPage.fxml"));
        primaryStage.setTitle("Napoli Pizza");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
