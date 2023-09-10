package simulationmanager;

import dto.DtoSimulationDetails;
import world.GeneralInformation;
import world.WorldInstance;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SimulationExecutionerManager {
    private Map<Integer, WorldInstance> idToSimulationMap;
    private int threadPoolSize;
    private ExecutorService currentThreadPool;
    private int countOfThreadInWork;

    public SimulationExecutionerManager(int numberOfThreads) {
        this.idToSimulationMap = new HashMap<>();
        this.threadPoolSize = numberOfThreads;
        this.currentThreadPool = Executors.newFixedThreadPool(numberOfThreads);
        this.countOfThreadInWork = 0;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public void addCurrentSimulationToManager(WorldInstance worldInstance) {
        this.idToSimulationMap.put(GeneralInformation.getIdOfSimulation(), worldInstance);
        this.currentThreadPool.execute(worldInstance);
        this.countOfThreadInWork++;
    }

    public void disposeThreadPool(){
        this.currentThreadPool.shutdown();
    }

    public Map<Integer, Boolean> getIdOfSimulation(){
        Map<Integer, Boolean> IdSimulation2IsDone = new HashMap<>();
        synchronized (this) {
            for (Integer integer : this.idToSimulationMap.keySet()) {
                IdSimulation2IsDone.put(integer, this.idToSimulationMap.get(integer).getInformationOfWorld().isSimulationDone());
            }
        }
        return IdSimulation2IsDone;
    }


    public DtoSimulationDetails getSimulationById(int userSimulationChoice) {
        synchronized (this){
            WorldInstance chosenSimulation = this.idToSimulationMap.get(userSimulationChoice);
            int numberOfTicks = chosenSimulation.getCurrentTick();
            int numberOfSeconds  = chosenSimulation.getCurrentTimePassed();
            if(chosenSimulation.getInformationOfWorld().getEntitiesToPopulations().size() == 1){
                return new DtoSimulationDetails(chosenSimulation.getInformationOfWorld().getEntitiesToPopulations().get(0).getCurrEntityPopulation(),
                        -1, numberOfTicks, numberOfSeconds);
            }

            return new DtoSimulationDetails(chosenSimulation.getInformationOfWorld().getEntitiesToPopulations().get(0).getCurrEntityPopulation(),
                    chosenSimulation.getInformationOfWorld().getEntitiesToPopulations().get(1).getCurrEntityPopulation(),
                    numberOfTicks, numberOfSeconds);
        }
    }
}
