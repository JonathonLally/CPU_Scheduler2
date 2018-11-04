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
        assignWaitAndTA();
        calculateTotals();
    }

    public SJFSim(int amount, int[] values) {       //Constructor for Fixed Data
        pArray = new SJFProcess[amount];
        numOfProcesses = amount;
        totalWait = 0;
        totalTATime = 0;
        fillFixedpArray(values);
        sortArray(pArray);
        assignWaitAndTA();
        calculateTotals();
    }

    private void fillpArray() {                                  //Fills pArray for RandomData
        Random r = new Random();
        for (int i = 0; i < numOfProcesses; i++) {
            pArray[i] = new SJFProcess(i, r.nextInt(10) + 1);
        }
    }

    private void sortArray(AProcess[] sjfArray) {          //Sorts Array by comparing burst times
        //Comparator is used to indicate burstTime
        Arrays.sort(sjfArray, (sjfProcess, t1) -> sjfProcess.getBurstTime() > t1.getBurstTime() ? 1 : -1);
    }

    private void fillFixedpArray(int[] in) {                     //Fills pArray for Fixed Data
        for (int i =0; i < numOfProcesses; i++) {
            pArray[i] = new SJFProcess(i, in[i]);
        }
    }

    private void assignWaitAndTA() {          //Assigns wait and turn around times to processes
        counter = 0;
        for (AProcess aPArray : pArray) {
            aPArray.setWaitTime(counter);
            aPArray.setTurnAroundTime(counter + aPArray.getBurstTime());
            counter += aPArray.getBurstTime();

        }

    }

    private void calculateTotals() {                             //Calculates Total Wait and Turn Around Time for Sim
        for (AProcess aPArray : pArray) {
            totalWait+= aPArray.getWaitTime();
            totalTATime+= aPArray.getTurnAroundTime();
        }
        calculateAverageWait();
        calculateAverageTA();

    }
}
