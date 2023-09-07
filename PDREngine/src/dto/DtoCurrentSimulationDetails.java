package dto;

public class DtoCurrentSimulationDetails {
    private int currentEntity1Population;
    private int currentEntity2Population;
    private int currentSimulationTick;
    private int currentSimulationTimePassed;

    public DtoCurrentSimulationDetails(int currentEntity1Population, int currentEntity2Population, int currentSimulationTick, int currentSimulationTimePassed) {
        this.currentEntity1Population = currentEntity1Population;
        this.currentEntity2Population = currentEntity2Population;
        this.currentSimulationTick = currentSimulationTick;
        this.currentSimulationTimePassed = currentSimulationTimePassed;
    }

    public int getCurrentEntity1Population() {
        return currentEntity1Population;
    }

    public void setCurrentEntity1Population(int currentEntity1Population) {
        this.currentEntity1Population = currentEntity1Population;
    }

    public int getCurrentEntity2Population() {
        return currentEntity2Population;
    }

    public void setCurrentEntity2Population(int currentEntity2Population) {
        this.currentEntity2Population = currentEntity2Population;
    }

    public int getCurrentSimulationTick() {
        return currentSimulationTick;
    }

    public void setCurrentSimulationTick(int currentSimulationTick) {
        this.currentSimulationTick = currentSimulationTick;
    }

    public int getCurrentSimulationTimePassed() {
        return currentSimulationTimePassed;
    }

    public void setCurrentSimulationTimePassed(int currentSimulationTimePassed) {
        this.currentSimulationTimePassed = currentSimulationTimePassed;
    }
}
