package world;

import entity.EntityToPopulation;
import pointCoord.PointCoord;
import termination.Termination;

import java.time.LocalDateTime;
import java.util.List;

public class GeneralInformation {
    private int primaryEntityStartPopulation;
    private int secondaryEntityStartPopulation;
    private PointCoord worldSize;
    private LocalDateTime startOfSimulationDate;
    private static int idOfSimulation = 0;
    private Termination termination;
    private boolean isSimulationDone = false;
    private List<EntityToPopulation> entitiesToPopulations;

    public GeneralInformation(int primaryEntityStartPopulation, int secondaryEntityStartPopulation, PointCoord worldSize, LocalDateTime startOfSimulationDate, Termination termination, List<EntityToPopulation> entitiesToPopulations) {
        this.primaryEntityStartPopulation = primaryEntityStartPopulation;
        this.secondaryEntityStartPopulation = secondaryEntityStartPopulation;

        this.worldSize = worldSize;
        this.startOfSimulationDate = startOfSimulationDate;
        this.termination = termination;
        this.isSimulationDone = false;
        this.entitiesToPopulations = entitiesToPopulations;
        idOfSimulation++;
    }

    public List<EntityToPopulation> getEntitiesToPopulations() {
        return entitiesToPopulations;
    }

    public void setEntitiesToPopulations(List<EntityToPopulation> entitiesToPopulations) {
        this.entitiesToPopulations = entitiesToPopulations;
    }

    public int getPrimaryEntityStartPopulation() {
        return primaryEntityStartPopulation;
    }

    public void setPrimaryEntityStartPopulation(int primaryEntityStartPopulation) {
        this.primaryEntityStartPopulation = primaryEntityStartPopulation;
    }

    public int getSecondaryEntityStartPopulation() {
        return secondaryEntityStartPopulation;
    }

    public void setSecondaryEntityStartPopulation(int secondaryEntityStartPopulation) {
        this.secondaryEntityStartPopulation = secondaryEntityStartPopulation;
    }

    public PointCoord getWorldSize() {
        return worldSize;
    }

    public void setWorldSize(PointCoord worldSize) {
        this.worldSize = worldSize;
    }

    public LocalDateTime getStartOfSimulationDate() {
        return startOfSimulationDate;
    }

    public void setStartOfSimulationDate(LocalDateTime startOfSimulationDate) {
        this.startOfSimulationDate = startOfSimulationDate;
    }

    public static int getIdOfSimulation() {
        return idOfSimulation;
    }

    public static void setIdOfSimulation(int idOfSimulation) {
        GeneralInformation.idOfSimulation = idOfSimulation;
    }

    public Termination getTermination() {
        return termination;
    }

    public void setTermination(Termination termination) {
        this.termination = termination;
    }

    public boolean isSimulationDone() {
        return isSimulationDone;
    }

    public void setSimulationDone(boolean simulationDone) {
        isSimulationDone = simulationDone;
    }
}
