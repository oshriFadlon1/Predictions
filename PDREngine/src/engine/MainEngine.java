package engine;
import constans.Constans;
import dto.*;
import entity.EntityDefinition;
import entity.EntityInstance;
import environment.EnvironmentDefinition;
import environment.EnvironmentInstance;
import interfaces.InterfaceMenu;
import property.*;
import range.Range;
import rule.ActivationForRule;
import rule.Rule;
import rule.action.IAction;
import utility.Utilities;
import world.WorldDefinition;
import world.WorldInstance;
import exceptions.GeneralException;
import xmlParser.XmlParser;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainEngine implements InterfaceMenu {

    private XmlParser xmlParser;
    private WorldDefinition xmlWorldDefinition;
    private WorldDefinition worldDefinitionForSimulation;
    private List<WorldInstance> allSimulations; // current running simulation

    private List<WorldInstance> oldSimulation; // old simulation with results

    private Map<Integer, String> simulationId2CurrentTimeAndDate;// get old World simulation info need to take index from map - 1;


    public MainEngine() {
        this.allSimulations = new ArrayList<>();
        this.oldSimulation = new ArrayList<>();
        this.simulationId2CurrentTimeAndDate = new HashMap<>();

    }

    public List<WorldInstance> getAllSimulations() {
        //
        return allSimulations;
    }

    public void setAllSimulations(List<WorldInstance> allSimulations) {
        this.allSimulations = allSimulations;
    }

    public List<WorldInstance> getOldSimulation() {
        return oldSimulation;
    }

    public void setOldSimulation(List<WorldInstance> oldSimulation) {
        this.oldSimulation = oldSimulation;
    }

    public Map<Integer, String> getSimulationId2CurrentTimeAndDate() {
        return simulationId2CurrentTimeAndDate;
    }

    public void setSimulationId2CurrentTimeAndDate(Map<Integer, String> simulationId2CurrentTimeAndDate) {
        this.simulationId2CurrentTimeAndDate = simulationId2CurrentTimeAndDate;
    }

    public void addWorld(WorldInstance juiceWrld)
    {
        this.allSimulations.add(juiceWrld);
    }


    private String getCurrentTimeAndDateInTheFormat() {
        Date currentDate = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy | hh.mm.ss");

        return sdf.format(currentDate);
    }

    public DtoResponsePreview previewWorldInfo(){

        return new DtoResponsePreview(getEnvironmentsInfo(), getEntitiesInfoSimulation(),getRulesInfoSimulation() ,
                new DtoResponsePreviewTermination(worldDefinitionForSimulation.getTermination().getTicks(),
                        worldDefinitionForSimulation.getTermination().getSeconds()));
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
                                    propertyDefinitionEntity.getPropertyDefinition().getPropertyRange()!=null?
                                            new Range(propertyDefinitionEntity.getPropertyDefinition().getPropertyRange().getFrom(),
                                                    propertyDefinitionEntity.getPropertyDefinition().getPropertyRange().getTo()):null),
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
        XmlParser xmlParserInCheck= new XmlParser(xmlPath);
        try {
            this.xmlWorldDefinition = xmlParserInCheck.tryToReadXml();
            this.worldDefinitionForSimulation = this.xmlWorldDefinition;
            this.oldSimulation.clear();
            this.simulationId2CurrentTimeAndDate.clear();
            this.allSimulations.clear();
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
    @Override
    public DtoResponseSimulationEnded runSimulations(DtoUiToEngine envInputFromUser){
        //TODO PUT POPULATION IN WORLD INSTANSE
        this.worldDefinitionForSimulation.resetEntityDefinition();
        this.worldDefinitionForSimulation.setPopulation(envInputFromUser.getPopulation1(), envInputFromUser.getPopulation2());
        Map<String, Object> environmentsForEngine = envInputFromUser.getEnvironmentToValue();
        DtoResponseSimulationEnded responseForUser = null;
        Map<String, EnvironmentInstance> environmentInstancesMap = createAllEnvironmentInstances(environmentsForEngine);
        WorldInstance worldInstance = new WorldInstance(environmentInstancesMap, worldDefinitionForSimulation.getWorldSize());
        this.allSimulations.add(worldInstance);
       try {
           //for (WorldInstance currentSimulation : this.allSimulations)
           for (int i = 0; i < this.allSimulations.size(); i++) {
               WorldInstance currentSimulation = this.allSimulations.get(i);
               currentSimulation.setAllEnvironments(environmentInstancesMap);
               DtoResponseTermination currSimulationTermination = currentSimulation.runSimulation(this.worldDefinitionForSimulation);

               Integer indexOfCurrentMovedSimulation = moveSimulationToOldSimulations(allSimulations.indexOf(currentSimulation));
               responseForUser = new DtoResponseSimulationEnded(currSimulationTermination, indexOfCurrentMovedSimulation);
               //here we will call function, move world to current simulation
           }
           return responseForUser;
       }
       catch(GeneralException e){
           return new DtoResponseSimulationEnded(e.getErrorMsg());
           //now we return a dto response that represents the error message
       }
    }

    private Integer moveSimulationToOldSimulations(int i) {
        this.oldSimulation.add(allSimulations.remove(i));
        int indexOfCurrentMovedWolrd = oldSimulation.size();
        String dateInfo = getCurrentTimeAndDateInTheFormat();

        this.simulationId2CurrentTimeAndDate.put(indexOfCurrentMovedWolrd, dateInfo);
        return indexOfCurrentMovedWolrd;
    }


    private Map<String, EnvironmentInstance> createAllEnvironmentInstances(Map<String, Object> environmentsForEngine) {
        Map<String, EnvironmentDefinition> allEnvDefs = this.worldDefinitionForSimulation.getAllEnvironments();
        Map<String, EnvironmentInstance> allEnvIns = new HashMap<>();
        for(String envName: environmentsForEngine.keySet()){
            EnvironmentDefinition currEnvDef = allEnvDefs.get(envName);
            allEnvIns.put(envName, new EnvironmentInstance(currEnvDef, environmentsForEngine.get(envName)));
        }

        return allEnvIns;
    }

    //func 4
    @Override
    public DtoOldSimulationsMap getMapOfOldSimulation() {
        return new DtoOldSimulationsMap(this.simulationId2CurrentTimeAndDate);
    }

    @Override
    public List<DtoReviewOldSimulation> fetchChosenWorld(int userSimulationChoice) {
        Map<String, EntityDefinition> entityDefsAvailable = new HashMap<>();
        for(EntityDefinition currEntDef: this.worldDefinitionForSimulation.getEntityDefinitions()){
            entityDefsAvailable.put(currEntDef.getEntityName(), currEntDef);
        }

        List<DtoReviewOldSimulation> resultOfOldSimulation = new ArrayList<>();
        // fetch the required world
        WorldInstance worldInstance = this.oldSimulation.get(userSimulationChoice - 1);

        for (String nameOfEntity: worldInstance.getAllEntities().keySet()) {
            List<EntityInstance> entityInstances = worldInstance.getAllEntities().get(nameOfEntity);
            EntityDefinition currentEntityDefinition = entityDefsAvailable.get(nameOfEntity);
            if (entityInstances.size() != 0)
            {
                resultOfOldSimulation.add(new DtoReviewOldSimulation(currentEntityDefinition, entityInstances));
            } else {
                resultOfOldSimulation.add(new DtoReviewOldSimulation(currentEntityDefinition, new ArrayList<>()));
            }
        }
        return resultOfOldSimulation;
    }

    public DtoEnvironments sendEnvironmentsToUser(){
        Map<String, EnvironmentDefinition> allEnvironments = worldDefinitionForSimulation.getAllEnvironments();
        DtoEnvironments environmentsForUser = new DtoEnvironments(allEnvironments);

        return environmentsForUser;
    }

    public Object initializeRandomEnvironmentValues(PropertyDefinition propertyDef){
        Object initVal = null;
        switch(propertyDef.getPropertyType().toLowerCase()){
//            case "decimal":
//                int initEnvValInt = Utilities.initializeRandomInt((int)propertyDef.getPropertyRange().getFrom(), (int)propertyDef.getPropertyRange().getTo());
//                initVal = initEnvValInt;
//                break;
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
            case "decimal":
                if(!Utilities.isInteger(userInput)){
                    return false;
                }
                else{
                    int valueOfInputInt = Integer.parseInt(userInput);
                    if(!(valueOfInputInt >= propDef.getPropertyRange().getFrom() && valueOfInputInt <= propDef.getPropertyRange().getTo())){
                        return false;
                    }
                }
                break;
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
            case "decimal":
                int initInt = Integer.parseInt(userInput);
                initVal = initInt;
                break;
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
    public DtoResponse saveWorldState(String stringPath){
        String path = stringPath+".dat";
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(path))) {
            out.writeObject(new DtoWorldState(this.worldDefinitionForSimulation,
                    this.oldSimulation,
                    this.simulationId2CurrentTimeAndDate));
            out.flush();
        }
        catch (IOException ex){
            return new DtoResponse("Failed to save the simulation from current path.", false);
        }

        return new DtoResponse("The Simulation saved succesfully", true);
    }

    @Override
    public DtoResponse loadWorldState(String stringPath){
        String path = stringPath+".dat";
        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(path))) {
            DtoWorldState worldState =
                    (DtoWorldState) in.readObject();
            if (worldState != null){
                this.worldDefinitionForSimulation = worldState.getWorldDefinitionForSimulation();
                this.oldSimulation = worldState.getOldSimulation();
                this.simulationId2CurrentTimeAndDate = worldState.getSimulationId2CurrentTimeAndDate();
            }
        }

        catch(IOException | ClassNotFoundException ex)
        {
            return new DtoResponse("Failed to load the simulation from current path.", false);
        }
        return new DtoResponse("The Simulation was loaded succesfully", true);
    }


}
