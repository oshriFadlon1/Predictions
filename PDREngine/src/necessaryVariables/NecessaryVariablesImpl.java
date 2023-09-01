package necessaryVariables;

import createAndKillEntities.CreateAndKillEntities;
import entity.EntityDefinition;
import entity.EntityInstance;
import entity.SecondEntity;
import enums.CreationType;
import environment.EnvironmentInstance;
import exceptions.GeneralException;
import utility.Utilities;

import javax.swing.text.html.parser.Entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NecessaryVariablesImpl implements NecessaryVariables, Serializable {
    private EntityInstance primaryEntityInstance;
    private EntityInstance secondaryEntityInstance;
    private List<EntityInstance> secondaryInstanceManager;
    private List<EntityInstance> entityInstanceManager;
    private Map<String, EnvironmentInstance> activeEnvironment;

    private EntityInstance entityToKill;
    private CreateAndKillEntities entityToKillAndCreate;

    public NecessaryVariablesImpl(EntityInstance primaryEntityInstance, List<EntityInstance> entityInstanceManager, Map<String, EnvironmentInstance> activeEnvironment) {
        this.primaryEntityInstance = primaryEntityInstance;
        this.entityInstanceManager = entityInstanceManager;
        this.activeEnvironment = activeEnvironment;
        this.entityToKillAndCreate = new CreateAndKillEntities();
        this.entityToKill = null;
    }

    public NecessaryVariablesImpl(Map<String, EnvironmentInstance> activeEnvironment) {
        this.activeEnvironment = activeEnvironment;
        this.entityInstanceManager = new ArrayList<>();
        this.primaryEntityInstance = null;//not sure yet
        this.entityToKillAndCreate= new CreateAndKillEntities();
        this.entityToKill = null;
    }

    public void setPrimaryEntityInstance(EntityInstance primaryEntityInstance) {
        this.primaryEntityInstance = primaryEntityInstance;
    }

    public List<EntityInstance> getEntityInstanceManager() {
        return entityInstanceManager;
    }

    public void setEntityInstanceManager(List<EntityInstance> entityInstanceManager) {
        this.entityInstanceManager = entityInstanceManager;
    }

    public Map<String, EnvironmentInstance> getActiveEnvironment() {
        return activeEnvironment;
    }

    public void setActiveEnvironment(Map<String, EnvironmentInstance> activeEnvironment) {
        this.activeEnvironment = activeEnvironment;
    }

    @Override
    public EntityInstance getPrimaryEntityInstance() {
        return primaryEntityInstance;
    }
    @Override
    public EntityInstance getSecondaryEntityInstance() {
        return secondaryEntityInstance;
    }

    public void setSecondaryEntityInstance(EntityInstance secondaryEntityInstance) {
        this.secondaryEntityInstance = secondaryEntityInstance;
    }

    @Override
    public EntityInstance getEntityToKill() {
        return entityToKill;
    }

    public CreateAndKillEntities getEntityToKillAndCreate() {
        return entityToKillAndCreate;
    }

    public void setEntityToKillAndCreate(CreateAndKillEntities entityToKillAndCreate) {
        this.entityToKillAndCreate = entityToKillAndCreate;
    }

    public void setEntityToKill(EntityInstance entityToKill) {
        this.entityToKill = entityToKill;
    }

    @Override
    public void removeEntity(EntityInstance entityInstance) {
        EntityInstance entityToRemove = null;
        for (EntityInstance instance: entityInstanceManager) {
            if (instance.getId() == entityInstance.getId()){
                entityToRemove = instance;
                break;
            }
        }
        // the entity removes himself from the list and updates the quantity
        entityToKill = entityToRemove;
//        entityToRemove.getDefinitionOfEntity().setEndPopulation(entityToRemove.getDefinitionOfEntity().getEndPopulation() - 1);
//        entityInstanceManager.remove(entityToRemove);
    }

    @Override
    public void killAndCreateEntity(EntityInstance entityInstance, EntityDefinition secondaryInstance, CreationType creationType){
        this.entityToKillAndCreate.setKill(entityInstance);
        this.entityToKillAndCreate.setCreate(secondaryInstance);
        this.entityToKillAndCreate.setCreationType(creationType);
//        switch(creationType){
//            case SCRATCH:
//                this.entityToKillAndCreate.setCreationType(creationType);
//                break;
//            case DERIVED:
//                this.entityToKillAndCreate.setCreationType(creationType);
//                break;
//        }

        //removeEntity(entityInstance);
    }



    @Override
    public EnvironmentInstance getEnvironmentVariable(String name) {
        return this.activeEnvironment.get(name);
    }

    public Object getValueFromString(String valueBy) throws GeneralException{
        Object o = null;

        if (valueBy.contains("("))
        {
            o = valueFromFunctionHelper(valueBy);
        }
        else if (this.primaryEntityInstance.getAllProperties().get(valueBy) != null)
        {
            o = getvalueFromProperty(valueBy);
        }
        else {
            o = valueAsIs(valueBy);
        }
        return o;
    }

    private Object getvalueFromProperty(String valueBy) {
        return this.primaryEntityInstance.getAllProperties().get(valueBy).getPropValue();
    }

    private Object valueAsIs(String valueBy) {
        if (Utilities.isInteger(valueBy))
        {
            return Integer.parseInt(valueBy);
        }
        else if (Utilities.isFloat(valueBy))
        {
            return Float.parseFloat(valueBy);
        }
        else if (valueBy.equals("true") || valueBy.equals("false"))
        {
            return Boolean.parseBoolean(valueBy);
        }
        return valueBy;
    }

    private Object valueFromFunctionHelper(String valueBy) throws GeneralException {
        Object result = null;
        int openingParenthesisIndex = valueBy.indexOf("(");

        String word = valueBy.substring(0, openingParenthesisIndex);

        String valueString = valueBy.substring(openingParenthesisIndex + 1, valueBy.length() - 1);
        switch (word.toLowerCase()){
            case "random":
                if (Utilities.isInteger(valueString)) {
                    int number = Integer.parseInt(valueString);
                    result = Utilities.initializeRandomInt(0,number);
                }
                else {
                    throw new GeneralException( "The function Random required numeric input but got" + valueString );
                }
                break;
            case "environment":
                EnvironmentInstance requiredEnv = activeEnvironment.get(valueString);
                if (requiredEnv == null)
                {
                    throw new GeneralException("Environment instance doesn't have an instance that his name is "+valueString);
                }
                result = requiredEnv.getEnvValue();
                break;
            case "ticks":
                int indexOfPointTick = valueString.indexOf(".");
                String EntityNameTick = valueString.substring(0, indexOfPointTick);
                String entityPropertyNameTick = valueString.substring(indexOfPointTick + 1, valueString.length());
                if (this.primaryEntityInstance.getDefinitionOfEntity().getEntityName().equalsIgnoreCase(EntityNameTick)){
                    result = this.primaryEntityInstance.getPropertyByName(entityPropertyNameTick).getCurrentTicksWithoutChange();
                } else if (this.secondaryEntityInstance.getDefinitionOfEntity().getEntityName().equalsIgnoreCase(EntityNameTick)){
                    result = this.secondaryEntityInstance.getPropertyByName(entityPropertyNameTick).getCurrentTicksWithoutChange();
                } else {
                    throw new GeneralException(EntityNameTick + " instance doesn't have an property that his name is "+ entityPropertyNameTick);
                }
                break;
            case"evaluate":
                int indexOfPointEvaluate = valueString.indexOf(".");
                String EntityNameEvaluate = valueString.substring(0, indexOfPointEvaluate);
                String entityPropertyNameEvaluate = valueString.substring(indexOfPointEvaluate + 1, valueString.length());
                if (this.primaryEntityInstance.getDefinitionOfEntity().getEntityName().equalsIgnoreCase(EntityNameEvaluate)){
                    result = this.primaryEntityInstance.getPropertyByName(entityPropertyNameEvaluate).getPropValue();
                } else if (this.secondaryEntityInstance.getDefinitionOfEntity().getEntityName().equalsIgnoreCase(EntityNameEvaluate)){
                    result = this.secondaryEntityInstance.getPropertyByName(entityPropertyNameEvaluate).getPropValue();
                } else {
                    throw new GeneralException(EntityNameEvaluate + " instance doesn't have an property that his name is "+ entityPropertyNameEvaluate);
                }
                break;
            case "percent":
                int indexOfComma = valueBy.indexOf(",");
                String expression1 = valueBy.substring(8, indexOfComma);
                String expression2 = valueBy.substring(indexOfComma + 1, valueBy.length() - 1);
                Object value1 = getValueFromString(expression1);
                Object value2 = getValueFromString(expression2);
                try {
                    result = (((Number) value1).floatValue() * ((Number) value2).floatValue()) / 100;
                } catch (Exception e){
                    throw new GeneralException("percent must get numeric values, and " + expression1 + " not numeric or "+ expression2 +" not numeric.");
                }
                break;
        }
        return result;
    }

    public void resetKillAndCreateAndKill(){
        this.entityToKill = null;
        this.entityToKillAndCreate.resetCreateAndKill();
    }
}
