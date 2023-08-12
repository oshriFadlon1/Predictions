package rule.action;

import entity.EntityDefinition;
import enums.Operation;
import exceptions.GeneralException;
import necessaryVariables.NecessaryVariablesImpl;
import property.PropertyInstance;
import utility.Utilities;

public class ActionCalculationMultiply extends ActionCalculation{

    private String arg1;
    private String arg2;

    public ActionCalculationMultiply(EntityDefinition entityDefinition, String propertyPlacement, String arg1, String arg2) {
        super(entityDefinition, propertyPlacement);
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    @Override
    public void invoke(NecessaryVariablesImpl context) throws GeneralException {
        Object y = null,x = null,result = null;
        if ((!Utilities.isInteger(this.arg1) && !Utilities.isInteger(this.arg2)) ||
                (!Utilities.isFloat(this.arg1) && !Utilities.isFloat(this.arg2)))
        {
            throw new IllegalArgumentException("multiply action can't operate on a none number property [" + super.getPropertyPlacement());
        }

        try {
            x = context.getValueFromString(this.arg1);
            y = context.getValueFromString(this.arg2);
        } catch (GeneralException e) {
            throw new IllegalArgumentException(e);
        }

        if (context.getPrimaryEntityInstance().getPropertyByName(super.getPropertyPlacement()).getPropertyDefinition().getPropertyType().equalsIgnoreCase("DECIMAL")){
            result = (Integer)x * (Integer)y;
        }
        else{
            result = (float)x * (float)y;
        }


        // updating result on the property
        PropertyInstance propertyInstance = context.getPrimaryEntityInstance().getPropertyByName(super.getPropertyPlacement());
        propertyInstance.setPropValue(result);
    }

    @Override
    public Operation getOperationType() {
        return Operation.CALCULATION;
    }

    @Override
    public EntityDefinition getContextEntity() {
        return super.getEntityDefinition();
    }
}
