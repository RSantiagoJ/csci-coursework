import java.util.ArrayList;

/**
 * Created by Ricardo on 11/2/2016.
 */
class BabyName implements Comparable<BabyName>, Cloneable {

    private String name;
    private int occurrences;
    private String sex;

    // this is for the scatterChart, but after some refactoring,  will replace the class value of occurences

    ArrayList listOfOcurrencesPerYear = new ArrayList();


    public BabyName(String name, String sex, int occurrences) {
        this.name = name;
        this.sex = sex;
        this.occurrences = occurrences;
        this.listOfOcurrencesPerYear.add(occurrences);
    }

    public BabyName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOccurrences() {
        return occurrences;
    }

    public void setOccurences(int number) {
        this.occurrences = number;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public boolean equals(BabyName babyName) {
        return (this.getName().equals(babyName.getName()));
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "[ name=" + this.getName() + ",sex = " + this.getSex() + ", Occurences=" + this.getOccurrences() + " ]";
    }


    @Override
    public int compareTo(BabyName babyName) {
        return this.getOccurrences() > babyName.getOccurrences() ? 1 : this.getOccurrences() < babyName.getOccurrences() ? -1 : 0;
    }

}

************************************************************************************************

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class BabyNameDataReader extends Application {

	ScatterChart<?, ?> scatterChart;

	Button btSearch;
	TextField tfStatus;
	TextField tfName;
	TextField tfSubString;

	static TextArea taResults;
	static RadioButton rbFilterName;
	static RadioButton rbFilterSubString;

	static ArrayList<BabyName> males;
	static ArrayList<BabyName> females;
	static ArrayList<BabyName> workingList;

	static ComboBox<String> comboBoxStartYear;
	static ComboBox<String> comboBoxEndYear;
	static ComboBox<String> comboBoxGender;
	static ComboBox<String> comboBoxSortOrder;
	static ComboBox<Integer> comboBoxPlotNumOfNames;

	static BorderPane bp;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		
		initializeLists();
		initializeComboBoxData();

		primaryStage.setTitle("Baby Names");
		bp = new BorderPane();
		bp.setPadding(new Insets(10, 10, 10, 10));
		bp.setLeft(getSearchPane());
		bp.setBottom(getStatusPane());
		bp.setRight(getRightPane());
		bp.setCenter(getScatterChart());
		
		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();
		primaryStage.setX(bounds.getMinX());
		primaryStage.setY(bounds.getMinY());
		primaryStage.setWidth(bounds.getWidth());
		primaryStage.setHeight(bounds.getHeight());
		
		Scene scene = new Scene(bp);
		bp.prefHeightProperty().bind(scene.heightProperty());
		bp.prefWidthProperty().bind(scene.widthProperty());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void readFile(int startYear, int endYear) throws FileNotFoundException {

		for (int currentYear = startYear; currentYear <= endYear; currentYear++) {
			Scanner sc = new Scanner(new File("src/names/yob" + currentYear + ".txt"));
			sc.useDelimiter("\\s*,\\s*|\\s+");

			while (sc.hasNextLine()) {
				String name = sc.next();
				String sex = sc.next().trim();
				int number = sc.nextInt();

				BabyName babyName = new BabyName(name, sex, number);

				if (sex.equals("F")) {
					females.add(babyName);
				} else {
					males.add(babyName);
				}

				sc.nextLine();
			}
			sc.close();
		}
	}

	public VBox getSearchPane() {
		VBox searchPane = new VBox(20);
		searchPane.setPadding(new Insets(5));
		searchPane.setStyle("-fx-border-color: Black;");
		this.btSearch = new Button("Search");

		btSearch.setOnAction((event) -> {

			clearLists();
			bp.setCenter(null);
			tfStatus.setText("");
			taResults.setText("");

			try {
				validateInputs();
				readFile(getStartYear(), getEndYear());
				workingList = new ArrayList<>(getListByGender(getGender()).size());
				cloneList(workingList, getListByGender(getGender()));

			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			if (rbFilterName.isSelected()) {
				bp.setCenter(getScatterChartFilteredName(filterName(tfName.getText(), workingList)));
				return;
			} else {

				if (getGender() != "Combined")
					removeDuplicates(workingList);

				sortNamesByOccurrences(workingList);
				displayNamesByOrder(workingList, getSortOrder());
				bp.setCenter(getScatterChartTopN(workingList, 5));
				tfStatus.setText("Names Sorted Successfully");
			}
		});

		Text txtFilterOptions = new Text("Filter Options");
		txtFilterOptions.setStyle("-fx-font-weight: bold;-fx-font-size: 1.3em;");

		searchPane.getChildren().addAll(getYearSelectionPane(), getGenderSelectionPane(), getOrderSelectionPane(),
				txtFilterOptions, getFilterNamePane(), getFilterSubStringPane(), btSearch);

		return searchPane;
	}

	public static void removeDuplicates(ArrayList<BabyName> list) {

		int count = list.size();

		for (int i = 0; i < count; i++) {
			for (int j = i + 1; j < count; j++) {
				if (list.get(i).equals(list.get(j))) {
					list.get(i).setOccurences(list.get(i).getOccurrences() + list.get(j).getOccurrences());
					list.get(i).listOfOcurrencesPerYear.add(list.get(j).getOccurrences());
					list.remove(j--);
					count--;
				}
			}
		}
	}

	public static void cloneList(ArrayList<BabyName> list, ArrayList<BabyName> listSource)
			throws CloneNotSupportedException {

		for (BabyName item : listSource)
			list.add((BabyName) item.clone());
	}

	public static void sortNamesByOccurrences(ArrayList<BabyName> list) {
		Collections.sort(list);
		Collections.reverse(list);
	}

	public static ArrayList<BabyName> getListByGender(String selection) {
		if (selection == "Females")
			return females;

		if (selection == "Males")
			return males;

		ArrayList<BabyName> list = new ArrayList<>();
		removeDuplicates(females);
		removeDuplicates(males);
		list.addAll(females);
		list.addAll(males);
		return list;
	}

	public BabyName filterName(String name, ArrayList<BabyName> list) {

		int count = list.size();
		BabyName babyName = new BabyName(name);

		for (int i = 0; i < count; i++) {
			if (babyName.equals(list.get(i))) {
				babyName.listOfOcurrencesPerYear.add(list.get(i).getOccurrences());
			}
		}
		return babyName;
	}

	public ArrayList<BabyName> filterSubString(String subString, ArrayList<BabyName> list) {

		int count = list.size();

		for (int i = 0; i < count; i++) {
			if (list.get(i).getName().toLowerCase().contains(subString)) {
				list.get(i).listOfOcurrencesPerYear.add(list.get(i).getOccurrences());
			}
		}
		return list;
	}

	public static void displayNamesByOrder(ArrayList<BabyName> list, String sortOrder) {

		if (getGender() == "Unisex")
			for (int i = 0; i < 10; i++) {
				list.get(i).setSex("U");
			}

		if (sortOrder == "Top Ten") {
			for (int i = 0; i < 10; i++) {
				taResults.appendText("\n" + list.get(i));
			}
			return;
		}

		if (sortOrder == "Bottom Ten") {
			Collections.reverse(list);
			for (int i = 0; i < 10; i++) {
				taResults.appendText("\n" + list.get(i));
			}

			return;
		}

		if (sortOrder == "Random") {
			for (int i = 0; i < 10; i++) {
				taResults.appendText("\n" + list.get(getRandomInRange(0, list.size() - 1)));
			}
		}
	}

	public static int getRandomInRange(int low, int high) {
		Random r = new Random();
		int result = r.nextInt(high - low) + low;
		return result;
	}

	public static void initializeLists() {
		males = new ArrayList<>();
		females = new ArrayList<>();
		workingList = new ArrayList<>();
	}

	public static void clearLists() {
		workingList.clear();
		females.clear();
		males.clear();
	}

	public static int getStartYear() {
		return Integer.parseInt(comboBoxStartYear.getSelectionModel().getSelectedItem().toString());
	}

	public static int getEndYear() {
		return Integer.parseInt(comboBoxEndYear.getSelectionModel().getSelectedItem().toString());
	}

	public static String getGender() {
		return comboBoxGender.getSelectionModel().getSelectedItem().toString();
	}

	public String getSortOrder() {
		return comboBoxSortOrder.getSelectionModel().getSelectedItem().toString();
	}

	public static int getPlotOptionNumOfNames() {
		return comboBoxPlotNumOfNames.getSelectionModel().getSelectedItem();
	}
	public static ScatterChart getScatterChart() {

		final NumberAxis xAxis = new NumberAxis(1880, 2015, 1);
		final NumberAxis yAxis = new NumberAxis(0, 75000, 1000);
		final ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
		xAxis.setLabel("Years");
		yAxis.setLabel("Occurrances");
		scatterChart.setTitle("Top Baby Names");
		scatterChart.setStyle("-fx-font-weight: bold;-fx-font-size: 1.3em;");
		return scatterChart;
	}
	
	public static ScatterChart getScatterChartTopN(ArrayList<BabyName> list, int n) {

		int max = (int) Collections.max(list.get(0).listOfOcurrencesPerYear);

		final NumberAxis xAxis = new NumberAxis(getStartYear() - 2, getEndYear() + 2, 1);
		final NumberAxis yAxis = new NumberAxis(0, max + 1000, 1000);
		final ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
		xAxis.setLabel("Years");
		yAxis.setLabel("Occurrances");

		scatterChart.setTitle("Top " + getPlotOptionNumOfNames() + " Names");
		scatterChart.setStyle("-fx-font-weight: bold;-fx-font-size: 1.3em;");

		if (scatterChart.getData() == null) {
			scatterChart.setData(FXCollections.observableArrayList());
		}

		int year;
		int count;
		for (int i = 0; i < n; i++) {

			year = getStartYear();
			count = getEndYear() - getStartYear();

			ScatterChart.Series<Number, Number> series = new ScatterChart.Series<>();
			series.setName(list.get(i).getName());

			for (int j = 0; j < count; j++) {
				int z = (int) list.get(i).listOfOcurrencesPerYear.get(j);
				series.getData().add(new ScatterChart.Data<>(year, z));
				year++;
			}
			scatterChart.getData().add(series);
		}
		return scatterChart;
	}

	public static ScatterChart getScatterChartFilteredName(BabyName babyName) {

		int max = (int) Collections.max(babyName.listOfOcurrencesPerYear);

		// this is duplicate code, no time to refactor right now
		final NumberAxis xAxis = new NumberAxis(getStartYear() - 2, getEndYear() + 2, 1);
		final NumberAxis yAxis = new NumberAxis(0, max + 500, 500);
		final ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
		xAxis.setLabel("Years");
		yAxis.setLabel("Occurrances");
		scatterChart.setTitle(babyName.getName());
		scatterChart.setStyle("-fx-font-weight: bold;-fx-font-size: 1.3em;");

		XYChart.Series series = new XYChart.Series();
		// populate the series with data
		int year = getStartYear();
		int count = getEndYear() - getStartYear();
		for (int j = 0; j < count; j++) {
			series.getData().add(new XYChart.Data(year, babyName.listOfOcurrencesPerYear.get(j)));
			year++;
		}

		scatterChart.getData().addAll(series);
		return scatterChart;
	}

	public void validateInputs() {

		if (comboBoxStartYear.getSelectionModel().getSelectedItem() == null)
			tfStatus.setText("Start Year is not selected");

		if (comboBoxEndYear.getSelectionModel().getSelectedItem() == null)
			tfStatus.setText("End Year is not selected");

		if (comboBoxGender.getSelectionModel().getSelectedItem() == null)
			tfStatus.setText("Gender is not selected");

		if (comboBoxSortOrder.getSelectionModel().getSelectedItem() == null)
			tfStatus.setText("Sort order is not selected");

	}

	public void initializeComboBoxData() {
		initializeComboBoxYearData();
		initializeComboBoxGenderData();
		initializeComboBoxSortOrderData();
		initializeComboBoxPlotNumOfNames();

		comboBoxPlotNumOfNames.getSelectionModel().select(4);
	}

	public void initializeComboBoxYearData() {
		ObservableList<String> years = FXCollections.observableArrayList();
		for (int i = 1880; i <= 2015; i++) {
			years.add(Integer.toString(i));
		}

		this.comboBoxStartYear = new ComboBox<>();
		this.comboBoxEndYear = new ComboBox<>();
		this.comboBoxStartYear.setItems(years);
		this.comboBoxEndYear.setItems(years);
	}

	public void initializeComboBoxGenderData() {

		String strMale = "Males";
		String strfemale = "Females";
		String strCombined = "Combined";
		String strUnisex = "Unisex";
		this.comboBoxGender = new ComboBox<>();
		comboBoxGender.getItems().addAll(strMale, strfemale, strCombined, strUnisex);
	}

	public void initializeComboBoxPlotNumOfNames() {
		ObservableList<Integer> numOfNames = FXCollections.observableArrayList();
		for (int i = 1; i < 11; i++) {
			numOfNames.add(i);
		}
		this.comboBoxPlotNumOfNames = new ComboBox<>();
		this.comboBoxPlotNumOfNames.setItems(numOfNames);
	}

	public void initializeComboBoxSortOrderData() {
		String strTopTen = "Top Ten";
		String strBottomTen = "Bottom Ten";
		String strRandom = "Random";
		this.comboBoxSortOrder = new ComboBox<>();
		comboBoxSortOrder.getItems().addAll(strTopTen, strBottomTen, strRandom);
	}

	public static VBox getRightPane() {

		VBox rightPane = new VBox(100);
		rightPane.setPrefWidth(150);
		rightPane.getChildren().addAll(getResultsPane(), getPlotOptionsPane());
		return rightPane;
	}

	public HBox getFilterNamePane() {
		HBox filterNamePane = new HBox();
		rbFilterName = new RadioButton("Name : ");
		tfName = new TextField("");
		tfName.setPrefWidth(150);
		filterNamePane.getChildren().addAll(rbFilterName, tfName);
		return filterNamePane;
	}

	public HBox getFilterSubStringPane() {
		HBox filterSubStringPane = new HBox();
		rbFilterSubString = new RadioButton("SubString : ");
		tfSubString = new TextField("");
		tfSubString.setPrefWidth(150);
		rbFilterSubString.setDisable(true);
		filterSubStringPane.getChildren().addAll(rbFilterSubString, tfSubString);
		return filterSubStringPane;
	}

	public HBox getStatusPane() {
		HBox statusPane = new HBox(10);
		statusPane.setPadding(new Insets(10, 10, 10, 10));
		statusPane.setAlignment(Pos.CENTER);
		statusPane.setStyle("-fx-border-color: Black;");
		statusPane.setStyle("-fx-font-weight: bold;-fx-font-size: 1.3em;");
		Label lblStatus = new Label("Status:");
		this.tfStatus = new TextField();
		this.tfStatus.setPrefColumnCount(20);
		this.tfStatus.setEditable(false);
		this.tfStatus.setAlignment(Pos.CENTER);
		statusPane.getChildren().addAll(lblStatus, this.tfStatus);
		return statusPane;
	}

	public static VBox getResultsPane() {
		VBox resultsPane = new VBox();
		resultsPane.setAlignment(Pos.TOP_CENTER);
		Text text = new Text("Results");
		text.setStyle("-fx-font-weight: bold;-fx-font-size: 1.3em;");
		taResults = new TextArea("");
		taResults.setEditable(false);
		taResults.setPrefSize(270, 300);
		resultsPane.getChildren().addAll(text, taResults);
		return resultsPane;
	}

	public static HBox getPlotNumOfNamesSelectionPane() {
		HBox numOfNames = new HBox(5);
		numOfNames.setPadding(new Insets(1));
		Text text = new Text("Number of names to plot :");
		numOfNames.getChildren().addAll(text, comboBoxPlotNumOfNames);
		return numOfNames;
	}

	private VBox getGenderSelectionPane() {
		VBox genderSelectionPane = new VBox(5);
		genderSelectionPane.setPadding(new Insets(1));
		Text txtSelectGender = new Text("Select gender");
		txtSelectGender.setStyle("-fx-font-weight: bold;-fx-font-size: 1.3em;");
		genderSelectionPane.getChildren().addAll(txtSelectGender, comboBoxGender);
		return genderSelectionPane;
	}

	private VBox getOrderSelectionPane() {
		VBox orderSelectionPane = new VBox(5);
		orderSelectionPane.setPadding(new Insets(1));
		Text txt = new Text("Select a sort order");
		txt.setStyle("-fx-font-weight: bold;-fx-font-size: 1.3em;");
		orderSelectionPane.getChildren().addAll(txt, comboBoxSortOrder);
		return orderSelectionPane;
	}

	public VBox getYearSelectionPane() {
		VBox yearSelectionPane = new VBox(5);
		yearSelectionPane.setPadding(new Insets(1));
		Text text = new Text("Select start and end year");
		text.setStyle("-fx-font-weight: bold;-fx-font-size: 1.3em;");
		yearSelectionPane.getChildren().addAll(text, getStartYearPane(), getEndYearPane());
		return yearSelectionPane;
	}

	private static HBox getStartYearPane() {
		HBox startYearPane = new HBox(5);
		startYearPane.setPadding(new Insets(1));
		Label lblStartYear = new Label("Start Year");
		startYearPane.getChildren().addAll(lblStartYear, comboBoxStartYear);
		return startYearPane;
	}

	public static HBox getEndYearPane() {
		HBox endYearPane = new HBox(5);
		endYearPane.setPadding(new Insets(1));
		Label lblEndYear = new Label("End Year");
		endYearPane.getChildren().addAll(lblEndYear, comboBoxEndYear);
		return endYearPane;
	}

	public static VBox getPlotOptionsPane() {
		VBox plotOptionsPane = new VBox(10);
		plotOptionsPane.setPadding(new Insets(10));
		plotOptionsPane.setStyle("-fx-border-color: Black;");
		plotOptionsPane.setAlignment(Pos.BOTTOM_CENTER);
		Text text = new Text("Plot Options");
		text.setStyle("-fx-font-weight: bold;-fx-font-size: 1.3em;");

		Line line = new Line(0, 30, plotOptionsPane.getWidth(), 30);
		line.setStroke(Color.BLACK);
		Button btPlot = new Button("Plot Values");

		btPlot.setOnAction((event) -> {
			bp.setCenter(getScatterChartTopN(workingList, getPlotOptionNumOfNames()));
		});

		plotOptionsPane.getChildren().addAll(text, line, getPlotNumOfNamesSelectionPane(), btPlot);
		return plotOptionsPane;
	}

	// for testing
	public static void printList(ArrayList<BabyName> list) {
		for (int i = 0; i < list.size(); i++)
			System.out.println(list.get(i).toString());
	}

	// if (rbFilterSubString.isSelected()) {
	// bp.setCenter(getScatterChartTopN(filterSubString(tfSubString.getText(),
	// workingList)));
	// return;
	// }

	// displayNamesByOrder(sortListOfNamesByOccurrences(removeDuplicates(getListByGender(getGender()))));

}