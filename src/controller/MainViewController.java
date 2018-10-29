package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.text.NumberFormat;
import java.util.ArrayList;
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

    private TextInputDialog input = new TextInputDialog();
    private Alert error = new Alert(Alert.AlertType.ERROR);
    private int[] burstTimes, priorities;
    private int numOfProcesses;
    private int quantum = 0;

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
            setOutputArea("Starting Shortest Remaining First Simulation");
            startSRT(getValueType());
        } else if (getAlgorithm() == "Round Robin") {
            setOutputArea("Starting Round Robin");
            startRR(getValueType());
        } else if (getAlgorithm() == "Priority Scheduling") {
            setOutputArea("Starting Priority Scheduling Simulation");
            startPS(getValueType());
        } else setOutputArea("Invalid");

    }
    @FXML
    void initialize() {                             //Initializes MainView
        setOutputArea("Initializing MainView");
        burstTimes = new int[10];
        priorities = new int[10];
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


    @FXML
    private void checkRR(ActionEvent event) {
        if(getAlgorithm().equals("Round Robin"))
            displayInput("Input the time quantum desired");
    }

    public void populateComboBoxes() {      //Populates ComboBoxes
        valueTypeBox.getItems().addAll("Random", "Fixed");
        valueTypeBox.getSelectionModel().select(0);
        algorithmBox.getItems().addAll("First Come First Serve", "Shortest Job First", "Shortest Remaining First",
                "Round Robin", "Priority Scheduling");
        algorithmBox.getSelectionModel().select(0);
    }

    private String getAlgorithm() {          //Gets value of Algorithm ChoiceBox
        return algorithmBox.getValue();
    }

    public String getValueType() {          //Gets value of Values ChoiceBox
        return valueTypeBox.getValue();
    }

    public void launchAddProcess() {    //Launches Add Process Window in a New Window for Fixed Values
        try {
            Stage secondaryStage = new Stage();                                                             //Stage for new window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddProcessView.fxml"));  //loader contains location
            Parent root = loader.load();                                                                    //root for new Scene
            AddProcessController launchCtrl = loader.getController();     //launchCtrl is controller object for add process

            Arrays.fill(burstTimes, 0);                   //Clears array from any old values
            Arrays.fill(priorities, 0);
            launchCtrl.setBurstArray(burstTimes);           //Injecting Dependency
            launchCtrl.setPriorityArray(priorities);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/resources/material-fx-v0_3.css").toExternalForm());
            secondaryStage.setScene(scene);
            //secondaryStage.setScene(new Scene(root, 400, 400));                    //Sizes new window
            secondaryStage.showAndWait();                                          //Show new window, and waits for it to close
            addFixedDataToView(burstTimes, priorities, 10);         //Assume 10, shrinks numOfProcesses later

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void launchAboutView() {     //Launches About us window
        try {
            Stage thirdStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AboutView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/resources/material-fx-v0_3.css").toExternalForm());
            thirdStage.setScene(scene);
            thirdStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private int getNumOfProcesses() {                        //Gets number of processes from numofprocesses textfield RANDOM entry
        return Integer.parseInt(numOfProcessField.getText());
    }

    public void getNumOfFixedProcesses() {                  //Gets number of processes from FIXED entry
        numOfProcesses = 0;
        for (int i=0; i<burstTimes.length;i++) {
            if (burstTimes[i] != 0) {
                numOfProcesses++;
            }
        }
        numOfProcessField.setText(Integer.toString(numOfProcesses));
    }

    private void clearViews() {                              //Clears ListViews and Average fields
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
            burstView.getItems().add(burstTime);
            taView.getItems().add(burstTime + waitTime);
            waitView.getItems().add(waitTime);
            waitTime += burstTime;
        }
    }

    private void addRRProcessToView(AProcess[] pArray) {      //Writes processSim to GUI
        clearViews();

        for(int i = 0; i < pArray.length; i++) {

            processIDView.getItems().add(pArray[i].getpId());
            //not too sure what you want to do for arrival so I'm leaving this here for now
            burstView.getItems().add(pArray[i].getBurstTime());
            taView.getItems().add(pArray[i].getTurnAroundTime());
            waitView.getItems().add(pArray[i].getWaitTime());

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

    private void addSRTProcessToView(SRTProcess[] psArray) {    //Writes SRT Process to View
        clearViews();

        for (SRTProcess aPsArray : psArray) {
            processIDView.getItems().add(aPsArray.getpId());
            burstView.getItems().add(aPsArray.getBurstTime());
            taView.getItems().add(aPsArray.getTurnAroundTime());
            waitView.getItems().add(aPsArray.getWaitTime());
            arrivalView.getItems().add(aPsArray.getArrivalTime());
        }
    }

    public void addFixedDataToView(int[] burstTimes, int[] priorities, int numProcesses) {        //Writes fixed data to GUI
        clearViews();

        numOfProcessField.setText(Integer.toString(numProcesses));

        for(int i = 0; i < burstTimes.length; i++) {
            if (burstTimes[i] == 0)
                break;

            processIDView.getItems().add(i);
            burstView.getItems().add(burstTimes[i]);

            if(priorities[i] != 0) //prevents addition of priorities when it is not accounted for
                priorityView.getItems().add(priorities[i]);
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

    private String generateListStr(ArrayList<RRProcess> list) {
        StringBuilder listStr = new StringBuilder();

        for (AProcess process : list)
            listStr.append("[P").append(process.getpId()).append(" | Burst: ").append(process.getBurstTime()).
                    append("]\n");

        return listStr.toString();
    }

    public void setOutputArea(String in) {                      //Writes to textArea in GUI, can use to write to users
        try {
            outputArea.clear();
            outputArea.setText(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayInput(String content) {
        input.setHeaderText("Define Time Quantum");
        input.setContentText(content);
        input.showAndWait();

        try {
            quantum = Integer.parseInt(input.getEditor().getText());
        } catch (NumberFormatException ex) {
            displayError("Invalid Input","Please input a valid integer.");
        }

        if(quantum < 1)  //easter egg
            displayError("!!ONE CANNOT ESCAPE TIME!!", "T̴̢̡͉̺͔̦͈̱ͨ̐͒̉̈̄ͫ͗̈́̌̐͒ͦ͋́ͅẖ̰̤͚̥͓̲̹̝̖̆̈́ͦ̽̒̍̊͐͐̏͗͐̂̄͢͟eͧ͑̓́" +
                    "̊̓̇ͬ̃ͯ̌͊̓͊̓͊̄͛ͮ͏̥̖͙͇̜̘͔̀͜ ̞̞͈̟͆͂͆̈́́͠t͖͓̱̥̭̭̗͍͍̬ͦ̑̓̐́ͥ̅ͣͧ͐̆̔͜͟ȋ̛̋ͥͣͥ͝͝҉̘̲̳̦̣̙̜̺̭̻̱̣̥̱̣m̨̡̨̡̠̃̎̇̍͋ͦ͌͂̆̌ͫ͋͂͞" +
                    "͉͔̞͎̳̗̰͇̪̩̭̗̭̝̫̩e͊ͧ͌ͪͫ͐̂̓ͥͪ̎ͯ͗ͭ̏͠҉̵̛͍̲͔ͅ ͕̪͉̻͚̫͙͕̼̪̪̻͔̯̗͕̜͎̮̽͒͒ͥ̃̐ͧͬ̿ͮ̿̓̽̋͐ͤ͠͞iͧ̈́͑ͣ̐̊̉̋ͦ̅ͥ̌ͭ̊̄͏̷̀͘" +
                    "̞͕̠̱̲̘̯̜̦̣sͥ͒͐ͧ̏ͫ̈̆͌҉̨͕͉̠͚̞ ̾̆͛͂̇͋̿̽͒ͥͯ̐̅͗͌̚͘͏̴̴̷̮̝̗̠̜̝͍͕̹̣̥̻͙̟͖g̢̩̺̦̣͎͑̂̾̎ͣͤ̾ͦͤ̌̌͛̉͆͘͜͠͝o̿ͫ̓͂͛̐̄̃ͭ͗̊" +
                    "̠͉̻̻͖͉̮̗̺̯̟͈͖̗ͮ͋̿ͦ̀͝n̨̧̛͖͎̮̼̻͎̭̐̆̒̄̉͟͢ͅͅḝͥͫͧ̐̊͏͇̱͇̼͍͖,ͭ͐ͮ̃̆͢͏̜͈̝͉̭͍͈͙̀́͠ͅͅ ̵̦̠͚͔͍̪ͭ͒ͧ̍ͨ̀͟͞t̵̓ͩ͆̊̆ͪ́̑͏" +
                    "̙̬̱̖̝͎̫̮͈̱̖̲͖͍h̰̞̳̠͕̳̠̙ͧͥ̐͛̓͋̋͢ͅe͍̪͍̞͉̝̜̜̅ͧͦ̒́̓̑̈̀͠͠ ͌͐̏ͨ̂ͨ̿̒̆̈͊͌͆͢͡҉̮̱̯͜s̶̖̼͕͖̺̙̏̓̐̾̅͒ͬ̐̍̈̈́͐̍ͯ̚" +
                    "ơ̩͈̮͎̳̬̱͈̥͚͖̺͙̻̙͙̞̳ͧ͛̉́̀n̴͎̫̣͕̻̬͋ͣ̿ͨͤ͑ͨ̃̒́̈́͆̐̚͜͜͝͝g̸̢̨̤̬̮̦͔͔̹͕̟̮̼̥͕ͮ̓̈́ͭ͟ ̧̧̘̬̲̳̜̹̫̻̣͆͋͊̈́ͥ̒̓̅͜͠͠i͂ͤ̑ͨ̂" +
                    "̢̨̱̟̰͎̘̲̲̓͊̌̋͗ͮ́́̀͗̓͛̽ṣ̷̢̦̣͈̦̬͙̻̗̗̣ͫͣ̑͆̏̇̀̐ͧͫ͜ ̶͕͖̫̞͎͕̫̜̻͕̫̱̥̙͍̏̇ͣ͌̍̎ͭͮ̎͆̏͐ͮͮ̕͘͢͡ȯ̀̀̓ͣ̾ͤ̾̅̉͑ͩ̌ͪ͏҉̳̫̣̭̭͙͕̝͙" +
                    "̦̠͔v̶̢̘̦̞̠̜̯͖̙̒ͬ́̔͘͞éͧ̒̓ͩ̽͋̿ͣ͒̔ͪ̔̿҉͞҉̢͕͓̦̱̪̜͘r̵̶̛̥̭̯͚̥͎͉ͣ̄ͬ͌̎ͦ̑̇̀ͮ̈ͨ̓̚.̦̮̻̱͔̮͍ͯ̃ͭ̒ͯ̆ͤ̄ͫ̾̊̒ͤͭ̾̿́̕ ̐ͯ" +
                    "̵̶̛̤͕͈̬̝̤̲̥̪̗̎͘͡T̡͍̮̗̺͆̅̅̋ͣ̒̇̎̌ͪ̕͟h̷̴̴̷̭̩͎̹̲̲̖̰̲ͩ̋͒̓̍̒ͫͭ̚ơ̢̨̫̠͖͓̱̘ͣ̊̐̑͐̋ͥͧ͋ͬ̎ͮ͆̐̽ͤ̐̌̀͞uͤ͒̈͆̇ͥ͛͑ͣ́" +
                    "̸̞̖̱̯̜̞͕̮̩̰̓̓͐ͨ͌̀́̚̚g̅ͭ͌̓̔̏͒̀̑͂͐͋͗̓̒͐ͦͯ͡҉̸̖͙̩̝̘̮͎̗͖͉̺̙̤̤̖h̷̢̜̲͙̹͙̗̺̗̻͈̦͛͒̾͒͛͋̿͗̓̏̒͋̂̇ͭ͠t̷̨̃ͫͩͤ̃́҉̬̣̥̳̕͢" +
                    "̗̤͕̠͍͖̠̯͉̠̲̰̦̼ ̶̡̮̭̦̦͇̬͉͔̺̫͈̟͌͗ͫͧ͊̍̔̆ͯ̌͗ͤ̓ͧ̃͌͗̌̓͢ͅͅĮ̛͎̥̮̩̞̠̫̺͓̘̎̌̒̊ͧ̒̃ͥ̓̏ͩ͌̃͢͢͡'̸̴̼̟̩̮̻̟̩̅ͩͦ̏̇͂͂ͥ̉" +
                    "͚͈̟d̸̡̦͎̘̳͔͙͉͍̗̮̓̅ͩ͋̑͗̃ͥ̍ͤ͋̇̇ͧ̚͘͢ ̴̝̼̟͖̣̗̦͓͕͈̥̣̥̤̺̜̪̬̙̎̃̅̃̈͂̎ͪ͑͐̕s̹̬͉͕̪̲̥̹͙̫̖̯͕̾̃̅ͤ̓̕͠o̸̿̄ͭ́ͣͤ̂̃̉̓̐̋̇͒̀̚͢͞" +
                    "̧̘̫̻͓̜͚̰̙̝͙͉̣̬̭m͒̅ͨͪͤ͂҉̨̨̠̹͖͇̺̱̣̥̞̥̫͈͉̳͚̕͡ę̡̰̫̞̩͉̻̼̲̝̗͈̗̼̆̋͒̈ͪͤ̔̍̉̃͗ͬ̔̄ț̷͈͕̹̺̠̝̥̲ͧͨ̒̅̀ͯ̏̃ͧ̍̊ͪ̂͐̒͛ͯ̒͂͜͞" +
                    "̫̹̪̪̘̭h̶̡̖̖̼͚̯̠̤̺̙̦̟̥̳̼̺̫͗ͯͮ̎̊ͥ̒ͪͫ̽̿̽̋ͪ̽͌͋͝͡i̸̮͍̣̦̮̙͙̙̲͆̌̐̓̿̆̽́͘͠n̴̬̬̻̗͎̺̯̠̙̩̮͕̝̗ͪ̉̐̽̍͒ͣ̽̓̇̕͡ͅǵ͆̎ͤ̈" +
                    "̸̶̵̺͓̹̹͈̗̜͇̬͇͇̞̳̮̹̙̟̳̖̓̒̊̿ͪ͋̍̄ͮ̚ ̦̱͈ͨͨ̋̍̐ͥ̆̔̌ͧ̅ͪͥ͋̾͒ͫ͗͞m̛͍̻͙̜̗̌̄ͤ̋͌̾ͮ̅͛͊̉͟͝o̴̭̠̘̘̜͓̙̩͓̜̹̫ͭ́̋͛̂́͘̕͢" +
                    "͈̦̙̯͈r̷͕͖͖̠̣̪̪͈͔͍̻ͭͧ̽̒͒̂̓̄̇̔̽ͧ̚͘͘ë̸̘͉̟̟͉̬͈̱͇͕̓̃ͭ̍̔̽ͧ͘͡ͅ ̍̃̄͐͆̅ͭ̇̆̊͆̿ͭͯ̍ͯͭ͢͝͏̵̳̻̗͚̟̤̹͓̝̲t̒ͬ͌ͭ̌̏̋̂͊ͫͦ̿ͦ" +
                    "ͨ̄҉̪̻͇͙͢͢ő̑̏̅̐ͣ̆͂̎̋̌́҉͏̶͚͍͓̠̺̹̺̭̣͕͙̮͘ͅ ̨̠̣̼̩̹̬͉͍̥̜̮̝̠͈͚͍͎̍ͮͣ̃̄͑͋ͨ͋̇̆̃̋̾̈́ͦ̒͌͘͠͝sͭ̓ͥͦ͌́͏̡̧̫̯̳͙̀͞ͅ" +
                    "͎̯̠ą̛̼͙̫̺̖̫͇̱͋̉̅̏͑͛̇̑̑̽͐ͧ͋ͧͮ̀͝͡ỳ̴̴̧̯̲̰͚̼̼̥̦͈̤͔̞̭͖̱͍ͧ͌ͨ̊̀ͥ́ͅ.̸̨ͧͧͤ͆̀̓ͦ͂҉̹͓̝̦͙̫̟̲͇ͅ");


    }

    private void displayError(String head, String content) {
        error.setHeaderText(head);
        error.setContentText(content);
        error.showAndWait();
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

        } else if (type =="Fixed") {
            ps = new PSSim(numOfProcesses, burstTimes, priorities);
            addPSProcessToView(ps.getPSArray());
        } else { ps = null;}
        waitAverage.setText(Double.toString(ps.getAverageWait()));
        taAverage.setText(Double.toString(ps.getAverageTA()));
    }

    public void startSRT(String type) {             //Starts a Shortest Remaining Time Simulation
        SRTSim srt;

        if(type == "Random") {
            srt = new SRTSim(getNumOfProcesses());
            addSRTProcessToView(srt.getsrtpArray());
        } else if (type == "Fixed") {
            srt = new SRTSim(numOfProcesses, burstTimes);
            addSRTProcessToView(srt.getsrtpArray());
        } else {srt = null; }
        waitAverage.setText(Double.toString(srt.getAverageWait()));
        taAverage.setText(Double.toString(srt.getAverageTA()));
    }

    public void exit() {        //Closes Application
        Platform.exit();
    }

    public void startRR(String type) {          //Starts a Round Robin Simulation
        RRSim rr;

        if(type == "Random") {
            rr = new RRSim(getNumOfProcesses(), quantum);
            addRRProcessToView(rr.getpArray());
            setOutputArea(outputArea.getText() + "\n" + generateListStr(rr.getOrderList()));
        } else if(type.equals("Fixed")) {
            rr = new RRSim(numOfProcesses, burstTimes, quantum);
            addRRProcessToView(rr.getpArray());
            setOutputArea(outputArea.getText() + "\n" + generateListStr(rr.getOrderList()));
        } else { rr = null; };
        waitAverage.setText(Double.toString(rr.getAverageWait()));
        taAverage.setText(Double.toString(rr.getAverageTA()));
    }

}
