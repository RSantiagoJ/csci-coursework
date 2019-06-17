// Ricardo Santiago
// 05/17/16
// CSCI-112
// HW#18 Phase 2 of 2
// This application queries a mySQL database for city, state, and zipcodes

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
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Pattern;

public class Phase2 extends Application {

	private TextField tfZip;
	private TextArea taCity;
	private TextArea taState;
	private TextField tfCity;
	private TextField tfState;
	private TextArea statusBody;
	private TextArea zipBody;

	static final String regexForZip = "^\\d{5}$";
	static final String regexForCity = "^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$";
	static final String regexForState = "^[A-Z]{2}$";

	private static final String DATABASE = "silvestri";
	private static final String USERNAME = "readonly";
	private static final String PASSWORD = "readonly";

	static String driver = "com.mysql.jdbc.Driver";
	static String url = "jdbc:mysql://cs.stcc.edu/" + DATABASE + "?user=" + USERNAME + "&password=" + PASSWORD;
	private static Connection conn;

	public void start(Stage primaryStage) {

		VBox vb = new VBox();
		vb.getChildren().addAll(getTpane(), getSeperator(), getCpane(), getSeperator(), getBpane());
		vb.setStyle("-fx-border-color: Black;");

		Scene scene = new Scene(vb, 620, 250);
		primaryStage.setTitle("Query Zip Codes Application");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
		initializeDB();
	}

	private HBox getTpane() {

		HBox tPane = new HBox(10);
		tPane.setAlignment(Pos.CENTER);
		tPane.setPadding(new Insets(10, 10, 10, 10));
		tfZip = new TextField();
		tfZip.setPrefColumnCount(5);
		taCity = new TextArea();
		taCity.setPrefColumnCount(13);
		taCity.setPrefRowCount(1);
		taCity.setEditable(false);
		taState = new TextArea();
		taState.setPrefColumnCount(3);
		taState.setPrefRowCount(1);
		taState.setEditable(false);
		Label lblZip = new Label("Zip Code:");
		lblZip.setStyle("-fx-font-weight: bold; -fx-font-size: 12pt");
		Label lblCity1 = new Label("City:");
		lblCity1.setStyle("-fx-font-weight: bold; -fx-font-size: 12pt");
		Label lblState1 = new Label("State:");
		lblState1.setStyle("-fx-font-weight: bold; -fx-font-size: 12pt");
		Button btnZipToCity = new Button("Zip To City");
		btnZipToCity.setOnAction(e -> {
			validateZip(tfZip.getText().trim());
		});

		tPane.getChildren().addAll(lblZip, tfZip, btnZipToCity, lblCity1, taCity, lblState1, taState);
		return tPane;
	}

	private HBox getCpane() {

		HBox cPane = new HBox(10);
		cPane.setAlignment(Pos.CENTER);
		cPane.setPadding(new Insets(10, 10, 10, 10));
		tfCity = new TextField();
		tfCity.setPrefColumnCount(13);
		tfState = new TextField();
		tfState.setPrefSize(50, 20);
		tfState.setText("  ");
		tfState.setText(tfState.getText().substring(0, 2));
		Label lblCity2 = new Label("City:");
		lblCity2.setStyle("-fx-font-weight: bold; -fx-font-size: 12pt");
		Label lblState2 = new Label("State:");
		lblState2.setStyle("-fx-font-weight: bold; -fx-font-size: 12pt");
		zipBody = new TextArea();
		zipBody.setPrefColumnCount(9);
		zipBody.setPrefRowCount(6);
		zipBody.setText("Zip Code(s)");
		zipBody.setEditable(false);

		Button btnCityToZip = new Button("City to Zip");
		btnCityToZip.setOnAction(e -> {
			validateCityAndState(tfCity.getText().trim(), tfState.getText().trim());
		});

		cPane.getChildren().addAll(lblCity2, tfCity, lblState2, tfState, btnCityToZip, zipBody);
		return cPane;
	}

	private HBox getBpane() {

		HBox bPane = new HBox(10);
		bPane.setAlignment(Pos.CENTER);
		bPane.setPadding(new Insets(5, 5, 5, 5));
		Label lblStatus = new Label("Status:");
		lblStatus.setStyle("-fx-font-weight: bold; -fx-font-size: 12pt");
		statusBody = new TextArea();
		statusBody.setPrefColumnCount(30);
		statusBody.setPrefRowCount(1);
		statusBody.setEditable(false);

		bPane.getChildren().addAll(lblStatus, statusBody);
		return bPane;
	}

	private HBox getSeperator() {
		HBox seperator = new HBox(10);
		Line line = new Line(0, 0, 860, 0);
		seperator.getChildren().add(line);
		return seperator;
	}

	private void validateZip(String zip) {
		// clear leftover data from previous query
		taCity.setText(null);
		taState.setText(null);

		if (Pattern.matches(regexForZip, zip) == false) {
			statusBody.setText("ERROR: Zipcode input is not valid, must be 5 consecutive digits.");
			statusBody.setStyle("-fx-font-weight: bold; -fx-text-fill:red");
		} else {
			statusBody.setText("SUCCESS!!! Zipcode input is valid.");
			statusBody.setStyle("-fx-font-weight: bold; -fx-text-fill: green");
			fetchCityAndState(zip);
			// This method can also be changed to return a boolean value,
			// where the fetchData method would be called if the method returns
			// true.
		}
	}

	private void validateCityAndState(String city, String state) {
		// clear leftover data from previous query
		zipBody.setText("Zip Code(s)");

		if (city.length() > 25 || Pattern.matches(regexForCity, city) == false) {
			statusBody.setText("City input is not valid, or exceeds 25 characters.");
			statusBody.setStyle("-fx-font-weight: bold; -fx-text-fill:red");
		} else if (Pattern.matches(regexForState, state) == false) {
			statusBody.setText("State input is not valid, or exceeds 2 characters.");
			statusBody.setStyle("-fx-font-weight: bold; -fx-text-fill:red");
		} else {
			statusBody.setText("SUCCESS!!! City/State input is valid.");
			statusBody.setStyle("-fx-font-weight: bold; -fx-text-fill:green");
			fetchZipcodes(city, state);
			// This method can also be changed to return a boolean value,
			// where the fetchData method would be called if the method returns
			// true.
		}
	}

	private void fetchCityAndState(String zip) {

		Statement stmt;
		try {
			stmt = conn.createStatement();
			String queryString = "select city, state from Zipcodes where zipcode = '" + zip + "';";
			ResultSet rSet = stmt.executeQuery(queryString);

			if (!rSet.isBeforeFirst()) {
				statusBody.setText("no data returned");
				statusBody.setStyle("-fx-font-weight: bold; -fx-text-fill: red");
			}
			while (rSet.next()) {
				taCity.setText(rSet.getString(1));
				taState.setText(rSet.getString(2));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void fetchZipcodes(String city, String state) {

		Statement stmt;
		int count = 0;
		// clear existing data
		zipBody.setText(null);

		try {
			stmt = conn.createStatement();
			String queryString = "select zipcode from Zipcodes where city = '" + city + "' and state = '" + state
					+ "';";
			ResultSet rSet = stmt.executeQuery(queryString);

			if (!rSet.isBeforeFirst()) {
				statusBody.setText("no data returned");
				statusBody.setStyle("-fx-font-weight: bold; -fx-text-fill: red");
			}
			while (rSet.next()) {
				zipBody.appendText("\n" + rSet.getString(1));
				count++;
			}

			zipBody.insertText(0, count + " Zip Code(s)\n");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void initializeDB() {

		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}