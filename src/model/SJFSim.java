package model;

import java.util.Arrays;
import java.util.Random;

public class SJFSim extends Sim {            //Shortest Job First Simulation

    public SJFSim(int amount) {             //Constructor for Random Data
        pArray = new SJFProcess[amount];
        numOfProcesses = amount;
        totalWait = 0;
        totalTATime = 0;
        fillpArray();
        sortArray(pArray);
        assignWaitAndTA(pArray);
        calculateTotals();
    }

    public SJFSim(int amount, int[] values) {       //Constructor for Fixed Data
        pArray = new SJFProcess[amount];
        numOfProcesses = amount;
        totalWait = 0;
        totalTATime = 0;
        fillFixedpArray(values);
        sortArray(pArray);
        assignWaitAndTA(pArray);
        calculateTotals();
    }

    public void fillpArray() {                                  //Fills pArray for RandomData
        Random r = new Random();
        for (int i = 0; i < numOfProcesses; i++) {
            pArray[i] = new SJFProcess(i, r.nextInt(10) + 1);
        }
    }

    public AProcess[] sortArray(AProcess[] sjfArray) {          //Sorts Array by comparing burst times
        //Comparator is used to indicate burstTime
        Arrays.sort(sjfArray, (sjfProcess, t1) -> sjfProcess.getBurstTime() > t1.getBurstTime() ? 1 : -1);
        return sjfArray;
    }

    public void fillFixedpArray(int[] in) {                     //Fills pArray for Fixed Data
        for (int i =0; i < numOfProcesses; i++) {
            pArray[i] = new SJFProcess(i, in[i]);
        }
    }

    public void assignWaitAndTA(AProcess[] sjfArray) {          //Assigns wait and turn around times to processes
        counter = 0;
        for (int i = 0; i<sjfArray.length; i++) {
            pArray[i].setWaitTime(counter);
            pArray[i].setTurnAroundTime(counter + pArray[i].getBurstTime());
        calculateAverageTA();
        calculateAverageWait();

    }

    public SJFSim(int amount, int[] values) {      //Overloaded Constructor for Fixed Values SJF Simulation
        pArray = new SJFProcess[amount];
        numOfProcesses = amount;
        counter = 0;
        totalWait = 0;
        totalTATime = 0;
        fixedBurstValues = values;
        fillFixedpArray();
        sortArray(pArray);
        calculateAverageWait();
        calculateAverageTA();

    }

    public AProcess[] sortArray(AProcess[] sjfArray) {          //Sorts Array by comparing burst times
        //Converted the comparator into a lambda expression
        Arrays.sort(sjfArray, (sjfProcess, t1) -> sjfProcess.getBurstTime() > t1.getBurstTime() ? 1 : -1);
        System.out.println("Sorting SJF");
        return sjfArray;
    }



    public void calculateTotals() {                             //Calculates Total Wait and Turn Around Time for Sim
        for (AProcess aPArray : pArray) {
            totalWait+= aPArray.getWaitTime();
            totalTATime+= aPArray.getTurnAroundTime();
        }
        calculateAverageWait();
        calculateAverageTA();

    }
}
