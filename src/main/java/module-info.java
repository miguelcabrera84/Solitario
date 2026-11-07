module com.example.solitario {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.solitario to javafx.fxml;
    exports com.example.solitario;
    exports DeckOfCards;
    exports solitaire;
    exports EstructuraDeDatos;
}