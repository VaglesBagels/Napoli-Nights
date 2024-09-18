module com.example.napolinights {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.napolinights to javafx.fxml;
    exports com.example.napolinights;
}