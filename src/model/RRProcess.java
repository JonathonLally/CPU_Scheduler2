package model;

public class RRProcess extends AProcess {

    private int arrivalTime, tempBurst; //tempBurst is for comparing what will happen in the sim while maintaining
    private boolean hasArrived;         //consistency in the burst to be used for display

    public RRProcess(int id, int burst) {
        this.pId = id;
        this.burstTime = burst;
        tempBurst = burst;
        arrivalTime = 0;
        hasArrived = false;
    }

    public void setArrivalTime(int arrivalTime) { this.arrivalTime = arrivalTime; }

    public int getArrivalTime() { return arrivalTime; }

    public void setHasArrived(boolean hasArrived) { this.hasArrived = hasArrived; }

    public boolean hasArrived() { return hasArrived; }

    public int getTempBurst() { return tempBurst; }

    public void setTempBurst(int tempBurst) { this.tempBurst = tempBurst; }
}
