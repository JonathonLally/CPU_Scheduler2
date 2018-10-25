package model;

import java.util.Arrays;
import java.util.Random;

public class SJFSim extends Sim {

    public SJFSim(int amount) {        //Constructor for Shortest Job First Simulation
        pArray = new SJFProcess[amount];
        numOfProcesses = amount;
        counter = 0;
        totalWait = 0;
        totalTATime = 0;
        fillpArray();
        sortArray(pArray);
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

    public void fillpArray() {          //This fills pArray with random values
        Random r = new Random();
        for (int i =0; i < numOfProcesses; i++) {
            pArray[i] = new SJFProcess(i, r.nextInt(10) + 1);
            pArray[i].setWaitTime(counter);
            totalWait += counter;
            pArray[i].setTurnAroundTime(counter + pArray[i].getBurstTime());
            totalTATime += pArray[i].getTurnAroundTime();
            counter += pArray[i].getBurstTime();
        }
    }

    public void fillFixedpArray() {     //This fills pArray with the fixed values given
        for (int i = 0; i < numOfProcesses; i++) {
            pArray[i] = new SJFProcess(i, fixedBurstValues[i]);        //PiD, BurstValue
            pArray[i].setWaitTime(counter);
            totalWait+= counter;
            pArray[i].setTurnAroundTime(counter + pArray[i].getBurstTime());
            totalTATime += pArray[i].getTurnAroundTime();
            counter+= pArray[i].getBurstTime();
        }
    }

}
