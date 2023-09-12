package dto;

public class DtoSimulationDetails {
    private int entity1Population;
    private int entity2Population;
    private String entity1Name;
    private String entity2Name;
    private int simulationTick;
    private long simulationTimePassed;
    private boolean isSimulationFinished;
    private int simulationId;

    public DtoSimulationDetails(int currentEntity1Population, int currentEntity2Population, String entity1Name, String entity2Name, int currentSimulationTick, long currentSimulationTimePassed, boolean isSimulationFinished, int simulationId) {
        this.entity1Population = currentEntity1Population;
        this.entity2Population = currentEntity2Population;
        this.simulationTick = currentSimulationTick;
        this.simulationTimePassed = currentSimulationTimePassed;
        this.isSimulationFinished = isSimulationFinished;
        this.entity1Name = entity1Name;
        this.entity2Name = entity2Name;
        this.simulationId = simulationId;
    }

    public int getSimulationId() {
        return simulationId;
    }

    public int getEntity1Population() {
        return entity1Population;
    }

    public int getEntity2Population() {
        return entity2Population;
    }


    public int getSimulationTick() {
        return simulationTick;
    }


    public long getSimulationTimePassed() {
        return simulationTimePassed;
    }

    public boolean getIsSimulationFinished() {
        return isSimulationFinished;
    }

    public String getEntity1Name() {
        return entity1Name;
    }

    public String getEntity2Name() {
        return entity2Name;
    }
}
