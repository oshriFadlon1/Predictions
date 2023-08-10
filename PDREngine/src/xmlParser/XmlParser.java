package xmlParser;

import entity.EntityDefinition;
import enums.Operation;
import environment.EnvironmentDefinition;
import exceptions.GeneralException;
import property.PropertyDefinition;
import property.PropertyDefinitionEntity;
import property.Value;
import range.Range;
import rule.ActivationForRule;
import rule.Rule;
import rule.action.AbstractAction;
import rule.action.ActionDecrease;
import rule.action.ActionIncrease;
import shema.genereated.*;
import termination.Termination;
import world.WorldDefinition;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlParser {

    private String xmlPath = "C:\\java_projects\\Predictions\\PDREngine\\src\\resources\\example.xml";
    private static final String xmlFiles = "shema.genereated";


    public XmlParser(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    public WorldDefinition tryToReadXml() throws GeneralException, JAXBException, IOException {
        //here we will try to read the xml.
        // we will create a new world and extract all the info from the xml file
        File file = new File(xmlPath);
        if (!file.exists()) {
            return null;
        }

        String fileName = file.getName();
        if(!fileName.toLowerCase().endsWith(".xml")){
           return null;
        }

        InputStream inputStream = Files.newInputStream(new File(xmlPath).toPath());
        JAXBContext jc = JAXBContext.newInstance(xmlFiles);
        Unmarshaller u = jc.createUnmarshaller();
        PRDWorld output = (PRDWorld)u.unmarshal(inputStream);
        WorldDefinition createdWorld = translateFromXmlToClassInstances(output);


        return createdWorld;
    }

    private WorldDefinition translateFromXmlToClassInstances(PRDWorld prdWorld) throws GeneralException{
        // create the terminations from xml object
        Termination terminations = createTerminationFromPrdTermination(prdWorld.getPRDTermination());
        // create new world
        WorldDefinition createdWorld = new WorldDefinition(terminations);
        // create the environments form xml object
        Map<String, EnvironmentDefinition> environments = createEnvironmentsFromPrdEnvironment(prdWorld.getPRDEvironment());
        createdWorld.setAllEnvironments(environments);
        //  create the entities from xml object
        List<EntityDefinition> entityDefinitions = createEntityDefinitionsFromPrdEntity(prdWorld.getPRDEntities());
        createdWorld.setEntityDefinitions(entityDefinitions);
        // create the rules from xml object
        Map<String, Rule> rules = createRulesFromPrdRules(prdWorld.getPRDRules(), entityDefinitions);
        createdWorld.setRules(rules);

        return createdWorld;

    }


    private Map<String , EnvironmentDefinition> createEnvironmentsFromPrdEnvironment(PRDEvironment prdEvironment) throws GeneralException{
        List<PRDEnvProperty> propertyList = prdEvironment.getPRDEnvProperty();
        Map<String, EnvironmentDefinition> result = new HashMap<>();
        for(PRDEnvProperty environmentProperty: propertyList){
            String name = environmentProperty.getPRDName();
            PRDRange prdRange = environmentProperty.getPRDRange();
            String type = environmentProperty.getType().toUpperCase();
            if(result.containsKey(name)){
                throw new GeneralException("environment Property name " + name + " already exists");
            }

            Range range = new Range((float)prdRange.getFrom(), (float)prdRange.getTo());
            result.put(name, new EnvironmentDefinition(new PropertyDefinition(name, type, range)));
        }

        return result;
    }

    private Termination createTerminationFromPrdTermination(PRDTermination prdTermination) throws GeneralException{
        List<Object> listOfTerminations = prdTermination.getPRDByTicksOrPRDBySecond();
        PRDByTicks elem1 = (PRDByTicks)listOfTerminations.get(0);
        PRDBySecond elem2 = (PRDBySecond)listOfTerminations.get(1);
        Termination terminations = new Termination(elem1.getCount(), elem2.getCount());
        return terminations;
    }

    private List<EntityDefinition> createEntityDefinitionsFromPrdEntity(PRDEntities prdEntities) throws GeneralException {
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

    private Map<String, Rule> createRulesFromPrdRules(PRDRules prdRules, List<EntityDefinition> entityDefinitionList) throws GeneralException{
        Map<String, Rule> ruleMap = new HashMap<>();
        double probability = 1;
        int ticks = 1;

        for (PRDRule prdRule:prdRules.getPRDRule()) {
            String prdRuleName = prdRule.getName();
            if(ruleMap.containsKey(prdRuleName)){
                throw new GeneralException("Rule name already exists");
            }
            if(prdRule.getPRDActivation().getProbability() != null){
                probability = prdRule.getPRDActivation().getProbability();
            }
            if(prdRule.getPRDActivation().getTicks() != null){
                ticks = prdRule.getPRDActivation().getTicks();
            }

            ruleMap.put(prdRuleName,new Rule(prdRuleName,new ActivationForRule(ticks, (float)probability),createActionListFromPrdActions(prdRule.getPRDActions(), entityDefinitionList)));
        }
        return ruleMap;
    }

    private Map<String, PropertyDefinitionEntity> createEntityPropertiesDefinitionFromPrd(PRDProperties prdProperties) throws GeneralException {
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

    private List<AbstractAction> createActionListFromPrdActions(PRDActions prdActions, List<EntityDefinition> entityDefinitionList) throws GeneralException{
        List<AbstractAction> ListOfActions = new ArrayList<>();
        AbstractAction actionToAdd = null;
        for(PRDAction prdAction: prdActions.getPRDAction()){
            Operation typeOfAction = Operation.valueOf(prdAction.getType().toUpperCase());
            switch(typeOfAction){
                case INCREASE:
                    actionToAdd = convertPrdActionToIncreaseAction(prdAction, entityDefinitionList);
                    break;
                case DECREASE:
                    actionToAdd = convertPrdActionToDecreaseAction(prdAction, entityDefinitionList);
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
                default:
                    throw new GeneralException("Action type does not exist.");
            }
            ListOfActions.add(actionToAdd);
        }
        return ListOfActions;
    }

    private AbstractAction convertPrdActionToIncreaseAction(PRDAction prdAction, List<EntityDefinition> entityDefinitions) throws GeneralException{
        String entityName = prdAction.getEntity();
        EntityDefinition entityDefinition = null;
        for(EntityDefinition entityDef: entityDefinitions){
            if(entityDef.getEntityName().equals(entityName)){
                entityDefinition = entityDef;
                break;
            }
        }
        if(entityDefinition == null){
            throw  new GeneralException("In decrease operation, The required entity name" + entityName + "does not exist");
        }

        if (prdAction.getProperty() == null){
            throw new GeneralException(prdAction.getType() +" require entity property that will change by it");
        }
        String entityProperty = prdAction.getProperty();
        if (!entityDefinition.isPropertyNameExist(entityProperty)){
            throw new GeneralException(prdAction.getType() +" require entity property that will change by it and it not exist in "+ entityDefinition.getEntityName());
        }

        if (prdAction.getBy() == null){
            throw new GeneralException(prdAction.getType() + " require a value to change the property value");
        }

        // need to check how to convert the by to the required value
        return new ActionIncrease(entityDefinition, prdAction.getBy(),entityProperty);
    }

    private AbstractAction convertPrdActionToDecreaseAction(PRDAction prdAction, List<EntityDefinition> entityDefinitions) throws GeneralException{
        String entityName = prdAction.getEntity();
        String entityPropName = prdAction.getProperty();
        String decreaseBy = prdAction.getBy();
        EntityDefinition entityDefinitionTocheck = null;
        boolean isEntityNameExist = false;
        boolean isPropNameExist = false;

        for(EntityDefinition entityDef: entityDefinitions){
            if(entityDef.getEntityName().equals(entityName)){
                isEntityNameExist = true;
                entityDefinitionTocheck = entityDef;
                break;
            }
        }

        if(!isEntityNameExist){
            throw  new GeneralException("In " + prdAction.getType() + ", The required entity name" + entityName + "does not exist");
        }

        if(entityPropName == null){
            throw new GeneralException("In " + prdAction.getType() + ", property name cannot be empty(null)");
        }

        if(!entityDefinitionTocheck.isPropertyNameExist(entityPropName)) {
            throw new GeneralException("In " + prdAction.getType() + ", in entity name " + entityName + "property name " + entityPropName + " doesnt exist");
        }


        if(decreaseBy == null){
            throw new GeneralException("In " + prdAction.getType() + ", the field 'by' cannot be empty(null)");
        }

        return new ActionDecrease(entityDefinitionTocheck,prdAction.getBy(),entityPropName);
    }
    private AbstractAction convertPrdActionToCalculation(PRDAction prdAction) {
        return null;
    }

    private AbstractAction convertPrdActionToCondition(PRDAction prdAction) {
        return null;
    }

    private AbstractAction convertPrdActionToSet(PRDAction prdAction) {
        return null;
    }

    private AbstractAction convertPrdActionToKill(PRDAction prdAction) {
        return null;
    }

//    public double isNumber(String argument) throws NumberFormatException{
//        double parsedNumber = Double.parseDouble(argument);
//    }
//
//    public void isPrefixHelpFunction(String ){
//
//    }

}
