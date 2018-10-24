package model;

public abstract class Sim {     //Framework for other Simulations
    private AProcess[] pArray;
    private int numOfProcesses;
    private int counter;
    private double averageWait;
    private double averageTA;

    public void printpArray() {     //Prints values of pArray + Averages
        for (AProcess aPArray : pArray) {
            System.out.println(aPArray.toString());
        }
        System.out.println("Average Wait Time : " + averageWait);
        System.out.println("Average TA Time : " + averageTA);
    }

    public void calculateAverageWait() {        //TODO

    }

    public void calculateAverageTA() {      //TODO

    }

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
}
