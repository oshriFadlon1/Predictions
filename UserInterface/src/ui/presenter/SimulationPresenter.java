package ui.presenter;

import javafx.scene.control.ListCell;

public class SimulationPresenter {
    private int simulationId;
    private boolean isSimulationFinished;

    public SimulationPresenter(int simulationId, boolean isRunning) {
        this.simulationId = simulationId;
        this.isSimulationFinished = isRunning;
    }

    public int getSimulationId() {
        return simulationId;
    }
    public void setSimulationId(int simulationId) {
        this.simulationId = simulationId;
    }

    public boolean getIsSimulationFinished() {
        return isSimulationFinished;
    }

    public void setSimulationRunning(boolean simulationRunning) {
        isSimulationFinished = simulationRunning;
    }
}
