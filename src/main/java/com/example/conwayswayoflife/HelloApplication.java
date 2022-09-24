package com.example.conwayswayoflife;

import com.almasb.fxgl.ui.Position;
import com.example.conwayswayoflife.extentions.DisplaySize;
import javafx.application.Application;
import javafx.css.Size;
import javafx.css.SizeUnits;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
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