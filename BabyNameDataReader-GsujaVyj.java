package misc;

/**
 * Created by Ricardo on 11/2/2016.
 */

/**
 * Created by Ricardo on 11/2/2016.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BabyNameDataReader extends Application {

    private static ArrayList<BabyName> males;
    private static ArrayList<BabyName> females;
    private static ArrayList<BabyName> workingList;

    Button btSearch;
    TextField tfStatus;
    TextField tfName;

    static TextArea taResults;
    static RadioButton rbFilterName;

    static ComboBox<String> comboBoxStartYear;
    static ComboBox<String> comboBoxEndYear;
    static ComboBox<String> comboBoxGender;
    static ComboBox<String> comboBoxSortOrder;

    static BorderPane bp;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        initializeComboBoxData();
        males = new ArrayList<>();
        females = new ArrayList<>();
        workingList = new ArrayList<>();

        primaryStage.setTitle("Baby Names");
        bp = new BorderPane();
        bp.setLeft(getSearchPane());
        bp.setBottom(getStatusPane());
        bp.setRight(getResultsPane());
        bp.setPadding(new Insets(10, 10, 10, 10));
        Scene scene = new Scene(bp, 1000, 600);
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

            if(rbFilterName.isSelected()){
                bp.setCenter(getScatterChartFilteredName(filterName(tfName.getText(),workingList)));
                workingList.clear();
                females.clear();
                males.clear();
            }
            else {

                if (getGender() != "Combined")
                    removeDuplicates(workingList);


                else {
                    sortNamesByOccurrences(workingList);
                    displayNamesByOrder(workingList, getSortOrder());
                    bp.setCenter(getScatterChartTopThree(workingList));

                    workingList.clear();
                    females.clear();
                    males.clear();
                    tfStatus.setText("Names Sorted Successfully");
                }

                // displayNamesByOrder(sortListOfNamesByOccurrences(removeDuplicates(getListByGender(getGender()))));
            } });

        searchPane.getChildren().addAll(getYearSelectionPane(), getGenderSelectionPane(), getOrderSelectionPane(),
                getFilterNamePane(), btSearch);

        return searchPane;
    }

    // private void filterName(String name, ArrayList<BabyName> list) {
    //
    // BabyName babyNameUnisex = new BabyName(name);
    //
    // int count = list.size();
    //
    //
    // for (int j = 0; j < count; j++) {
    // if (babyNameUnisex.equals(list.get(j))) {
    // //list.get(i).setOccurences(list.get(i).getOccurrences() +
    // list.get(j).getOccurrences());
    // babyNameUnisex.listOfOcurrencesPerYear.add(list.get(j).getOccurrences());
    // }
    // }
    //
    //
    // count = list.size();
    //
    // BabyName babyNameMale = new BabyName(name);
    //
    // for (int j = 0; j < count; j++) {
    // if (babyNameMale.equals(males.get(j))) {
    // //list.get(i).setOccurences(list.get(i).getOccurrences() +
    // list.get(j).getOccurrences());
    // babyNameMale.listOfOcurrencesPerYear.add(list.get(j).getOccurrences());
    // }
    // }
    //
    // count = list.size();
    //
    // BabyName babyNameFemale = new BabyName(name);
    //
    // for (int j = 0; j < count; j++) {
    // if (babyNameFemale.equals(females.get(j))) {
    // //list.get(i).setOccurences(list.get(i).getOccurrences() +
    // list.get(j).getOccurrences());
    // babyNameFemale.listOfOcurrencesPerYear.add(list.get(j).getOccurrences());
    // }
    // }
    //
    // ArrayList<BabyName> filteredNameList = new ArrayList<BabyName>();
    //
    // babyNameUnisex.setSex("U");
    // filteredNameList.add(babyNameUnisex);
    // filteredNameList.add(babyNameMale);
    // filteredNameList.add(babyNameFemale);
    //
    // printList(filteredNameList);
    //
    // getScatterChartFilteredName(filteredNameList);
    // }

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

    public HBox getFilterNamePane() {
        HBox filterNamePane = new HBox();

        Text text = new Text("Filter Name");
        text.setStyle("-fx-font-weight: bold;-fx-font-size: 1.3em;");
        rbFilterName = new RadioButton("Name : ");
        //rbFilterName.setDisable(true);

        tfName = new TextField("");
        tfName.setPrefWidth(150);

        filterNamePane.getChildren().addAll(rbFilterName, tfName);
        return filterNamePane;
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

    public VBox getResultsPane() {
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

    public VBox getYearSelectionPane() {
        VBox yearSelectionPane = new VBox(5);
        yearSelectionPane.setPadding(new Insets(1));

        Text text = new Text("Select start and end year");
        text.setStyle("-fx-font-weight: bold;-fx-font-size: 1.3em;");
        yearSelectionPane.getChildren().addAll(text, getStartYearPane(), getEndYearPane());

        return yearSelectionPane;
    }

    public void initializeComboBoxData() {
        initializeComboBoxYearData();
        initializeComboBoxGenderData();
        initializeComboBoxSortOrderData();
    }

    //
    private VBox getGenderSelectionPane() {
        VBox genderSelectionPane = new VBox(5);
        genderSelectionPane.setPadding(new Insets(1));

        Text txtSelectGender = new Text("Select gender");

        genderSelectionPane.getChildren().addAll(txtSelectGender, comboBoxGender);

        return genderSelectionPane;
    }

    public VBox getOrderSelectionPane() {
        VBox orderSelectionPane = new VBox(5);
        orderSelectionPane.setPadding(new Insets(1));

        Text txt = new Text("Select a sort order");

        orderSelectionPane.getChildren().addAll(txt, comboBoxSortOrder);

        return orderSelectionPane;
    }

    public static int getRandomInRange(int low, int high) {
        Random r = new Random();
        int result = r.nextInt(high - low) + low;

        return result;
    }

    public HBox getStartYearPane() {
        HBox startYearPane = new HBox(5);
        startYearPane.setPadding(new Insets(1));

        Label lblStartYear = new Label("Start Year");

        startYearPane.getChildren().addAll(lblStartYear, this.comboBoxStartYear);

        return startYearPane;
    }

    public HBox getEndYearPane() {
        HBox endYearPane = new HBox(5);
        endYearPane.setPadding(new Insets(1));

        Label lblEndYear = new Label("End Year");

        endYearPane.getChildren().addAll(lblEndYear, this.comboBoxEndYear);

        return endYearPane;
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

    public static String getSortOrder() {
        return comboBoxSortOrder.getSelectionModel().getSelectedItem().toString();
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

    public void initializeComboBoxSortOrderData() {

        String strTopTen = "Top Ten";
        String strBottomTen = "Bottom Ten";
        String strRandom = "Random";
        this.comboBoxSortOrder = new ComboBox<>();
        comboBoxSortOrder.getItems().addAll(strTopTen, strBottomTen, strRandom);

    }

    public static ScatterChart getScatterChartFilteredName(BabyName babyName) {

        int max = (int) Collections.max(babyName.listOfOcurrencesPerYear);

        // this is duplicate code, no time to refactor right now
        final NumberAxis xAxis = new NumberAxis(getStartYear() - 5, getEndYear() + 5, 1);
        final NumberAxis yAxis = new NumberAxis(0, max + 1000, 1000);
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

    public static ScatterChart getScatterChartTopThree(ArrayList<BabyName> list) {

        int max = (int) Collections.max(list.get(0).listOfOcurrencesPerYear);

        final NumberAxis xAxis = new NumberAxis(getStartYear() - 5, getEndYear() + 5, 1);
        final NumberAxis yAxis = new NumberAxis(0, max + 10000, 10000);
        final ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
        xAxis.setLabel("Years");
        yAxis.setLabel("Occurrances");
        scatterChart.setTitle("Top 3 Names");
        scatterChart.setStyle("-fx-font-weight: bold;-fx-font-size: 1.3em;");

        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        XYChart.Series series3 = new XYChart.Series();

        // populate the series with data
        getData(series1, list.get(0));
        getData(series2, list.get(1));
        getData(series3, list.get(2));

        scatterChart.getData().addAll(series1, series2, series3);

        return scatterChart;
    }

    public static void getData(XYChart.Series series, BabyName babyName) {

        if (rbFilterName.isSelected() == true) {

            series.setName(babyName.getSex());

        } else
            series.setName(babyName.getName());

        int year = getStartYear();
        int count = getEndYear() - getStartYear();
        for (int j = 0; j < count; j++) {
            series.getData().add(new XYChart.Data(year, babyName.listOfOcurrencesPerYear.get(j)));
            year++;
        }
    }

    public BabyName filterName(String name, ArrayList<BabyName> list) {

        int count = list.size();

        BabyName babyName = new BabyName(name);


        for (int i = 0; i < count; i++) {
           // for (int j = 0; j < count; j++) {
                if (babyName.equals(list.get(i))) {

                    babyName.listOfOcurrencesPerYear.add(list.get(i).getOccurrences());
                   // list.remove(i--);
                 //   count--;
                }
            //}
        }

        return babyName;
    }


    // for testing
    public static void printList(ArrayList<BabyName> list) {
        for (int i = 0; i < list.size(); i++)
            System.out.println(list.get(i).toString());
    }
}