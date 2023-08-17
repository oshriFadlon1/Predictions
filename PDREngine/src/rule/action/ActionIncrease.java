package rule.action;

import entity.EntityDefinition;
import enums.Operation;
import exceptions.GeneralException;
import necessaryVariables.NecessaryVariablesImpl;
import property.PropertyInstance;
import utility.Utilities;

import java.io.Serializable;

public class ActionIncrease extends AbstractAction implements Serializable {

    private String increaseBy;
    private String propertyName;

    public ActionIncrease(EntityDefinition entityDefinition, String increaseBy, String propertyName){
        super(Operation.INCREASE,entityDefinition);
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
    public void invoke(NecessaryVariablesImpl context) throws GeneralException {
        PropertyInstance propertyInstance = context.getPrimaryEntityInstance().getPropertyByName(propertyName);
        if (!Utilities.verifyNumericPropertyTYpe(propertyInstance)) {
            throw new GeneralException("increase action can't operate on a none number property [" + propertyName);
        }

        Object x = propertyInstance.getPropValue();

        Object y = null;
        try {
            y = context.getValueFromString(this.increaseBy);
        } catch (GeneralException e) {
            throw e ;
        }
        // actual calculation
        Object result;// need to take the value from

        if (propertyInstance.getPropertyDefinition().getPropertyType().equalsIgnoreCase("DECIMAL")){
            result = ((Number)x).intValue() + ((Number)y).intValue();
        }
        else {
            result = ((Number)x).floatValue() + ((Number)y).floatValue();
        }

        // updating result on the property
        propertyInstance.setPropValue(result);
    }

    @Override
    public Operation getOperationType() {
        return Operation.INCREASE;
    }

    @Override
    public EntityDefinition getContextEntity() {
        return super.getEntityDefinition();
    }


}
