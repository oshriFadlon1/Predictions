package dto;

public class DtoSimulationDetails {
    private int entity1Population;
    private int entity2Population;
    private int simulationTick;
    private int simulationTimePassed;

    public DtoSimulationDetails(int currentEntity1Population, int currentEntity2Population, int currentSimulationTick, int currentSimulationTimePassed) {
        this.entity1Population = currentEntity1Population;
        this.entity2Population = currentEntity2Population;
        this.simulationTick = currentSimulationTick;
        this.simulationTimePassed = currentSimulationTimePassed;
    }

    public DtoSimulationDetails(){

    }

    public int getEntity1Population() {
        return entity1Population;
    }

    public void setEntity1Population(int entity1Population) {
        this.entity1Population = entity1Population;
    }

    public int getEntity2Population() {
        return entity2Population;
    }

    public void setEntity2Population(int entity2Population) {
        this.entity2Population = entity2Population;
    }

    public int getSimulationTick() {
        return simulationTick;
    }

    public void setSimulationTick(int simulationTick) {
        this.simulationTick = simulationTick;
    }

    public int getSimulationTimePassed() {
        return simulationTimePassed;
    }

    public void setSimulationTimePassed(int simulationTimePassed) {
        this.simulationTimePassed = simulationTimePassed;
    }
}
