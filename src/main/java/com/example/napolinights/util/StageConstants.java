package com.example.napolinights.util;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Utility class for setting consistent stage properties across the application.
 */
public class StageConstants {

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
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.show();
    }
}
