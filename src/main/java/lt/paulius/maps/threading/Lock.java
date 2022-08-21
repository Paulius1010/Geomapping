package lt.paulius.maps.threading;

public class Lock {

    private int runningThreadsNumber;

    public Lock() {
        this.runningThreadsNumber = 0;
    }

    public int getRunningThreadsNumber() {
        return this.runningThreadsNumber;
    }

    public void addRunningThread() {
        this.runningThreadsNumber++;
    }

    public void removeRunningThread() {
        this.runningThreadsNumber--;
    }

}
