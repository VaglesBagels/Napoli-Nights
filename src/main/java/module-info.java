module com.example.napolinights {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.napolinights to javafx.fxml;
    exports com.example.napolinights;
}