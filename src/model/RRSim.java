package model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

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
        orderList = new ArrayList<>();
        Collections.addAll(pList, (RRProcess[])pArray);
        boolean hasSwapped = true;

        while(hasSwapped) { //No way for this to be O(n). Trust me, I tried.
            hasSwapped = false;

            for (int i = 0; i < pList.size(); i++) {
                RRProcess process = pList.get(i);

                if(!(process.hasArrived()))
                    process.setArrivalTime(totalWait);

                if(process.getTempBurst() < quantum) {
                    process.setTurnAroundTime(process.getTurnAroundTime() + process.getTempBurst());
                    updateWaitTime(i, process.getTempBurst());
                    orderList.add(new RRProcess(process.getpId(), process.getTempBurst()));
                    pList.remove(process);
                } else {
                    System.out.println(process);
                    process.setTurnAroundTime(process.getTurnAroundTime() + quantum);
                    updateWaitTime(i);
                    orderList.add(new RRProcess(process.getpId(), process.getTempBurst()));
                    process.setTempBurst(process.getTempBurst() - quantum);
                    hasSwapped = true;
                }
            }
        }

        for(RRProcess rp : (RRProcess[])pArray) {
            totalWait += rp.getWaitTime();
            totalTATime += rp.getTurnAroundTime() + (rp.getWaitTime() - (rp.getWaitTime() - rp.getArrivalTime()));
        }
    }

    private void updateWaitTime(int index) {

        for(int i = 0; i < pArray.length; i++)
            if (i != index)
                pArray[i].setWaitTime(pArray[i].getWaitTime() + quantum);

    }

    private void updateWaitTime(int index, int burstTime) {

        for(int i = 0; i < pArray.length; i++)
            if(i != index)
                pArray[i].setWaitTime(pArray[i].getWaitTime() + burstTime);

    }

    public ArrayList<RRProcess> getOrderList() {
        return orderList;
    }
}
