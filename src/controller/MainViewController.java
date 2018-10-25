package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.util.Arrays;

public class MainViewController {

    //FXML Variables
    @FXML    private Button startButton;
    @FXML    private TextField waitAverage;
    @FXML    private TextField taAverage;
    @FXML    private ComboBox<String> algorithmBox;
    @FXML    private TextField numOfProcessField;
    @FXML    private ComboBox<String> valueTypeBox;
    @FXML    private ListView processIDView;
    @FXML    private ListView priorityView;
    @FXML    private ListView burstView;
    @FXML    private ListView arrivalView;
    @FXML    private ListView waitView;
    @FXML    private ListView taView;
    @FXML    private TextArea outputArea;

    private int[] burstTimes;
    private int numOfProcesses;

    //FXML Methods
    @FXML
    void startSim(ActionEvent event) {           //This starts a simulation, activated by calculate button

        if (getAlgorithm() == "First Come First Serve") {
            setOutputArea("Starting First Come First Serve Simulation");
            startFCFS(getValueType());
        } else if (getAlgorithm() == "Shortest Job First") {
            setOutputArea("Starting Shortest Job First Simulation");
            startSJF(getValueType());
        } else if (getAlgorithm() == "Shortest Remaining First") {
            System.out.println("Starting SRF");
        } else if (getAlgorithm() == "Round Robin") {
            System.out.println("Starting RR");
        } else if (getAlgorithm() == "Priority Scheduling") {
            setOutputArea("Starting Priority Scheduling Simulation");
            startPS(getValueType());
        } else System.out.println("Invalid");

    }
    @FXML
    void initialize() {                             //Initializes MainView
        System.out.println("Initializing MainView");
        burstTimes = new int[10];
        populateComboBoxes();
        setOutputArea("Welcome to Process Project");
    }

    @FXML
    void checkFixed(ActionEvent event) {            //This method activates if "Fixed" is chosen from Data Type ComboBox
        if (getValueType() == "Fixed") {
            launchAddProcess();
        } else if (getValueType() == "Random") {
            clearViews();
            addProcesses();
        }

    }

    public void populateComboBoxes() {      //Populates ComboBoxes
        valueTypeBox.getItems().addAll("Random", "Fixed");
        valueTypeBox.getSelectionModel().select(0);
        algorithmBox.getItems().addAll("First Come First Serve", "Shortest Job First", "Shortest Remaining First",
                "Round Robin", "Priority Scheduling");
        algorithmBox.getSelectionModel().select(0);
    }

    public String getAlgorithm() {          //Gets value of Algorithm ChoiceBox
        String value = algorithmBox.getValue();
        return value;
    }

    public String getValueType() {          //Gets value of Values ChoiceBox
        return valueTypeBox.getValue();
    }

