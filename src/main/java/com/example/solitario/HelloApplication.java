package com.example.solitario;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        TableroEightOff tablero = new TableroEightOff();

        Scene scene = new Scene(tablero, 900, 650);
        stage.setTitle("Eight Off - UABC");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}