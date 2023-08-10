package engine;
import constans.Constans;
import dto.*;
import entity.EntityDefinition;
import environment.EnvironmentDefinition;
import environment.EnvironmentInstance;
import interfaces.InterfaceMenu;
import property.*;
import range.Range;
import rule.ActivationForRule;
import rule.Rule;
import rule.action.AbstractAction;
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


    public void moveWorld() {
        oldSimulation.add(this.allSimulations.remove(0));
        simulationId2CurrentTimeAndDate.put(oldSimulation.size(), getCurrentTimeAndDateInTheFormat());
    }

    private String getCurrentTimeAndDateInTheFormat() {
        Date currentDate = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy | hh.mm.ss");

        return sdf.format(currentDate);
    }

    public DtoResponsePreview previewWorldInfo(){

        return new DtoResponsePreview(getEntitiesInfoSimulation(),getRulesInfoSimulation() ,
                new DtoResponsePreviewTermination(worldDefinitionForSimulation.getTermination().getTicks(),
                        worldDefinitionForSimulation.getTermination().getSeconds()));
    }

    private DtoResponseEntities getEntitiesInfoSimulation() {
        List<PropertyDefinitionEntity> propertyDefinitionEntityList = new ArrayList<>();
        String nameEntity = "";
        int population = 0;
        boolean isTheFirst = true;
        for (EntityDefinition entityDefinition: worldDefinitionForSimulation.getEntityDefinitions()) {
            if (isTheFirst) {
                nameEntity = entityDefinition.getEntityName();
                population = entityDefinition.getStartPopulation();
                isTheFirst = false;
            }
            for (String key:entityDefinition.getPropertyDefinition().keySet()) {
                PropertyDefinitionEntity propertyDefinitionEntity = entityDefinition.getPropertyDefinition().get(key);
                propertyDefinitionEntityList.add(new PropertyDefinitionEntity(
                        new PropertyDefinition(propertyDefinitionEntity.getPropertyDefinition().getPropertyName(),
                        propertyDefinitionEntity.getPropertyDefinition().getPropertyType(),
                                propertyDefinitionEntity.getPropertyDefinition().getPropertyRange()!=null?
                                        new Range(propertyDefinitionEntity.getPropertyDefinition().getPropertyRange().getFrom(),
                                                propertyDefinitionEntity.getPropertyDefinition().getPropertyRange().getTo()):null),
                        new Value(propertyDefinitionEntity.getPropValue().getRandomInit(),
                                propertyDefinitionEntity.getPropValue().getInit())));
            }
            isTheFirst = true;
        }
        return new DtoResponseEntities(nameEntity, population, propertyDefinitionEntityList);
    }
    private List<DtoResponseRules> getRulesInfoSimulation() {
        List<DtoResponseRules> dtoResponseRules = new ArrayList<>();
        List<String> ActionName = new ArrayList<>();
        String ruleName = "";
        ActivationForRule activation = null;
        for (String key: worldDefinitionForSimulation.getRules().keySet()) {
            Rule rule = worldDefinitionForSimulation.getRules().get(key);
            for (AbstractAction action:rule.getActions()) {
                ActionName.add(action.getOperationType().name());
            }
            dtoResponseRules.add(new DtoResponseRules(rule.getRuleName(),
                    rule.getActivation().getTicks(),
                    rule.getActivation().getProbability(),
                    rule.getActions().size(), ActionName));
        }
        return dtoResponseRules;
    }


    //func 1
    @Override
    public String createWorldDefinition(String xmlPath){
        String resultErrorMsgToUser;
        XmlParser xmlParserInCheck= new XmlParser(xmlPath);
        try {
            this.worldDefinitionForSimulation = xmlParserInCheck.tryToReadXml();

        }
        catch(JAXBException | IOException | GeneralException e){
            if(e instanceof  JAXBException || e instanceof IOException){
                return "There was an error in reading the XML file.";
            }
            else{
                return ((GeneralException) e).getErrorMsg();
            }
        }

        this.xmlParser = xmlParserInCheck;
        return Constans.SUCCEED_LOAD_FILE;
    }
    //func 2
    @Override
    public DtoResponsePreview showCurrentSimulation(){
        return previewWorldInfo();
    }
    //func 3
    @Override
    public DtoResponseSimulationEnded runSimulations(Map<String, Object> environmentsForEngine){
        DtoResponseSimulationEnded responseForUser = null;
        Map<String, EnvironmentInstance> environmentInstancesMap = createAllEnvironmentInstances(environmentsForEngine);

       try {
           for (WorldInstance currentSimulation : this.allSimulations) {
               currentSimulation.setAllEnvironments(environmentInstancesMap);
               DtoResponseTermination currSimulationTermination = currentSimulation.runSimulation(this.worldDefinitionForSimulation);
               responseForUser = new DtoResponseSimulationEnded(currSimulationTermination, allSimulations.indexOf(currentSimulation));
           }
           return responseForUser;
       }
       catch(GeneralException e){
           return new DtoResponseSimulationEnded(e.getErrorMsg());
           //now we return a dto response that represents the error message
       }
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
    public String printPastSimulation(int indexOfSimulation, int userDisplay){
        return null;
    }

    public DtoEnvironments sendEnvironmentsToUser(){
        Map<String, EnvironmentDefinition> allEnvironments = worldDefinitionForSimulation.getAllEnvironments();
        DtoEnvironments environmentsForUser = new DtoEnvironments(allEnvironments);

        return environmentsForUser;
    }

    public Object initializeRandomEnvironmentValues(EnvironmentDefinition currEnv, PropertyDefinition propertyDef){
        Object initVal = null;
        switch(propertyDef.getPropertyType().toLowerCase()){
            case "decimal":
                int initEnvValInt = Utilities.initializeRandomInt((int)propertyDef.getPropertyRange().getFrom(), (int)propertyDef.getPropertyRange().getTo());
                initVal = initEnvValInt;
                break;
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

                //case default maybe? throwing exception? i think we did that in the xml parsing
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
                    if(!(valueOFInputFloat >= propDef.getPropertyRange().getFrom() && valueOFInputFloat <= propDef.getPropertyRange().getFrom())){
                        return false;
                    }
                }
                break;
            case "string":
                break;
            case "boolean":
                if(userInput.toLowerCase() != "true" && userInput.toLowerCase() != "false"){
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

}
