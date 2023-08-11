package rule.action;

import entity.EntityDefinition;
import enums.Operation;
import exceptions.GeneralException;
import necessaryVariables.NecessaryVariablesImpl;
import property.PropertyInstance;

public class ActionDecrease extends AbstractAction {

    private String decreaseBy;
    private String propertyName;

    public ActionDecrease(EntityDefinition entityDefinition, String increaseBy, String propertyName) {
        super(Operation.INCREASE, entityDefinition);
        this.increaseBy = increaseBy;
        this.propertyName = propertyName;
    }

    public String getDecreaseBy() {
        return decreaseBy;
    }

    public void setDecreaseBy(String decreaseBy) {
        this.decreaseBy = decreaseBy;
    }


    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public void invoke(NecessaryVariablesImpl context) throws GeneralException {
        PropertyInstance propertyInstance = context.getPrimaryEntityInstance().getPropertyByName(propertyName);
        if (!verifyNumericPropertyTYpe(propertyInstance)) {
            throw new GeneralException("Decrease action can't operate on a none number property [" + propertyName);
        }

        Object x = propertyInstance.getPropValue();

        Object y = null;
        try {
            y = context.getValueFromString(this.decreaseBy);
        } catch (GeneralException e) {
            throw e;
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
