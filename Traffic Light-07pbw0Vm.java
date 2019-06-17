// Ricardo Santiago
// 4/7/16
// CSCI-112
// Exam#2 - Traffic Light
// This program will display a traffic light to the user, along with three radio buttons that allow the user to 
// select a light. If one light is selected, it will turn on and the other will dim


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
 
public class TrafficLight extends Application {
 
    Circle redLight, yellowLight, greenLight;
    RadioButton rbRed, rbYellow, rbGreen;
    double dim = .20, lit = 1.0;
 
    @Override
    public void start(Stage primaryStage) throws Exception {
       
        BorderPane root = new BorderPane();
       
        Rectangle rectangle = new Rectangle(150, 75, 100, 250);
        rectangle.setFill(Color.GRAY);
        rectangle.setStroke(Color.BLACK);
 
        redLight = new Circle(200, 120, 30);
        redLight.setFill(Color.RED);
        redLight.setStroke(Color.BLACK);
        redLight.setOpacity(lit);
 
        yellowLight = new Circle(200, 200, 30);
        yellowLight.setFill(Color.YELLOW);
        yellowLight.setStroke(Color.BLACK);
        yellowLight.setOpacity(dim);
 
        greenLight = new Circle(200, 280, 30);
        greenLight.setFill(Color.GREEN);
        greenLight.setStroke(Color.BLACK);
        greenLight.setOpacity(dim);
 
        root.getChildren().addAll(rectangle, redLight, yellowLight, greenLight);
        root.setBottom(getBottomHBox());
       
        Scene scene = new Scene(root, 400, 400);
 
        primaryStage.setResizable(false);
        primaryStage.setTitle("Traffic Light");
        primaryStage.setScene(scene);
        primaryStage.show();
 
        rbRed.setOnAction(e -> {
            if (rbRed.isSelected()) {
                changeLight(0);
            }
        });
 
        rbYellow.setOnAction(e -> {
            if (rbYellow.isSelected()) {
                changeLight(1);
            }
        });
 
        rbGreen.setOnAction(e -> {
            if (rbGreen.isSelected()) {
                changeLight(2);
            }
        });
    }
   
    public void changeLight(int lightNum){
       
        redLight.setOpacity(dim);
        yellowLight.setOpacity(dim);
        greenLight.setOpacity(dim);
       
        if (lightNum == 0){
            redLight.setOpacity(lit);
        }
        if (lightNum == 1){
            yellowLight.setOpacity(lit);
        }
        if (lightNum == 2){
            greenLight.setOpacity(lit);
        }
       
    }
 
    private HBox getBottomHBox() {
 
        HBox bottomHBox = new HBox(40);
        bottomHBox.setPadding(new Insets(10, 10, 10, 10));
        bottomHBox.setStyle("-fx-border-color: black");
        bottomHBox.setAlignment(Pos.CENTER);
        rbRed = new RadioButton("Red");
        rbYellow = new RadioButton("Yellow");
        rbGreen = new RadioButton("Green");
 
        bottomHBox.getChildren().addAll(rbRed, rbYellow, rbGreen);
 
        ToggleGroup group = new ToggleGroup();
        rbRed.setToggleGroup(group);
        rbYellow.setToggleGroup(group);
        rbGreen.setToggleGroup(group);
 
        rbRed.setSelected(true);
 
        return bottomHBox;
    }
 
    public static void main(String[] args) {
        Application.launch(args);
    }
}