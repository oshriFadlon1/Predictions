package dto;

public class DtoQueueManagerInfo {
    private final String countOfSimulationInProgress;
    private final String countOfSimulationEnded;
    private final String countOfSimulationsPending;

    public DtoQueueManagerInfo(String countOfSimulationInProgress, String countOfSimulationEnded, String countOfSimulationsPending) {
        this.countOfSimulationInProgress = countOfSimulationInProgress;
        this.countOfSimulationEnded = countOfSimulationEnded;
        this.countOfSimulationsPending = countOfSimulationsPending;
    }

    public String getCountOfSimulationInProgress() {
        return countOfSimulationInProgress;
    }

    public String getCountOfSimulationEnded() {
        return countOfSimulationEnded;
    }

    public String getCountOfSimulationsPending() {
        return countOfSimulationsPending;
    }
}
