import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class KochSnowFlake extends Application {

	static int order;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		SnowflakePane pane = new SnowflakePane();
		Scene scene = new Scene(pane);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Koch Snowflake");
		primaryStage.show();
	}

	private class SnowflakePane extends BorderPane {

		Pane pane = new Pane();
		ObservableList<Node> list = pane.getChildren();

		SnowflakePane() {

			pane.setPrefSize(400, 400);
			setCenter(pane);
			setMargin(pane, new Insets(100));
			setBottom(getButtonPane());
			draw();
		}

		private void draw() {
			list.clear();
			double length = 400;

			// line(startX, startY, endX, endY)
			Line line1 = new Line(200, 0, 200 + length * Math.cos(1 * (Math.PI * 2 / 6)),
					0 + length * Math.sin(1 * (Math.PI * 2 / 6)));
			Line line2 = new Line(line1.getEndX(), line1.getEndY(), line1.getEndX() - length, line1.getEndY());
			Line line3 = new Line(line2.getEndX(), line2.getEndY(), line1.getStartX(), line1.getStartY());
			list.addAll(line1, line2, line3);
			draw(order);
		}

		// recursive draw method
		private void draw(int order) {

			if (order == 0)
				return;

			Line[] lines = new Line[list.size()];
			for (int i = 0; i < list.size(); i++) {
				lines[i] = (Line) list.get(i);
			}

			for (Line line : lines) {
				createTriangle(line);
			}
			draw(order - 1);

		}

		private void createTriangle(Line line) {

			double distance = distance(line) / 3;

			double dy = (line.getStartY() - line.getEndY());
			double dx = (line.getEndX() - line.getStartX());
			double theta = Math.atan2(dy, dx);

			// points for horizontal line 1
			double x1 = line.getStartX() + distance * Math.cos(theta);
			double y1 = line.getStartY() - distance * Math.sin(theta);

			// points for horizontal line 2
			double x2 = line.getEndX() + distance * Math.cos(theta + Math.toRadians(180));
			double y2 = line.getEndY() - distance * Math.sin(theta + Math.toRadians(180));

			// this is the "pain in the ass point"
			double x3 = x2 + distance * Math.cos(theta + Math.toRadians(120));
			double y3 = y2 - distance * Math.sin(theta + Math.toRadians(120));

			/*
			 * Spatial visualization ability or visual-spatial ability is the
			 * ability to mentally manipulate 2-dimensional, 3-dimensional and
			 * 4-dimensional figures
			 * 
			 * toggle colors to assist with spatial visualization
			 */

			// lines 1 and 2 form 2 horizontal lines, using 2 180 degree angles
			Line line1 = new Line(line.getStartX(), line.getStartY(), x1, y1);
			// line1.setStroke(Color.GREEN);
			
			Line line2 = new Line(x2, y2, line.getEndX(), line.getEndY());
			// line2.setStroke(Color.MAGENTA);

			// lines 3 and 4 form the "pain in the ass point", using 2 symmetrical 120 degree angles
			Line line3 = new Line(line1.getEndX(), line1.getEndY(), x3, y3);
			// line3.setStroke(Color.ORANGE);
			
			Line line4 = new Line(line3.getEndX(), line3.getEndY(), x2, y2);
			// line4.setStroke(Color.CYAN);

			pane.getChildren().remove(line);
			list.addAll(line1, line2, line3, line4);

		}

		public double distance(Line line) {
			return distance(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
		}

		public double distance(double x1, double y1, double x2, double y2) {

			return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
		}

		public HBox getButtonPane() {
			HBox buttonPane = new HBox(10);
			buttonPane.setPadding(new Insets(10, 10, 10, 10));
			buttonPane.setAlignment(Pos.CENTER);
			buttonPane.setStyle("-fx-font-weight: bold;-fx-font-size: 1.3em;");

			Text txtOrder = new Text("Order = 0");

			Button btIncrement = new Button("++");
			Button btDecrement = new Button("--");

			btIncrement.setOnAction(e -> {
				// I set a cap of 7 on the order, system becomes very unstable
				// when attempting an order of 8
				if (order == 7)
					return;

				order++;
				draw();
				txtOrder.setText("Order = " + order);
			});

			btDecrement.setOnAction(e -> {
				if (order == 0)
					return;

				order--;
				draw();
				txtOrder.setText("Order = " + order);
			});

			buttonPane.getChildren().addAll(txtOrder, btIncrement, btDecrement);
			return buttonPane;
		}
	}
}