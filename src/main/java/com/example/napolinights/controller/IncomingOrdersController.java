package com.example.napolinights.controller;

import com.example.napolinights.model.Order;
import com.example.napolinights.model.OrderDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.example.napolinights.model.SqliteConnection;


public class IncomingOrdersController {
    @FXML
    public AnchorPane incomingOrdersPane;

    @FXML
    public VBox headerPane;

    @FXML
    private TableView<Order> tblIncomingOrders; // A TableView for displaying the orders

    @FXML
    private TableColumn<Order, Integer> colOrderNumber; // Order ID column

    @FXML
    private TableColumn<Order, Integer> colOrderItemID; // Order item ID column

    @FXML
    private TableColumn<Order, Integer> colQuantity; // Quantity column

    @FXML
    private TableColumn<Order, String> colSpecialInstructions; // Notes (special instructions) column

    private OrderDAO orderDAO;

    @FXML
    private Button staffLandingPageButton;

    @FXML
    private void initialize() {

        // Set up the table columns
        colOrderNumber.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        colOrderItemID.setCellValueFactory(new PropertyValueFactory<>("orderItemID"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colSpecialInstructions.setCellValueFactory(new PropertyValueFactory<>("specialInstructions"));

        // Load confirmed orders from the database
        loadConfirmedOrders();

        // Set padding for the checkout pane to provide spacing
        incomingOrdersPane.setPadding(new Insets(0, 0, 0, 10)); // Top, right, bottom, left padding

        // Ensure that the stage size is adjusted after the scene is loaded
        Platform.runLater(() -> {
            Stage stage = (Stage) incomingOrdersPane.getScene().getWindow();
            stage.setMinWidth(800);
            stage.setMinHeight(600);
        });
    }

    private void loadConfirmedOrders() {
        try {
            // Initialize the DAO to fetch confirmed orders
            orderDAO = new OrderDAO(SqliteConnection.getInstance());
            List<Order> confirmedOrders = orderDAO.fetchConfirmedOrders(); // Fetch confirmed orders

            // Populate the TableView with the confirmed orders
            ObservableList<Order> orderList = FXCollections.observableArrayList(confirmedOrders);
            tblIncomingOrders.setItems(orderList);

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
            // Optionally, display an error message to the user (e.g., using an alert)
        }
    }


    @FXML
    private void handleStaffHomePage () {
        System.out.println("Staff Main Page button clicked");
        openStaffHomePage();
    }
    private void openStaffHomePage () {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StaffLandingPage.fxml"));
            Parent staffLandingPage = loader.load();

            // Add null check
            Stage stage = (Stage) staffLandingPageButton.getScene().getWindow();
            stage.setTitle("Staff Home Page");
            Scene scene = new Scene(staffLandingPage);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



