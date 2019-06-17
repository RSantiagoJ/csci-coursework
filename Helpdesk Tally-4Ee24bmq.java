import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.OpenWeatherMap;
import org.json.JSONException;
import org.json.JSONObject;

public class HelpDeskTally extends Application {

    /**
     * @param args
     */

    int[] totals = new int[6];
    String city = "Holyoke";
    String state = "MA";
    String currentWeather;
    String moonPhase;
    String dayOfWeek;

    String username = System.getProperty("user.name");
    Path path = Paths.get("C:\\users\\"+username+"\\desktop\\Helpdesk Tally");
    File countFile = new File("C:\\users\\" + username + "\\desktop\\Helpdesk Tally\\tallyCount.txt");
    File logFile = new File("C:\\users\\" + username + "\\desktop\\Helpdesk Tally\\tallyLog.txt");

    RadioButton rbOS = new RadioButton();
    RadioButton rbAD = new RadioButton();
    RadioButton rbCall = new RadioButton();
    RadioButton rbEmail = new RadioButton();
    RadioButton rbWalkin = new RadioButton();
    TextArea taInfo = new TextArea();
    TextArea taStats = new TextArea();

    JSONObject json;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Application.launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        initializeFiles();
        fetchMoonPhase();
        fetchCurrentWeather(city);
        updateInfoPane();

        VBox gui = new VBox(03);
        gui.getChildren().addAll(getSelectionPane(), getInfoPane(), getStatusPane());

        Scene scene = new Scene(gui, 500, 500);
        primaryStage.setTitle("Helpdesk Tally Assistant");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    private VBox getSelectionPane() {
        VBox selectionPane = new VBox(10);
        selectionPane.setStyle("-fx-border-color: Black;");
        selectionPane.setAlignment(Pos.CENTER);
        selectionPane.setPadding(new Insets(10, 10, 10, 10));

        selectionPane.getChildren().addAll(getMessagePane(), getOptionPane());

        return selectionPane;
    }

    private VBox getServicePane() {

        VBox servicePane = new VBox(30);
        servicePane.setStyle("-fx-border-color: Black;");
        servicePane.setAlignment(Pos.TOP_LEFT);
        servicePane.setPadding(new Insets(20, 20, 20, 20));

        Text serviceText = new Text();
        serviceText.setText("Service");
        ToggleGroup serviceGroup = new ToggleGroup();

        rbOS.setText("Online Services");
        rbOS.setToggleGroup(serviceGroup);
        rbAD.setText("Active Directory");
        rbAD.setToggleGroup(serviceGroup);

        servicePane.getChildren().addAll(serviceText, rbOS, rbAD);
        return servicePane;
    }

    private VBox getIssuePane() {

        VBox issuePane = new VBox(30);
        issuePane.setStyle("-fx-border-color: Black;");
        issuePane.setAlignment(Pos.TOP_LEFT);
        issuePane.setPadding(new Insets(20, 20, 20, 20));

        Text issueText = new Text();
        issueText.setText("Issue");

        ToggleGroup serviceGroup = new ToggleGroup();

        rbCall.setText("Call");
        rbCall.setToggleGroup(serviceGroup);
        rbEmail.setText("Email");
        rbEmail.setToggleGroup(serviceGroup);
        rbWalkin.setText("Walk-in");
        rbWalkin.setToggleGroup(serviceGroup);

        issuePane.getChildren().addAll(issueText, rbCall, rbEmail, rbWalkin);
        return issuePane;
    }

