package world;

import createAndKillEntities.CreateAndKillEntities;
import dto.DtoResponseTermination;
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
import rule.action.ActionReplace;
import rule.action.IAction;
import termination.Termination;
import utility.Utilities;
import worldPhysicalSpace.WorldPhysicalSpace;

import java.io.Serializable;
import java.util.*;

public class WorldInstance implements Serializable, Runnable {
    // map<String, Integer> population;
    // from name of entity to number of population
    // save with prefix start to set the start population
    // and End to set the end population.
    private Map<String, EnvironmentInstance> allEnvironments;
    private Map<String,List<EntityInstance>> allEntities;
    private List<EntityDefinition> entityDefinitions;
    private List<Rule> allRules;
    private GeneralInformation informationOfWorld;
    private List<EntityInstance> entitiesToKill;
    private List<CreateAndKillEntities> entitiesToKillAndReplace;
    private WorldPhysicalSpace physicalSpace;
    private int primaryEntityPopulation;
    private int secondaryEntityPopulation;
    private int currentTick;
    private long currentTimePassed;
    private long currentTimeStarted;
    private boolean isPaused;
    private boolean isStopped;



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
        this.primaryEntityPopulation = informationOfWorld.getPrimaryEntityStartPopulation();
        this.secondaryEntityPopulation = informationOfWorld.getSecondaryEntityStartPopulation();
        this.currentTick = 0;
        this.currentTimePassed = 0;
        this.currentTimeStarted = System.currentTimeMillis();
        this.isPaused = false;
        this.isStopped = false;
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

        necessaryVariables.setWorldPhysicalSpace(this.physicalSpace);

//        this.allRules = worldDefinitionForSimulation.getRules();
//        this.termination = worldDefinitionForSimulation.getTermination();
        Termination currentTermination = this.informationOfWorld.getTermination();

        int currentTickCount = 0;
        Random random = new Random();
        long timeStarted = System.currentTimeMillis();
        long currentTime = System.currentTimeMillis();
        List<String> entityNamesForChecking = new ArrayList<>();

