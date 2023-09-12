package dto;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Map;

public class DtoAllSimulationDetails {
    private Map<Integer, Boolean> mapOfAllSimulations;

    public DtoAllSimulationDetails(Map<Integer, Boolean> allSimulations) {
        this.mapOfAllSimulations = allSimulations;
    }

    public Map<Integer, Boolean> getMapOfAllSimulations() {
        return mapOfAllSimulations;
    }
}
