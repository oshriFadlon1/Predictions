package dto;

import com.sun.org.apache.xpath.internal.operations.Bool;
import enums.SimulationState;

import java.util.Map;

public class DtoAllSimulationDetails {
    private Map<Integer, SimulationState> mapOfAllSimulations;

    public DtoAllSimulationDetails(Map<Integer, SimulationState> allSimulations) {
        this.mapOfAllSimulations = allSimulations;
    }

    public Map<Integer, SimulationState> getMapOfAllSimulations() {
        return mapOfAllSimulations;
    }
}
