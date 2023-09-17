package dto;

public class DtoSimulationDetails {
    private int entity1Population;
    private int entity2Population;
    private String entity1Name;
    private String entity2Name;
    private int simulationTick;
    private long simulationTimePassed;
    private boolean isSimulationFinished;
    private boolean isSimulationPaused;
    private int simulationId;

    public DtoSimulationDetails(int entity1Population, int entity2Population, String entity1Name, String entity2Name, int simulationTick, long simulationTimePassed, boolean isSimulationFinished, boolean isSimulationPaused, int simulationId) {
        this.entity1Population = entity1Population;
        this.entity2Population = entity2Population;
        this.entity1Name = entity1Name;
        this.entity2Name = entity2Name;
        this.simulationTick = simulationTick;
        this.simulationTimePassed = simulationTimePassed;
        this.isSimulationFinished = isSimulationFinished;
        this.isSimulationPaused = isSimulationPaused;
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

    public boolean isSimulationFinished() {
        return isSimulationFinished;
    }

    public String getEntity1Name() {
        return entity1Name;
    }

    public String getEntity2Name() {
        return entity2Name;
    }

    public boolean isSimulationPaused() {
        return isSimulationPaused;
    }
}
