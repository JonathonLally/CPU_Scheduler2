package model;

import java.util.Random;
import java.util.Arrays;

public class PSSim extends Sim {            //Priority Scheduling Simulation

    private PSProcess[] pArray;

    public PSSim(int amount) {              //Constructor for Random Data
        pArray = new PSProcess[amount];
        numOfProcesses = amount;
        fillpArray();
        sortpArray(pArray);
        calculateWaitsAndTA();

    }

    public void fillpArray() {              //Fills pArray with Random Data
        Random r = new Random();
        for(int i = 0; i < numOfProcesses; i++) {
            pArray[i] = new PSProcess(i, r.nextInt(10 ) + 1, r.nextInt(10) + 1);
        }
    }

    public PSProcess[] sortpArray(PSProcess[] psArray) {        //Sorts PSProcess Array by Priority
        Arrays.sort(psArray, (psProcess, t1) -> psProcess.getPriority() > t1.getPriority() ? 1 : -1);
        return psArray;
    }

    public void calculateWaitsAndTA() {             //Assigns wait and turn around time to pArray
        counter = 0;
        for (int i = 0; i < pArray.length; i++) {
            pArray[i].setWaitTime(counter);
            totalWait += counter;
            pArray[i].setTurnAroundTime(counter + pArray[i].getBurstTime());
            counter += pArray[i].getBurstTime();
        }
    }

    public PSProcess[] getPSArray() {       //Returns PSProcess[] instead of AProcess[]
        return pArray;
    }


}
