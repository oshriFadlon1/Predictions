package xmlParser;

import entity.EntityDefinition;
import enums.Operation;
import environment.EnvironmentDefinition;
import exceptions.GeneralException;
import interfaces.IConditionComponent;
import pointCoord.PointCoord;
import potentialerror.PotentionalError;
import property.PropertyDefinition;
import property.PropertyDefinitionEntity;
import property.Value;
import range.Range;
import rule.ActivationForRule;
import rule.Rule;
import rule.action.*;
import shema.generated.*;
import termination.Termination;
import utility.Utilities;
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

    private String xmlPath;
    private static final String xmlFiles = "shema.generated";


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
            throw new GeneralException("File does not exist.");
        }

        String fileName = file.getName();
        if(!fileName.toLowerCase().endsWith(".xml")){
           throw new GeneralException("File is not an xml file");
        }

        InputStream inputStream = Files.newInputStream(new File(xmlPath).toPath());
        JAXBContext jc = JAXBContext.newInstance(xmlFiles);
        Unmarshaller u = jc.createUnmarshaller();
        PRDWorld output = (PRDWorld)u.unmarshal(inputStream);
        WorldDefinition createdWorld = translateFromXmlToClassInstances(output);


        return createdWorld;
    }

    private WorldDefinition translateFromXmlToClassInstances(PRDWorld prdWorld) throws GeneralException{
        //getting thread count from xml object
        int threadCount = prdWorld.getPRDThreadCount();
        //getting grid sizes and generating pointcoord according to rows and cols
        PRDWorld.PRDGrid gridOfWorld = prdWorld.getPRDGrid();
        int worldRows = gridOfWorld.getRows();
        int worldCols = gridOfWorld.getColumns();
        PointCoord coordsOfWorld = new PointCoord(worldRows, worldCols);
        // create the terminations from xml object
        Termination terminations = createTerminationFromPrdTermination(prdWorld.getPRDTermination());
        // create new world
        WorldDefinition createdWorld = new WorldDefinition(terminations);
        createdWorld.setPointCoord(coordsOfWorld);
        createdWorld.setThreadCount(threadCount);
        // create the environments form xml object
        Map<String, EnvironmentDefinition> environments = createEnvironmentsFromPrdEnvironment(prdWorld.getPRDEnvironment());
        createdWorld.setAllEnvironments(environments);
        //  create the entities from xml object
        List<EntityDefinition> entityDefinitions = createEntityDefinitionsFromPrdEntity(prdWorld.getPRDEntities());
        createdWorld.setEntityDefinitions(entityDefinitions);
        // create the rules from xml object
        List<Rule> rules = createRulesFromPrdRules(prdWorld.getPRDRules(), entityDefinitions, environments);
        createdWorld.setRules(rules);

        return createdWorld;

    }


    private Map<String , EnvironmentDefinition> createEnvironmentsFromPrdEnvironment(PRDEnvironment prdEvironment) throws GeneralException{
        List<PRDEnvProperty> propertyList = prdEvironment.getPRDEnvProperty();
        Range range = null;
        Map<String, EnvironmentDefinition> result = new HashMap<>();
        for(PRDEnvProperty environmentProperty: propertyList){
            String name = environmentProperty.getPRDName();
            PRDRange prdRange = environmentProperty.getPRDRange();
            String type = environmentProperty.getType().toUpperCase();
            if(result.containsKey(name)){
                throw new GeneralException("environment Property name " + name + " already exists");
            }
            if(prdRange != null) {
                range = new Range((float) prdRange.getFrom(), (float) prdRange.getTo());
                result.put(name, new EnvironmentDefinition(new PropertyDefinition(name, type, range)));
            }
            else{
                result.put(name, new EnvironmentDefinition(new PropertyDefinition(name, type)));
            }
        }

        return result;
    }

    private Termination createTerminationFromPrdTermination(PRDTermination prdTermination) throws GeneralException{
        List<Object> listOfTerminations = prdTermination.getPRDBySecondOrPRDByTicks();
        Termination terminations = new Termination(-1, -1);
        PRDByTicks elem1 = null;
        PRDBySecond elem2 = null;
        Object byUser = prdTermination.getPRDByUser();
        if(listOfTerminations.size() == 2 ){
            elem1 = (PRDByTicks)listOfTerminations.get(0);
            elem2 = (PRDBySecond)listOfTerminations.get(1);
            terminations.setTicks(elem1.getCount());
            terminations.setSeconds(elem2.getCount());
        }
        else if(listOfTerminations.size() == 1){
            if(listOfTerminations.get(0) instanceof PRDByTicks){
                elem1 = (PRDByTicks)listOfTerminations.get(0);
                terminations.setTicks(elem1.getCount());
            }
            else{
                elem2 = (PRDBySecond)listOfTerminations.get(0);
                terminations.setSeconds(elem2.getCount());
            }
        }

        if(byUser != null){
            terminations.setByUser(true);
        }
        else{
            terminations.setByUser(false);
        }
        return terminations;
    }

    private List<EntityDefinition> createEntityDefinitionsFromPrdEntity(PRDEntities prdEntities) throws GeneralException {
        List<EntityDefinition> entityDefinitions = new ArrayList<>();
        Map<String, EntityDefinition> entityDefinitionName2EntityDefinition = new HashMap<>();
        List<PRDEntity> allPrdEntities = prdEntities.getPRDEntity();
        for (PRDEntity prdEntity : allPrdEntities) {
            String name = prdEntity.getName();
            //int population = prdEntity.getPRDPopulation();
            if (entityDefinitionName2EntityDefinition.containsKey(name)) {
                throw new GeneralException("Entity " + name + " already exists");
            }
//            if(population < 0){
//                throw new GeneralException("Population is less than 0");
//            }
            EntityDefinition newEntityDef = new EntityDefinition(name, createEntityPropertiesDefinitionFromPrd(prdEntity.getPRDProperties(), name));
            entityDefinitionName2EntityDefinition.put(name, newEntityDef);
            entityDefinitions.add(newEntityDef);
//            entityDefinitionName2EntityDefinition.put(name,
//                    new EntityDefinition(name, population, createEntityPropertiesDefinitionFromPrd(prdEntity.getPRDProperties(), name)));
//            entityDefinitions.add(new EntityDefinition(name, population, createEntityPropertiesDefinitionFromPrd(prdEntity.getPRDProperties(), name)));
        }
        return entityDefinitions;
    }

    private List<Rule> createRulesFromPrdRules(PRDRules prdRules, List<EntityDefinition> entityDefinitionList, Map<String, EnvironmentDefinition> environments) throws GeneralException{
        List<Rule> allRules = new ArrayList<>();
        Map<String, Rule> ruleMap = new HashMap<>();
        double probability = 1;
        int ticks = 1;

        for (PRDRule prdRule:prdRules.getPRDRule()) {
            String prdRuleName = prdRule.getName();
            if(ruleMap.containsKey(prdRuleName)){
                throw new GeneralException("Rule name already exists");
            }
            if(prdRule.getPRDActivation() != null) {
                if (prdRule.getPRDActivation().getProbability() != null) {
                    probability = prdRule.getPRDActivation().getProbability();
                }
                if (prdRule.getPRDActivation().getTicks() != null) {
                    ticks = prdRule.getPRDActivation().getTicks();
                }
            }

            allRules.add(new Rule(prdRuleName, new ActivationForRule(ticks, (float)probability), createActionListFromPrdActions(prdRule.getPRDActions().getPRDAction(), entityDefinitionList, environments)));

            //ruleMap.put(prdRuleName,new Rule(prdRuleName,new ActivationForRule(ticks, (float)probability),createActionListFromPrdActions(prdRule.getPRDActions().getPRDAction(), entityDefinitionList, environments)));
        }
        return allRules;
    }

    private Map<String, PropertyDefinitionEntity> createEntityPropertiesDefinitionFromPrd(PRDProperties prdProperties, String entityName) throws GeneralException {
        String init;
        boolean randomInit;
        double from = 0,to = 0;
        String name,type;
        Map<String, PropertyDefinitionEntity> mapEntityPropertyDefinitionToEntityPropertyDefinition = new HashMap<>();
        for (PRDProperty prdProperty: prdProperties.getPRDProperty()) {
            init = prdProperty.getPRDValue().getInit();
            randomInit = prdProperty.getPRDValue().isRandomInitialize();
            if (prdProperty.getPRDRange() != null) {
                from = prdProperty.getPRDRange().getFrom();
                to = prdProperty.getPRDRange().getTo();
            }
            name = prdProperty.getPRDName();
            type = prdProperty.getType().toUpperCase();
            if (mapEntityPropertyDefinitionToEntityPropertyDefinition.containsKey(name)) {
                throw new GeneralException("In entity " + entityName + ", Property " + name + " already exists");
            }
            if (!randomInit && init == null) {
                throw new GeneralException("In entity " + entityName + ", in property definition" + name + ", 'random-init' is false but init value is not specified");
            }
            if (prdProperty.getPRDRange() != null && from > to) {
                throw new GeneralException("In entity " + entityName + ", in property definition" + name + ", 'from' cannot be bigger than 'to'");
            }
            if (prdProperty.getPRDRange() == null && randomInit && (prdProperty.getType().equalsIgnoreCase("decimal") || prdProperty.getType().equalsIgnoreCase("float")))//
            {
                throw new GeneralException("In entity " + entityName + " In property definition " + name + ", random init is true, but there is no range to randomize from");
            }

            if (prdProperty.getPRDRange() != null){
                mapEntityPropertyDefinitionToEntityPropertyDefinition.put(name,
                        new PropertyDefinitionEntity(
                                new PropertyDefinition(name, type, new Range((float) from, (float) to)),
                                new Value(randomInit, init)));
            } else {
                mapEntityPropertyDefinitionToEntityPropertyDefinition.put(name,
                        new PropertyDefinitionEntity(
                                new PropertyDefinition(name, type),
                                new Value(randomInit, init)));
            }
        }
        return mapEntityPropertyDefinitionToEntityPropertyDefinition;
    }

    private List<IAction> createActionListFromPrdActions(List<PRDAction> prdActions, List<EntityDefinition> entityDefinitionList, Map<String, EnvironmentDefinition> environments) throws GeneralException{
        List<IAction> ListOfActions = new ArrayList<>();
        IAction actionToAdd = null;
        for(PRDAction prdAction: prdActions){
            Operation typeOfAction = Operation.valueOf(prdAction.getType().toUpperCase());
            switch(typeOfAction){
                case INCREASE:
                    actionToAdd = convertPrdActionToIncreaseAction(prdAction, entityDefinitionList, environments);
                    break;
                case DECREASE:
                    actionToAdd = convertPrdActionToDecreaseAction(prdAction, entityDefinitionList, environments);
                    break;
                case CALCULATION:
                    actionToAdd = convertPrdActionToCalculation(prdAction, entityDefinitionList, environments);
                    break;
                case CONDITION:
                    actionToAdd = convertPrdActionToCondition(prdAction,entityDefinitionList, environments);
                    break;
                case SET:
                    actionToAdd = convertPrdActionToSet(prdAction, entityDefinitionList, environments);
                    break;
                case KILL:
                    actionToAdd = convertPrdActionToKill(prdAction, entityDefinitionList);
                    break;
                case PROXIMITY:
                    actionToAdd = null;
                    break;
                case REPLACE:
                    actionToAdd = null;
                    break;
                default:
                    throw new GeneralException("Action type does not exist.");
            }
            ListOfActions.add(actionToAdd);
        }
        return ListOfActions;
    }

    private IAction convertPrdActionToIncreaseAction(PRDAction prdAction, List<EntityDefinition> entityDefinitions, Map<String, EnvironmentDefinition> environments) throws GeneralException{
//        PotentionalError potentionalError = checkErrors(prdAction, entityDefinitions);
//        if(!potentionalError.getErrorMsg().equalsIgnoreCase("")){
//            throw new GeneralException(potentionalError.getErrorMsg());
//        }
//        EntityDefinition entityDefinition = potentionalError.getEntittyDef();
//        PropertyDefinitionEntity propDef = potentionalError.getPropertyDef();
//        String entityProperty = propDef.getPropertyDefinition().getPropertyName();
        String entityName = prdAction.getEntity();
        EntityDefinition entityDefinition = null;
        for(EntityDefinition entityDef: entityDefinitions){
            if(entityDef.getEntityName().equals(entityName)){
                entityDefinition = entityDef;
                break;
            }
        }
        if(entityDefinition == null){
            throw  new GeneralException("In " + prdAction.getType() + ", The required entity name" + entityName + "does not exist");
        }

        if (prdAction.getProperty() == null){
            throw new GeneralException("In " + prdAction.getType() + ", property name" + prdAction.getProperty() + " cannot be empty(null)");
        }
        String entityProperty = prdAction.getProperty();
        if (!entityDefinition.isPropertyNameExist(entityProperty)){
            throw new GeneralException("In " + prdAction.getType() +" property name " + entityProperty + " does not exist");
        }

        if (prdAction.getBy() == null){
            throw new GeneralException("In " + prdAction.getType() + " require a value to change the property value");
        }
        //we call the following options to check by. if not number then throws exception. otherwise no need to do anything
        Utilities.isValueCalculationNumeric(prdAction.getBy(), environments, entityDefinition);

        return new ActionIncrease(entityDefinition, prdAction.getBy(),entityProperty);
    }

    private IAction convertPrdActionToDecreaseAction(PRDAction prdAction, List<EntityDefinition> entityDefinitions, Map<String, EnvironmentDefinition> environments) throws GeneralException{
//        PotentionalError potentionalError = checkErrors(prdAction, entityDefinitions);
//        if(!potentionalError.getErrorMsg().equalsIgnoreCase("")){
//            throw new GeneralException(potentionalError.getErrorMsg());
//        }

//        EntityDefinition entityDefinitionTocheck = potentionalError.getEntittyDef();
//        PropertyDefinitionEntity propDef = potentionalError.getPropertyDef();
//        String entityPropName = propDef.getPropertyDefinition().getPropertyName();
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

        //we call the following options to check by. if not number then throws exception. otherwise no need to do anything
        Utilities.isValueCalculationNumeric(prdAction.getBy(), environments, entityDefinitionTocheck);

        return new ActionDecrease(entityDefinitionTocheck,prdAction.getBy(),entityPropName);
    }
    private IAction convertPrdActionToCalculation(PRDAction prdAction, List<EntityDefinition> entityDefinitions, Map<String, EnvironmentDefinition> environments) throws GeneralException {
//        PotentionalError potentionalError = checkErrors(prdAction, entityDefinitions);
//        if(!potentionalError.getErrorMsg().equalsIgnoreCase("")){
//            throw new GeneralException(potentionalError.getErrorMsg());
//        }
        ActionCalculation actionToDo = null;
//       String entityPropName = prdAction.getProperty();
        String entityName = prdAction.getEntity();
        String resultProp = prdAction.getResultProp();
        boolean isEntityNameExist = false;
        EntityDefinition entityDefinitionTocheck = null;

        //checking for calculation if entity name exists
        for(EntityDefinition entityDef: entityDefinitions) {
            if (entityDef.getEntityName().equals(entityName)) {
                isEntityNameExist = true;
                entityDefinitionTocheck = entityDef;
                break;
            }
        }

        if(!isEntityNameExist){
            throw  new GeneralException("In " + prdAction.getType() + ", The required entity name" + entityName + "does not exist");
        }

//        if(entityPropName == null){
//            throw new GeneralException("In " + prdAction.getType() + ", property name cannot be empty(null)");
//        }

        if(!entityDefinitionTocheck.isPropertyNameExist(resultProp)){
            throw new GeneralException("In " + prdAction.getType() + ", in entity name " + entityName + "property name " + resultProp + " doesnt exist");
        }
        //EntityDefinition entityDefinitionTocheck = potentionalError.getEntittyDef();
        if(!entityDefinitionTocheck.isPropertyNameExist(resultProp)){
            throw new GeneralException("In " + prdAction.getType() + ", in entity name " + entityDefinitionTocheck.getEntityName() + "property name " + resultProp + " doesnt exist");
        }


       if(prdAction.getPRDDivide() == null){//action is multiply
            PRDMultiply prdMultiply = prdAction.getPRDMultiply();
            String arg1 = prdMultiply.getArg1();
            Utilities.isValueCalculationNumeric(arg1, environments, entityDefinitionTocheck);
            String arg2 = prdMultiply.getArg2();
            Utilities.isValueCalculationNumeric(arg2, environments, entityDefinitionTocheck);
            actionToDo = new ActionCalculationMultiply(entityDefinitionTocheck, resultProp, arg1, arg2);
       }
       else{//action is divide
           PRDDivide prdDivide = prdAction.getPRDDivide();
           String arg1 = prdDivide.getArg1();
           Utilities.isValueCalculationNumeric(arg1, environments, entityDefinitionTocheck);
           String arg2 = prdDivide.getArg2();
           Utilities.isValueCalculationNumeric(arg2, environments, entityDefinitionTocheck);
           actionToDo = new ActionCalculationDivide(entityDefinitionTocheck, resultProp, arg1, arg2);
       }

       return actionToDo;
    }

    private IAction convertPrdActionToCondition(PRDAction prdAction, List<EntityDefinition> entityDefinitions, Map<String, EnvironmentDefinition> environments) throws GeneralException{
        String entityName = prdAction.getEntity();
        String entityPropName = prdAction.getProperty();

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

        EntityDefinition checkedEntity = checkIfEntityNameExist(prdAction.getEntity(), entityDefinitions);
        if(checkedEntity == null){
            throw new GeneralException("In " + prdAction.getType() + ", The required entity name" + entityName + "does not exist");
        }

        if(!isEntityNameExist){
            throw  new GeneralException("In " + prdAction.getType() + ", The required entity name " + entityName + " does not exist");
        }
        List<IAction> caseTrue = parseTrueConditionFromPRDThen(prdAction.getPRDThen().getPRDAction(),entityDefinitions,environments);
        List<IAction> caseFalse = parseFalseConditionFromPRDElse(prdAction.getPRDElse(),entityDefinitions, environments);
        IConditionComponent conditionComponent = parseConditionFromPRDCondition(prdAction.getPRDCondition(), checkedEntity);
        return new ActionCondition(entityDefinitionTocheck, conditionComponent, caseTrue, caseFalse);
    }

    private boolean checkIfPropertyExsistFromCurrentEntity(String property, EntityDefinition entityDef) {
        if(entityDef.isPropertyNameExist(property)){
            return true;
        }

        return false;
    }

    private EntityDefinition checkIfEntityNameExist(String entity, List<EntityDefinition> entityDefinitions) {
        EntityDefinition entityDefResult = null;
        for(EntityDefinition currEntity: entityDefinitions){
            if(entity.equalsIgnoreCase(currEntity.getEntityName())){
                entityDefResult = currEntity;
            }
        }

        return entityDefResult;
    }

    private IConditionComponent parseConditionFromPRDCondition(PRDCondition prdCondition, EntityDefinition entityDefinitions) throws GeneralException {
        multipleCondition conditionComponent = new multipleCondition("and",null);
        List<IConditionComponent> listOfCondition = new ArrayList<>();
        if (prdCondition.getPRDCondition().size() == 0)
        {
            IConditionComponent singleCondition = new SingleCondition(prdCondition.getProperty(), prdCondition.getOperator(), prdCondition.getValue());
            listOfCondition.add(singleCondition);
        } else {
            for (PRDCondition condition : prdCondition.getPRDCondition()) {
                listOfCondition.add(createSubConditions(condition, entityDefinitions));
            }
        }
        if (prdCondition.getLogical() != null)
        {
            conditionComponent.setLogical(prdCondition.getLogical());
        }

        conditionComponent.setSubConditions(listOfCondition);

        return conditionComponent;
    }

    private IConditionComponent createSubConditions(PRDCondition prdCondition, EntityDefinition entityDefinitions) throws GeneralException {
        IConditionComponent result = null;
        if (prdCondition.getSingularity().equalsIgnoreCase("single")){
            result = createSingleCondition(prdCondition, entityDefinitions);
        }
        if (prdCondition.getSingularity().equalsIgnoreCase("multiple")){
            result = createMultipleCondition(prdCondition, entityDefinitions);
        }
        return result;
    }

    private IConditionComponent createMultipleCondition(PRDCondition prdCondition, EntityDefinition entityDefinitions) throws GeneralException {
        List<IConditionComponent> listOfCondition = new ArrayList<>();
        for (PRDCondition condition : prdCondition.getPRDCondition()) {
            listOfCondition.add(createSubConditions(condition, entityDefinitions));
        }
        if (!Utilities.isOperatorFromMultipleCondition(prdCondition.getLogical())){
            throw new GeneralException("In Multiple condition, operator " + prdCondition.getOperator() + " doesnt exist");
        }
        return new multipleCondition(prdCondition.getLogical(), listOfCondition);
    }

    private IConditionComponent createSingleCondition(PRDCondition prdCondition, EntityDefinition entityDefinitions) throws GeneralException {
        if(!checkIfPropertyExsistFromCurrentEntity(prdCondition.getProperty(), entityDefinitions)){
            throw new GeneralException("In single condition, in entity name " + entityDefinitions.getEntityName() + "property name " + prdCondition.getProperty() + " doesnt exist");
        }
        if (!Utilities.isOperatorFromSingleCondition(prdCondition.getOperator())){
            throw new GeneralException("In single condition, operator " + prdCondition.getOperator() + " doesnt exist");
        }
        SingleCondition singleCondition = new SingleCondition(prdCondition.getProperty(),
                prdCondition.getOperator(), prdCondition.getValue());
        return singleCondition;
    }


    private List<IAction> parseFalseConditionFromPRDElse(PRDElse elseAction, List<EntityDefinition> entityDefinitions, Map<String, EnvironmentDefinition> environments) throws GeneralException{
        // need to check if elseAction null
        List<IAction> listOfAction = null;

        if(elseAction != null){
           listOfAction = createActionListFromPrdActions(elseAction.getPRDAction(), entityDefinitions, environments);
        }

        return listOfAction;
    }

    private List<IAction> parseTrueConditionFromPRDThen(List<PRDAction> thenAction, List<EntityDefinition> entityDefinitions, Map<String, EnvironmentDefinition> environments) throws GeneralException{
        //then cannot be null.
        List<IAction> listOfAction = createActionListFromPrdActions(thenAction, entityDefinitions, environments);
        return listOfAction;
    }

    private IAction convertPrdActionToSet(PRDAction prdAction, List<EntityDefinition> entityDefinitions, Map<String, EnvironmentDefinition> environments) throws GeneralException{
//        PotentionalError potentionalError = checkErrors(prdAction, entityDefinitions);
//        if(!potentionalError.getErrorMsg().equalsIgnoreCase("")){
//            throw new GeneralException(potentionalError.getErrorMsg());
//        }
        String entityName = prdAction.getEntity();
        String entityPropName = prdAction.getProperty();

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

        //EntityDefinition entityDefinitionTocheck = potentionalError.getEntittyDef();
        //PropertyDefinitionEntity propDefToCheck = potentionalError.getPropertyDef();
        //String entityPropName = propDefToCheck.getPropertyDefinition().getPropertyName();

        return new ActionSet(entityDefinitionTocheck, entityPropName, prdAction.getValue());
    }

    private IAction convertPrdActionToKill(PRDAction prdAction, List<EntityDefinition> entityDefinitions) throws GeneralException{
        String entityName = prdAction.getEntity();
        EntityDefinition entityDefinition = null;
        for(EntityDefinition entityDef: entityDefinitions){
            if(entityDef.getEntityName().equals(entityName)){
                entityDefinition = entityDef;
                break;
            }
        }
        if(entityDefinition == null){
            throw new GeneralException("In " + prdAction.getType() + ", The required entity name" + entityName + "does not exist");
        }
        return new ActionKill(entityDefinition, entityName);
    }

    private PotentionalError checkErrors(PRDAction prdAction, List<EntityDefinition> entityDefinitions){
        PotentionalError returnObj = new PotentionalError();
        String entityName = prdAction.getEntity();
        EntityDefinition entityDefinition = null;
        for(EntityDefinition entityDef: entityDefinitions){
            if(entityDef.getEntityName().equals(entityName)){
                entityDefinition = entityDef;
                break;
            }
        }
        if(entityDefinition == null){
            returnObj.setErrorMsg("In " + prdAction.getType() + ", The required entity name" + entityName + "does not exist");
            return returnObj;
        }
        returnObj.setEntittyDef(entityDefinition);
        if (prdAction.getProperty() == null){
           returnObj.setErrorMsg("In " + prdAction.getType() + ", property name" + prdAction.getProperty() + " cannot be empty(null)");
           return returnObj;
        }
        if(!entityDefinition.isPropertyNameExist(prdAction.getProperty())){
            returnObj.setErrorMsg("In " + prdAction.getType() + ", in entity name " + entityName + "property name " + prdAction.getProperty() + " doesnt exist");
            return returnObj;
        }

        returnObj.setPropertyDef(entityDefinition.getPropertyDefinition().get(prdAction.getProperty()));
        return returnObj;
    }

//    public double isNumber(String argument) throws NumberFormatException{
//        double parsedNumber = Double.parseDouble(argument);
//    }
//
//    public void isPrefixHelpFunction(String ){
//
//    }//= "C:\\java_projects\\currentJavaProject\\PDREngine\\src\\resources\\ex1-cigarets.xml  master-ex1.xml    error3.xml";
//
//    // C:\\java_projects\\currentJavaProject\\PDREngine\\src\\resources\\example.xml

}
