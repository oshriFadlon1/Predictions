package dto;

import world.WorldDefinition;
import world.WorldInstance;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DtoWorldState implements Serializable {

    private WorldDefinition worldDefinitionForSimulation;

    private List<WorldInstance> oldSimulation;
    private Map<Integer, String> simulationId2CurrentTimeAndDate;

    public DtoWorldState(WorldDefinition worldDefinitionForSimulation, List<WorldInstance> oldSimulation, Map<Integer, String> simulationId2CurrentTimeAndDate) {
        this.worldDefinitionForSimulation = worldDefinitionForSimulation;
        this.oldSimulation = oldSimulation;
        this.simulationId2CurrentTimeAndDate = simulationId2CurrentTimeAndDate;
    }

    public WorldDefinition getWorldDefinitionForSimulation() {
        return worldDefinitionForSimulation;
    }

    public void setWorldDefinitionForSimulation(WorldDefinition worldDefinitionForSimulation) {
        this.worldDefinitionForSimulation = worldDefinitionForSimulation;
    }

    public List<WorldInstance> getOldSimulation() {
        return oldSimulation;
    }

    public void setOldSimulation(List<WorldInstance> oldSimulation) {
        this.oldSimulation = oldSimulation;
    }

    public Map<Integer, String> getSimulationId2CurrentTimeAndDate() {
        return simulationId2CurrentTimeAndDate;
    }

    public void setSimulationId2CurrentTimeAndDate(Map<Integer, String> simulationId2CurrentTimeAndDate) {
        this.simulationId2CurrentTimeAndDate = simulationId2CurrentTimeAndDate;
    }
}
