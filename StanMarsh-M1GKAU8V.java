
// Ricardo Santiago
// 4/4/16
// CS-112
// HW12
// This program will generate and image of StanMarsh using JavaFX

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class StanMarsh extends Application {

	Color red = new Color(.75, .08, .15, 1.0);
	Color blue = new Color(.24, .33, .53, 1.0);
	Color brown = new Color(.61, .36, .31, 1.0);
	Color skin = new Color(.99, .85, .70, 1.0);

	public void start(Stage primaryStage) {

		Ellipse arm1 = new Ellipse(485, 380, 80, 35);
		arm1.setRotate(70);
		arm1.setStroke(brown);
		arm1.setFill(brown);
		arm1.setStrokeWidth(3);

		Ellipse arm2 = new Ellipse(305, 380, 80, 35);
		arm2.setRotate(110);
		arm2.setStroke(brown);
		arm2.setFill(brown);
		arm2.setStrokeWidth(3);

		Arc shoe1 = new Arc(345, 530, 75, 40, 45, 90);
		shoe1.setType(ArcType.CHORD);
		shoe1.setStroke(Color.BLACK);
		shoe1.setFill(Color.BLACK);
		shoe1.setStrokeWidth(3);

		Arc shoe2 = new Arc(445, 530, 75, 40, 45, 90);
		shoe2.setType(ArcType.CHORD);
		shoe2.setStroke(Color.BLACK);
		shoe2.setFill(Color.BLACK);
		shoe2.setStrokeWidth(3);

		Rectangle body = new Rectangle(302, 300, 190, 165);
		body.setFill(brown);

		Line jacketLine = new Line(400, 480, 400, 395);
		jacketLine.setStrokeWidth(4);

		Line armLine1 = new Line(297, 430, 305, 370);
		armLine1.setStrokeWidth(2);

		Line armLine2 = new Line(488, 370, 494, 430);
		armLine2.setStrokeWidth(2);

		Circle button1 = new Circle(385, 405, 05, Color.BLACK);
		Circle button2 = new Circle(385, 435, 05, Color.BLACK);
		Circle button3 = new Circle(385, 465, 05, Color.BLACK);

		Rectangle pants = new Rectangle(300, 460, 190, 40);
		pants.setFill(blue);

		Rectangle edge = new Rectangle(247, 172, 303, 25);
		edge.setFill(red);

		Circle head = new Circle(400, 230, 150, skin);
		Circle pupil1 = new Circle(420, 230, 05, Color.BLACK);
		Circle pupil2 = new Circle(380, 230, 05, Color.BLACK);

		Ellipse eye1 = new Ellipse(445, 235, 49, 40);
		eye1.setRotate(45);
		eye1.setFill(Color.WHITE);
		Ellipse eye2 = new Ellipse(355, 235, 49, 40);
		eye2.setRotate(135);
		eye2.setFill(Color.WHITE);

		Arc mouth = new Arc(400, 340, 30, 15, 45, 90);
		mouth.setType(ArcType.OPEN);
		mouth.setStroke(Color.BLACK);
		mouth.setFill(null);
		mouth.setStrokeWidth(3);

		Arc hat = new Arc(400, 83, 140, 100, 180, 180);
		hat.setType(ArcType.ROUND);
		hat.setFill(blue);
		hat.setStrokeWidth(3);
		hat.setRotate(180);

		Arc brim = new Arc(400, 188, 150, 15, 25, 130);
		brim.setType(ArcType.OPEN);
		brim.setStroke(red);
		brim.setFill(null);
		brim.setStrokeWidth(25);

		Circle glove1 = new Circle(280, 440, 25, red);
		Circle glove2 = new Circle(510, 440, 25, red);
		Circle thumb1 = new Circle(295, 440, 10, red);
		Circle thumb2 = new Circle(495, 440, 10, red);
		thumb1.setStroke(Color.BLACK);
		thumb2.setStroke(Color.BLACK);

		Ellipse collar1 = new Ellipse(355, 370, 50, 15);
		collar1.setRotate(135);
		collar1.setFill(red);
		collar1.setRotate(25);

		Ellipse collar2 = new Ellipse(445, 370, 50, 15);
		collar2.setRotate(135);
		collar2.setFill(red);
		collar2.setRotate(155);

		Arc jacketBottom = new Arc(395, 470, 95, 15, 20, 135);
		jacketBottom.setType(ArcType.OPEN);
		jacketBottom.setStroke(brown);
		jacketBottom.setRotate(180);
		jacketBottom.setFill(null);
		jacketBottom.setStrokeWidth(25);

		Group root = new Group();
		root.getChildren().addAll(edge, pants, body, jacketBottom, arm1, arm2, collar1, collar2, head, eye1, eye2,
				pupil1, pupil2, mouth, hat, brim, jacketLine, button1, button2, button3, shoe1, shoe2, armLine1,
				armLine2, glove1, glove2, thumb1, thumb2);

		for (int i = 0; i < 8; i++) {
			Rectangle r = new Rectangle(365, 70, 70, 8);
			r.setArcWidth(10);
			r.setArcHeight(10);
			r.setRotate(i * 360 / 16);
			r.setStroke(Color.BLACK);
			r.setFill(red);
			root.getChildren().add(r);
		}

		Scene scene = new Scene(root, 800, 700);

		primaryStage.setResizable(false);
		primaryStage.setTitle("Stan Marsh");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}