    public void launchAddProcess() {    //Launches Add Process Window for Fixed Values
        try {
            Stage secondaryStage = new Stage();                                                             //Stage for new window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddProcessView.fxml"));  //loader contains location
            Parent root = loader.load();                                                                    //root for new Scene
            AddProcessController launchCtrl = (AddProcessController)loader.getController();     //launchCtrl is controller object for add process

            Arrays.fill(burstTimes, 0);                   //Clears array from any old values
            launchCtrl.setBurstArray(burstTimes);           //Injecting Dependency


            secondaryStage.setScene(new Scene(root, 400, 400));                    //Sizes new window
            secondaryStage.showAndWait();                                          //Show new window, and waits for it to close
            addFixedDataToView(burstTimes, 10);         //Assume 10, shrinks numOfProcesses later

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public int getNumOfProcesses() {        //Gets number of processes from numofprocesses textfield RANDOM entry
        return Integer.parseInt(numOfProcessField.getText());
    }

    public void getNumOfFixedProcesses() {      //Gets number of processes from FIXED entry
        numOfProcesses = 0;
        for (int i=0; i<burstTimes.length;i++) {
            if (burstTimes[i] != 0) {
                numOfProcesses++;
            }
        }
        numOfProcessField.setText(Integer.toString(numOfProcesses));
    }

    public void clearViews() {      //Clears ListViews and Average fields
        processIDView.getItems().clear();
        priorityView.getItems().clear();
        burstView.getItems().clear();
        arrivalView.getItems().clear();
        waitView.getItems().clear();
        taView.getItems().clear();
        waitAverage.clear();
        taAverage.clear();
    }

    private void addProcessToView(AProcess[] pArray) {      //Writes processSim to GUI
        clearViews();

        int waitTime = 0;
        int burstTime;

        for(int i = 0; i < pArray.length; i++) {
            burstTime = pArray[i].getBurstTime();

            processIDView.getItems().add(pArray[i].getpId());
            //not too sure what you want to do for arrival so I'm leaving this here for now
            burstView.getItems().add(burstTime);
            taView.getItems().add(burstTime + waitTime);
            waitView.getItems().add(waitTime);
            waitTime += burstTime;

        }
    }

    private void addPSProcessToView(PSProcess[] psArray) {      //Writes Priority Scheduling to GUI
        clearViews();

        for(int i = 0; i < psArray.length; i++) {
            processIDView.getItems().add(psArray[i].getpId());
            burstView.getItems().add(psArray[i].getBurstTime());
            taView.getItems().add(psArray[i].getTurnAroundTime());
            waitView.getItems().add(psArray[i].getWaitTime());
            priorityView.getItems().add(psArray[i].getPriority());
        }
    }

    public void addFixedDataToView(int[] burstTimes, int numProcesses) {        //Writes fixed data to GUI
        clearViews();

        numOfProcessField.setText(Integer.toString(numProcesses));

        for(int i = 0; i < burstTimes.length; i++) {
            if (burstTimes[i] == 0)
                break;

            processIDView.getItems().add(i);
            burstView.getItems().add(burstTimes[i]);
        }
        getNumOfFixedProcesses();
    }

    public void addProcesses() {        //Adds pIDs to view if random is selected, just pIds no value until sim
        clearViews();
        numOfProcesses = getNumOfProcesses();

        for(int i = 0; i < numOfProcesses; i++) {
            processIDView.getItems().addAll(i);
        }
    }

    public void setOutputArea(String in) {                      //Writes to textArea in GUI, can use to write to users
        try {
            outputArea.clear();
            outputArea.setText(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startFCFS(String type) {                //Starts a first come first serve Sim
        FCFSSim fcfs;

        if(type == "Random") {
            fcfs = new FCFSSim(getNumOfProcesses());
            addProcessToView(fcfs.getpArray());
        } else if (type == "Fixed") {
            fcfs = new FCFSSim(numOfProcesses, burstTimes);
            addProcessToView(fcfs.getpArray());
        } else { fcfs = null; }
        waitAverage.setText(Double.toString(fcfs.getAverageWait()));
        taAverage.setText(Double.toString(fcfs.getAverageTA()));
    }


    public void startSJF(String type) {             //Starts a Shortest Job First Sim

        SJFSim sjf;

        if(type == "Random") {
            sjf = new SJFSim(getNumOfProcesses());
            addProcessToView(sjf.getpArray());
        } else if (type == "Fixed") {
            sjf = new SJFSim(numOfProcesses, burstTimes);
            addProcessToView(sjf.getpArray());
        } else { sjf = null; }      
        waitAverage.setText(Double.toString(sjf.getAverageWait()));
        taAverage.setText(Double.toString(sjf.getAverageTA()));
    }


    public void startPS(String type) {          //Starts a Priority Scheduling Simulation
        PSSim ps;

        if(type == "Random") {
            ps = new PSSim(getNumOfProcesses());
            addPSProcessToView(ps.getPSArray());

        } else if (type =="Fixed") {                //TODO Fixed Data PS
            ps = null;
        } else { ps = null;}
        waitAverage.setText(Double.toString(ps.getAverageWait()));
        taAverage.setText(Double.toString(ps.getAverageTA()));
    }

}
