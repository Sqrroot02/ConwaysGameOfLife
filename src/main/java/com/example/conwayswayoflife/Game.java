package com.example.conwayswayoflife;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Game {

    public Game(Stage s){
        mainStage = s;
        fieldState = new boolean[fieldsX][fieldsY];
        initEvents();
    }

    // region Fields X
    private int fieldsX = 100;
    public int getFieldsX() {
        return fieldsX;
    }
    public void setFieldsX(int fieldsX) {
        this.fieldsX = fieldsX;
        fieldState = new boolean[fieldsX][fieldsY];
    }
    // endregion
    // region Fields Y
    private int fieldsY = 100;
    public int getFieldsY() {
        return fieldsY;
    }
    public void setFieldsY(int fieldsY) {
        this.fieldsY = fieldsY;
        fieldState = new boolean[fieldsX][fieldsY];
    }
    // endregion
    //region Stage Matrix
    private Stage mainStage;
    public Stage getMainStage() {
        return mainStage;
    }
    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }
    //endregion
    // region Scene
    private Scene scene;
    public Scene getScene() {
        return scene;
    }
    public void setScene(Scene scene) {
        this.scene = scene;
    }
    // endregion
    private double fieldWidth;
    private double fieldHeight;
    private boolean[][] fieldState;
    public void refresh(){
        Group root = new Group();
        Canvas canvas = new Canvas(1000,1000);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        initStartupField(gc,canvas);

        root.getChildren().add(canvas);

        mainStage.setScene(new Scene(root));
        mainStage.show();
    }

    // Visual refresh component
    public void initStartupField(GraphicsContext gc, Canvas canvas){
        fieldWidth = canvas.getWidth() / fieldsX;
        fieldHeight = canvas.getHeight() / fieldsY;

        for (int i = 0; i < fieldsX; i++) {
            for (int j = 0; j < fieldsY; j++) {
                if (fieldState[i][j]){
                    gc.setFill(Color.BLACK);
                    gc.fillRect(i*fieldWidth,j*fieldHeight,fieldWidth,fieldHeight);
                }
                else {
                    gc.setStroke(Color.GRAY);
                    gc.strokeRect(i*fieldWidth,j*fieldHeight,fieldWidth,fieldHeight);
                }
            }
        }
        Group p = new Group();
        p.getChildren().add(canvas);
    }

    private void initEvents(){
        mainStage.addEventFilter(MouseEvent.MOUSE_CLICKED,mouseClickedHandler);
        mainStage.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedEvent);
    }

    public EventHandler<MouseEvent> mouseClickedHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            int x = (int)Math.floor(mouseEvent.getX()/fieldWidth);
            int y = (int)Math.floor(mouseEvent.getY()/fieldHeight);
            fieldState[x][y] = !fieldState[x][y];
            refresh();
        }
    };

    public EventHandler<KeyEvent> keyPressedEvent = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent keyEvent) {
            if (keyEvent.getCode() == KeyCode.SPACE){
                fieldState = Evaluate(fieldState);
                refresh();
            }
        }
    };

    public boolean[][] Evaluate(boolean[][] field){
        ArrayList<Point2D> selectedFields = new ArrayList<>(); // Points which are alive
        // Searching for selected Fields;
        for (int i = 0, k = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j]){
                    selectedFields.add(new Point2D(i,j));
                }
            }
        }

        ArrayList<Point2D> nextGenDeadAddition = new ArrayList<>(); // Points which should die in Next Gen
        ArrayList<Point2D> nextGenAliveAddition = new ArrayList<>(); // Points which should be summorized in next Gen

        // Algorithm: With fucking 5 inner for-loops! Im gonna doot mysqlf...
        for (int i = 0; i < selectedFields.size(); i++) {
            int cellAlive = 0; // Counter for Cells alive nearby an alive Cell
            for (int j = -1; j <= 1; j++) { // X Check
                for (int k = -1; k <= 1; k++) { // Y Check
                    int aliveX = (int)selectedFields.get(i).getX() + j; // Next Neighbour X of the current Alive cell
                    int aliveY = (int)selectedFields.get(i).getY() + k; // Next Neighbour Y of the current Alive cell
                    if (!(aliveX > field.length || aliveY > field[0].length || aliveX < 0 || aliveY < 0)){
                        if (!(k == 0 && j == 0) && field[aliveX][aliveY]){
                            cellAlive++;
                        }
                        if (!field[aliveX][aliveY]) { // Proofs every single Dead-Cell nearby a single Alive Cell
                            int deadCellAlive = 0; // Counter for Cells alive nearby a dead Cell
                            for (int l = -1; l <= 1; l++) { // X Dead Check
                                for (int m = -1; m <= 1; m++) { // Y Dead Check
                                    int deadX = aliveX + l; // Next Neighbour X of the current Dead cell
                                    int deadY = aliveY + m; // Next Neighbour Y of the current Dead cell
                                    if (!(deadX > field.length || deadY > field[0].length || deadX < 0 || deadY < 0)){
                                        if (!(l == 0 && m == 0) && field[deadX][deadY]){
                                            deadCellAlive++;
                                        }
                                    }
                                }
                            }
                            if (deadCellAlive == 3){ // Neighbour-Condition: Alive if Dead Cell has exact 3 Alive Neighbours
                                nextGenAliveAddition.add(new Point2D(aliveX, aliveY));
                            }
                        }
                    }
                }
            }
            if (cellAlive >= 4 || cellAlive <= 1){ // Neighbour-Condition: Dead above 3 and under 2
                nextGenDeadAddition.add(selectedFields.get(i));
            }
        }
        for (Point2D d : nextGenAliveAddition) { // Updates all NextGen AliveCells: #Jesus
            field[(int) d.getX()][(int) d.getY()] = true;
        }
        for (Point2D point2D : nextGenDeadAddition) { // Updates all NextGen DeathCells: #Killer
            field[(int) point2D.getX()][(int) point2D.getY()] = false;
        }
        return field;
    }
}
