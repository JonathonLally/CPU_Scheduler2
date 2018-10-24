package model;

public abstract class AProcess {        //Framework for other processes, named AProcess so it doesn't mix with "Process"
    protected int pId;
    protected int burstTime;
    protected int waitTime;
    protected int turnAroundTime;


    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public int getTurnAroundTime() {
        return turnAroundTime;
    }

    public void setTurnAroundTime(int turnAroundTime) {
        this.turnAroundTime = turnAroundTime;
    }

    @Override
    public String toString() {
        return "AProcess{" +
                "pId=" + pId +
                ", burstTime=" + burstTime +
                ", waitTime=" + waitTime +
                ", turnAroundTime=" + turnAroundTime +
                '}';
    }
}
