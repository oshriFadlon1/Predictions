package simulationmanager;

import world.WorldInstance;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class SimulationExecutionerManager {
    private Map<Integer, WorldInstance> idToSimulationMap;
    private int threadPoolSize;
    private ExecutorService currentThreadPool;

    public SimulationExecutionerManager() {
        this.idToSimulationMap = new HashMap<>();
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }
}
