package world;

import pointCoord.PointCoord;
import termination.Termination;

import java.time.LocalDateTime;

public class GeneralInformation {
    private int primaryEntityStartPopulation;
    private int secondaryEntityStartPopulation;
    private int primaryEntityEndPopulation;
    private int secondaryEntityEndPopulation;
    private PointCoord worldSize;
    private LocalDateTime startOfSimulationDate;
    private static int idOfSimulation = 0;
    private Termination termination;
    private boolean isSimulationDone;

    public GeneralInformation(int primaryEntityStartPopulation, int secondaryEntityStartPopulation, int primaryEntityEndPopulation,
                              int secondaryEntityEndPopulation, PointCoord worldSize, LocalDateTime startOfSimulationDate, Termination termination) {
        this.primaryEntityStartPopulation = primaryEntityStartPopulation;
        this.secondaryEntityStartPopulation = secondaryEntityStartPopulation;
        this.primaryEntityEndPopulation = primaryEntityEndPopulation;
        this.secondaryEntityEndPopulation = secondaryEntityEndPopulation;
        this.worldSize = worldSize;
        this.startOfSimulationDate = startOfSimulationDate;
        this.termination = termination;
        this.isSimulationDone = false;
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

    public int getPrimaryEntityEndPopulation() {
        return primaryEntityEndPopulation;
    }

    public void setPrimaryEntityEndPopulation(int primaryEntityEndPopulation) {
        this.primaryEntityEndPopulation = primaryEntityEndPopulation;
    }

    public int getSecondaryEntityEndPopulation() {
        return secondaryEntityEndPopulation;
    }

    public void setSecondaryEntityEndPopulation(int secondaryEntityEndPopulation) {
        this.secondaryEntityEndPopulation = secondaryEntityEndPopulation;
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
