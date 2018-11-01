package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


//Mostly Brendan Kiernan, closeWindow() and setters added by Jonathon Lally
public class AddProcessController {

    @FXML    private TextField p0BurstField;
    @FXML    private TextField p1BurstField;
    @FXML    private TextField p2BurstField;
    @FXML    private TextField p3BurstField;
    @FXML    private TextField p4BurstField;
    @FXML    private TextField p5BurstField;
    @FXML    private TextField p6BurstField;
    @FXML    private TextField p7BurstField;
    @FXML    private TextField p8BurstField;
    @FXML    private TextField p9BurstField;
    @FXML    private TextField p0PriorityField;
    @FXML    private TextField p1PriorityField;
    @FXML    private TextField p2PriorityField;
    @FXML    private TextField p3PriorityField;
    @FXML    private TextField p4PriorityField;
    @FXML    private TextField p5PriorityField;
    @FXML    private TextField p6PriorityField;
    @FXML    private TextField p7PriorityField;
    @FXML    private TextField p8PriorityField;
    @FXML    private TextField p9PriorityField;
    @FXML    private Button cancelBtn;
    @FXML    private Button confirmBtn;
    @FXML    private TextField numProcessField;
    @FXML    private Button minusBtn;
    @FXML    private Button plusBtn;

    private int[] burstTimes, priorities;
    private TextField[] burstTimeFields, priorityFields;
    private int numProcesses = 0;

    @FXML
    void initialize() {     //Initializes AddProcess
        System.out.println("Initializing AddProcess");
                                                                        //Gets MainViewController
        burstTimeFields = new TextField[]{p0BurstField, p1BurstField, p2BurstField, p3BurstField, p4BurstField,
                p5BurstField, p6BurstField, p7BurstField, p8BurstField, p9BurstField};
        priorityFields = new TextField[]{p0PriorityField, p1PriorityField, p2PriorityField, p3PriorityField,
                p4PriorityField, p5PriorityField, p6PriorityField, p7PriorityField, p8PriorityField, p9PriorityField};
        numProcesses = 0;
        numProcessField.setText("1");

        //Hides TextFields not in use (in this case, all except P0's)
        for(int i = 0; i < burstTimeFields.length - 1; i++) {
            burstTimeFields[i + 1].setVisible(false);
            priorityFields[i + 1].setVisible(false);
        }

    }


    @FXML
    private void cancelBtnPressed(ActionEvent event) {
        closeWindow();

    }

    @FXML
    private void confirmBtnPressed(ActionEvent event) {

        for(int i = 0; i < numProcesses + 1; i++) {
            try {
                burstTimes[i] = Integer.parseInt(burstTimeFields[i].getText());
                priorities[i] = Integer.parseInt(priorityFields[i].getText());
            } catch (NumberFormatException ex) {
                System.out.println(ex.getMessage());
            }
        }
        closeWindow();

    }

    @FXML
    private void minusBtnPressed(ActionEvent event) {
        if(numProcesses != 0) {
            switchVisibility(numProcesses);
            numProcesses--;
            numProcessField.setText(Integer.toString(numProcesses + 1));
        }
    }

    @FXML
    private void plusBtnPressed(ActionEvent event) {
        if(numProcesses != 9) {
            numProcesses++;
            switchVisibility(numProcesses);
            numProcessField.setText(Integer.toString(numProcesses + 1));
        }
    }

    public void setBurstArray(int [] burst) {
        this.burstTimes = burst;
    }

    public void setPriorityArray(int[] priorities) { this.priorities = priorities; }

    public void switchVisibility(int index) {
        TextField[] toSwitch = {burstTimeFields[index], priorityFields[index]};

        //I do if-else's with brackets weird, I know, feel free to change if you wish
        if(toSwitch[0].isVisible()) {
            toSwitch[0].setVisible(false);
            toSwitch[1].setVisible(false);
        } else {
            toSwitch[0].setVisible(true);
            toSwitch[1].setVisible(true);
        }
    }

    public void closeWindow() {
        Stage stage = (Stage) confirmBtn.getScene().getWindow();
        stage.close();
    }


}
