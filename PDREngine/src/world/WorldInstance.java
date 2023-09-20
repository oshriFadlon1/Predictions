package world;

import createAndKillEntities.CreateAndKillEntities;
import dto.DtoCountTickPopulation;
import entity.EntityDefinition;
import entity.EntityInstance;
import entity.EntityToPopulation;
import entity.SecondEntity;
import environment.EnvironmentInstance;
import exceptions.GeneralException;
import interfaces.IConditionComponent;
import necessaryVariables.NecessaryVariablesImpl;
import pointCoord.PointCoord;
import property.PropertyDefinition;
import property.PropertyDefinitionEntity;
import property.PropertyInstance;
import property.Value;
import range.Range;
import rule.ActivationForRule;
import rule.Rule;
import rule.action.IAction;
import termination.Termination;
import utility.Utilities;
import worldPhysicalSpace.WorldPhysicalSpace;

import java.io.Serializable;
import java.util.*;

public class WorldInstance implements Serializable, Runnable {
    private Map<String, EnvironmentInstance> allEnvironments;
    private Map<String,List<EntityInstance>> allEntities;
    private List<EntityDefinition> entityDefinitions;
    private List<Rule> allRules;
    private GeneralInformation informationOfWorld;
    private List<EntityInstance> entitiesToKill;
    private List<CreateAndKillEntities> entitiesToKillAndReplace;
    private WorldPhysicalSpace physicalSpace;
//    private int primaryEntityPopulation;
//    private int secondaryEntityPopulation;
    private int currentTick;
    private long currentTimePassed;
    private long currentTimeResume;
    private long startTheSimulation;
    private long timeFinished;
    private volatile boolean isPaused;
    private volatile boolean isStopped;
    private boolean isStarted;
    private long totalTimeInPause;
    private final Object lockForSyncPause;
    private List<DtoCountTickPopulation> entityPopulationInEachTick;


    public WorldInstance(Map<String, EnvironmentInstance> allEnvironments, List<EntityDefinition> entitiesDefinition,
                         List<Rule> allRules, GeneralInformation informationOfWorld) {
        this.allEnvironments = allEnvironments;
        this.allEntities = new HashMap<>();
        entityDefinitions = entitiesDefinition;
        this.allRules = allRules;
        this.informationOfWorld = informationOfWorld;
        this.entitiesToKill = new ArrayList<>();
        this.entitiesToKillAndReplace = new ArrayList<>();
        this.physicalSpace = new WorldPhysicalSpace(informationOfWorld.getWorldSize());
        this.totalTimeInPause = 0;
        this.currentTick = 0;
        this.currentTimePassed = 0;
        this.currentTimeResume = 0;
        this.isPaused = false;
        this.isStopped = false;
        this.startTheSimulation = -1;
        this.lockForSyncPause = new Object();
        this.entityPopulationInEachTick = new ArrayList<>();
        this.isStarted = false;
    }


    public GeneralInformation getInformationOfWorld() {
        return informationOfWorld;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }

    public void setInformationOfWorld(GeneralInformation informationOfWorld) {
        this.informationOfWorld = informationOfWorld;
    }

    public Map<String, EnvironmentInstance> getAllEnvironments() {
        return allEnvironments;
    }

    public void setAllEnvironments(Map<String, EnvironmentInstance> allEnvironments) {
        this.allEnvironments = allEnvironments;
    }

    public Map<String, List<EntityInstance>> getAllEntities() {
        return allEntities;
    }

    public void setAllEntities(Map<String, List<EntityInstance>> allEntities) {
        this.allEntities = allEntities;
    }

    public List<Rule> getAllRules() {
        return allRules;
    }

    public void setAllRules(List<Rule> allRules) {
        this.allRules = allRules;
    }

    public Object getLockForSyncPause() {
        return lockForSyncPause;
    }

    public long getCurrentTimeResume() {
        return currentTimeResume;
    }

    public void setCurrentTimeResume(long currentTimeResume) {
        this.currentTimeResume = currentTimeResume;
    }

    public List<DtoCountTickPopulation> getEntityPopulationInEachTick() {
        return entityPopulationInEachTick;
    }

