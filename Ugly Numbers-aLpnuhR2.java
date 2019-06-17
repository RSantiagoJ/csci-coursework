/*
* Name: Ricardo Santiago
* Date: 9/10/16
* Course Number: CSC-220
* Course Name: Data Structures
* Problem Number: NA
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

import java.util.regex.Pattern;

public class UglyNumbers extends Application {

    private TextField tfN;
    private TextField tfI;
    private TextField tfStatus;
    private TextArea taUglyResults;
    private TextArea taNonUglyResults;


    private int[] initializeArrayWithRange(int a, int b) {
        int[] arrNumbers = new int[b];
        int c = 0;

        for (int j = a; j <= b; j++) {
            arrNumbers[c] = j;
            c++;
        }
        return arrNumbers;
    }

    private void processArray(int[] arr) {

        int[] arrUglyNumbers = new int[arr.length];
        int c = 0;

        for (int i = 0; i < arr.length; i++) {
            if (isUgly(arr[i]) == true) {
                arrUglyNumbers[c] = arr[i];
                this.taUglyResults.appendText("\n          " + arrUglyNumbers[c]);
                c++;
            } else this.taNonUglyResults.appendText("\n          " + arr[i]);
        }
    }

    private boolean isUgly(int n) {
        while (n % 2 == 0)
            n /= 2;
        while (n % 3 == 0)
            n /= 3;
        while (n % 5 == 0)
            n /= 5;
        return n == 1 ? true : false;
    }

    private void btCalculateOnClick() {
        taNonUglyResults.setText("");
        taUglyResults.setText("");

        try {
            int i = Integer.parseInt(this.tfI.getText());
            int n = Integer.parseInt(this.tfN.getText());

            if (Pattern.matches("^[0-9]*[1-9][0-9]*$",
                    Integer.toString(i) + Integer.toString(n))) ;
            {
                tfStatus.setText("Input is valid");

                processArray(initializeArrayWithRange(Integer.parseInt(tfI.getText()), Integer.parseInt(tfN.getText())));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            this.tfStatus.setText("A number format exception has occured");
        }
    }

    private VBox getTextPane() {
        VBox textPane = new VBox(30);
        textPane.setPadding(new Insets(30, 30, 30, 30));
        textPane.setAlignment(Pos.CENTER);
        textPane.setStyle("-fx-font-weight: bold;-fx-font-size: 1.3em;");

        Text txtDescription = new Text("Find ugly numbers in the range of i through n");
        Text txtInstruction = new Text("Enter values for i and n :");
        textPane.getChildren().addAll(txtDescription, txtInstruction);

        return textPane;
    }

    private HBox getUserInputPane() {
        HBox userInputPane = new HBox(10);
        userInputPane.setAlignment(Pos.CENTER);
        userInputPane.setStyle("-fx-font-weight: bold;-fx-font-size: 1.3em;");

        Label lblI = new Label("i =");
        this.tfI = new TextField();
        this.tfI.setMaxWidth(90);
        Label lblN = new Label(", n =");
        this.tfN = new TextField();
        this.tfN.setMaxWidth(90);

        userInputPane.getChildren().addAll(lblI, tfI, lblN, tfN);

        return userInputPane;
    }

    private HBox getButtonPane() {
        HBox buttonPane = new HBox();
        buttonPane.setPadding(new Insets(10, 10, 10, 10));
        buttonPane.setAlignment(Pos.CENTER);

        Button btOk = new Button("Calculate");
        btOk.setOnAction(e -> this.btCalculateOnClick());

        buttonPane.getChildren().addAll(btOk);

        return buttonPane;
    }

    private HBox getResultsPane() {
        HBox resultsPane = new HBox(30);
        resultsPane.setPadding(new Insets(10, 10, 10, 10));
        resultsPane.setAlignment(Pos.CENTER);

        resultsPane.getChildren().addAll(getUglyResultsPane(), getNonUglyResultsPane());

        return resultsPane;
    }

    private VBox getUglyResultsPane() {
        VBox uglyResultsPane = new VBox(10);
        uglyResultsPane.setAlignment(Pos.CENTER);
        uglyResultsPane.setPrefWidth(150);

        Label lbUgly = new Label(" Ugly Numbers");
        lbUgly.setStyle("-fx-font-weight: bold;-fx-font-size: 1.3em;");

        this.taUglyResults = new TextArea();
        this.taUglyResults.setEditable(false);
        this.taUglyResults.setPrefRowCount(100);

        uglyResultsPane.getChildren().addAll(lbUgly, taUglyResults);
        return uglyResultsPane;
    }

    private VBox getNonUglyResultsPane() {
        VBox nonUglyResultsPane = new VBox(10);
        nonUglyResultsPane.setAlignment(Pos.CENTER);
        nonUglyResultsPane.setPrefWidth(150);

        Label lbNonUgly = new Label(" Non-Ugly Numbers");
        lbNonUgly.setStyle("-fx-font-weight: bold;-fx-font-size: 1.3em;");

        this.taNonUglyResults = new TextArea();
        this.taNonUglyResults.setEditable(false);
        this.taNonUglyResults.setPrefRowCount(100);

        nonUglyResultsPane.getChildren().addAll(lbNonUgly, taNonUglyResults);

        return nonUglyResultsPane;
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
        gui.getChildren().addAll(getTextPane(), getUserInputPane(), getButtonPane(), getResultsPane(), getStatusPane());

        Scene scene = new Scene(gui, 400, 600);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Ugly Numbers");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}