package com.example.napolinights.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    public List<MenuItem> readMenuFromCSV(String fileName) {
        List<MenuItem> menuItemList = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            // Skip header line
            String line = reader.readLine(); // Read and discard header line

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                if (values.length == 5) {
                    // Assuming the columns in the CSV are: Category, Name, Description, Price, ImageURL
                    String categoryString = values[0].trim().toUpperCase();
                    Category category;
                    try {
                        category = Category.valueOf(categoryString);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid category in CSV: " + categoryString);
                        continue; // Skip this entry if category is invalid
                    }

                    String name = values[1].trim();
                    String description = values[2].trim().replace("\"", "");
                    double price;
                    try {
                        price = Double.parseDouble(values[3].trim());
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid price in CSV: " + values[3]);
                        continue; // Skip this entry if price is invalid
                    }
                    String imageURL = values[4].trim();

                    MenuItem menuItem = new MenuItem(category, name, description, price, imageURL);
                    menuItemList.add(menuItem);
                } else {
                    System.err.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return menuItemList;
    }

    public List<User> readUserDataFromCSV(String fileName) {
        List<User> userList = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            // Skip header line
            String line = reader.readLine(); // Read and discard header line

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                if (values.length == 7) {
                    // Assuming the columns in the CSV are: FirstName, LastName, Mobile, Email, Password, UserRole, Status
                    String firstName = values[0].trim();
                    String lastName = values[1].trim();
                    String mobile = values[2].trim();
                    String email = values[3].trim();
                    String password = values[4].trim();
                    String userRole = values[5].trim();
                    boolean status;
                    try {
                        status = Boolean.parseBoolean(values[6].trim());
                    } catch (Exception e) {
                        System.err.println("Invalid status in CSV: " + values[6]);
                        continue; // Skip this entry if status is invalid
                    }

                    User user = new User(0, firstName, lastName, mobile, email, password, userRole, status);
                    userList.add(user);
                } else {
                    System.err.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userList;
    }

}
