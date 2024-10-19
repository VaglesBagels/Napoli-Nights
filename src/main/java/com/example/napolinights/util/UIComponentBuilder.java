package com.example.napolinights.util;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

/**
 * Utility class for creating common UI components.
 */
public class UIComponentBuilder {

    /**
     * Creates a styled label.
     *
     * @param text  The text for the label.
     * @param style The CSS style string for the label.
     * @return A new Label with the given text and style.
     */
    public static Label createLabel(String text, String style) {
        Label label = new Label(text);
        label.setStyle(style);
        return label;
    }

    /**
     * Creates an ImageView for a menu item, using a default image if none is provided.
     *
     * @param imageUrl The URL of the image to load.
     * @return A new ImageView with the given image URL.
     */
    public static ImageView createImageView(String imageUrl) {
        String imagePath = getMenuItemImageOrDefault(imageUrl);
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        return imageView;
    }

    /**
     * Returns the image URL or a default image if none is available.
     *
     * @param imageUrl The URL of the image.
     * @return A valid image URL, or a fallback image if the original is not available.
     */
    public static String getMenuItemImageOrDefault(String imageUrl) {
        if (imageUrl != null && !imageUrl.trim().isEmpty() && !imageUrl.equalsIgnoreCase("null")) {
            URL menuItemImageUrl = UIComponentBuilder.class.getResource("/com/example/napolinights/images/menu_images/" + imageUrl);
            if (menuItemImageUrl != null) {
                return menuItemImageUrl.toExternalForm();
            }
        }
        return UIComponentBuilder.class.getResource("/com/example/napolinights/images/menu_images/no_image_found.png").toExternalForm();
    }
}
