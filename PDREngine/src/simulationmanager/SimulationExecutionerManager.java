package simulationmanager;

import dto.DtoAllSimulationDetails;
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
            long numberOfSeconds  = chosenSimulation.getCurrentTimePassed();
            if(chosenSimulation.getInformationOfWorld().getEntitiesToPopulations().size() == 1){
                String entity1Name = chosenSimulation.getInformationOfWorld().getEntitiesToPopulations().get(0).getCurrEntityDef().getEntityName();
                return new DtoSimulationDetails(chosenSimulation.getInformationOfWorld().getEntitiesToPopulations().get(0).getCurrEntityPopulation(),
                        -1, entity1Name, "", numberOfTicks, numberOfSeconds, chosenSimulation.getInformationOfWorld().isSimulationDone(), userSimulationChoice);
            }

            String entity1Name = chosenSimulation.getInformationOfWorld().getEntitiesToPopulations().get(0).getCurrEntityDef().getEntityName();
            String entity2Name = chosenSimulation.getInformationOfWorld().getEntitiesToPopulations().get(1).getCurrEntityDef().getEntityName();
            return new DtoSimulationDetails(chosenSimulation.getInformationOfWorld().getEntitiesToPopulations().get(0).getCurrEntityPopulation(),
                    chosenSimulation.getInformationOfWorld().getEntitiesToPopulations().get(1).getCurrEntityPopulation(),
                    entity1Name, entity2Name, numberOfTicks, numberOfSeconds, chosenSimulation.getInformationOfWorld().isSimulationDone(), userSimulationChoice);
        }
    }

    public DtoAllSimulationDetails createMapOfSimulationsToIsRunning() {
        Map<Integer, Boolean> allSimulations = new HashMap<>();
        for(int currId: this.idToSimulationMap.keySet()){
            Boolean isSimulationRunning = this.idToSimulationMap.get(currId).getInformationOfWorld().isSimulationDone();
            allSimulations.put(currId, isSimulationRunning);
        }
        DtoAllSimulationDetails allSimulationDetails = new DtoAllSimulationDetails(allSimulations);
        return allSimulationDetails;
    }

    public void pauseCurrentSimulation(int simulationId) {
        this.idToSimulationMap.get(simulationId).setPaused(true);
    }

    public void resumeCurrentSimulation(int simulationId) {
        this.idToSimulationMap.get(simulationId).setPaused(false);
    }

    public void stopCurrentSimulation(int simulationId) {
        this.idToSimulationMap.get(simulationId).setStopped(true);
    }
}
