package model;

public class SRTProcess extends AProcess{       //Process used by Shortest-Remaining-Time-First Scheduling

    private int arrivalTime;
    private int timeLeft;

    public SRTProcess(int id, int burst, int arrival) {
        this.pId = id;
        this.burstTime = burst;
        this.arrivalTime = arrival;
        this.timeLeft = burst;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    @Override
    public String toString() {
        return "SRTProcess{" +
                " timeLeft=" + timeLeft +
                ", pId=" + pId +
                '}';
    }
}
