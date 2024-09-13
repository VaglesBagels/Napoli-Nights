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
}
