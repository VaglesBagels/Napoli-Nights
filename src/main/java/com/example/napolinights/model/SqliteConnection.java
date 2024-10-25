package com.example.napolinights.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class to manage a SQLite database connection.
 * Ensures a single instance of the database connection throughout the application.
 */
public class SqliteConnection {

    private static final String URL = "jdbc:sqlite:database.db"; // SQLite database file URL.
    private static Connection instance = null;

    private SqliteConnection() {
        try {
            instance = DriverManager.getConnection(URL);
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx);
        }
    }

    /**
     * Provides the singleton instance of the database connection.
     * If the connection does not exist, it initializes a new one.
     *
     * @return The singleton instance of the `Connection` object.
     */
    /*public static Connection getInstance() {
        if (instance == null) {
            new SqliteConnection();
        }
        return instance;
    }*/
    public static Connection getInstance() {
        try {
            // Check if the connection is null or closed, and create a new one if necessary
            if (instance == null || instance.isClosed()) {
                new SqliteConnection();
            }
        } catch (SQLException sqlEx) {
            System.err.println("Error getting database connection: " + sqlEx.getMessage());
        }
        return instance;
    }

    /**
     * Closes the SQLite connection if it is open.
     */
    public static void closeConnection() {
        try {
            if (instance != null && !instance.isClosed()) {
                instance.close();
                System.out.println("SQLite connection has been closed.");
                instance = null;  // Set instance to null to indicate it's closed
            }
        } catch (SQLException sqlEx) {
            System.err.println("Error closing database connection: " + sqlEx.getMessage());
        }
    }

}