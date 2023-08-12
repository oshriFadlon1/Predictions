package rule.action;

import exceptions.GeneralException;
import interfaces.IConditionComponent;
import necessaryVariables.NecessaryVariablesImpl;
import property.PropertyInstance;

public class SingleCondition implements IConditionComponent {

    private String propertyName;
    private String operator;
    private String valueToCompare;

    public SingleCondition(String propertyName, String operator, String valueToCompare) {
        this.propertyName = propertyName;
        this.operator = operator;
        this.valueToCompare = valueToCompare;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValueToCompare() {
        return valueToCompare;
    }

    public void setValueToCompare(String valueToCompare) {
        this.valueToCompare = valueToCompare;
    }

    @Override
    public boolean getResultFromCondition(NecessaryVariablesImpl context) throws GeneralException {

        PropertyInstance propertyInstance = context.getPrimaryEntityInstance().getPropertyByName(propertyName);
        Object valueCompareTo = null;
        String type = propertyInstance.getPropertyDefinition().getPropertyType().toLowerCase();
        try{
            valueCompareTo = context.getValueFromString(valueToCompare);
        }
        catch (GeneralException e){
            throw e;
        }
        
        boolean result = false;
        if (operator.equalsIgnoreCase("=")) {
            switch (type){
                case "decimal":
                    if(valueCompareTo instanceof Integer)
                    {
                        result = ((Integer)valueCompareTo).equals((Integer)propertyInstance.getPropValue());
                    }else {
                        throw new GeneralException("operator "+ this.operator + "can't compare between to diffrent");
                    }
                    break;
                case "float":
                    if(valueCompareTo instanceof Float)
                    {
                        result = ((Float)valueCompareTo).equals((Float)propertyInstance.getPropValue());
                    }else {
                        throw new GeneralException("operator "+ this.operator + "can't compare between to diffrent types");
                    }
                    break;
                case "boolean":
                    if(valueCompareTo instanceof Boolean)
                    {
                        result = ((Boolean)valueCompareTo).equals((Boolean)propertyInstance.getPropValue());
                    }else {
                        throw new GeneralException("operator "+ this.operator + "can't compare between to diffrent types");
                    }
                    break;
                case "string":
                    if(valueCompareTo instanceof String)
                    {
                        result = ((String)valueCompareTo).equalsIgnoreCase((String)propertyInstance.getPropValue());
                    }else {
                        throw new GeneralException("operator "+ this.operator + "can't compare between to diffrent types");
                    }
                    break;
            }
        } else if (operator.equalsIgnoreCase("!=")){
            switch (type){
                case "decimal":
                    if(valueCompareTo instanceof Integer)
                    {
                        result = !(((Integer)valueCompareTo).equals((Integer)propertyInstance.getPropValue()));
                    }else {
                        throw new GeneralException("operator "+ this.operator + "can't compare between to diffrent");
                    }
                    break;
                case "float":
                    if(valueCompareTo instanceof Float)
                    {
                        result = !(((Float)valueCompareTo).equals((Float)propertyInstance.getPropValue()));
                    }else {
                        throw new GeneralException("operator "+ this.operator + "can't compare between to diffrent types");
                    }
                    break;
                case "boolean":
                    if(valueCompareTo instanceof Boolean)
                    {
                        result = !(((Boolean)valueCompareTo).equals((Boolean)propertyInstance.getPropValue()));
                    }else {
                        throw new GeneralException("operator "+ this.operator + "can't compare between to diffrent types");
                    }
                    break;
                case "string":
                    if(valueCompareTo instanceof String)
                    {
                        result = !((String)valueCompareTo).equalsIgnoreCase((String)propertyInstance.getPropValue());
                    }else {
                        throw new GeneralException("operator "+ this.operator + "can't compare between to diffrent types");
                    }
                    break;
            }
            
        } else if (operator.equalsIgnoreCase("bt")) {
            switch (type){
                case "decimal":
                    if(valueCompareTo instanceof Integer)
                    {
                        result = 0 > (((Integer)valueCompareTo).compareTo((Integer)propertyInstance.getPropValue()));
                    }else {
                        throw new GeneralException("operator "+ this.operator + "can't compare between to diffrent");
                    }
                    break;
                case "float":
                    if(valueCompareTo instanceof Float)
                    {
                        result = 0 > (((Float)valueCompareTo).compareTo((Float)propertyInstance.getPropValue()));
                    }else {
                        throw new GeneralException("operator "+ this.operator + "can't compare between to diffrent types");
                    }
                    break;
            }
            
        } else if (operator.equalsIgnoreCase("lt")) {
            switch (type){
                case "decimal":
                    if(valueCompareTo instanceof Integer)
                    {

                        result = 0 < (((Integer)valueCompareTo).compareTo((Integer)propertyInstance.getPropValue()));
                    }else {
                        throw new GeneralException("operator "+ this.operator + "can't compare between to diffrent");
                    }
                    break;
                case "float":
                    if(valueCompareTo instanceof Float)
                    {
                        result = 0 < (((Float)valueCompareTo).compareTo((Float)propertyInstance.getPropValue()));
                    }else {
                        throw new GeneralException("operator "+ this.operator + "can't compare between to diffrent types");
                    }
                    break;
            }
            
        }
        return result;
    }
}
