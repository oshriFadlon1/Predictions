package ui.presenter;

public class SimulationPresenter {
    private int simulationId;

    public SimulationPresenter(int simulationId) {
        this.simulationId = simulationId;
    }

    public int getSimulationId() {
        return simulationId;
    }

    public void setSimulationId(int simulationId) {
        this.simulationId = simulationId;
    }
}
