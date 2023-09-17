package simulationmanager;

import dto.DtoAllSimulationDetails;
import dto.DtoCountTickPopulation;
import dto.DtoSimulationDetails;
import dto.DtoQueueManagerInfo;
import entity.EntityDefinition;
import enums.SimulationState;
import world.GeneralInformation;
import world.WorldInstance;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


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
                        -1, entity1Name, "", numberOfTicks, numberOfSeconds, chosenSimulation.getInformationOfWorld().isSimulationDone(), chosenSimulation.isPaused(), userSimulationChoice);
            }

            String entity1Name = chosenSimulation.getInformationOfWorld().getEntitiesToPopulations().get(0).getCurrEntityDef().getEntityName();
            String entity2Name = chosenSimulation.getInformationOfWorld().getEntitiesToPopulations().get(1).getCurrEntityDef().getEntityName();
            return new DtoSimulationDetails(chosenSimulation.getInformationOfWorld().getEntitiesToPopulations().get(0).getCurrEntityPopulation(),
                    chosenSimulation.getInformationOfWorld().getEntitiesToPopulations().get(1).getCurrEntityPopulation(),
                    entity1Name, entity2Name, numberOfTicks, numberOfSeconds, chosenSimulation.getInformationOfWorld().isSimulationDone(), chosenSimulation.isPaused(), userSimulationChoice);
        }
    }

    public DtoAllSimulationDetails createMapOfSimulationsToIsRunning() {
        Map<Integer, SimulationState> allSimulations = new HashMap<>();
        for(int currId: this.idToSimulationMap.keySet()){
            Boolean isSimulationRunning = this.idToSimulationMap.get(currId).getInformationOfWorld().isSimulationDone();
            if (isSimulationRunning){
                allSimulations.put(currId, SimulationState.FINISHED);
            } else if (this.idToSimulationMap.get(currId).getInformationOfWorld().isSimulationPaused()){
                allSimulations.put(currId, SimulationState.WAITING);
            }
            else {
                allSimulations.put(currId, SimulationState.RUNNING);
            }

        }
        DtoAllSimulationDetails allSimulationDetails = new DtoAllSimulationDetails(allSimulations);
        return allSimulationDetails;
    }

    public void pauseCurrentSimulation(int simulationId) {
        this.idToSimulationMap.get(simulationId).setPaused(true);
    }

    public void resumeCurrentSimulation(int simulationId) {
        resumeSimulationRun(simulationId);
    }

    public void stopCurrentSimulation(int simulationId) {
        this.idToSimulationMap.get(simulationId).setStopped(true);
        resumeSimulationRun(simulationId);
    }

    private void resumeSimulationRun(int simulationId) {
        if ( this.idToSimulationMap.get(simulationId).isPaused()){
            synchronized (this.idToSimulationMap.get(simulationId).getLockForSyncPause()){
                while ( this.idToSimulationMap.get(simulationId).isPaused()){
                    this.idToSimulationMap.get(simulationId).getLockForSyncPause().notifyAll();
                    this.idToSimulationMap.get(simulationId).setPaused(false);
                }
            }
        }
    }

    public DtoQueueManagerInfo getQueueManagerInfo(){
        long doneSimulations = this.idToSimulationMap.entrySet().stream().filter(entry-> entry.getValue().getInformationOfWorld().isSimulationDone()).count();
        long runningSimulations = this.idToSimulationMap.entrySet().stream().filter(entry-> !entry.getValue().getInformationOfWorld().isSimulationDone()).count();
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)this.currentThreadPool;

        return new DtoQueueManagerInfo(String.valueOf(runningSimulations - threadPoolExecutor.getQueue().size()), String.valueOf(doneSimulations), String.valueOf(threadPoolExecutor.getQueue().size()));
    }

    public void clearInformation() {
        if(this.idToSimulationMap.size() > 0) {
            GeneralInformation.setIdOfSimulation(0);
        }
        this.idToSimulationMap.clear();
    }

    public List<DtoCountTickPopulation> getSimulationListOfPopulationPerTick(int simulationId) {
        return this.idToSimulationMap.get(simulationId).getEntityPopulationInEachTick();
    }

    public List<String> bringPropertyNamesList(int simulationId, String entityName) {
        List<String> propertyNamesList = this.idToSimulationMap.get(simulationId).bringPropertyNamesListAccordingToEntityName(entityName);
        return propertyNamesList;
    }
}
