package com.example.conwayswayoflife;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        Game g = new Game(primaryStage);
        g.refresh();
    }
    public static void main(String[] args) {
        launch();
    }
}