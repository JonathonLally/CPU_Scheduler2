package model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

//Brendan Kiernan
public class RRSim extends Sim {

    private int quantum;
    private ArrayList<RRProcess> orderList, pList;

    public RRSim(int amount, int quantum) {
        pArray = new RRProcess[amount];
        this.quantum = quantum;
        numOfProcesses = amount;
        counter = 0;
        totalWait = 0;
        totalTATime = 0;
        fillpArray();
        calculateWaitsAndTA();
        calculateAverageTA();
        calculateAverageWait();
    }

    public RRSim(int amount, int[] burstTimes, int quantum) {
        pArray = new RRProcess[amount];
        this.quantum = quantum;
        numOfProcesses = amount;
        counter = 0;
        totalWait = 0;
        totalTATime = 0;
        fillpArrayFixed(burstTimes);
        calculateWaitsAndTA();
        calculateAverageTA();
        calculateAverageWait();
    }

    private void fillpArray() {
        Random r = new Random();

        for(int i = 0; i < numOfProcesses; i++)
            pArray[i] = new RRProcess(i, r.nextInt(10 ) + 1);

    }

    private void fillpArrayFixed(int[] burstTimes) {

        for(int i = 0; i < numOfProcesses; i++)
            pArray[i] = new RRProcess(i, burstTimes[i]);

    }

    private void calculateWaitsAndTA() {
        pList = new ArrayList<>();
        orderList = new ArrayList<>(); //keeps track of the order these processes go through the algo
        Collections.addAll(pList, (RRProcess[])pArray);
        boolean hasSwapped = true; //noted as hasSwapped as that's what it would do in a queue
        int numCycles = 0; //used for calculating arrival time

        while(hasSwapped) { //No way for this to be O(n). Trust me, I tried.
            hasSwapped = false;

            for (int i = 0; i < pList.size(); i++) {
                RRProcess process = pList.get(i);

                if(!(process.hasArrived())) {
                    process.setArrivalTime(numCycles * quantum);
                    process.setHasArrived(true);
                }

                if(process.getTempBurst() <= quantum) {
                    process.setTurnAroundTime(process.getTurnAroundTime() + process.getTempBurst()); //sets TAT to itself plus what the burst time would be
                    updateWaitTime(i, process.getTempBurst());
                    orderList.add(new RRProcess(process.getpId(), process.getTempBurst()));
                    pList.remove(process);
                    pList.trimToSize();
                    i--; //removal and trimming would result in a process skip, this solves that issue
                } else {
                    System.out.println(process);
                    process.setTurnAroundTime(process.getTurnAroundTime() + quantum); //sets TAT to itself plus the quantum
                    updateWaitTime(i);
                    orderList.add(new RRProcess(process.getpId(), process.getTempBurst()));
                    process.setTempBurst(process.getTempBurst() - quantum); //subtracts quantum from tempBurst for future comparisons
                    hasSwapped = true;
                }

                numCycles++;
            }
        }

        //sets Sim values, at this point wait and TA times are absolute for processes
        for(RRProcess rp : (RRProcess[])pArray) {
            int turnAround = rp.getTurnAroundTime() + (rp.getWaitTime()); //this may be the wrong way to do it
            rp.setTurnAroundTime(turnAround);
            totalWait += rp.getWaitTime();
            totalTATime += turnAround;
        }
    }

    //updates wait times except for process selected by calculateWaits and TA with the quantum
    private void updateWaitTime(int index) {

        for(int i = 0; i < pList.size(); i++)
            if (i != index)
                pList.get(i).setWaitTime(pList.get(i).getWaitTime() + quantum);

    }

    //updates wait times except for process selected by calculateWaits and TA with a process' theoretical remaining burst time
    private void updateWaitTime(int index, int burstTime) {

        for(int i = 0; i < pList.size(); i++)
            if(i != index)
                pList.get(i).setWaitTime(pList.get(i).getWaitTime() + burstTime);

    }

    public ArrayList<RRProcess> getOrderList() {
        return orderList;
    }
}
