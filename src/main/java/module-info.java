module com.example.napolinights {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.security.sasl;
    requires java.desktop;


    opens com.example.napolinights to javafx.fxml;
    exports com.example.napolinights;
    exports com.example.napolinights.model;
    opens com.example.napolinights.model to javafx.fxml;
    exports com.example.napolinights.controller;
    opens com.example.napolinights.controller to javafx.fxml;
}