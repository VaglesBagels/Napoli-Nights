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
    public static Connection getInstance() {
        if (instance == null) {
            new SqliteConnection();
        }
        return instance;
    }
}