/*
* Name: Ricardo Santiago
* Date: 10/03/16
* Course Number: CSC-220
* Course Name: Data Structures
* Problem Number: HW3
* Email: rjsantiago0001@student.stcc.edu
 */
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class StarDataAnalyzer extends Application {

	private TextField tfStatus;
	private TextField tfFilePath;
	private TextArea taResult;

	private int[][] grid;
	private int[][] grid1;
	private int[][] grid2;
	private int[][] grid3;
	private int[][] grid4;

	private void btOnClick() {
		this.tfStatus.setText("");
		this.taResult.setText("");

		try {
			readStarData(tfFilePath.getText().replace("\\", "\\\\").trim());
			displayAllGrids();
			this.tfStatus.setText("Analysis Complete");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			this.tfStatus.setText("A File Not Found Exception has occured");

		} catch (IOException e) {
			e.printStackTrace();
			this.tfStatus.setText("An IO Exception has occured");
		}
	}

	public void readStarData(String filename) throws IOException {

		String line;
		int arrCount = 0;
		int row = 0;
		int rows;

		BufferedReader br = new BufferedReader(new FileReader(filename));

		while ((line = br.readLine()) != null) {

			if (line.contains("Y") || line.contains("N")) {
				initializeArrayByCount(arrCount);
				arrCount++;
				continue;
			}

			String[] string = line.trim().split(" +");
			int[] temp = convertToInt(string);

			if (string.length == 2) {
				rows = temp[0];
				row = 0;
				grid = new int[rows][];
				continue;
			}

			int[] nums = convertToInt(string);
			buildRow(grid, row, nums);
			row++;
		}
	}

	public void initializeArrayByCount(int arrCount) {

		if (arrCount == 0)
			this.grid1 = this.grid;

		else if (arrCount == 1)
			this.grid2 = this.grid;

		else if (arrCount == 2)
			this.grid3 = this.grid;

		else if (arrCount == 3)
			this.grid4 = this.grid;
	}

	private char[][] analyzeStarData(int[][] grid) {

		int tmp[][] = cloneArray(grid);
		int rows = tmp.length;
		int cols = tmp[0].length;
		char[][] charGrid = new char[rows][cols];

		for (int x = 1; x < rows - 1; x++) {

			for (int y = 1; y < cols - 1; y++) {
				charGrid[x][y] = isStar(tmp, x, y) ? '*' : ' ';
			}
		}
		return charGrid;
	}

	public static int[][] cloneArray(int[][] src) {
		int length = src.length;
		int[][] target = new int[length][src[0].length];
		for (int i = 0; i < length; i++) {
			System.arraycopy(src[i], 0, target[i], 0, src[i].length);
		}
		return target;
	}

	private boolean isStar(int[][] grid, int x, int y) {
		int sum = grid[x][y];
		int[] adjacent = new int[8];

		adjacent[0] = grid[x][y + 1];
		adjacent[1] = grid[x + 1][y + 1];
		adjacent[2] = grid[x - 1][y + 1];
		adjacent[3] = grid[x][y - 1];
		adjacent[4] = grid[x + 1][y - 1];
		adjacent[5] = grid[x - 1][y - 1];
		adjacent[6] = grid[x - 1][y];
		adjacent[7] = grid[x + 1][y];

		Arrays.sort(adjacent);
		for (int i = adjacent.length - 1; i >= 0; i--) {
			sum = sum + adjacent[i];
			if ((sum / 5) > 6)
				return true;
		}
		return false;
	}

	private int[] convertToInt(String[] arr) {
		int[] arrInt = new int[arr.length];

		for (int i = 0; i < arr.length; i++) {
			arrInt[i] = Integer.parseInt(arr[i]);
		}
		return arrInt;
	}

	private void buildRow(int[][] grid, int row, int[] nums) {
		grid[row] = new int[nums.length];
		for (int col = 0; col < nums.length; col++) {
			int n = nums[col];
			grid[row][col] = n;
		}
	}

	private void outputStarData(char[][] grid) {
		for (int row = 0; row < grid.length; row++) {

			for (int c = 0; c < grid[row].length; c++)
				this.taResult.appendText("+---------");

			this.taResult.appendText("\n");

			for (int c = 0; c < grid[row].length; c++)
				this.taResult.appendText("\t" + grid[row][c] + " \t|");

			this.taResult.appendText("\n");
		}
		this.taResult.appendText("\n");
	}

	private void displayAllGrids() {
		this.taResult.appendText("Grid 1 \n");
		outputStarData(analyzeStarData(grid1));

		this.taResult.appendText("Grid 2 \n");
		outputStarData(analyzeStarData(grid2));

		this.taResult.appendText("Grid 3 \n");
		outputStarData(analyzeStarData(grid3));

		this.taResult.appendText("Grid 4 \n");
		outputStarData(analyzeStarData(grid4));
	}

	private VBox getTextPane() {
		VBox textPane = new VBox(30);
		textPane.setPadding(new Insets(30, 30, 30, 30));
		textPane.setAlignment(Pos.CENTER);
		textPane.setStyle("-fx-font-weight: bold;-fx-font-size: 1.3em;");

		Text txtDescription = new Text("Star Data Analyzer");

		textPane.getChildren().addAll(txtDescription);

		return textPane;
	}

	private HBox getButtonPane() {
		HBox buttonPane = new HBox(10);
		buttonPane.setPadding(new Insets(10, 10, 10, 10));
		buttonPane.setAlignment(Pos.CENTER);

		Label lbFilePath = new Label("Enter Filepath :");
		this.tfFilePath = new TextField("");
		this.tfFilePath.setPrefColumnCount(20);

		Button btOk = new Button("Analalyze");
		btOk.setOnAction(e -> this.btOnClick());

		buttonPane.getChildren().addAll(lbFilePath, tfFilePath, btOk);

		return buttonPane;
	}

	private VBox getResultPane() {
		VBox resultPane = new VBox(10);
		resultPane.setAlignment(Pos.CENTER);
		resultPane.setPrefWidth(150);

		Label lbResult = new Label(" Result : ");
		lbResult.setStyle("-fx-font-weight: bold;-fx-font-size: 1.3em;");

		this.taResult = new TextArea();
		this.taResult.setEditable(false);
		this.taResult.setPrefRowCount(100);

		resultPane.getChildren().addAll(lbResult, taResult);
		return resultPane;
	}

	private HBox getStatusPane() {
		HBox statusPane = new HBox(10);
		statusPane.setPadding(new Insets(10, 10, 10, 10));
		statusPane.setAlignment(Pos.CENTER);
		statusPane.setStyle("-fx-font-weight: bold;-fx-font-size: 1.3em;");

		Label lblStatus = new Label("Status:");
		this.tfStatus = new TextField();
		this.tfStatus.setPrefColumnCount(20);
		this.tfStatus.setEditable(false);
		this.tfStatus.setAlignment(Pos.CENTER);

		statusPane.getChildren().addAll(lblStatus, this.tfStatus);
		return statusPane;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		VBox gui = new VBox(10);
		gui.getChildren().addAll(getTextPane(), getButtonPane(), getResultPane(), getStatusPane());

		Scene scene = new Scene(gui, 800, 900);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Star Data Analyzer");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}