    @Override
    public void run() {
        try {
            runSimulation();
            this.informationOfWorld.setSimulationDone(true);
        }
        catch(GeneralException generalException){

        }
    }

    //dto response of ending simulation
    public void runSimulation() throws GeneralException{
        boolean endedByTicks = false, endedBySeconds = false;
        NecessaryVariablesImpl necessaryVariables = new NecessaryVariablesImpl(allEnvironments);
        initializeAllEntityInstancesLists();
        this.isStarted = true;
        necessaryVariables.setWorldPhysicalSpace(this.physicalSpace);

        for (EntityDefinition entityDefinition:this.entityDefinitions) {
            necessaryVariables.getEntityDefinitions().add(entityDefinition);
        }
        Termination currentTermination = this.informationOfWorld.getTermination();

        //int currentTickCount = 0;
        Random random = new Random();
//        long timeStarted = System.currentTimeMillis();
//        long currentTime = System.currentTimeMillis();
        List<String> entityNamesForChecking = new ArrayList<>();
        this.startTheSimulation = System.currentTimeMillis();

        while (currentTermination.isTicksActive(this.currentTick) && currentTermination.isSecondsActive(getCurrentTimePassed()) && !isStopped){
 //           System.out.println("thread number "+Thread.currentThread() +"current tick : "+this.currentTick + "general info: " + this.informationOfWorld );

            if (isPaused){
                synchronized (this.lockForSyncPause){
                    while (isPaused){
                        try {
                            this.currentTimePassed = System.currentTimeMillis();
                            this.lockForSyncPause.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    this.currentTimeResume = System.currentTimeMillis();
                    //TODO CREATE A LONG TOTAL PAUSE TIME DO (currentTimeResume - currentTimePassed)
                    this.totalTimeInPause += this.currentTimeResume - this.currentTimePassed;
                }
                continue;
            }
            //TODO: change back
           moveAllEntitiesInPhysicalWorld();

            List<IAction> activeActionsInCurrentTick = new ArrayList<>();
            for (Rule rule : this.allRules ) {
                float probabilityToCheckAgainstCurrentRuleProbability = random.nextFloat();
                ActivationForRule activitionForCurrentRule = rule.getActivation();
                int activitionTicksForCurrentRule = activitionForCurrentRule.getTicks();
                float activitionProbabilityForCurrentRule = activitionForCurrentRule.getProbability();
                if(activitionProbabilityForCurrentRule >= probabilityToCheckAgainstCurrentRuleProbability
                        && (this.currentTick != 0 && this.currentTick % activitionTicksForCurrentRule == 0)){
                    activeActionsInCurrentTick.addAll(rule.getActions());
                }
            }

            for (String entityName:this.allEntities.keySet()) {
                List<EntityInstance> listOfEntityInstance = new ArrayList<>(this.allEntities.get(entityName));
                for (EntityInstance entityInstance: listOfEntityInstance ) {
                    for (IAction action:activeActionsInCurrentTick) {
                        if (action.getContextEntity().getEntityName().equalsIgnoreCase(entityInstance.getDefinitionOfEntity().getEntityName())){
                            necessaryVariables.resetKillAndCreateAndKill();
                            necessaryVariables.setPrimaryEntityInstance(entityInstance);
                            if (action.getSecondaryEntity() != null){
                                // TODO GET ENTITIES SECONDARY INSTANCES FOR CURRENT ACTION AND ACTIVATE IT
                                SecondEntity secondaryEntity = action.getSecondaryEntity();
                                List<EntityInstance> secondaryEntityInstances;
                                List<EntityInstance> filteredSecondaryEntityInstances;
                                // we hava all the entity instances that we need
                                if(secondaryEntity.getCondition() != null){
                                    secondaryEntityInstances = generateSecondaryInstancesListFromCondition(this.allEntities.get(secondaryEntity.getEntity().getEntityName()), secondaryEntity.getCondition(), necessaryVariables);
                                }
                                else{
                                    secondaryEntityInstances = this.allEntities.get(secondaryEntity.getEntity().getEntityName());
                                }

                                if(!secondaryEntity.getCount().equalsIgnoreCase("all")){
                                   filteredSecondaryEntityInstances =  getSecondaryInstancesByNumber(secondaryEntityInstances, secondaryEntity.getCount());
                                }
                                else{
                                    filteredSecondaryEntityInstances = secondaryEntityInstances;
                                }

                                for(EntityInstance currentSecondaryEntityInstance: filteredSecondaryEntityInstances){
                                    necessaryVariables.setSecondaryEntityInstance(currentSecondaryEntityInstance);
                                    action.invoke(necessaryVariables);
                                    if (necessaryVariables.getEntityToKill() != null) { //i dont know if i really need this. i think so
                                        this.entitiesToKill.add(necessaryVariables.getEntityToKill());
                                        break;
                                    }
                                    if (necessaryVariables.getEntityToKillAndCreate().getCreate() != null &&
                                            necessaryVariables.getEntityToKillAndCreate().getKill() != null) {
                                        this.entitiesToKillAndReplace.add(necessaryVariables.getEntityToKillAndCreate());
                                        break;
                                    }
                                }

                            }
                            else{
                                // i have a physical world + environment + entity instance primary no need for secondary

                                action.invoke(necessaryVariables);
                                if (necessaryVariables.getEntityToKill() != null) { //i dont know if i really need this. i think so
                                    this.entitiesToKill.add(necessaryVariables.getEntityToKill());
                                    break;
                                }
                                if (necessaryVariables.getEntityToKillAndCreate().getCreate() != null &&
                                        necessaryVariables.getEntityToKillAndCreate().getKill() != null) {
                                    this.entitiesToKillAndReplace.add(necessaryVariables.getEntityToKillAndCreate());
                                    break;
                                }
                            }
                        }
                        necessaryVariables.resetKillAndCreateAndKill();
                    }
                }
            }
            killAllEntities();
            killAndReplaceAllEntities();
            checkAllPropertyInstancesIfChanged();
            this.entitiesToKillAndReplace.clear();
            this.entitiesToKill.clear();
            this.currentTick++;
            savePopulationInCurrentTick(this.informationOfWorld.getEntitiesToPopulations(),this.currentTick);
        }

        this.timeFinished = System.currentTimeMillis();
        this.informationOfWorld.setSimulationDone(true);
        this.isStopped = true;
    }

    private void savePopulationInCurrentTick(List<EntityToPopulation> entitiesToPopulations, int currentTick) {
        int count = 0;
        int population1 = 0;
        int population2 = -1;
        String population1Name = "";
        String population2Name = "";
        for (EntityToPopulation entityToPopulation : entitiesToPopulations) {
            if (count == 0 ){
                population1Name = entityToPopulation.getCurrEntityDef().getEntityName();
                population1 = entityToPopulation.getCurrEntityPopulation();
            }
            if (count == 1){
                population2Name = entityToPopulation.getCurrEntityDef().getEntityName();
                population2 = entityToPopulation.getCurrEntityPopulation();
            }
            count++;
        }
        this.entityPopulationInEachTick.add(new DtoCountTickPopulation(population1Name, population1, population2Name, population2, currentTick));

    }

    private void moveAllEntitiesInPhysicalWorld() {
        for(String currentEntityName: allEntities.keySet()){
            List<EntityInstance> currentEntityInstanceList = allEntities.get(currentEntityName);
            moveAllInstances(currentEntityInstanceList);
        }
    }

//    private void invokeActionsOnEntityInstances(NecessaryVariablesImpl necessaryVariables, List<IAction> allActionsForCurrentRule, List<EntityInstance> copyOfEntityInstancesList) throws GeneralException {
//        // for each entity instance in the list
//        for(EntityInstance currentEntityInstance: copyOfEntityInstancesList){
//            // for each action in current rule in action list
//            for(IAction currentActionToInvoke: allActionsForCurrentRule) {
//
//                necessaryVariables.setPrimaryEntityInstance(currentEntityInstance);
//                // check if we have a secondary entity
//                if (currentActionToInvoke.getSecondaryEntity() == null) {
//
//                    if (currentActionToInvoke instanceof ActionReplace) {
//                        ActionReplace parsedAction = (ActionReplace)currentActionToInvoke;
//                        EntityDefinition definitionOfSecondEntity = getDefinitionByName(parsedAction.getEntityToCreate());
//                        //EntityDefinition definitionOfSecondEntity = getDefinitionByName(worldDefinitionForSimulation, parsedAction.getEntityToCreate());
//                        necessaryVariables.setSecondaryEntityDefinition(definitionOfSecondEntity);
//                    }
//
//                    currentActionToInvoke.invoke(necessaryVariables);
//                    if (necessaryVariables.getEntityToKill() != null) {
//                        this.entitiesToKill.add(necessaryVariables.getEntityToKill());
//                    }
//                    if (necessaryVariables.getEntityToKillAndCreate().getCreate() != null &&
//                            necessaryVariables.getEntityToKillAndCreate().getKill() != null) {
//                        this.entitiesToKillAndReplace.add(necessaryVariables.getEntityToKillAndCreate());
//                    }
//                    necessaryVariables.resetKillAndCreateAndKill();
//                }
//                else {
//                    SecondEntity secondEntity = currentActionToInvoke.getSecondaryEntity();
//                    List<EntityInstance> secondaryEntityInstances;
//                    {
//                        if (secondEntity.getCondition() == null) {
//                            if (secondEntity.getCount().equalsIgnoreCase("all")) {
//                                secondaryEntityInstances = this.allEntities.get(secondEntity.getEntity().getEntityName());
//                            } else {
//                                secondaryEntityInstances = getSecondaryInstancesByNumber(this.allEntities.get(secondEntity.getEntity().getEntityName()), secondEntity.getCount());
//                            }
//
//                        } else {
//                            if (secondEntity.getCount().equalsIgnoreCase("all")) {
//                                secondaryEntityInstances = generateSecondaryInstancesListFromCondition(this.allEntities.get(secondEntity.getEntity().getEntityName()), secondEntity.getCondition(), new NecessaryVariablesImpl(allEnvironments));
//                            } else {
//                                List<EntityInstance> conditionList = generateSecondaryInstancesListFromCondition(this.allEntities.get(secondEntity.getEntity().getEntityName()), secondEntity.getCondition(), new NecessaryVariablesImpl(allEnvironments));
//                                secondaryEntityInstances = getSecondaryInstancesByNumber(conditionList, secondEntity.getCount());
//                            }
//                        }
//
//                        for (EntityInstance currSecondaryEntityInstance : secondaryEntityInstances) {
//                            necessaryVariables.setSecondaryEntityInstance(currSecondaryEntityInstance);
//                            currentActionToInvoke.invoke(necessaryVariables);
//                            if (necessaryVariables.getEntityToKill() != null) { //i dont know if i really need this. i think so
//                                this.entitiesToKill.add(necessaryVariables.getEntityToKill());
//                                break;
//                            }
//                            if (necessaryVariables.getEntityToKillAndCreate().getCreate() != null &&
//                                    necessaryVariables.getEntityToKillAndCreate().getKill() != null) {
//                                this.entitiesToKillAndReplace.add(necessaryVariables.getEntityToKillAndCreate());
//                                break;
//                            }
//                        }
//                        necessaryVariables.resetKillAndCreateAndKill();
//                    }
//                }
//            }
//        }
//    }

    private void checkAllPropertyInstancesIfChanged() {
        for(String currEntityName: this.allEntities.keySet()){
            List<EntityInstance> currentEntityInstancesList = this.allEntities.get(currEntityName);
            for(EntityInstance currentEntityInstance: currentEntityInstancesList){
                for(String currentPropertyInstanceName: currentEntityInstance.getAllProperties().keySet()){
                    PropertyInstance currentPropertyInstance = currentEntityInstance.getAllProperties().get(currentPropertyInstanceName);
                    if(!currentPropertyInstance.getIsPropertyChangedInCurrTick()){
                        currentPropertyInstance.increaseTick();
                    }
                    else{
                        currentPropertyInstance.setIsPropertyChangedInCurrTick(false);
                        currentPropertyInstance.resetTicksToZero();
                    }
                }
            }
        }
    }

    private void initializeAllEntityInstancesLists() throws GeneralException{
        for(EntityToPopulation currentEntityToPopulation: this.getInformationOfWorld().getEntitiesToPopulations()){
            String entityDefName = currentEntityToPopulation.getCurrEntityDef().getEntityName();
            if(currentEntityToPopulation.getCurrEntityPopulation() == 0){
                this.allEntities.put(entityDefName, new ArrayList<>());
                continue;
            }

            for(int i = 0; i < currentEntityToPopulation.getCurrEntityPopulation(); i++){
                EntityInstance newEntityInstance = initializeEntityInstanceAccordingToEntityDefinition(currentEntityToPopulation.getCurrEntityDef(), i);
                if(i == 0){
                    this.allEntities.put(entityDefName, new ArrayList<>());
                }

               this.allEntities.get(entityDefName).add(newEntityInstance);
                this.physicalSpace.putEntityInWorld(newEntityInstance);
            }
        }
    }

    private EntityDefinition getDefinitionByName(String entityName) {
        for(EntityDefinition currEntityDef: this.entityDefinitions){
            if(currEntityDef.getEntityName().equalsIgnoreCase(entityName)){
                return currEntityDef;
            }
        }

        return null;
    }

    private List<EntityInstance> generateSecondaryInstancesListFromCondition(List<EntityInstance> entityInstances, IConditionComponent conditionComponent, NecessaryVariablesImpl necessaryVariables) throws GeneralException{
        List<EntityInstance> conditionListInstances = new ArrayList<>();
        for(EntityInstance currInstance: entityInstances){
            necessaryVariables.setPrimaryEntityInstance(currInstance);
            if(conditionComponent.getResultFromCondition(necessaryVariables)) {
                conditionListInstances.add(currInstance);
            }
        }

        return conditionListInstances;
    }

    private List<EntityInstance> getSecondaryInstancesByNumber(List<EntityInstance> entityInstances, String count) throws GeneralException{
        List<EntityInstance> countInstances = new ArrayList<>();
        int maxIndx = entityInstances.size() - 1;
        int numberOfInstances = Math.min(Integer.parseInt(count), maxIndx);
        for(int i = 0; i < numberOfInstances; i++){
            int randomIndx = Utilities.initializeRandomInt(0, maxIndx);
            countInstances.add(entityInstances.get(randomIndx));
        }

        return countInstances;
    }

    private void moveAllInstances(List<EntityInstance> currentEntityInstanceList) {
        for(EntityInstance currentEntityInstance: currentEntityInstanceList){
            this.physicalSpace.moveCurrentEntity(currentEntityInstance);
        }
    }

    private EntityInstance initializeEntityInstanceAccordingToEntityDefinition(EntityDefinition entityDefinitionToInitiateFrom, int id) throws GeneralException {
        EntityInstance resultEntityInstance = new EntityInstance(entityDefinitionToInitiateFrom, id);
        //now setting all property instances
        Map<String, PropertyDefinitionEntity> mapOfPropertyDefinitionsForEntity = entityDefinitionToInitiateFrom.getPropertyDefinition();

        for(String currentPropertyDefinitionName: mapOfPropertyDefinitionsForEntity.keySet()) {
            PropertyDefinitionEntity currentEntityPropertyDefinition = mapOfPropertyDefinitionsForEntity.get(currentPropertyDefinitionName);
            PropertyDefinition currentPropertyDefinition = currentEntityPropertyDefinition.getPropertyDefinition();
            PropertyInstance newPropertyInstance = new PropertyInstance(currentPropertyDefinition);
            Value valueForProperty = currentEntityPropertyDefinition.getPropValue();
            boolean isRandomInit = valueForProperty.getRandomInit();
            String initVal = valueForProperty.getInit();

            if (isRandomInit) {//meaning init val is null. need to random initialize
                switch (currentPropertyDefinition.getPropertyType().toLowerCase()) {
                    case "decimal":
                        Range rangeOfProperty1 = currentPropertyDefinition.getPropertyRange();
                        int randomInitializedInt = Utilities.initializeRandomInt((int) rangeOfProperty1.getFrom(), (int) rangeOfProperty1.getTo());
                        newPropertyInstance.setPropValue(randomInitializedInt);
                        break;
                    case "float":
                        Range rangeOfProperty2 = currentPropertyDefinition.getPropertyRange();
                        float randomInitializedFloat = Utilities.initializeRandomFloat(rangeOfProperty2);
                        newPropertyInstance.setPropValue(randomInitializedFloat);
                        break;
                    case "string":
                        String randomInitializedString = Utilities.initializeRandomString();
                        newPropertyInstance.setPropValue(randomInitializedString);
                        break;
                    case "boolean":
                        boolean randomInitializedBoolean = Utilities.initializeRandomBoolean();
                        newPropertyInstance.setPropValue(randomInitializedBoolean);
                }
            } else {
                switch (currentPropertyDefinition.getPropertyType().toLowerCase()) {
                    case "decimal":
                        if (Utilities.isInteger(initVal)) {
                            int valueToInsertInt = Integer.parseInt(initVal);
                            newPropertyInstance.setPropValue(valueToInsertInt);
                        } else {
                            throw new GeneralException("In entity " + entityDefinitionToInitiateFrom.getEntityName() + "" +
                                    "in property " + currentPropertyDefinitionName + "which is of type" + currentPropertyDefinition.getPropertyType() +
                                    "tried to insert an invalid init value type");
                        }
                        break;
                    case "float":
                        if (Utilities.isFloat(initVal)) {
                            float valueToInsertFloat = Float.parseFloat(initVal);
                            newPropertyInstance.setPropValue(valueToInsertFloat);
                        } else {
                            throw new GeneralException("In entity " + entityDefinitionToInitiateFrom.getEntityName() + "" +
                                    "in property " + currentPropertyDefinitionName + "which is of type" + currentPropertyDefinition.getPropertyType() +
                                    "tried to insert an invalid init value type");
                        }
                        break;
                    case "string":
                        newPropertyInstance.setPropValue(initVal);
                        break;
                    case "boolean":
                        boolean valueToInsertBoolean = Boolean.parseBoolean(initVal);
                        newPropertyInstance.setPropValue(valueToInsertBoolean);
                }
            }

            resultEntityInstance.addProperty(newPropertyInstance);
        }
        return resultEntityInstance;
    }

    private void killAllEntities(){
        for(EntityInstance entityInstanceToKill: this.entitiesToKill){
            removeFromEntityInstancesList(entityInstanceToKill, this.allEntities.get(entityInstanceToKill.getDefinitionOfEntity().getEntityName()));
        }
    }

    private void removeFromEntityInstancesList(EntityInstance entityInstanceToKill, List<EntityInstance> listOfEntityInstances) {
        decreaseOneEntity(entityInstanceToKill.getDefinitionOfEntity().getEntityName());
        //entityInstanceToKill.getDefinitionOfEntity().setEndPopulation(entityInstanceToKill.getDefinitionOfEntity().getEndPopulation() - 1);
        this.physicalSpace.removeEntityFromWorld(entityInstanceToKill.getPositionInWorld());
        listOfEntityInstances.remove(entityInstanceToKill);
    }

    private void decreaseOneEntity(String entityName) {
        for(EntityToPopulation currEntToPopulation: this.informationOfWorld.getEntitiesToPopulations()){
            if(entityName.equalsIgnoreCase(currEntToPopulation.getCurrEntityDef().getEntityName())){
                currEntToPopulation.setCurrEntityPopulation(currEntToPopulation.getCurrEntityPopulation() - 1);
            }
        }
    }
    private void increaseOneEntity(String entityName) {
        for(EntityToPopulation currEntToPopulation: this.informationOfWorld.getEntitiesToPopulations()){
            if(entityName.equalsIgnoreCase(currEntToPopulation.getCurrEntityDef().getEntityName())){
                currEntToPopulation.setCurrEntityPopulation(currEntToPopulation.getCurrEntityPopulation() + 1);
            }
        }
    }

    private void killAndReplaceAllEntities()throws GeneralException{
        for(CreateAndKillEntities currentKillAndReplace: this.entitiesToKillAndReplace){
            EntityInstance instanceToCreate = createAndReplace(currentKillAndReplace);
            this.allEntities.get(instanceToCreate.getDefinitionOfEntity().getEntityName()).add(instanceToCreate);
            removeFromEntityInstancesList(currentKillAndReplace.getKill(), this.allEntities.get(currentKillAndReplace.getKill().getDefinitionOfEntity().getEntityName()));

            this.physicalSpace.putEntityInWorld(instanceToCreate);
        }
    }

    private EntityInstance createAndReplace(CreateAndKillEntities currentKillAndReplace)throws GeneralException{
        EntityInstance createdInstance = null;
        switch(currentKillAndReplace.getCreationType()){
            case SCRATCH:
                createdInstance = initializeEntityInstanceAccordingToEntityDefinition(currentKillAndReplace.getCreate(),
                        this.allEntities.get(currentKillAndReplace.getCreate().getEntityName()).size());
                this.physicalSpace.replaceEntities(createdInstance, currentKillAndReplace.getKill().getPositionInWorld());

                break;
            case DERIVED:
                createdInstance = createInstanceFromAnother(currentKillAndReplace.getKill(), currentKillAndReplace.getCreate());
                this.physicalSpace.replaceEntities(createdInstance, currentKillAndReplace.getKill().getPositionInWorld());
                break;
        }

        //currentKillAndReplace.getCreate().setEndPopulation(currentKillAndReplace.getCreate().getEndPopulation() + 1);
        increaseOneEntity(currentKillAndReplace.getCreate().getEntityName());
        //currentKillAndReplace.getKill().getDefinitionOfEntity().setEndPopulation(currentKillAndReplace.getKill().getDefinitionOfEntity().getEndPopulation() - 1);
        return createdInstance;
    }

    private EntityInstance createInstanceFromAnother(EntityInstance kill, EntityDefinition create) {
        EntityInstance createdInstance = new EntityInstance(create, this.allEntities.get(create.getEntityName()).size());
        createdInstance.setPositionInWorld(kill.getPositionInWorld());
        Map<String, PropertyDefinitionEntity> propertyDefinitionMap = create.getPropertyDefinition();
        for(String currPropDefName: propertyDefinitionMap.keySet()){
            if(kill.getAllProperties().containsKey(currPropDefName)){
                PropertyInstance currentPropInstance = kill.getPropertyByName(currPropDefName);
                currentPropInstance.resetAllTicks();
                createdInstance.addProperty(currentPropInstance);
            }
            else{
                PropertyInstance newPropInstance = new PropertyInstance(create.getPropertyDefinition().get(currPropDefName).getPropertyDefinition());

                switch(newPropInstance.getPropertyDefinition().getPropertyType().toLowerCase()){
                    case "float":
                        float floatVal = Utilities.initializeRandomFloat(create.getPropertyDefinition().get(currPropDefName).getPropertyDefinition().getPropertyRange());
                        newPropInstance.setPropValue(floatVal);
                        break;
                    case "boolean":
                        boolean booleanVal = Utilities.initializeRandomBoolean();
                        newPropInstance.setPropValue(booleanVal);
                        break;
                    case "string":
                        String stringVal = Utilities.initializeRandomString();
                        newPropInstance.setPropValue(stringVal);
                        break;
                }
                newPropInstance.resetAllTicks();
                createdInstance.addProperty(newPropInstance);
            }
        }

        return createdInstance;
    }

    public int getCurrentTick() {
        return this.currentTick;
    }

    public long getCurrentTimePassed() {
        if (this.isStopped || this.informationOfWorld.isSimulationDone()){
            return (this.timeFinished - this.startTheSimulation - this.totalTimeInPause)/1000;
        }
        else{
            if (isPaused){
                return (this.currentTimePassed - this.startTheSimulation - this.totalTimeInPause)/1000;
            }
            else if (!this.isStarted){
                return 0;
            }
            else{
                return (System.currentTimeMillis() - this.startTheSimulation - this.totalTimeInPause)/1000;
            }
        }
    }

    public List<String> bringPropertyNamesListAccordingToEntityName(String entityName) {
        List<String> propertyNames = null;
        for(EntityDefinition currEntityDef: this.entityDefinitions){
            if(entityName.equalsIgnoreCase(currEntityDef.getEntityName())){
                Set<String> allPropertyNames = currEntityDef.getPropertyDefinition().keySet();
                propertyNames = new ArrayList<>(allPropertyNames);
                break;
            }
        }
        return propertyNames;
    }
}
