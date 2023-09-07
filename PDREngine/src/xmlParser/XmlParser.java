package xmlParser;

import dto.DtoActionArgsContainer;
import entity.EntityDefinition;
import entity.SecondEntity;
import enums.CreationType;
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

import javax.swing.text.html.parser.Entity;
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
        if(!(worldRows >= 10 && worldRows <= 100) || !(worldCols >= 10 && worldCols <= 100)){
            throw new GeneralException("Invalid world size");
        }

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

    // Fetch termination from PRD
    private Termination createTerminationFromPrdTermination(PRDTermination prdTermination) {
        List<Object> listOfTerminations = prdTermination.getPRDBySecondOrPRDByTicks();
        Termination terminations = null;
        PRDByTicks elem1 = null;
        PRDBySecond elem2 = null;
        Object byUser = prdTermination.getPRDByUser();
        if(listOfTerminations.size() == 2 ){
            elem1 = (PRDByTicks)listOfTerminations.get(0);
            elem2 = (PRDBySecond)listOfTerminations.get(1);
            terminations = new Termination(elem1.getCount(), elem2.getCount(), false);
        }
        else if(listOfTerminations.size() == 1){
            if(listOfTerminations.get(0) instanceof PRDByTicks){
                elem1 = (PRDByTicks)listOfTerminations.get(0);
                terminations = new Termination(elem1.getCount(), -1, false);
            }
            else{
                elem2 = (PRDBySecond)listOfTerminations.get(0);
                terminations = new Termination(-1, elem2.getCount(), false);
            }
        }

        if(byUser != null){
            terminations = new Termination(-1, -1, true);
        }
        return terminations;
    }

    // Fetch Environment from PRD
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


    // fetch info for create entity definition
    private List<EntityDefinition> createEntityDefinitionsFromPrdEntity(PRDEntities prdEntities) throws GeneralException {
        List<EntityDefinition> entityDefinitions = new ArrayList<>();
        Map<String, EntityDefinition> entityDefinitionName2EntityDefinition = new HashMap<>();
        List<PRDEntity> allPrdEntities = prdEntities.getPRDEntity();
        for (PRDEntity prdEntity : allPrdEntities) {
            String name = prdEntity.getName();
            if (entityDefinitionName2EntityDefinition.containsKey(name)) {
                throw new GeneralException("Entity " + name + " already exists");
            }
            EntityDefinition newEntityDef = new EntityDefinition(name, createEntityPropertiesDefinitionFromPrd(prdEntity.getPRDProperties(), name));
            entityDefinitionName2EntityDefinition.put(name, newEntityDef);
            entityDefinitions.add(newEntityDef);
        }
        return entityDefinitions;
    }

    // fetch property info for current entity
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
            if (prdProperty.getPRDRange() == null && randomInit && (prdProperty.getType().equalsIgnoreCase("float")))//
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

    // fetch info on rules
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



    private List<IAction> createActionListFromPrdActions(List<PRDAction> prdActions, List<EntityDefinition> entityDefinitionList, Map<String, EnvironmentDefinition> environments) throws GeneralException{
        List<IAction> ListOfActions = new ArrayList<>();
        SecondEntity secondEntity = null;
        IAction actionToAdd = null;
        for(PRDAction prdAction: prdActions){
            List<EntityDefinition> entitiesInContext = getAllEntitiesInContext(prdAction, entityDefinitionList);
            if (prdAction.getPRDSecondaryEntity() != null){
                secondEntity = checkAndGetSecondEntity(environments, entitiesInContext, prdAction.getPRDSecondaryEntity());
            }
            DtoActionArgsContainer dtoActionArgsContainer = new DtoActionArgsContainer(entityDefinitionList, environments, secondEntity, entitiesInContext);
            Operation typeOfAction = Operation.valueOf(prdAction.getType().toUpperCase());
            switch(typeOfAction){
                case INCREASE:
                    actionToAdd = convertPrdActionToIncreaseAction(prdAction, dtoActionArgsContainer);
                    break;
                case DECREASE:
                    actionToAdd = convertPrdActionToDecreaseAction(prdAction, dtoActionArgsContainer);
                    break;
                case CALCULATION:
                    actionToAdd = convertPrdActionToCalculation(prdAction, dtoActionArgsContainer);
                    break;
                case CONDITION:
                    actionToAdd = convertPrdActionToCondition(prdAction, dtoActionArgsContainer);
                    // TODO set second entity as in PRD-second-entity
                    break;
                case SET:
                    actionToAdd = convertPrdActionToSet(prdAction, dtoActionArgsContainer);
                    break;
                case KILL:
                    actionToAdd = convertPrdActionToKill(prdAction, dtoActionArgsContainer);
                    break;
                case PROXIMITY:
                    actionToAdd = convertPrdActionToProximity(prdAction, dtoActionArgsContainer);
                    // TODO set second Entity as ALL and condition null
                    break;
                case REPLACE:
                    actionToAdd = convertPrdActionToReplace(prdAction, dtoActionArgsContainer);
                    break;
                default:
                    throw new GeneralException("Action type does not exist.");
            }
            ListOfActions.add(actionToAdd);
        }
        return ListOfActions;
    }

    private List<EntityDefinition> getAllEntitiesInContext(PRDAction prdAction, List<EntityDefinition> entityDefinitionList) throws GeneralException{
        String firstEntityName;
        String secondEntityName;
        List<EntityDefinition> entityContextList = new ArrayList<>();

        if (prdAction.getPRDBetween() != null)//proximity
        {
            firstEntityName = prdAction.getPRDBetween().getSourceEntity();
            secondEntityName = prdAction.getPRDBetween().getTargetEntity();
            return checkIfEntitiesInContextExist(entityDefinitionList, firstEntityName, secondEntityName);
        }
        if(prdAction.getType().equalsIgnoreCase("replace"))
        {
            firstEntityName = prdAction.getKill();
            secondEntityName = prdAction.getCreate();
            return checkIfEntitiesInContextExist(entityDefinitionList, firstEntityName, secondEntityName);
        }

        firstEntityName = prdAction.getEntity();
        EntityDefinition contextEntity1 = getEntityContextFromList(entityDefinitionList, firstEntityName);
        if(contextEntity1 == null){
            throw new GeneralException("In action " + prdAction.getType() + "entity " + firstEntityName + " doesn't exist");
        }

        entityContextList.add(contextEntity1);
        if(prdAction.getPRDSecondaryEntity() != null){
            secondEntityName = prdAction.getPRDSecondaryEntity().getEntity();
            EntityDefinition contextEntity2 = getEntityContextFromList(entityDefinitionList, secondEntityName);
            if(contextEntity2 == null){
                throw new GeneralException("In action " + prdAction.getType() + "secondary entity " + secondEntityName + " doesn't exist");
            }

            entityContextList.add(contextEntity2);
        }

        return entityContextList;
    }

    private EntityDefinition getEntityContextFromList(List<EntityDefinition> entityDefsList, String entityName){
        for(EntityDefinition entityDefinition: entityDefsList){
            if(entityName.equalsIgnoreCase(entityDefinition.getEntityName())){
                return entityDefinition;
            }
        }
        return null;
    }

    private List<EntityDefinition> checkIfEntitiesInContextExist(List<EntityDefinition> entityDefinitionList, String firstEntityName, String secondEntityName) throws GeneralException {
        List<EntityDefinition> EntitiesInContext = new ArrayList<>();
        EntityDefinition firstEntity;
        EntityDefinition secondEntity;
        if (firstEntityName != null)
        {
            firstEntity = getEntityContextFromList(entityDefinitionList, firstEntityName);
            if (firstEntity != null){
                EntitiesInContext.add(firstEntity);
            } else {
                throw new GeneralException("Entity " + firstEntityName + " does not exist");
            }

        } else {
            throw new GeneralException("In call between need to by 2 entities.");
        }
        if (secondEntityName != null)
        {
            secondEntity = getEntityContextFromList(entityDefinitionList, secondEntityName);
            if (secondEntity != null){
                EntitiesInContext.add(secondEntity);
            } else {
                throw new GeneralException("Entity " + secondEntityName + " does not exist");
            }

        } else {
            throw new GeneralException("In call between need to by 2 entities.");
        }
        return EntitiesInContext;
    }

    private SecondEntity checkAndGetSecondEntity(Map<String, EnvironmentDefinition> environments, List<EntityDefinition> entitiesInContext, PRDAction.PRDSecondaryEntity prdSecondaryEntity) throws GeneralException{
        EntityDefinition entityDefinition = getEntityContextFromList(entitiesInContext, prdSecondaryEntity.getEntity());
        String count = prdSecondaryEntity.getPRDSelection().getCount();
        if(!Utilities.isInteger(count)){
            throw new GeneralException("Invalid value for attribute 'count' in secondary entity definition");
        }
        IConditionComponent conditionComponent = null;
        if (prdSecondaryEntity.getPRDSelection().getPRDCondition() != null){
            conditionComponent = parseConditionFromPRDCondition(prdSecondaryEntity.getPRDSelection().getPRDCondition(), entitiesInContext, environments);
        }
        return new SecondEntity(entityDefinition, count, conditionComponent);
    }

    private IAction convertPrdActionToIncreaseAction(PRDAction prdAction, DtoActionArgsContainer dtoActionArgsContainer) throws GeneralException{
//        PotentionalError potentionalError = checkErrors(prdAction, entityDefinitions);
//        if(!potentionalError.getErrorMsg().equalsIgnoreCase("")){
//            throw new GeneralException(potentionalError.getErrorMsg());
//        }
//        EntityDefinition entityDefinition = potentionalError.getEntittyDef();
//        PropertyDefinitionEntity propDef = potentionalError.getPropertyDef();
//        String entityProperty = propDef.getPropertyDefinition().getPropertyName();
        ActionIncrease increaseAction;
        List<EntityDefinition> entityDefinitions = dtoActionArgsContainer.getEntitiesInContext();
        Map<String, EnvironmentDefinition> environments = dtoActionArgsContainer.getEnvironments();
        SecondEntity secondEntityIfExist = dtoActionArgsContainer.getSecondEntity();
        String entityName = prdAction.getEntity(); // primery entity
        EntityDefinition entityDef = checkIfEntityNameExist(entityName, entityDefinitions);

        if(entityDef == null){
            throw  new GeneralException("In " + prdAction.getType() + ", The required entity name" + entityName + "does not exist");
        }

        if (prdAction.getProperty() == null){
            throw new GeneralException("In " + prdAction.getType() + ", property name" + prdAction.getProperty() + " cannot be empty(null)");
        }

        String entityProperty = prdAction.getProperty();

        if (!entityDef.isPropertyNameExist(entityProperty)){
            throw new GeneralException("In " + prdAction.getType() +" property name " + entityProperty + " does not exist");
        }
        
        if (prdAction.getBy() == null){
            throw new GeneralException("In " + prdAction.getType() + " require a value to change the property value");
        }
        //we call the following options to check by. if not number then throws exception. otherwise no need to do anything
        Utilities.isValueCalculationNumeric(prdAction.getBy(), environments, entityDefinitions);
        increaseAction = new ActionIncrease(entityDef, prdAction.getBy(), entityProperty);
        
        if(secondEntityIfExist != null){
            increaseAction.SetSecondEntity(secondEntityIfExist);
        }
        return increaseAction;
    }

    private IAction convertPrdActionToDecreaseAction(PRDAction prdAction, DtoActionArgsContainer dtoActionArgsContainer) throws GeneralException{
//        PotentionalError potentionalError = checkErrors(prdAction, entityDefinitions);
//        if(!potentionalError.getErrorMsg().equalsIgnoreCase("")){
//            throw new GeneralException(potentionalError.getErrorMsg());
//        }

//        EntityDefinition entityDefinitionTocheck = potentionalError.getEntittyDef();
//        PropertyDefinitionEntity propDef = potentionalError.getPropertyDef();
//        String entityPropName = propDef.getPropertyDefinition().getPropertyName();
        ActionDecrease decreaseAction;
        List<EntityDefinition> entityDefinitions = dtoActionArgsContainer.getEntitiesInContext();
        Map<String, EnvironmentDefinition> environments = dtoActionArgsContainer.getEnvironments();
        SecondEntity secondEntityIfExist = dtoActionArgsContainer.getSecondEntity();
        String entityName = prdAction.getEntity(); // primery entity

        EntityDefinition entityDef = checkIfEntityNameExist(entityName, entityDefinitions);
        if(entityDef == null){
            throw  new GeneralException("In " + prdAction.getType() + ", The required entity name" + entityName + "does not exist");
        }
        String entityPropName = prdAction.getProperty();
        String decreaseBy = prdAction.getBy();

        if(entityPropName == null){
            throw new GeneralException("In " + prdAction.getType() + ", property name cannot be empty(null)");
        }

        if(!entityDef.isPropertyNameExist(entityPropName)) {
            throw new GeneralException("In " + prdAction.getType() + ", in entity name " + entityName + "property name " + entityPropName + " doesnt exist");
        }


        if(decreaseBy == null){
            throw new GeneralException("In " + prdAction.getType() + ", the field 'by' cannot be empty(null)");
        }

        //we call the following options to check by. if not number then throws exception. otherwise no need to do anything
        Utilities.isValueCalculationNumeric(prdAction.getBy(), environments, entityDefinitions);
        decreaseAction = new ActionDecrease(entityDef, prdAction.getBy(), entityPropName);
        if(secondEntityIfExist != null){
            decreaseAction.SetSecondEntity(secondEntityIfExist);
        }

        return decreaseAction;
    }
    private IAction convertPrdActionToCalculation(PRDAction prdAction, DtoActionArgsContainer dtoActionArgsContainer) throws GeneralException {
//        PotentionalError potentionalError = checkErrors(prdAction, entityDefinitions);
//        if(!potentionalError.getErrorMsg().equalsIgnoreCase("")){
//            throw new GeneralException(potentionalError.getErrorMsg());
//        }
        List<EntityDefinition> entityDefinitions = dtoActionArgsContainer.getEntitiesInContext();
        Map<String, EnvironmentDefinition> environments = dtoActionArgsContainer.getEnvironments();
        ActionCalculation actionToDo = null;
        SecondEntity secondaryEntity = dtoActionArgsContainer.getSecondEntity();
        String entityName = prdAction.getEntity();
        String resultProp = prdAction.getResultProp();
        EntityDefinition entityDefinitionTocheck = checkIfEntityNameExist(entityName, entityDefinitions);

        if(entityDefinitionTocheck == null){
            throw  new GeneralException("In " + prdAction.getType() + ", The required entity name" + entityName + "does not exist");
        }

//        if(entityPropName == null){
//            throw new GeneralException("In " + prdAction.getType() + ", property name cannot be empty(null)");
//        }

        if(!entityDefinitionTocheck.isPropertyNameExist(resultProp)){
            throw new GeneralException("In " + prdAction.getType() + ", in entity name " + entityName + "property name " + resultProp + " doesnt exist");
        }

       if(prdAction.getPRDDivide() == null){//action is multiply
            PRDMultiply prdMultiply = prdAction.getPRDMultiply();
            String arg1 = prdMultiply.getArg1();
            Utilities.isValueCalculationNumeric(arg1, environments, entityDefinitions);
            String arg2 = prdMultiply.getArg2();
            Utilities.isValueCalculationNumeric(arg2, environments, entityDefinitions);
            actionToDo = new ActionCalculationMultiply(entityDefinitionTocheck, resultProp, arg1, arg2);
            if(secondaryEntity != null){
               actionToDo.SetSecondEntity(secondaryEntity);
           }
       }
       else{//action is divide
           PRDDivide prdDivide = prdAction.getPRDDivide();
           String arg1 = prdDivide.getArg1();
           Utilities.isValueCalculationNumeric(arg1, environments, entityDefinitions);
           String arg2 = prdDivide.getArg2();
           Utilities.isValueCalculationNumeric(arg2, environments, entityDefinitions);
           actionToDo = new ActionCalculationDivide(entityDefinitionTocheck, resultProp, arg1, arg2);
           if(secondaryEntity != null){
               actionToDo.SetSecondEntity(secondaryEntity);
           }
       }

       return actionToDo;
    }

    private IAction convertPrdActionToCondition(PRDAction prdAction, DtoActionArgsContainer dtoActionArgsContainer) throws GeneralException{
        String entityName = prdAction.getEntity();
        List<EntityDefinition> entityDefinitions = dtoActionArgsContainer.getEntityDefinitionList();
        Map<String, EnvironmentDefinition> environments = dtoActionArgsContainer.getEnvironments();
        SecondEntity secondEntity = dtoActionArgsContainer.getSecondEntity();

        EntityDefinition entityDefinitionTocheck = null;
        boolean isEntityNameExist = false;

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
        // TODO add check if the property is a expression
        List<IAction> caseTrue = parseTrueConditionFromPRDThen(prdAction.getPRDThen().getPRDAction(),entityDefinitions,environments);
        List<IAction> caseFalse = parseFalseConditionFromPRDElse(prdAction.getPRDElse(),entityDefinitions, environments);
        IConditionComponent conditionComponent = parseConditionFromPRDCondition(prdAction.getPRDCondition(), entityDefinitions, environments);

        // TODO if need to add second entity we can add here
        return new ActionCondition(entityDefinitionTocheck, conditionComponent, caseTrue, caseFalse, secondEntity);
    }

    private boolean checkIfExpressionExistFromCurrentEntity(String property, Map<String, EnvironmentDefinition> environmentDefinitionMap, List<EntityDefinition> entityDef) {
        for (EntityDefinition currEntityDefinition : entityDef) {
            if(currEntityDefinition.isPropertyNameExist(property)){
                return true;
            }
        }

        try {
            Utilities.isValueCalculationNumeric(property, environmentDefinitionMap, entityDef);
            return true;
        }
        catch(GeneralException generalException){
            return false;
        }
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

    private IConditionComponent parseConditionFromPRDCondition(PRDCondition prdCondition, List<EntityDefinition> entityDefinitions,  Map<String, EnvironmentDefinition> environments) throws GeneralException {
        MultipleCondition conditionComponent = new MultipleCondition("and",null);
        List<IConditionComponent> listOfCondition = new ArrayList<>();
        if (prdCondition.getPRDCondition().size() == 0)
        {
            IConditionComponent singleCondition = new SingleCondition(prdCondition.getProperty(), prdCondition.getOperator(), prdCondition.getValue());
            listOfCondition.add(singleCondition);
        } else {
            for (PRDCondition condition : prdCondition.getPRDCondition()) {
                listOfCondition.add(createSubConditions(condition, entityDefinitions, environments));
            }
        }
        if (prdCondition.getLogical() != null)
        {
            conditionComponent.setLogical(prdCondition.getLogical());
        }

        conditionComponent.setSubConditions(listOfCondition);
        // TODO if the size is 1 need to be simple
        return conditionComponent;
    }

    private IConditionComponent createSubConditions(PRDCondition prdCondition, List<EntityDefinition> entityDefinitions, Map<String, EnvironmentDefinition> environments) throws GeneralException {
        IConditionComponent result = null;
        if (prdCondition.getSingularity().equalsIgnoreCase("single")){
            result = createSingleCondition(prdCondition, environments, entityDefinitions);
        }
        if (prdCondition.getSingularity().equalsIgnoreCase("multiple")){
            result = createMultipleCondition(prdCondition, entityDefinitions, environments);
        }
        return result;
    }

    private IConditionComponent createMultipleCondition(PRDCondition prdCondition, List<EntityDefinition> entityDefinitions, Map<String, EnvironmentDefinition> environments) throws GeneralException {
        List<IConditionComponent> listOfCondition = new ArrayList<>();
        for (PRDCondition condition : prdCondition.getPRDCondition()) {
            listOfCondition.add(createSubConditions(condition, entityDefinitions, environments));
        }
        if (!Utilities.isOperatorFromMultipleCondition(prdCondition.getLogical())){
            throw new GeneralException("In Multiple condition, operator " + prdCondition.getOperator() + " doesnt exist");
        }
        return new MultipleCondition(prdCondition.getLogical(), listOfCondition);
    }

    private IConditionComponent createSingleCondition(PRDCondition prdCondition,  Map<String, EnvironmentDefinition> environments, List<EntityDefinition> entityDefinitions) throws GeneralException {

        if(!checkIfExpressionExistFromCurrentEntity(prdCondition.getProperty(),  environments, entityDefinitions)){
            // TODO CREATE NEW STRING TO THROW
            throw new GeneralException("In single condition, in entity name " + "property name " + prdCondition.getProperty() + " doesnt exist");
        }

        if (!Utilities.isOperatorFromSingleCondition(prdCondition.getOperator())){
            throw new GeneralException("In single condition, operator " + prdCondition.getOperator() + " doesnt exist");
        }
        SingleCondition singleCondition = new SingleCondition(prdCondition.getProperty(), prdCondition.getOperator(), prdCondition.getValue());
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

    private IAction convertPrdActionToSet(PRDAction prdAction, DtoActionArgsContainer dtoActionArgsContainer) throws GeneralException{
//        PotentionalError potentionalError = checkErrors(prdAction, entityDefinitions);
//        if(!potentionalError.getErrorMsg().equalsIgnoreCase("")){
//            throw new GeneralException(potentionalError.getErrorMsg());
//        }
        String entityName = prdAction.getEntity();
        String entityPropName = prdAction.getProperty();
        List<EntityDefinition> entityDefinitions = dtoActionArgsContainer.getEntitiesInContext();
        SecondEntity secondEntity = dtoActionArgsContainer.getSecondEntity();

        EntityDefinition entityDefinitionTocheck = checkIfEntityNameExist(entityName, entityDefinitions);
        if(entityDefinitions == null){
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

        return new ActionSet(entityDefinitionTocheck, entityPropName, prdAction.getValue(), secondEntity);
    }

    private IAction convertPrdActionToKill(PRDAction prdAction, DtoActionArgsContainer dtoActionArgsContainer) throws GeneralException{
        String entityName = prdAction.getEntity();
        EntityDefinition entityDefinition = null;
        List<EntityDefinition> entityDefinitions = dtoActionArgsContainer.getEntityDefinitionList();
        SecondEntity secondEntity = dtoActionArgsContainer.getSecondEntity();

        for(EntityDefinition entityDef: entityDefinitions){
            if(entityDef.getEntityName().equals(entityName)){
                entityDefinition = entityDef;
                break;
            }
        }
        if(entityDefinition == null){
            throw new GeneralException("In " + prdAction.getType() + ", The required entity name" + entityName + "does not exist");
        }
        return new ActionKill(entityDefinition, entityName, secondEntity);
    }

    private IAction convertPrdActionToProximity(PRDAction prdAction, DtoActionArgsContainer dtoActionArgsContainer) throws GeneralException{
        ActionProximity actionProximity = null;
        String firstEntityName = prdAction.getPRDBetween().getSourceEntity();
        String secondEntityName = prdAction.getPRDBetween().getTargetEntity();
        List<EntityDefinition> entityDefinitions = dtoActionArgsContainer.getEntityDefinitionList();
        Map<String, EnvironmentDefinition> environments = dtoActionArgsContainer.getEnvironments();
        EntityDefinition firstEntityDef = checkIfEntityNameExist(firstEntityName, entityDefinitions);

        if(firstEntityDef == null){
            throw new GeneralException("In action " + prdAction.getType() + " entity " + firstEntityName + " doesn't exist");
        }

        EntityDefinition secondaryEntityDef = checkIfEntityNameExist(secondEntityName, entityDefinitions);
        if(secondaryEntityDef == null){
            throw new GeneralException("In action " + prdAction.getType() + " entity " + secondEntityName + " doesn't exist");
        }

        String depth = prdAction.getPRDEnvDepth().getOf();
        Utilities.isValueCalculationNumeric(depth, environments, entityDefinitions);


        List<IAction> listOfAction = createActionListFromPrdActions(prdAction.getPRDActions().getPRDAction(), entityDefinitions, environments);
        actionProximity = new ActionProximity(firstEntityDef, new SecondEntity(secondaryEntityDef,"All",null), depth, listOfAction);
        return actionProximity;
    }

    private IAction convertPrdActionToReplace(PRDAction prdAction, DtoActionArgsContainer dtoActionArgsContainer) throws GeneralException {
        ActionReplace actionReplace = null;
        String firstEntityName = prdAction.getKill();
        String secondEntityName = prdAction.getCreate();
        String mode = prdAction.getMode();
        CreationType typeOfCreation = null;
        List<EntityDefinition> entityDefinitions = dtoActionArgsContainer.getEntityDefinitionList();
        EntityDefinition firstEntityDef = checkIfEntityNameExist(firstEntityName, entityDefinitions);
        if(firstEntityDef == null){
            throw new GeneralException("In action " + prdAction.getType() + " entity " + firstEntityName + " doesn't exist");
        }

        EntityDefinition secondaryEntityDef = checkIfEntityNameExist(secondEntityName, entityDefinitions);
        if(secondaryEntityDef == null){
            throw new GeneralException("In action " + prdAction.getType() + " entity " + secondEntityName + " doesn't exist");
        }

        if(!mode.equalsIgnoreCase("derived") && !mode.equalsIgnoreCase("scratch")){
            throw new GeneralException("In action " + prdAction.getType() + " mode " + mode + " is invalid");
        }
        CreationType typeOfAction = CreationType.valueOf(mode.toUpperCase());
        switch(typeOfAction){
            case DERIVED:
                typeOfCreation = CreationType.DERIVED;
                break;
            case SCRATCH:
                typeOfCreation = CreationType.SCRATCH;
                break;
        }

        actionReplace = new ActionReplace(firstEntityDef, firstEntityName, secondEntityName, typeOfCreation);
        return actionReplace;
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
//    }//= "C:\\java_projects\\currentJavaProject\\PDREngine\\src\\resources\\  master-ex1.xml    error3.xml";
//
//    // C:\\java_projects\\currentJavaProject\\PDREngine\\src\\resources\\example.xml

}
