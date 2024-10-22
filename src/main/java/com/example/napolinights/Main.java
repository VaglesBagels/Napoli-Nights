package com.example.napolinights;

import com.example.napolinights.model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;



/**
 * Main application class for the Napoli Nights ordering system.
 * Handles the initial seeding of the database and opening the landing page.
 */
public class Main extends Application {

    /**
     * Main entry point for the JavaFX application.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * JavaFX start method. Sets up the primary stage and opens the landing page.
     * @param primaryStage the main window of the JavaFX application
     * @throws IOException if there is an issue loading the landing page FXML file
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Seed the database with initial data
        seedDatabase();

        // Open the landing page
        openLandingPage(primaryStage);
    }

    /**
     * Seeds the database with initial data for users, menu items, and orders.
     * This includes creating necessary tables and inserting data from CSV files.
     */
    private void seedDatabase() {
        // Get a connection to the SQLite database
        Connection connection = SqliteConnection.getInstance();

        // Create DAO objects for database operations
        UserDAO userDAO = new UserDAO(connection);
        MenuItemDAO menuItemDAO = new MenuItemDAO(connection);
        OrderDAO orderDAO = new OrderDAO(connection);
        CSVReader csvReader = new CSVReader();

        // Initialize the user, menu, and order tables in the database
        userDAO.createUserTable();
        menuItemDAO.createMenuTable();
        orderDAO.createOrdersTable();
        orderDAO.createOrderItemsTable();

        // Seed user data from a CSV file and insert it into the database
        List<User> users = csvReader.readUserDataFromCSV("user_data.csv");
        for (User user : users) {
            try {
                userDAO.addUser(user);
            } catch (SQLException sqlEx) {
                System.out.println("Error occurred while adding user to database.");
                System.out.println(sqlEx.getMessage());
            }
        }

        // Seed menu item data from a CSV file and insert it into the database
        List<MenuItem> menuItems = csvReader.readMenuFromCSV("menu_data.csv");
        for (MenuItem menuItem : menuItems) {
            menuItemDAO.addMenuItem(menuItem);
        }

        // Optionally print all menu items to verify insertion
        System.out.println("All Menu Items:");
        List<MenuItem> menu = menuItemDAO.fetchAllMenuItems();
        for (MenuItem menuItem : menu) {
            System.out.println(menuItem);
        }
    }


    /**
     * Loads and opens the landing page of the application.
     * Sets up the scene and displays it in the primary stage.
     * @param primaryStage the main window of the application
     * @throws IOException if there is an issue loading the FXML file for the landing page
     */
    private void openLandingPage(Stage primaryStage) throws IOException {
        // Load the FXML file for the landing page
        Parent root = FXMLLoader.load(getClass().getResource("/view/LandingPage.fxml"));

        // Set up the scene and display the landing page
        primaryStage.setTitle("Napoli Pizza");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
