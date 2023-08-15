package dto;

import java.util.HashMap;
import java.util.Map;

public class DtoOldSimulationsMap {
    private Map<Integer, String> oldSimulationsMap;


    public DtoOldSimulationsMap(Map<Integer, String> oldSimulationsMap) {
        this.oldSimulationsMap = new HashMap<>();
        for (Integer integer: oldSimulationsMap.keySet()) {
            this.oldSimulationsMap.put(integer,oldSimulationsMap.get(integer));
        }
    }

    public Map<Integer, String> getOldSimulationsMap() {
        return oldSimulationsMap;
    }

    public void setOldSimulationsMap(Map<Integer, String> oldSimulationsMap) {
        this.oldSimulationsMap = oldSimulationsMap;
    }
}
