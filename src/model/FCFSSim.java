package model;

import java.util.Random;

public class FCFSSim extends Sim {      //First Come First Serve Simulation

    public FCFSSim(int amount) {        //Constructor for First Come First Serve Simulation
        pArray = new FCFSProcess[amount];
        numOfProcesses = amount;
        counter = 0;
        totalWait = 0;
        totalTATime = 0;
        fillpArray();
        calculateAverageTA();
        calculateAverageWait();

    }

    public FCFSSim(int amount, int[] values) {      //Overloaded Constructor for Fixed Values FCFS Simulation
        pArray = new FCFSProcess[amount];
        numOfProcesses = amount;
        counter = 0;
        totalWait = 0;
        totalTATime = 0;
        fixedBurstValues = values;
        fillFixedpArray();
        calculateAverageWait();
        calculateAverageTA();

    }

    public void fillpArray() {          //This fills pArray with random values
        Random r = new Random();
        for (int i =0; i < numOfProcesses; i++) {
            pArray[i] = new FCFSProcess(i, r.nextInt(10) + 1);
            pArray[i].setWaitTime(counter);
            totalWait += counter;
            pArray[i].setTurnAroundTime(counter + pArray[i].getBurstTime());
            totalTATime += pArray[i].getTurnAroundTime();
            counter += pArray[i].getBurstTime();
        }
    }

    public void fillFixedpArray() {     //This fills pArray with the fixed values given
        for (int i = 0; i < numOfProcesses; i++) {
            pArray[i] = new FCFSProcess(i, fixedBurstValues[i]);        //PiD, BurstValue
            pArray[i].setWaitTime(counter);
            totalWait+= counter;
            pArray[i].setTurnAroundTime(counter + pArray[i].getBurstTime());
            totalTATime += pArray[i].getTurnAroundTime();
            counter+= pArray[i].getBurstTime();
        }
    }


}
