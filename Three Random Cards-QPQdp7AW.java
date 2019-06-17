// Ricardo Santiago
// 3/29/16
// CS-112
// HW11
// This program will shuffle an ArrayList containing the values 1-54(a deck of playing cards, including jokers)
// After the shuffle, the program displays the three cards associated with the values in positions 0-2 of the ArrayList
// I can be reached at : rjsantiago0001@student.stcc.edu

import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ThreeRandomCards extends Application {

	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {

		ArrayList<Integer> deck = new ArrayList<>();
		// start incrementing at i because card0.png does not exist in card
		// directory
		// increment to 54 instead of 52, so that the jokers will be included
		for (int i = 1; i < 55; i++) {
			deck.add(i);

		}
		// shuffle the deck
		java.util.Collections.shuffle(deck);

		// Create a pane to hold the image views
		Pane pane = new HBox(10);
		pane.setPadding(new Insets(5, 5, 5, 5));
		Image image = new Image("/card/" + deck.get(0) + ".png");
		pane.getChildren().add(new ImageView(image));

		ImageView imageView2 = new ImageView("/card/" + deck.get(1) + ".png");
		// imageView2.setFitHeight(50);
		// imageView2.setPreserveRatio(true);
		// imageView2.setFitWidth(100);
		pane.getChildren().add(imageView2);

		ImageView imageView3 = new ImageView("/card/" + deck.get(2) + ".png");
		// imageView3.setRotate(90);
		pane.getChildren().add(imageView3);

		// Create a scene and place it in the stage
		Scene scene = new Scene(pane);
		primaryStage.setTitle("ShowImage"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
	}

	/**
	 * The main method is only needed for the IDE with limited JavaFX support.
	 * Not needed for running from the command line.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}