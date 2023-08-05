package dto;

public class DtoOldSimulationResponse {

    private String allInfoSimulation;
    private int numberOfSimulations;

    public DtoOldSimulationResponse(String allInfoSimulation, int numberOfSimulations) {
        this.allInfoSimulation = allInfoSimulation;
        this.numberOfSimulations = numberOfSimulations;
    }

    public String getAllInfoSimulation() {
        return allInfoSimulation;
    }

    public void setAllInfoSimulation(String allInfoSimulation) {
        this.allInfoSimulation = allInfoSimulation;
    }

    public int getNumberOfSimulations() {
        return numberOfSimulations;
    }

    public void setNumberOfSimulations(int numberOfSimulations) {
        this.numberOfSimulations = numberOfSimulations;
    }
}
