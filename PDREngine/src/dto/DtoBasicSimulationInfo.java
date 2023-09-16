package dto;

import java.util.HashMap;
import java.util.Map;

public class DtoBasicSimulationInfo {
    Map<Integer, DtoUiToEngine> allSimulationsStartingInfo;
    public DtoBasicSimulationInfo(){
        this.allSimulationsStartingInfo = new HashMap<>();
    }

    public void addStartingSimulationDetails(DtoUiToEngine simulationToAdd, int simulationNumber){
        this.allSimulationsStartingInfo.put(simulationNumber, simulationToAdd);
    }

    public DtoUiToEngine getChosenRerunSimulation(int idOfSimulation){
        return this.allSimulationsStartingInfo.get(idOfSimulation);
    }
}
