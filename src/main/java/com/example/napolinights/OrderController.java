package com.example.napolinights;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.List;

public class OrderController {

    @FXML
    private Hyperlink lnkEntree;

    @FXML
    private Hyperlink lnkMains;

    @FXML
    private AnchorPane contentAnchorPane;

    @FXML
    private VBox entreeSection;

    @FXML
    private VBox mainsSection;

    @FXML
    private void initialize() {
        System.out.println("Initialize Menu Controller");
        lnkEntree.setOnAction(event -> showSection(entreeSection));
        lnkMains.setOnAction(event -> showSection(mainsSection));
    }

    private void showSection(VBox sectionToShow) {
        System.out.println("Show Section");
        System.out.println(sectionToShow.getId());
        // Hide all sections
        entreeSection.setVisible(false);
        mainsSection.setVisible(false);

        // Show selected section
        sectionToShow.setVisible(true);
    }

}

