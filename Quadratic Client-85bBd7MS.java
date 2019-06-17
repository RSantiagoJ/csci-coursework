// Ricardo Santiago
// 4/29/16 
// CSCI-112
// Quadratic Equation Solver Client

import java.io.*;
import java.net.*;
import java.util.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Client extends Application {

	static String strA, strB, strC;
	private static String ansStr;
	private static double root1;
	private static double root2;
	private static int statusNum;
	private static String statusString;

	public void start(Stage primaryStage) {

		BorderPane root = new BorderPane();
		Button btEnter = new Button("Enter");
		Button btClear = new Button("Clear");
		HBox buttonPanel = new HBox(5);

		buttonPanel.setAlignment(Pos.CENTER);
		buttonPanel.getChildren().addAll(btEnter, btClear);
		buttonPanel.setPadding(new Insets(5));

		TextField tfA = new TextField("C");
		tfA.setMaxSize(50, 10);
		TextField tfB = new TextField("B");
		tfB.setMaxSize(50, 10);
		TextField tfC = new TextField("A");
		tfC.setMaxSize(50, 10);

		TextArea tA = new TextArea();
		tA.setMaxSize(250, 80);
		tA.setEditable(false);

		Text text = new Text("Enter values for a, b, and c");
		text.setFont(new Font(20));

		VBox textFieldPanel = new VBox(5);
		textFieldPanel.setPadding(new Insets(5));
		textFieldPanel.setAlignment(Pos.CENTER_LEFT);
		textFieldPanel.getChildren().addAll(tfC, tfB, tfA);

		HBox hB = new HBox();
		hB.getChildren().addAll(text);
		hB.setAlignment(Pos.CENTER);

		root.setBottom(buttonPanel);
		root.setCenter(tA);
		root.setLeft(textFieldPanel);
		root.setTop(hB);

		Scene scene = new Scene(root, 400, 200);
		primaryStage.setTitle("Quadratic Equation Solver");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();

		btEnter.setOnAction(e -> {
			setStrA(tfA.getText());
			setStrB(tfB.getText());
			setStrC(tfC.getText());
			Client.runClient();

			if (getStatusNum() == 0) {
				tA.setText(getStatusString());
			} else {
				tA.setText("root 1 = " + getRoot1() + "\n root 2 = " + getRoot2() + "\n status = " + getStatusNum());
			}
		});

		btClear.setOnAction(e -> {
			tfA.setText(null);
			tfB.setText(null);
			tfC.setText(null);
			tA.setText(null);
		});
	}

	static void runClient() {

		Socket client;
		DataOutputStream output;
		DataInputStream input;

		try {
			pStr("Creating Client Socket ");
			client = new Socket("cs.stcc.edu", 5013);
			pStr("SUCCESS!!!");

			input = new DataInputStream(client.getInputStream());
			output = new DataOutputStream(client.getOutputStream());

			double argA = Double.parseDouble(strA);
			output.writeDouble(argA);
			output.flush();

			double argB = Double.parseDouble(strB);
			output.writeDouble(argB);
			output.flush();

			double argC = Double.parseDouble(strC);
			output.writeDouble(argC);
			output.flush();

			setRoot1(input.readDouble());
			setRoot2(input.readDouble());
			setStatusNum(input.readInt());

			input.close();
			output.close();
			client.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getStatusString() {
		return statusString;
	}

	public static void setStatusString(String statusString) {
		Client.statusString = statusString;
	}

	public String getAnsStr() {
		return ansStr;
	}

	public static void setAnsStr(String str) {
		ansStr = str;
	}

	public static String getStrA() {
		return strA;
	}

	public static void setStrA(String strA) {
		Client.strA = strA;
	}

	public static String getStrB() {
		return strB;
	}

	public static void setStrB(String strB) {
		Client.strB = strB;
	}

	public static String getStrC() {
		return strC;
	}

	public static void setStrC(String strC) {
		Client.strC = strC;
	}

	public static double getRoot1() {
		return root1;
	}

	public static void setRoot1(double root1) {
		Client.root1 = root1;
	}

	public static double getRoot2() {
		return root2;
	}

	public static void setRoot2(double root2) {
		Client.root2 = root2;
	}

	public static int getStatusNum() {
		return statusNum;
	}

	public static void setStatusNum(int statusVal) {
		Client.statusNum = statusVal;
	}

	static void pStr(String p) {
		System.out.println(p);
	}
}