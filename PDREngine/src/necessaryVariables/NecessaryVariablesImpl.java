package necessaryVariables;

import entity.EntityInstance;
import environment.EnvironmentInstance;
import exceptions.GeneralException;
import utility.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NecessaryVariablesImpl implements NecessaryVariables {
    private EntityInstance primaryEntityInstance;
    private List<EntityInstance> entityInstanceManager;
    private Map<String, EnvironmentInstance> activeEnvironment;

    public NecessaryVariablesImpl(EntityInstance primaryEntityInstance, List<EntityInstance> entityInstanceManager, Map<String, EnvironmentInstance> activeEnvironment) {
        this.primaryEntityInstance = primaryEntityInstance;
        this.entityInstanceManager = entityInstanceManager;
        this.activeEnvironment = activeEnvironment;
    }

    public NecessaryVariablesImpl(Map<String, EnvironmentInstance> activeEnvironment) {
        this.activeEnvironment = activeEnvironment;
        this.entityInstanceManager = new ArrayList<>();
        this.primaryEntityInstance = null;//not sure yet
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
    public void removeEntity(EntityInstance entityInstance) {
        EntityInstance entityToRemove = null;
        for (EntityInstance instance: entityInstanceManager) {
            if (instance.getId() == entityInstance.getId()){
                entityToRemove = instance;
                break;
            }
        }
        // the entity removes himself from the list and updates the quantity
        entityToRemove.getDefinitionOfEntity().setEndPopulation(entityToRemove.getDefinitionOfEntity().getEndPopulation() - 1);
        entityInstanceManager.remove(entityToRemove);
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

        int closingParenthesisIndex = valueBy.indexOf(")");

        // Extract the word "random" before the opening parenthesis
        String word = valueBy.substring(0, openingParenthesisIndex);
        // Extract the number between the parentheses
        String valueString = valueBy.substring(openingParenthesisIndex + 1, closingParenthesisIndex);
        if (word.equals("random"))
        {
            if (Utilities.isInteger(valueString)) {
                int number = Integer.parseInt(valueString);
                result = Utilities.initializeRandomInt(0,number);
            }
            else {
                throw new GeneralException( "The function Random required numeric input but got" + valueString );
            }
        } else if (word.equals("environment")){
            EnvironmentInstance requiredEnv = activeEnvironment.get(valueString);
            if (requiredEnv == null)
            {
                throw new GeneralException("Environment instance doesn't have an instance that his name is "+valueString);
            }
            result = requiredEnv.getEnvValue();
        }
        return result;
    }
}