    private VBox getButtonPane() {

        VBox buttonPane = new VBox(30);
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.setPadding(new Insets(10, 10, 10, 10));

        Button btSubmit = new Button("submit");

        btSubmit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                // parse selection to string
                incrementTotal(verifySelection());

                try {
                    updateCountFile();
                    updateLogFile(verifySelection());
                    updateInfoPane();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        buttonPane.getChildren().addAll(btSubmit);
        return buttonPane;
    }

    private HBox getOptionPane() {

        HBox optionPane = new HBox(20);
        optionPane.setAlignment(Pos.CENTER);
        optionPane.setPadding(new Insets(10, 10, 10, 10));

        optionPane.getChildren().addAll(getServicePane(), getIssuePane(), getButtonPane());
        return optionPane;
    }

    private HBox getMessagePane() {

        HBox messagePane = new HBox(10);
        messagePane.setAlignment(Pos.CENTER);

        Text txtMessage = new Text();
        txtMessage.setDisable(true);
        txtMessage.setText("Hello " + username + ", Make a selection");

        messagePane.getChildren().addAll(txtMessage);
        return messagePane;
    }

    private HBox getStatusPane() {

        HBox statusPane = new HBox(10);
        statusPane.setAlignment(Pos.CENTER);
        statusPane.setStyle("-fx-border-color: Black;");

        Text txtStatus = new Text();
        txtStatus.setText("In Progress");

        statusPane.getChildren().addAll(txtStatus);
        return statusPane;
    }

    private HBox getInfoPane() throws JSONException {

        HBox infoPane = new HBox();
        infoPane.setAlignment(Pos.CENTER);
        taStats.setEditable(false);
        taInfo.setEditable(false);

        infoPane.setPrefHeight(500);

        updateInfoPane();

        infoPane.getChildren().addAll(taInfo, taStats);

        return infoPane;
    }

    private void initializeFiles() throws IOException {

    	
    	if (Files.exists(path)){
    		initializeArray();
    	}else{
    		path = Paths.get("C:\\users\\"+username+"\\desktop\\Helpdesk Tally");
    		Files.createDirectories(path);
    		countFile.createNewFile();
    		logFile.createNewFile();
    	}
    }

    private void initializeArray() throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(countFile));

        String total = null;
        for (int i = 0; i < 6; ++i) {
            total = br.readLine();
            int count = Integer.parseInt(total);
            totals[i] = count;
        }
        br.close();
    }

    private void incrementTotal(String selection) {

        if (selection.contains("Online Services")) {

            if (selection.contains("Call")) {
                totals[0]++;
            }

            if (selection.contains("Email")) {
                totals[1]++;
            }

            if (selection.contains("Walkin")) {
                totals[2]++;
            }
        } else if (selection.contains("Active Directory")) {

            if (selection.contains("Call")) {
                totals[3]++;
            }

            if (selection.contains("Email")) {
                totals[4]++;
            }

            if (selection.contains("Walkin")) {
                totals[5]++;
            }
        }
    }

    private String verifySelection() {

        String selection = null;

        if (rbOS.isSelected()) {
          selection= "Online Services";
        }

        if (rbAD.isSelected()) {
           selection= "Active Directory";
        }
  
        if (rbCall.isSelected()) {
            selection+= (" Call");
        }

        if (rbEmail.isSelected()) {
            selection+=(" Email");
        }

        if (rbWalkin.isSelected()) {
            selection+=(" Walkin");
        }
        return (selection);
    }

    private void updateCountFile() throws IOException {

        FileOutputStream fos = new FileOutputStream(countFile);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        for (int i = 0; i < 6; i++) {
            bw.write(Integer.toString(totals[i]));
            bw.newLine();
        }
        bw.close();
    }

    private void updateLogFile(String selection) {

        try (FileWriter fw = new FileWriter(logFile, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(fetchDate("MM/dd/yyyy HH:mm:ss") + " " + selection);
            bw.close();

        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }

    private String fetchDate(String format) {

        // " MM/dd/yyyy HH:mm:ss"
        DateFormat df = new SimpleDateFormat(format);
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);

        return reportDate;
    }

    private void fetchCurrentWeather(String location) {

        OpenWeatherMap owm = new OpenWeatherMap("ea950a82a1470f29811e9fe0592d6afa");

        CurrentWeather cwd = null;
        try {
            cwd = owm.currentWeatherByCityName(location);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        currentWeather = (location + "\nTemperature: " + cwd.getMainInstance().getMaxTemperature()
                + "/" + cwd.getMainInstance().getMinTemperature() + "\'F\nHumidity: " + cwd.getMainInstance().getHumidity()) + "\nPressure: " + cwd.getMainInstance().getPressure();
    }

    private void updateInfoPane() throws JSONException {
        taInfo.setText("Online Services\nCalls: " + totals[0] + "\nEmails: " + totals[1] + "\nWalkin: " + totals[2] + "\n\nActive Directory\nCalls: " + totals[3] + "\nEmails: " + totals[4] + "\nWalkin: " + totals[5]);
        taStats.setText(fetchDate("MM/dd/yyyy") + "\n" + dayOfWeek + "\n\n" + currentWeather + "\n\nlunar phase: " + moonPhase);
    }
    
    private void fetchMoonPhase() throws IOException, JSONException {
        json = readJsonFromUrl("http://api.usno.navy.mil/rstt/oneday?date=" + fetchDate("MM/dd/yyyy") + "&loc=" + city + ",%20" + state);
        Object phase = json.get("curphase");
        Object day = json.get("dayofweek");
        
        moonPhase = phase.toString();
        dayOfWeek = day.toString();  
    }
    
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

}