        while (currentTermination.isTicksActive(this.currentTick) && currentTermination.isSecondsActive(currentTime - timeStarted)/*worldDefinitionForSimulation.getTermination().isTicksActive(currentTickCount) &&
                worldDefinitionForSimulation.getTermination().isSecondsActive(currentTime - timeStarted)*/ && !isStopped){
            if(isPaused){
                long lastSavedSeconds = this.currentTimePassed;
                int lastSavedTick = this.currentTick;
                while(isPaused){
                    try {
                        Thread.sleep(100);
                        if(!isPaused){

                            this.currentTick = lastSavedTick;
                            this.currentTimePassed = lastSavedSeconds;
                            break;
                        }
                        else{
                            isPaused = true;
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }


            for(String currentEntityName: allEntities.keySet()){
                List<EntityInstance> currentEntityInstanceList = allEntities.get(currentEntityName);
                moveAllInstances(currentEntityInstanceList);
                //entityNamesForChecking.add(currentEntityName);

            }
            //TODO: REMEMBER TO DELETE THE FOLLOWING FOUR LINES. THIS IS ONLY A TEST!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//            EntityInstance instance1 = this.allEntities.get(entityNamesForChecking.get(0)).get(0);
//            EntityInstance instance2 = this.allEntities.get(entityNamesForChecking.get(1)).get(0);
//            instance1.setPositionInWorld(new PointCoord(5, 5));
//            instance2.setPositionInWorld(new PointCoord(3, 4));

            for(Rule currentRuleToInvokeOnEntities: this.allRules){
                List<IAction> allActionsForCurrentRule = currentRuleToInvokeOnEntities.getActions();
                float probabilityToCheckAgainstCurrentRuleProbability = random.nextFloat();
                ActivationForRule activitionForCurrentRule = currentRuleToInvokeOnEntities.getActivation();
                int activitionTicksForCurrentRule = activitionForCurrentRule.getTicks();
                float activitionProbabilityForCurrentRule = activitionForCurrentRule.getProbability();
                if(activitionProbabilityForCurrentRule >= probabilityToCheckAgainstCurrentRuleProbability
                        && (this.currentTick != 0 && this.currentTick % activitionTicksForCurrentRule == 0)){
                    //need to invoke the rule for each entity instance
                    for (String currentEntityName: allEntities.keySet()) {
                        // the current list of entity instances from the map
                        List<EntityInstance> currentEntityInstanceList = allEntities.get(currentEntityName);
                        // set the list of entities to the "context" object to invoke
                        necessaryVariables.setEntityInstanceManager(currentEntityInstanceList);
                        // create a copy of the entity instance list to run on it.
                        List<EntityInstance> copyOfEntityInstancesList = new ArrayList<>(currentEntityInstanceList);
                        // get secondary entities

                        // invoke each action on each entity
                        for(EntityInstance currentEntityInstance: copyOfEntityInstancesList){
                            for(IAction currentActionToInvoke: allActionsForCurrentRule) {
                                necessaryVariables.setPrimaryEntityInstance(currentEntityInstance);
                                if (currentActionToInvoke.getSecondaryEntity() == null) {
                                    if (currentActionToInvoke instanceof ActionReplace) {
                                        ActionReplace parsedAction = (ActionReplace)currentActionToInvoke;
                                        EntityDefinition definitionOfSecondEntity = getDefinitionByName(parsedAction.getEntityToCreate());
                                        //EntityDefinition definitionOfSecondEntity = getDefinitionByName(worldDefinitionForSimulation, parsedAction.getEntityToCreate());
                                        necessaryVariables.setSecondaryEntityDefinition(definitionOfSecondEntity);
                                    }

                                    currentActionToInvoke.invoke(necessaryVariables);
                                    if (necessaryVariables.getEntityToKill() != null) {
                                        this.entitiesToKill.add(necessaryVariables.getEntityToKill());
                                    }
                                    if (necessaryVariables.getEntityToKillAndCreate().getCreate() != null &&
                                            necessaryVariables.getEntityToKillAndCreate().getKill() != null) {
                                        this.entitiesToKillAndReplace.add(necessaryVariables.getEntityToKillAndCreate());
                                    }
                                    necessaryVariables.resetKillAndCreateAndKill();
                                } else {
                                    SecondEntity secondEntity = currentActionToInvoke.getSecondaryEntity();
                                    List<EntityInstance> secondaryEntityInstances;
                                    {
                                        if (secondEntity.getCondition() == null) {
                                            if (secondEntity.getCount().equalsIgnoreCase("all")) {
                                                secondaryEntityInstances = this.allEntities.get(secondEntity.getEntity().getEntityName());
                                            } else {
                                                secondaryEntityInstances = getSecondaryInstancesByNumber(this.allEntities.get(secondEntity.getEntity().getEntityName()), secondEntity.getCount());
                                            }

                                        } else {
                                            if (secondEntity.getCount().equalsIgnoreCase("all")) {
                                                secondaryEntityInstances = generateSecondaryInstancesListFromCondition(this.allEntities.get(secondEntity.getEntity().getEntityName()), secondEntity.getCondition(), new NecessaryVariablesImpl(allEnvironments));
                                            } else {
                                                List<EntityInstance> conditionList = generateSecondaryInstancesListFromCondition(this.allEntities.get(secondEntity.getEntity().getEntityName()), secondEntity.getCondition(), new NecessaryVariablesImpl(allEnvironments));
                                                secondaryEntityInstances = getSecondaryInstancesByNumber(conditionList, secondEntity.getCount());
                                            }
                                        }

                                        for (EntityInstance currSecondaryEntityInstance : secondaryEntityInstances) {
                                            necessaryVariables.setSecondaryEntityInstance(currSecondaryEntityInstance);
                                            currentActionToInvoke.invoke(necessaryVariables);
                                            if (necessaryVariables.getEntityToKill() != null) { //i dont know if i really need this. i think so
                                                this.entitiesToKill.add(necessaryVariables.getEntityToKill());
                                            }
                                            if (necessaryVariables.getEntityToKillAndCreate().getCreate() != null &&
                                                    necessaryVariables.getEntityToKillAndCreate().getKill() != null) {
                                                this.entitiesToKillAndReplace.add(necessaryVariables.getEntityToKillAndCreate());
                                            }
                                            necessaryVariables.resetKillAndCreateAndKill();
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
            killAllEntities();
            killAndReplaceAllEntities();
            checkAllPropertyInstancesIfChanged();
            this.entitiesToKillAndReplace.clear();;
            this.entitiesToKill.clear();
            this.currentTick++;
            currentTime = System.currentTimeMillis();
            this.currentTimePassed = (currentTime - timeStarted) / 1000;

        }

        if(currentTermination.getTicks() <= currentTick/*worldDefinitionForSimulation.getTermination().getTicks() <= currentTickCount*/){
            endedByTicks = true;
        }
        if((currentTime - timeStarted / 1000) >= currentTermination.getSeconds()/*(currentTime - timeStarted) / 1000 >= worldDefinitionForSimulation.getTermination().getSeconds()*/){
            endedBySeconds = true;
        }

        DtoResponseTermination responseOfSimulation = new DtoResponseTermination(endedByTicks, endedBySeconds);
        //return responseOfSimulation;

    }

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
        EntityInstance createdInstance = new EntityInstance(create, this.allEntities.size());
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

    public int getPrimaryEntityPopulation() {
        return this.primaryEntityPopulation;
    }

    public int getSecondaryEntityPopulation() {
        return this.secondaryEntityPopulation;
    }

    public int getCurrentTick() {
        return this.currentTick;
    }

    public long getCurrentTimePassed() {
        return this.currentTimePassed;
    }
}
