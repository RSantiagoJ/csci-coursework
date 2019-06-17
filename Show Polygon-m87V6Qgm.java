// Ricardo Santiago
// 4/6/16
// CSCI-112
// Homework13
// display polygon with buttons to add or subtract sides

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class ShowPolygon extends Application {

	static int numOfSides = 6;
	final double WIDTH = 200;
	final double HEIGHT = 200;
	double radius = Math.min(WIDTH, HEIGHT) * 0.4;
	double centerX = WIDTH / 2;
	double centerY = HEIGHT / 2;
	Button btnAdd, btnSub;
	ObservableList<Double> list;
	BorderPane root = new BorderPane();
	Polygon polygon = new Polygon();
	TextArea showNumOfSides = new TextArea("" + numOfSides);

	@Override
	public void start(Stage primaryStage) throws Exception {

		root.setBottom(getBottomHBox());

		list = polygon.getPoints();
		polygon.setFill(Color.WHITE);
		polygon.setStroke(Color.BLACK);
		root.setCenter(polygon);

		generatePolygon(numOfSides);

		Scene scene = new Scene(root, 200, 200);
		primaryStage.setTitle("Show Polygon");
		primaryStage.setScene(scene);
		primaryStage.show();

		btnAdd.setOnMouseClicked(e -> {
			generatePolygon(numOfSides++);
		});

		btnSub.setOnMouseClicked(e -> {
			generatePolygon(numOfSides--);
		});
	}

	public void generatePolygon(int sides) {
		list.clear();
		root.setCenter(polygon);

		for (int i = 0; i < numOfSides; i++) {
			list.add(centerX + radius * Math.cos(2 * i * Math.PI / numOfSides));
			list.add(centerY + radius * Math.sin(2 * i * Math.PI / numOfSides));
		}

	}

	public int getNumOfSides() {
		return numOfSides;
	}

	private HBox getBottomHBox() {
		HBox bottomHBox = new HBox();
		bottomHBox.setAlignment(Pos.CENTER);
		bottomHBox.setSpacing(10.0);
		root.setPadding(new Insets(10));
		btnAdd = new Button("+");
		btnSub = new Button("-");
		bottomHBox.getChildren().addAll(btnAdd, btnSub);

		return bottomHBox;

	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}