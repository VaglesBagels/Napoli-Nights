module com.example.napolinights {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.napolinights to javafx.fxml;
    exports com.example.napolinights;
    exports com.example.napolinights.model;
    opens com.example.napolinights.model to javafx.fxml;
    exports com.example.napolinights.controller;
    opens com.example.napolinights.controller to javafx.fxml;
    exports com.example.napolinights.model.obsolete;
    opens com.example.napolinights.model.obsolete to javafx.fxml;
}