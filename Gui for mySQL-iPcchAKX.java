 
// Ricardo Santiago
// 5/06/16
// CSCI-112
// Gui for mySQL
 
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.util.regex.Pattern;

public class Gui extends Application {
 
    private TextField tfZip;
    private TextArea taCity;
    private TextArea taState;
    private TextField tfCity;
    private Button btnZipToCity;
    private Button btnCityToZip;
    private TextField tfState;
    private TextArea statusBody;
    private TextArea zipBody;
    private static String temp;
    static final String regexForZip = "^\\d{5}$";
    static final String regexForCity = "^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$";
    static final String regexForState = "^[A-Z]{2}$";
    
    private static final String DATABASE = "silvestri";
    private static final String USERNAME = "readonly";
    private static final String PASSWORD = "readonly";
 
    static String driver = "com.mysql.jdbc.Driver";
    static String url = "jdbc:mysql://cs.stcc.edu/" + DATABASE +
                        "?user=" + USERNAME + "&password=" + PASSWORD;
 
    public void start(Stage primaryStage) {
 
        FlowPane tPane = new FlowPane(5, 5);
        tPane.setAlignment(Pos.CENTER);
        tPane.setPadding(new Insets(10, 10, 10, 10));
        tPane.setHgap(10);
        tfZip = new TextField();
        tfZip.setPrefColumnCount(5);
        btnZipToCity = new Button("Zip To City");
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
        Line sepLine1 = new Line(10, 340, 600, 340);
        tPane.getChildren().addAll(lblZip, tfZip, btnZipToCity, lblCity1, taCity, lblState1, taState, sepLine1);
 
        FlowPane cPane = new FlowPane(5, 5);
        cPane.setAlignment(Pos.CENTER);
        cPane.setPadding(new Insets(10, 10, 10, 10));
        cPane.setHgap(10);
        tfCity = new TextField();
        tfCity.setPrefColumnCount(13);
        btnCityToZip = new Button("City to Zip");
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
        cPane.getChildren().addAll(lblCity2, tfCity, lblState2, tfState, btnCityToZip, zipBody);
 
        FlowPane bPane = new FlowPane(5, 5);
        bPane.setAlignment(Pos.CENTER);
        bPane.setPadding(new Insets(5, 5, 5, 5));
        Line sepLine2 = new Line(10, 340, 600, 340);
        Label lblStatus = new Label("Status:");
        lblStatus.setStyle("-fx-font-weight: bold; -fx-font-size: 12pt");
        statusBody = new TextArea();
        statusBody.setPrefColumnCount(30);
        statusBody.setPrefRowCount(1);
        statusBody.setEditable(false);
       
        bPane.getChildren().addAll(sepLine2, lblStatus, statusBody);
 
        BorderPane bp = new BorderPane();
        bp.setTop(tPane);
        bp.setCenter(cPane);
        bp.setBottom(bPane);
        bp.setStyle("-fx-border-color: Black;");
 
        btnZipToCity.setOnAction(e -> {
 
            statusBody.setText(null);
 
            String strZip;
 
            temp = tfZip.getText();
            strZip = temp.trim();
 
            if (isValidZip(strZip) == false) {
 
                statusBody.setText("ERROR: Zipcode input is not valid, must be 5 consecutive digits.");
                statusBody.setStyle("-fx-font-weight: bold; -fx-text-fill:red");
            }
            else{
            	statusBody.setText("SUCCESS!!! Zipcode input is valid.");
            	statusBody.setStyle("-fx-font-weight: bold; -fx-text-fill: green");
            }
        });
 
        btnCityToZip.setOnAction(e -> {
 
            statusBody.setText(null);
 
            String strCity;
            String strState;
 
            temp = tfCity.getText();
            strCity = temp.trim();
            temp = tfState.getText();
            strState = temp.trim();
 
            if (strCity.length() > 25 || isValidCity(strCity) == false) {
                statusBody.setText("City input is not valid, or exceeds 25 characters.");
                statusBody.setStyle("-fx-font-weight: bold; -fx-text-fill:red");
            }
 
            else if (isValidState(strState) == false) {
                statusBody.setText("State input is not valid, or exceeds 2 characters.");
                statusBody.setStyle("-fx-font-weight: bold; -fx-text-fill:red");
            }
            
            else {
            	statusBody.setText("SUCCESS!!! City/State input is valid.");
            	statusBody.setStyle("-fx-font-weight: bold; -fx-text-fill:green");
            
            }
 
        });
 
        Scene scene = new Scene(bp, 620, 250);
        primaryStage.setTitle("Query Zip Codes Application");
 
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
 
    }
 
    public static void main(String[] args) {
        Application.launch(args);
    }
 
    public static boolean isValidZip(String zip) {
        return Pattern.matches(regexForZip, zip);
    }
 
    public static boolean isValidCity(String city) {
        return Pattern.matches(regexForCity, city);
    }
 
    public static boolean isValidState(String state) {
        return Pattern.matches(regexForState, state);
    }
 
}