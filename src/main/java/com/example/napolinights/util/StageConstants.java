package com.example.napolinights.util;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Utility class for setting consistent stage properties across the application.
 */
public class StageConstants {

    // Constant for GST (Goods and Services Tax)
    public static final double GST = 0.1;  // Define GST as 10%

    private static final double DEFAULT_MIN_WIDTH = 800;
    private static final double DEFAULT_MIN_HEIGHT = 600;

    /**
     * Set the minimum width and height for the given stage.
     *
     * @param stage the stage to set the size for.
     */
    public static void setStageSize(Stage stage) {
        stage.setMinWidth(DEFAULT_MIN_WIDTH);
        stage.setMinHeight(DEFAULT_MIN_HEIGHT);
    }

    /**
     * Configures a stage with a consistent size and sets its scene.
     *
     * @param stage The stage to configure.
     * @param root  The root node of the scene.
     * @param title The title of the window.
     */
    public static void configureStage(Stage stage, Parent root, String title) {
        stage.setTitle(title);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        setStageSize(stage);
        stage.show();
    }

    /**
     * Opens a page based on the specified FXML file and sets the stage title.
     * Uses the StageConstants utility to ensure consistent stage size settings.
     *
     * @param fxmlPath Path to the FXML file.
     * @param stage The current stage where the scene will be set.
     * @param title The title of the window.
     */
    public static void openPage(String fxmlPath, Stage stage, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(StageConstants.class.getResource(fxmlPath));
            Parent page = loader.load();
            configureStage(stage, page, title);
        } catch (IOException e) {
            System.out.println("Error occurred while opening the " + title + " page.");
            e.printStackTrace();
        }
    }
}
