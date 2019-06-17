/**
 * Created by Ricardo Santiago on 10/17/2016.
 * csc-220
 * HW4 - Tic-Tac-Toe
 * Email: rjsantiago0001@student.stcc.edu
 */

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class TicTacToe extends Application {

    private boolean playable = true;
    private Tile[][] board = new Tile[3][3];
    private List<Combo> combos = new ArrayList<>();

    BorderPane borderPane = new BorderPane();
    private int turns;
    private Line winningLine;
    private TextField tfTurns;
    private Pane root = new Pane();

public void setTurns(int turns) {
       this.turns = turns;
    }

    private int getTurns(){
        return this.turns;
    }
    private BorderPane getBorderPane(){

        borderPane.setCenter(createContent());
        borderPane.setBottom(getStatsPane());
        return borderPane;
    }

    private HBox getStatsPane(){
        HBox statsPane = new HBox();
        statsPane.setPrefSize(600,50);
        statsPane.setAlignment(Pos.CENTER);
        statsPane.setPadding(new Insets(10, 10, 10, 10));
        statsPane.setSpacing(15);
        statsPane.setStyle("-fx-font-weight: bold;-fx-font-size: 1.3em;");
        Button btReset = new Button("Reset");
        btReset.setOnAction(e -> this.btResetOnClick());

        this.tfTurns = new TextField(""+getTurns());
        this.tfTurns.setEditable(false);
        this.tfTurns.setPrefColumnCount(2);

        Label lblTurns = new Label("Turns :");

        statsPane.getChildren().addAll(lblTurns,tfTurns, btReset);
        return statsPane;
    }

    private void resetBoard(){

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                board[j][i].clearTile();
            }
        }

        root.getChildren().remove(winningLine);
        resetTurns();
        playable = true;
    }

    private void resetTurns() {
        setTurns(0);
        tfTurns.setText(""+getTurns());
    }
    private void btResetOnClick() {
        resetBoard();
    }

    private Parent createContent() {

        root.setPrefSize(600, 600);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Tile tile = new Tile();
                tile.setTranslateX(j * 200);
                tile.setTranslateY(i * 200);

                root.getChildren().add(tile);

                board[j][i] = tile;
            }
        }

        // horizontal combo
        for (int y = 0; y < 3; y++) {
            combos.add(new Combo(board[0][y], board[1][y], board[2][y]));
        }

        // vertical combo
        for (int x = 0; x < 3; x++) {
            combos.add(new Combo(board[x][0], board[x][1], board[x][2]));
        }

        // diagonal combo
        combos.add(new Combo(board[0][0], board[1][1], board[2][2]));
        combos.add(new Combo(board[2][0], board[1][1], board[0][2]));

        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(getBorderPane()));
        primaryStage.show();
    }

    // checks for three in a row
    private void checkGameState() {
        setTurns(turns+1);
        tfTurns.setText(""+getTurns());
        for (Combo combo : combos) {
            if (combo.isComplete()) {
                playable = false;
                playWinAnimation(combo);
                break;
            }
        }
    }

    private class Combo {
        private Tile[] tiles;

        public Combo(Tile... tiles) {
            this.tiles = tiles;
        }

        public boolean isComplete() {
            if (tiles[0].getValue().isEmpty())
                return false;

            return tiles[0].getValue().equals(tiles[1].getValue())
                    && tiles[0].getValue().equals(tiles[2].getValue());
        }
    }

    private void playWinAnimation(Combo combo) {
        winningLine = new Line();
        winningLine.setStartX(combo.tiles[0].getCenterX());
        winningLine.setStartY(combo.tiles[0].getCenterY());
        winningLine.setEndX(combo.tiles[0].getCenterX());
        winningLine.setEndY(combo.tiles[0].getCenterY());

        root.getChildren().add(winningLine);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(2),
                new KeyValue(winningLine.endXProperty(), combo.tiles[2].getCenterX()),
                new KeyValue(winningLine.endYProperty(), combo.tiles[2].getCenterY())));
        timeline.play();
    }

    private class Tile extends StackPane {
        private Text text = new Text();

        public Tile() {

            Rectangle border = new Rectangle(200, 200);
            border.setFill(null);
            border.setStroke(Color.BLACK);

            text.setFont(Font.font(72));

            setAlignment(Pos.CENTER);
            getChildren().addAll(border, text);

            setOnMouseClicked(event -> {

                if (getValue().isEmpty() == false)
                    return;

                if (!playable)
                    return;


                if (getTurns()%2 == 0) {
                        drawX();
                    checkGameState();
                } else {
                    drawO();
                    checkGameState();
                }
            });

        }

        public double getCenterX() {
            return getTranslateX() + 100;
        }

        public double getCenterY() {
            return getTranslateY() + 100;
        }

        public String getValue() {
            return text.getText();
        }

        private void drawX() {
            text.setText("X");
        }

        private void drawO() {
            text.setText("O");
        }

        private void clearTile() {
            text.setText("");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}