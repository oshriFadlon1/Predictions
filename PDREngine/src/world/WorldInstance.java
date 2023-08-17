package world;

import dto.DtoResponseTermination;
import entity.EntityDefinition;
import entity.EntityInstance;
import environment.EnvironmentInstance;
import exceptions.GeneralException;
import necessaryVariables.NecessaryVariablesImpl;
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

import java.io.Serializable;
import java.util.*;

public class WorldInstance implements Serializable {
    private Map<String, EnvironmentInstance> allEnvironments;
    private Map<String,List<EntityInstance>> allEntities;
    private List<Rule> allRules;
    private Termination termination;

    public WorldInstance(Map<String, EnvironmentInstance> allEnvironments) {
        this.allEnvironments = allEnvironments;
        this.allEntities = new HashMap<>();
        this.allRules = new ArrayList<>();
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

    public Termination getTermination() {
        return termination;
    }

    public void setTermination(Termination termination) {
        this.termination = termination;
    }

//    public void addEnvironment(EnvironmentInstance environmentDataMember) throws GeneralException {
//        if(this.allEnvironments.containsKey(environmentDataMember.getEnvironmentDefition().getPropertyName()))
//        {
//            throw new GeneralException("Current environment already exists");
//        }
//
//        this.allEnvironments.put(environmentDataMember.getEnvPropertyDefinition().getPropertyName(), environmentDataMember);
//    }
          //dto response of ending simulation
    public DtoResponseTermination runSimulation(WorldDefinition worldDefinitionForSimulation) throws GeneralException{
        boolean endedByTicks = false, endedBySeconds = false;
        NecessaryVariablesImpl necessaryVariables = new NecessaryVariablesImpl(allEnvironments);

//        initializing all entity instances
        List<EntityDefinition> allEntityDefinitions = worldDefinitionForSimulation.getEntityDefinitions();
        for(EntityDefinition currentEntityDefinition: allEntityDefinitions){
            String entityDefinitionName = currentEntityDefinition.getEntityName();
            for (int i = 0; i < currentEntityDefinition.getStartPopulation(); i++) {
                EntityInstance newEntityInstance = initializeEntityInstanceAccordingToEntityDefinition(currentEntityDefinition, i);
                if (i == 0){
                    allEntities.put(entityDefinitionName,new ArrayList<>());
                }
                allEntities.get(entityDefinitionName).add(newEntityInstance);
            }
        }

        this.allRules = worldDefinitionForSimulation.getRules();
        this.termination = worldDefinitionForSimulation.getTermination();

        int currentTickCount = 0;
        Random random = new Random();
        long timeStarted = System.currentTimeMillis();
        long currentTime = System.currentTimeMillis();

        while (worldDefinitionForSimulation.getTermination().isTicksActive(currentTickCount) &&
                worldDefinitionForSimulation.getTermination().isSecondsActive(currentTime - timeStarted)){

            for(Rule currentRuleToInvokeOnEntities: allRules){
                List<IAction> allActionsForCurrentRule = currentRuleToInvokeOnEntities.getActions();
                float probabilityToCheckAgainstCurrentRuleProbability = random.nextFloat();
                ActivationForRule activitionForCurrentRule = currentRuleToInvokeOnEntities.getActivation();
                int activitionTicksForCurrentRule = activitionForCurrentRule.getTicks();
                float activitionProbabilityForCurrentRule = activitionForCurrentRule.getProbability();
                if(activitionProbabilityForCurrentRule >= probabilityToCheckAgainstCurrentRuleProbability
                        && (currentTickCount != 0 && currentTickCount % activitionTicksForCurrentRule == 0)){
                    //need to invoke the rule for each entity instance
                    for (String currentEntityName: allEntities.keySet()) {
                        // the current list of entity instances from the map
                        List<EntityInstance> currentEntityInstanceList = allEntities.get(currentEntityName);
                        // set the list of entities to the "context" object to invoke
                        necessaryVariables.setEntityInstanceManager(currentEntityInstanceList);
                        // create a copy of the entity instance list to run on it.
                        List<EntityInstance> copyOfEntityInstancesList = new ArrayList<>(currentEntityInstanceList);
                        // invoke each action on each entity
                        for(EntityInstance currentEntityInstance: copyOfEntityInstancesList){
                            for(IAction currentActionToInvoke: allActionsForCurrentRule){
                                necessaryVariables.setPrimaryEntityInstance(currentEntityInstance);
                                currentActionToInvoke.invoke(necessaryVariables);
                            }
                        }
                    }
                }


            }
            currentTickCount++;
            currentTime = System.currentTimeMillis();
        }

        if(worldDefinitionForSimulation.getTermination().getTicks() <= currentTickCount){
            endedByTicks = true;
        }
        if((currentTime - timeStarted) / 1000 >= worldDefinitionForSimulation.getTermination().getSeconds()){
            endedBySeconds = true;
        }

//        for(String currentEntityName: allEntities.keySet()){
//
//            for(EntityInstance entityInstance:allEntities.get(currentEntityName)){
//                System.out.println(entityInstance.getId());
//                for(String propName: entityInstance.getAllProperties().keySet()){
//                    System.out.println(entityInstance.getAllProperties().get(propName));
//                }
//
//            }
//        }

        DtoResponseTermination responseOfSimulation = new DtoResponseTermination(endedByTicks, endedBySeconds);
        return responseOfSimulation;

    }

    private EntityInstance initializeEntityInstanceAccordingToEntityDefinition(EntityDefinition entityDefinitionToInitiateFrom, int id) throws GeneralException {
        EntityInstance resultEntityInstance = new EntityInstance(entityDefinitionToInitiateFrom, id);
        //now setting all property instances
        Map<String, PropertyDefinitionEntity> mapOfPropertyDefinitionsForEntity = entityDefinitionToInitiateFrom.getPropertyDefinition();

        for(String currentPropertyDefinitionName: mapOfPropertyDefinitionsForEntity.keySet()){
            PropertyDefinitionEntity currentEntityPropertyDefinition = mapOfPropertyDefinitionsForEntity.get(currentPropertyDefinitionName);
            PropertyDefinition currentPropertyDefinition = currentEntityPropertyDefinition.getPropertyDefinition();
            PropertyInstance newPropertyInstance = new PropertyInstance(currentPropertyDefinition);
            Value valueForProperty = currentEntityPropertyDefinition.getPropValue();
            boolean isRandomInit = valueForProperty.getRandomInit();
            String initVal = valueForProperty.getInit();

            if(isRandomInit){//meaning init val is null. need to random initialize
                switch(currentPropertyDefinition.getPropertyType().toLowerCase()){
                    case "decimal":
                        Range rangeOfProperty1 = currentPropertyDefinition.getPropertyRange();
                        int randomInitializedInt = Utilities.initializeRandomInt((int)rangeOfProperty1.getFrom(), (int)rangeOfProperty1.getTo());
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
            }
            else{
                    switch (currentPropertyDefinition.getPropertyType().toLowerCase()) {
                        case "decimal":
                            if(Utilities.isInteger(initVal)) {
                                int valueToInsertInt = Integer.parseInt(initVal);
                                newPropertyInstance.setPropValue(valueToInsertInt);
                            }
                            else{
                                throw new GeneralException("In entity " + entityDefinitionToInitiateFrom.getEntityName() + "" +
                                        "in property " + currentPropertyDefinitionName + "which is of type" + currentPropertyDefinition.getPropertyType() +
                                        "tried to insert an invalid init value type");
                            }
                            break;
                        case "float":
                            if(Utilities.isFloat(initVal)) {
                                float valueToInsertFloat = Float.parseFloat(initVal);
                                newPropertyInstance.setPropValue(valueToInsertFloat);
                            }
                            else{
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

}
