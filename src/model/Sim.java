package model;

public abstract class Sim {     //Framework for other Simulations
    protected AProcess[] pArray;
    protected int[] fixedBurstValues;
    protected int numOfProcesses;
    protected int counter;
    protected double averageWait;
    protected double averageTA;
    protected int totalWait;
    protected int totalTATime;


    public void printpArray() {     //Prints values of pArray + Averages
        for (AProcess aPArray : pArray) {
            System.out.println(aPArray.toString());
        }
        System.out.println("Average Wait Time : " + averageWait);
        System.out.println("Average TA Time : " + averageTA);
    }

    public void calculateAverageWait() {    //Calculates the Average Wait

        averageWait = (double)totalWait / (double)numOfProcesses;
        averageWait = Math.round(averageWait * 100.0)/100.0;        //Rounds to .00

    }

    public void calculateAverageTA() {      //Calculates the Average Turn Around Time
        averageTA = (double)totalTATime / (double)numOfProcesses;
        averageTA = Math.round(averageTA * 100.0)/100.0;

    }

    //Getters and Setters

    public AProcess[] getpArray() {
        return pArray;
    }

    public void setpArray(AProcess[] pArray) {
        this.pArray = pArray;
    }

    public int getNumOfProcesses() {
        return numOfProcesses;
    }

    public void setNumOfProcesses(int numOfProcesses) {
        this.numOfProcesses = numOfProcesses;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public double getAverageWait() {
        return averageWait;
    }

    public void setAverageWait(double averageWait) {
        this.averageWait = averageWait;
    }

    public double getAverageTA() {
        return averageTA;
    }

    public void setAverageTA(double averageTA) {
        this.averageTA = averageTA;
    }

    public int getTotalWait() {
        return totalWait;
    }

    public void setTotalWait(int totalWait) {
        this.totalWait = totalWait;
    }

    public int[] getFixedBurstValues() {
        return fixedBurstValues;
    }

    public void setFixedBurstValues(int[] fixedBurstValues) {
        this.fixedBurstValues = fixedBurstValues;
    }

    public int getTotalTATime() {
        return totalTATime;
    }

    public void setTotalTATime(int totalTATime) {
        this.totalTATime = totalTATime;
    }
}
