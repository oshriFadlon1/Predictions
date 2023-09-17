package engine;

import constans.Constans;
import dto.*;
import entity.EntityDefinition;
import entity.EntityToPopulation;
import environment.EnvironmentDefinition;
import environment.EnvironmentInstance;
import interfaces.InterfaceMenu;
import property.*;
import range.Range;
import rule.ActivationForRule;
import rule.Rule;
import rule.action.IAction;
import simulationmanager.SimulationExecutionerManager;
import utility.Utilities;
import world.GeneralInformation;
import world.WorldDefinition;
import world.WorldInstance;
import exceptions.GeneralException;
import xmlParser.XmlParser;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class MainEngine implements InterfaceMenu {

    private XmlParser xmlParser;
    private WorldDefinition xmlWorldDefinition;
    private WorldDefinition worldDefinitionForSimulation;
    private SimulationExecutionerManager simulationExecutionerManager;
    private DtoBasicSimulationInfo dtoAllSimulationsStartingInfo;


    public MainEngine() {
        this.simulationExecutionerManager = null;
        this.dtoAllSimulationsStartingInfo = null;

    }

    public WorldDefinition getWorldDefinitionForSimulation(){
        return this.worldDefinitionForSimulation;
    }


    private String getCurrentTimeAndDateInTheFormat() {
        Date currentDate = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy | hh.mm.ss");

        return sdf.format(currentDate);
    }

    public DtoResponsePreview previewWorldInfo(){
        return new DtoResponsePreview(this.worldDefinitionForSimulation.getWorldSize().getRow(), this.worldDefinitionForSimulation.getWorldSize().getCol(), getEnvironmentsInfo(), getEntitiesInfoSimulation(),getRulesInfoSimulation(),
                new DtoResponsePreviewTermination(worldDefinitionForSimulation.getTermination().getTicks(),
                        worldDefinitionForSimulation.getTermination().getSeconds()));
    }

    public void clearAllInformation(){
        if(this.simulationExecutionerManager != null) {
            this.simulationExecutionerManager.clearInformation();
        }
    }

    public List<String> bringPropertiesByEntityName(int simulationId, String entityName){
        List<String> propertyNamesList = this.simulationExecutionerManager.bringPropertyNamesList(simulationId, entityName);
        return propertyNamesList;
    }

    private DtoEnvironments getEnvironmentsInfo() {
        return new DtoEnvironments(this.worldDefinitionForSimulation.getAllEnvironments());
    }

    private List<DtoResponseEntities> getEntitiesInfoSimulation() {
        List<DtoResponseEntities> entities = new ArrayList<>();
        String nameEntity = "";
        int population = 0;
        boolean isTheFirst = true;
        for (EntityDefinition entityDefinition: worldDefinitionForSimulation.getEntityDefinitions()) {
            List<PropertyDefinitionEntity> propertyDefinitionEntityList = new ArrayList<>();
            if (isTheFirst) {
                nameEntity = entityDefinition.getEntityName();
                isTheFirst = false;
            }
            for (String key:entityDefinition.getPropertyDefinition().keySet()) {
                PropertyDefinitionEntity propertyDefinitionEntity = entityDefinition.getPropertyDefinition().get(key);
                if (propertyDefinitionEntity.getPropertyDefinition().getPropertyRange() == null){
                    propertyDefinitionEntityList.add(new PropertyDefinitionEntity(
                            new PropertyDefinition(propertyDefinitionEntity.getPropertyDefinition().getPropertyName(),
                                    propertyDefinitionEntity.getPropertyDefinition().getPropertyType()),
                            new Value(propertyDefinitionEntity.getPropValue().getRandomInit(),
                                    propertyDefinitionEntity.getPropValue().getInit())));
                } else {
                    propertyDefinitionEntityList.add(new PropertyDefinitionEntity(
                            new PropertyDefinition(propertyDefinitionEntity.getPropertyDefinition().getPropertyName(),
                                    propertyDefinitionEntity.getPropertyDefinition().getPropertyType(),
                                    new Range(propertyDefinitionEntity.getPropertyDefinition().getPropertyRange().getFrom(),
                                            propertyDefinitionEntity.getPropertyDefinition().getPropertyRange().getTo())),
                            new Value(propertyDefinitionEntity.getPropValue().getRandomInit(),
                                    propertyDefinitionEntity.getPropValue().getInit())));
                }
            }
            isTheFirst = true;
            entities.add( new DtoResponseEntities(nameEntity, propertyDefinitionEntityList));
        }
        return entities;
    }
    private List<DtoResponseRules> getRulesInfoSimulation() {
        List<DtoResponseRules> dtoResponseRules = new ArrayList<>();
        List<DtoActionResponse> ActionName;
        String ruleName = "";
        ActivationForRule activation = null;
        for (Rule rule: worldDefinitionForSimulation.getRules()) {
            ActionName = new ArrayList<>();
            for (IAction action:rule.getActions()) {
                ActionName.add(action.getActionResponse());
            }
            dtoResponseRules.add(new DtoResponseRules(rule.getRuleName(),
                    rule.getActivation().getTicks(),
                    rule.getActivation().getProbability(),
                    ActionName));
        }
        return dtoResponseRules;
    }


    //func 1
    @Override
    public DtoResponse createWorldDefinition(String xmlPath){
        String resultErrorMsgToUser;
        XmlParser xmlParserInCheck = new XmlParser(xmlPath);
        try {
            this.xmlWorldDefinition = xmlParserInCheck.tryToReadXml();
            this.worldDefinitionForSimulation = this.xmlWorldDefinition;
            if (this.simulationExecutionerManager != null){
                this.simulationExecutionerManager.disposeThreadPool();
            }
            this.simulationExecutionerManager = new SimulationExecutionerManager(this.worldDefinitionForSimulation.getNumberOfThreads());
            this.dtoAllSimulationsStartingInfo = new DtoBasicSimulationInfo();
        }
        catch(JAXBException | IOException | GeneralException e){
            if(e instanceof  JAXBException || e instanceof IOException){
                return new DtoResponse("There was an error in reading the XML file.",false);
            }
            else{
                return new DtoResponse(((GeneralException) e).getErrorMsg(),false);
            }
        }

        this.xmlParser = xmlParserInCheck;
        return new DtoResponse(Constans.SUCCEED_LOAD_FILE,true);
    }
    //func 2
    @Override
    public DtoResponsePreview showCurrentSimulation(){
        return previewWorldInfo();
    }
    //func 3
    public void executeSimulation(DtoUiToEngine envInputFromUser){
        Map<String, Object> environmentsForEngine = envInputFromUser.getEnvironmentToValue();
        List<EntityToPopulation> entitiesToPopulations = createEntitiesToPopulationList(envInputFromUser);
        GeneralInformation infoForSimulation = new GeneralInformation(envInputFromUser.getPopulation1(), envInputFromUser.getPopulation2(),
                this.worldDefinitionForSimulation.getWorldSize(), LocalDateTime.now() , this.worldDefinitionForSimulation.getTermination(), entitiesToPopulations);
        Map<String, EnvironmentInstance> environmentInstancesMap = createAllEnvironmentInstances(environmentsForEngine);
        WorldInstance worldInstance = new WorldInstance(environmentInstancesMap, this.worldDefinitionForSimulation.getEntityDefinitions(), this.worldDefinitionForSimulation.getRules(), infoForSimulation);
        this.dtoAllSimulationsStartingInfo.addStartingSimulationDetails(envInputFromUser, GeneralInformation.getIdOfSimulation());
        this.simulationExecutionerManager.addCurrentSimulationToManager(worldInstance);
    }

    @Override
    public DtoSimulationDetails getSimulationById(int idOfCurrentSimulation) {
        return this.simulationExecutionerManager.getSimulationById(idOfCurrentSimulation);
    }

    public DtoUiToEngine getSimulationStartingInfoById(int idOfChosenSimulation){
        return this.dtoAllSimulationsStartingInfo.getChosenRerunSimulation(idOfChosenSimulation);
    }

    @Override
    public List<DtoCountTickPopulation> getSimulationListOfPopulationPerTick(int simulationId) {
        return this.simulationExecutionerManager.getSimulationListOfPopulationPerTick(simulationId);
    }

    @Override
    public DtoAllSimulationDetails getAllSimulations() {
        return this.simulationExecutionerManager.createMapOfSimulationsToIsRunning();
    }

    @Override
    public void pauseCurrentSimulation(int simulationId) {
        this.simulationExecutionerManager.pauseCurrentSimulation(simulationId);
    }

    @Override
    public void resumeCurretnSimulation(int simulationId) {
        this.simulationExecutionerManager.resumeCurrentSimulation(simulationId);
    }

    @Override
    public void stopCurrentSimulation(int simulationId) {
        this.simulationExecutionerManager.stopCurrentSimulation(simulationId);
    }

    @Override
    public DtoQueueManagerInfo getQueueManagerInfo() {
        if (this.simulationExecutionerManager != null){
            DtoQueueManagerInfo simulationsStateManager = this.simulationExecutionerManager.getQueueManagerInfo();
            return simulationsStateManager;
        }
        return null;
    }

    private List<EntityToPopulation> createEntitiesToPopulationList(DtoUiToEngine inputFromUser) {
        List<EntityToPopulation> entitiesToPopulationList = new ArrayList<>();
        List<EntityDefinition> entityDefsList = this.worldDefinitionForSimulation.getEntityDefinitions();
        for(EntityDefinition currEntityDefinition: entityDefsList){
            EntityToPopulation newEntityPopulation;
            if(currEntityDefinition.getEntityName().equalsIgnoreCase(inputFromUser.getPrimaryEntityName())){
                newEntityPopulation = new EntityToPopulation(currEntityDefinition, inputFromUser.getPopulation1());
                entitiesToPopulationList.add(newEntityPopulation);
            }
            else{
                newEntityPopulation = new EntityToPopulation(currEntityDefinition, inputFromUser.getPopulation2());
                entitiesToPopulationList.add(newEntityPopulation);
            }
        }
        return entitiesToPopulationList;
    }

    private Map<String, EnvironmentInstance> createAllEnvironmentInstances(Map<String, Object> environmentsForEngine) {
        Map<String, EnvironmentDefinition> allEnvDefs = this.worldDefinitionForSimulation.getAllEnvironments();
        Map<String, EnvironmentInstance> allEnvIns = new HashMap<>();
        for(String envName: environmentsForEngine.keySet()){
            EnvironmentDefinition currEnvDef = allEnvDefs.get(envName);
            allEnvIns.put(envName, new EnvironmentInstance(currEnvDef.createCloneOfEnvironmentDefinition(), environmentsForEngine.get(envName)));
        }

        return allEnvIns;
    }

    @Override
    public DtoSimulationDetails fetchChosenWorld(int userSimulationChoice) {
        DtoSimulationDetails currentChosenSimulation = this.simulationExecutionerManager.getSimulationById(userSimulationChoice);
        return currentChosenSimulation;
    }

    public DtoEnvironments sendEnvironmentsToUser(){
        Map<String, EnvironmentDefinition> allEnvironments = worldDefinitionForSimulation.getAllEnvironments();
        DtoEnvironments environmentsForUser = new DtoEnvironments(allEnvironments);

        return environmentsForUser;
    }

    public Object initializeRandomEnvironmentValues(PropertyDefinition propertyDef){
        Object initVal = null;
        switch(propertyDef.getPropertyType().toLowerCase()){
            case "float":
                float initEnvValFloat = Utilities.initializeRandomFloat(propertyDef.getPropertyRange());
                initVal = initEnvValFloat;
                break;
            case "string":
                String initEnvValString = Utilities.initializeRandomString();
                initVal = initEnvValString;
                break;
            case "boolean":
                boolean initEnvValBoolean = Utilities.initializeRandomBoolean();
                initVal = initEnvValBoolean;
                break;
            default:
                break;
        }
        return initVal;
    }

    public boolean validateUserEnvInput(String userInput, PropertyDefinition propDef){
        switch(propDef.getPropertyType().toLowerCase()){
            case "float":
                if(!Utilities.isFloat(userInput)){
                    return false;
                }
                else{
                    float valueOFInputFloat = Float.parseFloat(userInput);
                    if(!(valueOFInputFloat >= propDef.getPropertyRange().getFrom() && valueOFInputFloat <= propDef.getPropertyRange().getTo())){
                        return false;
                    }
                }
                break;
            case "string":
                break;
            case "boolean":
                if(!userInput.equalsIgnoreCase("true")  && !userInput.equalsIgnoreCase("false")){
                    return false;
                }
                break;

        }

        return true;
    }
    public Object initializeValueFromUserInput(String userInput, PropertyDefinition propDef){
        Object initVal = null;
        switch(propDef.getPropertyType().toLowerCase()){
            case "float":
                float initFloat = Float.parseFloat(userInput);
                initVal = initFloat;
                break;
            case "string":
                String initString = userInput;
                initVal = initString;
                break;
            case "boolean":
                boolean initBoolean = Boolean.parseBoolean(userInput);
                initVal = initBoolean;

        }

        return initVal;
    }

    @Override
    public SimulationExecutionerManager getExecutionsManager() {
        return this.simulationExecutionerManager;
    }
}
