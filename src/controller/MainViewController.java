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

        switch (getAlgorithm()) {
            case "First Come First Serve":
                setOutputArea("Starting First Come First Serve Simulation");
                startFCFS(getValueType());
                break;
            case "Shortest Job First":
                setOutputArea("Starting Shortest Job First Simulation");
                startSJF(getValueType());
                break;
            case "Shortest Remaining First":
                setOutputArea("Starting Shortest Remaining First Simulation");
                startSRT(getValueType());
                break;
            case "Round Robin":
                setOutputArea("Starting Round Robin");
                startRR(getValueType());
                break;
            case "Priority Scheduling":
                setOutputArea("Starting Priority Scheduling Simulation");
                startPS(getValueType());
                break;
            default:
                setOutputArea("Invalid");
                break;
        }

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
        if (getValueType().equals("Fixed")) {
            launchAddProcess();
        } else if (getValueType().equals("Random")) {
            clearViews();
            addProcesses();
        }

    }

    @FXML
    private void checkRR(ActionEvent event) {       //Checks if RR is selected in the algorithmBox ComboBox
        if(getAlgorithm().equals("Round Robin"))
            inputQuantum();
    }

    private void populateComboBoxes() {      //Populates ComboBoxes
        valueTypeBox.getItems().addAll("Random", "Fixed");
        valueTypeBox.getSelectionModel().select(0);
        algorithmBox.getItems().addAll("First Come First Serve", "Shortest Job First", "Shortest Remaining First",
                "Round Robin", "Priority Scheduling");
        algorithmBox.getSelectionModel().select(0);
    }

    public void launchAddProcess() {    //Launches Add Process Window in a New Window for Fixed Values
        try {
            Stage secondaryStage = new Stage();                                                              //Stage for new window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddProcessView.fxml"));  //loader contains location
            Parent root = loader.load();                                                                    //root for new Scene
            AddProcessController launchCtrl = loader.getController();     //launchCtrl is controller object for add process

            Arrays.fill(burstTimes, 0);                 //Clears array from any old values
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

    private void addProcesses() {        //Adds pIDs to view if random is selected, just pIds no value until sim
        clearViews();
        numOfProcesses = getNumOfProcesses();

        for(int i = 0; i < numOfProcesses; i++) {
            processIDView.getItems().addAll(i);
        }
    }

    private int getNumOfProcesses() {                        //Gets number of processes from numofprocesses textfield RANDOM entry
        try {
            int parsed = Integer.parseInt(numOfProcessField.getText());

            if(parsed < 1 || parsed > 10) {
                displayError("Out of Range", "Please enter an integer between 1 and 10.");
                return 1; //defaults to 1 since I don't want to check in every startSim method
            }

            return parsed;
        } catch (NumberFormatException ex) {
            displayError("Invalid Format", "Please enter an integer between 1 and 10.");
            return 1;
        }
    }

    private void getNumOfFixedProcesses() {                  //Gets number of processes from FIXED entry
        numOfProcesses = 0;

        for (int burstTime : burstTimes)
            if (burstTime != 0)
                numOfProcesses++;

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

        for(AProcess process : pArray) {
            burstTime = process.getBurstTime();
            processIDView.getItems().add(process.getpId());
            burstView.getItems().add(burstTime);
            taView.getItems().add(burstTime + waitTime);
            waitView.getItems().add(waitTime);
            waitTime += burstTime;
        }
    }

    private void addRRProcessToView(AProcess[] pArray) {      //Writes Round Robin to GUI
        clearViews();

        for(AProcess process : pArray) {

            processIDView.getItems().add(process.getpId());
            burstView.getItems().add(process.getBurstTime());
            arrivalView.getItems().add(((RRProcess)process).getArrivalTime());
            taView.getItems().add(process.getTurnAroundTime());
            waitView.getItems().add(process.getWaitTime());

        }
    }

    private void addPSProcessToView(PSProcess[] psArray) {      //Writes Priority Scheduling to GUI
        clearViews();

        for(PSProcess process : psArray) {
            processIDView.getItems().add(process.getpId());
            burstView.getItems().add(process.getBurstTime());
            taView.getItems().add(process.getTurnAroundTime());
            waitView.getItems().add(process.getWaitTime());
            priorityView.getItems().add(process.getPriority());
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

    private void addFixedDataToView(int[] burstTimes, int[] priorities, int numProcesses) {        //Writes fixed data to GUI
        clearViews();

        numOfProcessField.setText(Integer.toString(numProcesses));

        for(int i = 0; i < burstTimes.length; i++) {
            if (burstTimes[i] == 0)
                break;

            processIDView.getItems().add(i);
            burstView.getItems().add(burstTimes[i]);

            if(priorities[i] != 0) //prevents addition of priorities when they are not accounted for
                priorityView.getItems().add(priorities[i]);
        }

        getNumOfFixedProcesses();
    }

    private void startFCFS(String type) {                //Starts a First Come First Serve Sim
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


    private void startSJF(String type) {             //Starts a Shortest Job First Sim
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


    private void startPS(String type) {          //Starts a Priority Scheduling Simulation
        PSSim ps;

        if(type.equals("Random")) {
            ps = new PSSim(getNumOfProcesses());
            addPSProcessToView(ps.getPSArray());
        } else if (type.equals("Fixed")) {
            ps = new PSSim(numOfProcesses, burstTimes, priorities);
            addPSProcessToView(ps.getPSArray());
        } else { ps = null;}

        waitAverage.setText(Double.toString(ps.getAverageWait()));
        taAverage.setText(Double.toString(ps.getAverageTA()));
    }

    private void startSRT(String type) {             //Starts a Shortest Remaining Time Simulation
        SRTSim srt;

        if(type.equals("Random")) {
            srt = new SRTSim(getNumOfProcesses());
            addSRTProcessToView(srt.getsrtpArray());
        } else if (type.equals("Fixed")) {
            srt = new SRTSim(numOfProcesses, burstTimes);
            addSRTProcessToView(srt.getsrtpArray());
        } else {srt = null; }

        waitAverage.setText(Double.toString(srt.getAverageWait()));
        taAverage.setText(Double.toString(srt.getAverageTA()));
    }

    private void startRR(String type) {          //Starts a Round Robin Simulation
        RRSim rr;
        outputArea.clear();

        if(type.equals("Random")) {
            rr = new RRSim(getNumOfProcesses(), quantum);
            addRRProcessToView(rr.getpArray());
            setOutputArea(generateListStr(rr.getOrderList())); //gives order of processes
        } else if(type.equals("Fixed")) {
            rr = new RRSim(numOfProcesses, burstTimes, quantum);
            addRRProcessToView(rr.getpArray());

            setOutputArea(generateListStr(rr.getOrderList()));
        } else { rr = null; }

        waitAverage.setText(Double.toString(rr.getAverageWait()));
        taAverage.setText(Double.toString(rr.getAverageTA()));
    }

    private String generateListStr(ArrayList<RRProcess> list) {
        StringBuilder listStr = new StringBuilder();

        for (AProcess process : list)
            listStr.append("[P").append(process.getpId()).append(" | Burst: ").append(process.getBurstTime()).
                    append("]\n");

        return listStr.toString();
    }

    private void inputQuantum() {
        displayInput("Define Time Quantum", "Please enter the desired quantum");

        try {
            quantum = Integer.parseInt(input.getEditor().getText());

            if(quantum < 1) { //easter egg
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

                algorithmBox.getSelectionModel().select(0); //selects FCFS
                quantum = 0;
            }
        } catch (NumberFormatException ex) {
            displayError("Invalid Input","Please input a valid integer.");
            algorithmBox.getSelectionModel().select(0);
        }
    }

    private void setOutputArea(String in) {                      //Writes to textArea in GUI, can use to write to users
        try {
            outputArea.clear();
            outputArea.setText(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayInput(String head, String content) {    //sets text for the InputDialog and displays it
        input.setHeaderText(head);
        input.setContentText(content);
        input.showAndWait();
    }

    private void displayError(String head, String content) {    //sets text for the Alert and displays it
        error.setHeaderText(head);
        error.setContentText(content);
        error.showAndWait();
    }

    public void exit() {        //Closes Application
        Platform.exit();
    }

    private String getAlgorithm() {          //Gets value of Algorithm ChoiceBox
        return algorithmBox.getValue();
    }

    private String getValueType() {          //Gets value of Values ChoiceBox
        return valueTypeBox.getValue();
    }

}
