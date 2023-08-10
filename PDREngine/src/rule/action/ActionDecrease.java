package rule.action;

import entity.EntityDefinition;
import entity.EntityInstance;
import enums.Operation;
import exceptions.GeneralException;
import necessaryVariables.NecessaryVariablesImpl;
import property.PropertyInstance;
import utility.Utilities;

public class ActionDecrease extends AbstractAction {

    private String increaseBy;
    private String propertyName;

    public ActionDecrease(EntityDefinition entityDefinition, String increaseBy, String propertyName) {
        super(Operation.INCREASE, entityDefinition);
        this.increaseBy = increaseBy;
        this.propertyName = propertyName;
    }

    public String getIncreaseBy() {
        return increaseBy;
    }

    public void setIncreaseBy(String increaseBy) {
        this.increaseBy = increaseBy;
    }


    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public void invoke(NecessaryVariablesImpl context) {
        PropertyInstance propertyInstance = context.getPrimaryEntityInstance().getPropertyByName(propertyName);
        if (!verifyNumericPropertyTYpe(propertyInstance)) {
            throw new IllegalArgumentException("Decrease action can't operate on a none number property [" + propertyName);
        }

        Object x = propertyInstance.getPropValue();

        Object y = null;
        try {
            y = context.getValueFromString(this.increaseBy);
        } catch (GeneralException e) {
            throw new IllegalArgumentException(e);
        }
        // something that evaluates expression to a number, say the result is 5...
        // now you can also access the environment variables through the active environment...
        // PropertyInstance blaPropertyInstance = activeEnvironment.getProperty("bla");

        // actual calculation
        Object result;// need to take the value from

        if (propertyInstance.getPropertyDefinition().getPropertyType().equalsIgnoreCase("DECIMAL")){
            result = (Integer)x - (Integer)y;
        }else {
            result = (float)x - (float)y;
        }


        // updating result on the property
        propertyInstance.setPropValue(result);
    }

    @Override
    public Operation getActionType() {
        return Operation.DECREASE;
    }

    @Override
    public EntityDefinition getContextEntity() {
        return super.getEntityDefinition();
    }

    private boolean verifyNumericPropertyTYpe(PropertyInstance propertyValue) {
        return propertyValue.getPropertyDefinition().getPropertyType().equalsIgnoreCase("DECIMAL") ||
                propertyValue.getPropertyDefinition().getPropertyType().equalsIgnoreCase("FLOAT");
    }


}
