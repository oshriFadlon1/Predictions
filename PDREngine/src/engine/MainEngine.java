package engine;

//import dto.DtoOldSimulationResponse;
import dto.DtoResponse;
import entity.EntityDefinition;
import entity.EntityInstance;
import enums.Operation;
import property.*;
import range.Range;
import rule.ActivationForRule;
import rule.Rule;
import rule.action.Action;
import shema.genereated.*;
import termination.Termination;
import world.World;
import exceptions.GeneralException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainEngine {
    private static final String XML_PATH_EX ="C:\\java_projects\\Predictions\\PDREngine\\src\\resources\\ex1-cigarets.xml";
    private static final String XML_RES_FILE = "shema.genereated";

    private List<World> allSimulations; // current running simulation

    private List<World> oldSimulation; // old simulation with results

    private Map<Integer, String> simulationId2CurrentTimeAndDate;// get old World simulation info need to take index from map - 1;


    public MainEngine() {
        this.allSimulations = new ArrayList<>();
        this.oldSimulation = new ArrayList<>();
        this.simulationId2CurrentTimeAndDate = new HashMap<>();
    }

    public List<World> getAllSimulations() {
        //
        return allSimulations;
    }

    public void setAllSimulations(List<World> allSimulations) {
        this.allSimulations = allSimulations;
    }

    public List<World> getOldSimulation() {
        return oldSimulation;
    }

    public void setOldSimulation(List<World> oldSimulation) {
        this.oldSimulation = oldSimulation;
    }

    public Map<Integer, String> getSimulationId2CurrentTimeAndDate() {
        return simulationId2CurrentTimeAndDate;
    }

    public void setSimulationId2CurrentTimeAndDate(Map<Integer, String> simulationId2CurrentTimeAndDate) {
        this.simulationId2CurrentTimeAndDate = simulationId2CurrentTimeAndDate;
    }

    public void addWorld(World juiceWrld)
    {
        this.allSimulations.add(juiceWrld);
    }

    public String  printCurrentWorld(){
        if (!allSimulations.isEmpty())
        {
            return allSimulations.get(0).toString();
        }
        return null;
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

//    public DtoOldSimulationResponse getOldSimulationsInfo(){
//
//        StringBuilder res = new StringBuilder();
//        for(Integer key: simulationId2CurrentTimeAndDate.keySet()){
//            res.append(key).append(": ");
//            String value = simulationId2CurrentTimeAndDate.get(key);
//            res.append(value);
//            res.append("\n");
//        }
//
//        return new DtoOldSimulationResponse(res.toString(), this.simulationId2CurrentTimeAndDate.size());
//    }

//    public String getSimulationsByQuantity(int indexOfSimulation){
//
//        World world = this.oldSimulation.get(indexOfSimulation - 1 );
//        StringBuilder result = new StringBuilder();
//        Map<String, EntityDefinition> allEntityDefinitions = new HashMap<>();
//
//        for(EntityInstance entityInstance: world.getAllEntities()){
//            EntityDefinition entityDefinition = entityInstance.getDefinitionOfEntity();
//            String entityName = entityDefinition.getEntityName();
//            if(!allEntityDefinitions.containsKey(entityName)){
//                allEntityDefinitions.put(entityName, entityDefinition);
//            }
//        }
//
//        for(String key: allEntityDefinitions.keySet()){
//            result.append("Name: ");
//            result.append(key);
//            result.append("Population of entity at the start of the simulation: ");
//            result.append(allEntityDefinitions.get(key).getStartPopulation());
//            result.append("Population of entity at the end of the simulation: ");
//            result.append(allEntityDefinitions.get(key).getEndPopulation());
//        }
//        return result.toString();
//    }

    public static void main(String[] args) {
        parseXmlToSimulation(XML_PATH_EX);
    }

    public static DtoResponse parseXmlToSimulation(String xmlPath) {
        File file = new File(XML_PATH_EX);
          if (!file.exists()) {
          return new DtoResponse(null, "File don't exist in this path");
          }

        String fileName = file.getName();
        if(!fileName.toLowerCase().endsWith(".xml")){
            return new DtoResponse(null, "File don't ending with .xml ");
        }

        try {
           tryToReadXml(XML_PATH_EX);
//         succeed to load xml info create DtoResponse (currentWorldFromXmlAfterParse, Constans.SUCCEED_LOAD_FILE);
        } catch (GeneralException e) {
            return new DtoResponse(null, e.getErrorMsg());
        }
        catch(JAXBException | IOException e){

        }
        return null;
    }
    public static void tryToReadXml(String xmlPath) throws GeneralException, JAXBException, IOException {
        //here we will try to read the xml.
        // we will create a new world and extract all the info from the xml file
        InputStream inputStream = Files.newInputStream(new File(xmlPath).toPath());
        JAXBContext jc = JAXBContext.newInstance(XML_RES_FILE);
        Unmarshaller u = jc.createUnmarshaller();
        PRDWorld output = (PRDWorld)u.unmarshal(inputStream);
        World createdWorld = translateFromXmlToClassInstances(output);
        System.out.println(output.toString());
        System.out.println((PRDWorld)u.unmarshal(inputStream));
    }

    private static World translateFromXmlToClassInstances(PRDWorld prdWorld) throws GeneralException{
        // create the terminations from xml object
        Termination terminations = createTerminationFromPrdTermination(prdWorld.getPRDTermination());
        // create new world
        World createdWorld = new World(terminations);
        // create the environments form xml object
        Map<String, Environment> environments = createEnvironmentsFromPrdEnvironment(prdWorld.getPRDEvironment());
        createdWorld.setAllEnvironments(environments);
        //  create the entities from xml object
        List<EntityDefinition>entityDefinitions = createEntityDefinitionsFromPrdEntity(prdWorld.getPRDEntities());
        Map<EntityDefinition,List<EntityInstance>> entityDefinitionListHashMap= new HashMap<>();
        for (EntityDefinition entityDefinition:entityDefinitions) {
            entityDefinitionListHashMap.put(entityDefinition, new ArrayList<>());
        }
        createdWorld.setAllEntities(entityDefinitionListHashMap);
        // create the rules from xml object
        Map<String, Rule> rules = createRulesFromPrdRules(prdWorld.getPRDRules());

        return createdWorld;

    }


    private static Map<String ,Environment> createEnvironmentsFromPrdEnvironment(PRDEvironment prdEvironment) throws GeneralException{
        List<PRDEnvProperty> propertyList = prdEvironment.getPRDEnvProperty();
        Map<String, Environment> result = new HashMap<>();
        for(PRDEnvProperty environmentProperty: propertyList){
            String name = environmentProperty.getPRDName();
            PRDRange prdRange = environmentProperty.getPRDRange();
            String type = environmentProperty.getType().toUpperCase();
            if(result.containsKey(name)){
                throw new GeneralException("environment Property name " + name + " already exists");
            }

            Range range = new Range((float)prdRange.getFrom(), (float)prdRange.getTo());
            result.put(name, new Environment(new PropertyDefinition(name, type, range)));
        }

        return result;
    }

    private static Termination createTerminationFromPrdTermination(PRDTermination prdTermination) throws GeneralException{
        List<Object> listOfTerminations = prdTermination.getPRDByTicksOrPRDBySecond();
        PRDByTicks elem1 = (PRDByTicks)listOfTerminations.get(0);
        PRDBySecond elem2 = (PRDBySecond)listOfTerminations.get(1);
        Termination terminations = new Termination(elem1.getCount(), elem2.getCount());
        return terminations;
    }

    private static List<EntityDefinition> createEntityDefinitionsFromPrdEntity(PRDEntities prdEntities) throws GeneralException {
        List<EntityDefinition> entityDefinitions = new ArrayList<>();
        Map<String, EntityDefinition> entityDefinitionName2EntityDefinition = new HashMap<>();
        List<PRDEntity> allPrdEntities = prdEntities.getPRDEntity();
        for (PRDEntity prdEntity : allPrdEntities) {
            String name = prdEntity.getName();
            int population = prdEntity.getPRDPopulation();
            if (entityDefinitionName2EntityDefinition.containsKey(name)) {
                throw new GeneralException("Entity " + name + " already exists");
            }
            if(population < 0){
                throw new GeneralException("Population is less than 0");
            }

            entityDefinitionName2EntityDefinition.put(name,
                    new EntityDefinition(name, population, createEntityPropertiesDefinitionFromPrd(prdEntity.getPRDProperties())));
            entityDefinitions.add(new EntityDefinition(name, population, createEntityPropertiesDefinitionFromPrd(prdEntity.getPRDProperties())));
        }
        return entityDefinitions;
    }

    private static Map<String, Rule> createRulesFromPrdRules(PRDRules prdRules) throws GeneralException{
        Map<String, Rule> ruleMap = new HashMap<>();

        for (PRDRule prdRule:prdRules.getPRDRule()) {
            String prdRuleName = prdRule.getName();
            if(ruleMap.containsKey(prdRuleName)){
                throw new GeneralException("Rule name already exists");
            }
            int ticks = prdRule.getPRDActivation().getTicks();
            double probability = prdRule.getPRDActivation().getProbability();

            ruleMap.put(prdRuleName,new Rule(prdRuleName,new ActivationForRule(ticks, (float)probability),createActionListFromPrdActions(prdRule.getPRDActions())));
        }
    }

    private static Map<String, PropertyDefinitionEntity> createEntityPropertiesDefinitionFromPrd(PRDProperties prdProperties) throws GeneralException {
        String init;
        boolean randomInit;
        double from = 0,to = 0;
        String name,type;
        Map<String, PropertyDefinitionEntity> mapEntityPropertyDefinitionToEntityPropertyDefinition = new HashMap<>();
        for (PRDProperty prdProperty: prdProperties.getPRDProperty()) {
            init = prdProperty.getPRDValue().getInit();
            randomInit = prdProperty.getPRDValue().isRandomInitialize();
            if (prdProperty.getPRDRange() != null){
                from = prdProperty.getPRDRange().getFrom();
                to = prdProperty.getPRDRange().getTo();
            }
            name = prdProperty.getPRDName();
            type = prdProperty.getType().toUpperCase();
            if (mapEntityPropertyDefinitionToEntityPropertyDefinition.containsKey(name)) {
                throw new GeneralException("Property " + name + " already exists");
            }
            if (!randomInit && init == null) {
                throw new GeneralException("In property definition, 'random-init' is false but init value is not specified");
            }
            if (prdProperty.getPRDRange() != null  && from > to)
            {
                throw new GeneralException("In property defintion, 'from' cannot be bigger than 'to'");
            }
            if (prdProperty.getPRDRange() == null && randomInit)
            {
                throw new GeneralException("In property defintion, 'from' cannot be bigger than 'to'");
            }

            mapEntityPropertyDefinitionToEntityPropertyDefinition.put(name,
                    new PropertyDefinitionEntity(
                            new PropertyDefinition(name,type,new Range((float) from, (float) to)),
                            new Value(randomInit,init)));
        }
        return mapEntityPropertyDefinitionToEntityPropertyDefinition;
    }

    private static List<Action> createActionListFromPrdActions(PRDActions prdActions) {
        List<Action> ListOfActions = new ArrayList<>();
        Action actionToAdd = null;
       for(PRDAction prdAction: prdActions.getPRDAction()){
          Operation typeOfAction = Operation.valueOf(prdAction.getType().toUpperCase());
          switch(typeOfAction){
              case INCREASE:
                  actionToAdd = convertPrdActionToIncreaseAction(prdAction);
                  break;
              case DECREASE:
                  actionToAdd = converPrdActionToDecreaseAction(prdAction);
                  break;
              case CALCULATION:
                  actionToAdd = convertPrdActionToCalculation(prdAction);
                  break;
              case CONDITION:
                  actionToAdd = convertPrdActionToCondition(prdAction);
                  break;
              case SET:
                  actionToAdd = convertPrdActionToSet(prdAction);
                  break;
              case KILL:
                  actionToAdd = convertPrdActionToKill(prdAction);
                  break;
           }
           ListOfActions.add(actionToAdd);
       }
       return ListOfActions;
    }

    private static Action convertPrdActionToIncreaseAction(PRDAction prdAction) {
        return null;
    }

    private static Action converPrdActionToDecreaseAction(PRDAction prdAction) {
        return null;
    }
    private static Action convertPrdActionToCalculation(PRDAction prdAction) {
    }

    private static Action convertPrdActionToCondition(PRDAction prdAction) {
        return null;
    }

    private static Action convertPrdActionToSet(PRDAction prdAction) {
        return null;
    }

    private static Action convertPrdActionToKill(PRDAction prdAction) {
        return null;
    }











}


//    private static List<EntityInstance> createEntityInstancesFromPrdEntity(PRDEntities prdEntities) throws GeneralException{
//        List<PRDEntity> allPrdEntities = prdEntities.getPRDEntity();
//        Map<String, PropertyInstance> propertyCheckerMap = new HashMap<>();
//        for (PRDEntity prdEntity: allPrdEntities){
//            String name = prdEntity.getName();
//            int population = prdEntity.getPRDPopulation();
//            EntityDefinition entityDefinition = new EntityDefinition(name, population);
//            List <PRDProperty> prdProperties = prdEntity.getPRDProperties().getPRDProperty();
//            for(PRDProperty prdProperty: prdProperties){
//                String propName = prdProperty.getPRDName();
//                String propType = prdProperty.getType();
//                PRDRange prdRange = prdProperty.getPRDRange();
//                PRDValue prdValue = prdProperty.getPRDValue();
//                if(propertyCheckerMap.containsKey(propName)){
//                    throw new GeneralException("Property name " + propName + " already exists");
//                }
//                Range ranage = new Range((float)prdRange.getFrom(), (float)prdRange.getTo());
//                Value val = new Value(prdValue.isRandomInitialize(), prdValue.getInit());
//                for(int i = 0; i < population; i++){
//                    EntityInstance entityInstance = new EntityInstance(entityDefinition);
//
//                }
////                EntityInstance entityInstance = new EntityInstance()
//            }
//        }
//        return null;
//
//    }