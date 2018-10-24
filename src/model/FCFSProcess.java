package model;

public class FCFSProcess extends AProcess {     //Process used by first come first serve
    public FCFSProcess(int id, int burst) {
        this.pId = id;
        this.burstTime = burst;
    }
}
