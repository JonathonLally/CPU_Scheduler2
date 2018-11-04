package model;

import java.util.*;

public class SRTSim extends Sim {                        //Shortest Remaining Time First Simulation
    
    private SRTProcess[] pArray;
    private int totalBurst;
    private StringBuilder result;                       //Holds order of what happened
    
    public SRTSim(int amount) {                         //Constructor for Random Data
        pArray = new SRTProcess[amount];
        numOfProcesses = amount;
        result = new StringBuilder();
        fillpArray();
        calculateWait();
        setTATime();
        calculateTotals();
    }

    public SRTSim(int amount, int[] burstTimes) {       //Overloaded Constructor for Fixed Data
        pArray = new SRTProcess[amount];
        numOfProcesses = amount;
        result = new StringBuilder();
        fillFixedpArray(burstTimes);
        calculateWait();
        setTATime();
        calculateTotals();
    }

    private void fillpArray() {                          //Fills pArray with Random Data
        Random r = new Random();
        for(int i = 0; i < numOfProcesses; i++) {
            pArray[i] = new SRTProcess(i, r.nextInt(10 ) + 1, r.nextInt(10) + 1);
            pArray[i].setArrivalTime(i);
            totalBurst+=pArray[i].getBurstTime();
        }
    }

    private void fillFixedpArray(int[] burstTimes) {     //Fills pArray with Fixed Data, Assume arrivalTime = pId
        for (int i = 0; i < numOfProcesses; i++) {
            pArray[i] = new SRTProcess(i, burstTimes[i], i);
            pArray[i].setArrivalTime(i);
            totalBurst+=pArray[i].getBurstTime();
        }
    }


    private void calculateWait() {                                   //This calculates the waits round by round
        ArrayList<SRTProcess> subList = new ArrayList<SRTProcess>();         //Use arraylist as temp list because we don't know size
        for (int i = 0; i < totalBurst; i++) {                      //Repeat For total amount of cycles
            for (SRTProcess srtP : pArray) {                        //For all Processes in pArray

                //Here we will build a new subList each time with processes that qualify
                //They must have arrived, and have something left to process
                if (srtP.getArrivalTime() <= i && srtP.getTimeLeft() != 0) {
                    subList.add(srtP);                              //Sublist of Processes
                }

                //Here we add wait to all processes with timeLeft to process
                //Subtract the one chosen to process later
                if (srtP.getTimeLeft() != 0) {
                    srtP.setWaitTime(srtP.getWaitTime() + 1);
                }
            }
            //After sublist has been created
            SRTProcess min = Collections.min(subList, Comparator.comparing(s -> s.getTimeLeft()));      //Find Smallest timeLeft
            min.setTimeLeft(min.getTimeLeft() - 1);                 //Subtract 1 time
            int temp = min.getpId();
            pArray[temp].setWaitTime(pArray[temp].getWaitTime() - 1);          //Corrects wait time added
            result.append(i + " ");                                 //This keeps a string of everything that happened
            result.append(min.getpId() + " ");
            subList.clear();                                        //Clear temp Sub List
        }
    }

    private void setTATime() {                   //Sets Turn Around Time
        for (SRTProcess sp : pArray) {
            sp.setWaitTime(sp.getWaitTime() - sp.getArrivalTime());             //Corrects arrival time into wait
            sp.setTurnAroundTime(sp.getWaitTime() + sp.getBurstTime());
        }
    }

    private void calculateTotals() {             //Calculates average wait and turn around time
        for (SRTProcess sp : pArray) {
            totalWait += sp.getWaitTime();
            totalTATime += sp.getTurnAroundTime();
        }
        calculateAverageWait();
        calculateAverageTA();
    }

    public SRTProcess[] getsrtpArray() {         //Returns pArray for SRTSim
        return pArray;
    }

}
