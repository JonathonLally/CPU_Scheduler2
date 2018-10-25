package model;

public class PSProcess extends AProcess {       //Process used by Priority Scheduling

    private int priority;

    public PSProcess(int id, int burst, int priority) {
        this.pId = id;
        this.burstTime = burst;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
