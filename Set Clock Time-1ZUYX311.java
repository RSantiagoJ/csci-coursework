// Ricardo Santiago
// 4/16/16
// CSCI-112
// HW#15 Clock with user input

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class SetClockTime extends Application {
  
	Timeline animation;
	TextField tfHour,tfMinutes,tfSeconds;
  public void start(Stage primaryStage) {
	 
    ClockPane clock = new ClockPane(); // Create a clock
    Button btStart = new Button("Start");
    Button btStop = new Button("Stop");
    
    // Hour
    tfHour = new TextField(clock.getHour() + "");
    tfHour.setPrefColumnCount(2);
    Label lblHour = new Label("Hour:", tfHour);
    lblHour.setContentDisplay(ContentDisplay.RIGHT);
    tfHour.setOnAction(e-> clock.setHour(Integer.parseInt(tfHour.getText())));

    // Minutes
    tfMinutes = new TextField(clock.getMinute() + "");
    tfMinutes.setPrefColumnCount(2);
    Label lblMinutes = new Label("Minute:", tfMinutes);
    lblMinutes.setContentDisplay(ContentDisplay.RIGHT);
    tfMinutes.setOnAction(e-> clock.setMinute(Integer.parseInt(tfMinutes.getText())));

    // Seconds
    tfSeconds = new TextField(clock.getSecond() + "");
    tfSeconds.setPrefColumnCount(2);
    Label lblSeconds = new Label("Second:", tfSeconds);
    lblSeconds.setContentDisplay(ContentDisplay.RIGHT);
    tfSeconds.setOnAction(e-> clock.setSecond(Integer.parseInt(tfSeconds.getText())));


    // Create a handler for animation
    EventHandler<ActionEvent> eventHandler = e -> {
      clock.setCurrentTime(); // Set a new clock time
      
    };
    
    // Button to start clock
    btStart.setOnMousePressed((MouseEvent e) -> {
   	 animation = new Timeline(
   		      new KeyFrame(Duration.millis(1000), eventHandler));
   		    animation.setCycleCount(Timeline.INDEFINITE);
   		    animation.play(); // Start animation
	});
    
    // Button to stop clock
    btStop.setOnMousePressed((MouseEvent e) -> {
    	animation.stop();
	});
   
    BorderPane pane = new BorderPane(clock);
    BorderPane.setAlignment(clock, Pos.CENTER);

    HBox textFieldPane = new HBox(5);
    textFieldPane.setAlignment(Pos.BOTTOM_CENTER);
    textFieldPane.getChildren().addAll(lblHour, lblMinutes, lblSeconds, btStart, btStop);
    textFieldPane.setPadding(new Insets(5));
    
    VBox buttonPane = new VBox(5);
    buttonPane.setAlignment(Pos.CENTER_LEFT);
    buttonPane.getChildren().addAll(btStart, btStop);
    buttonPane.setPadding(new Insets(5));
    
    pane.setBottom(textFieldPane);
    pane.setLeft(buttonPane);
    primaryStage.setScene(new Scene(pane,400,400));
    primaryStage.setTitle("Clock");
    primaryStage.show();
    clock.setFocusTraversable(true);
    

    // Create a scene and place it in the stage
//    Scene scene = new Scene(clock, 250, 250);
//    primaryStage.setTitle("ClockAnimation"); // Set the stage title
//    primaryStage.setScene(scene); // Place the scene in the stage
//    primaryStage.show(); // Display the stage
  }

  /**
   * The main method is only needed for the IDE with limited
   * JavaFX support. Not needed for running from the command line.
   */
  public static void main(String[] args) {
    launch(args);
  }
}