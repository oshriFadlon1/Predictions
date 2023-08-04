package engine;

//import dto.DtoOldSimulationResponse;
import dto.DtoResponse;
import entity.EntityDefinition;
import entity.EntityInstance;
import property.Environment;
import property.PropertyDefinition;
import property.PropertyInstance;
import property.Value;
import range.Range;
import shema.genereated.*;
import termination.Termination;
import world.World;
import exceptions.GeneralException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainEngine {
    private static final String XML_PATH_EX ="C:\\Users\\PC-1\\ideaProjects\\Predictions\\PDREngine\\src\\resources\\ex1-cigarets.xml";
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

    public String getSimulationsByQuantity(int indexOfSimulation){

        World world = this.oldSimulation.get(indexOfSimulation - 1 );
        StringBuilder result = new StringBuilder();
        Map<String, EntityDefinition> allEntityDefinitions = new HashMap<>();

        for(EntityInstance entityInstance: world.getAllEntities()){
            EntityDefinition entityDefinition = entityInstance.getDefinitionOfEntity();
            String entityName = entityDefinition.getEntityName();
            if(!allEntityDefinitions.containsKey(entityName)){
                allEntityDefinitions.put(entityName, entityDefinition);
            }
        }

        for(String key: allEntityDefinitions.keySet()){
            result.append("Name: ");
            result.append(key);
            result.append("Population of entity at the start of the simulation: ");
            result.append(allEntityDefinitions.get(key).getStartPopulation());
            result.append("Population of entity at the end of the simulation: ");
            result.append(allEntityDefinitions.get(key).getEndPopulation());
        }
        return result.toString();
    }

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
        catch(JAXBException | FileNotFoundException e){

        }
        return null;
    }
    public static void tryToReadXml(String xmlPath) throws GeneralException, JAXBException, FileNotFoundException {
        //here we will try to read the xml.
        // we will create a new world and extract all the info from the xml file
        InputStream inputStream = new FileInputStream(new File(xmlPath));
        JAXBContext jc = JAXBContext.newInstance(XML_RES_FILE);
        Unmarshaller u = jc.createUnmarshaller();
        PRDWorld output = (PRDWorld)u.unmarshal(inputStream);
        World createdWorld = translateFromXmlToClassInstances(output);
        System.out.println(output.toString());
        System.out.println((PRDWorld)u.unmarshal(inputStream));
    }

    private static World translateFromXmlToClassInstances(PRDWorld prdWorld) throws GeneralException{
        Termination terminations = createTerminationFromPrdTermination(prdWorld.getPRDTermination());
        Map<String, Environment> environments = createEnvironmentsFromPrdEnvironment(prdWorld.getPRDEvironment());
        List<EntityDefinition>entityDefinitions = createEntityDefinitionsFromPrdEntity(prdWorld.getPRDEntities());
        List<EntityInstance> entityInstances = createEntityInstancesFromPrdEntity(prdWorld.getPRDEntities());
        World createdWorld = new World(terminations);
        createdWorld.setAllEnvironments(environments);

        return null;

    }

    private static List<EntityInstance> createEntityInstancesFromPrdEntity(PRDEntities prdEntities) throws GeneralException{
        List<PRDEntity> allPrdEntities = prdEntities.getPRDEntity();
        Map<String, PropertyInstance> propertyCheckerMap = new HashMap<>();
        for (PRDEntity prdEntity: allPrdEntities){
            String name = prdEntity.getName();
            int population = prdEntity.getPRDPopulation();
            EntityDefinition entityDefinition = new EntityDefinition(name, population);
            List <PRDProperty> prdProperties = prdEntity.getPRDProperties().getPRDProperty();
            for(PRDProperty prdProperty: prdProperties){
                String propName = prdProperty.getPRDName();
                String propType = prdProperty.getType();
                PRDRange prdRange = prdProperty.getPRDRange();
                PRDValue prdValue = prdProperty.getPRDValue();
                if(propertyCheckerMap.containsKey(propName)){
                    throw new GeneralException("Property name " + propName + " already exists");
                }
                Range ranage = new Range((float)prdRange.getFrom(), (float)prdRange.getTo());
                Value val = new Value(prdValue.isRandomInitialize(), prdValue.getInit());
                for(int i = 0; i < population; i++){
                    EntityInstance entityInstance = new EntityInstance(entityDefinition);

                }
//                EntityInstance entityInstance = new EntityInstance()
            }
        }
        return null;

    }


    private static Termination createTerminationFromPrdTermination(PRDTermination prdTermination) throws GeneralException{
        List<Object> listOfTerminations = prdTermination.getPRDByTicksOrPRDBySecond();
        PRDByTicks elem1 = (PRDByTicks)listOfTerminations.get(0);
        PRDBySecond elem2 = (PRDBySecond)listOfTerminations.get(1);
        Termination terminations = new Termination(elem1.getCount(), elem2.getCount());
        return terminations;
    }

    private static List<EntityDefinition> createEntityDefinitionsFromPrdEntity(PRDEntities prdEntities) throws GeneralException{
        List<EntityDefinition> entityDefinitions = new ArrayList<>();
        Map<String, EntityDefinition> entityCheckerMap = new HashMap<>();
        List<PRDEntity> allPrdEntities = prdEntities.getPRDEntity();
        for(PRDEntity prdEntity: allPrdEntities){
            String name = prdEntity.getName();
            int population = prdEntity.getPRDPopulation();
            if(entityCheckerMap.containsKey(name)){
                throw new GeneralException("Entity " + name + " already exists");
            }
            if(population < 0){
                throw new GeneralException("Population is less than 0");
            }
            entityCheckerMap.put(name, new EntityDefinition(name, population));
        }
        return entityDefinitions;
    }

    private static Map<String ,Environment> createEnvironmentsFromPrdEnvironment(PRDEvironment prdEvironment) throws GeneralException{
        List<PRDEnvProperty> propertyList = prdEvironment.getPRDEnvProperty();
        Map<String, Environment> result = new HashMap<>();
        for(PRDEnvProperty environmentProperty: propertyList){
            String name = environmentProperty.getPRDName();
            PRDRange prdRange = environmentProperty.getPRDRange();
            String type = environmentProperty.getType();
            if(result.containsKey(name)){
                throw new GeneralException("environment Property name " + name + " already exists");
            }

            Range range = new Range((float)prdRange.getFrom(), (float)prdRange.getTo());
            result.put(name, new Environment(new PropertyDefinition(name, type, range)));
        }

        return result;
    }

}
