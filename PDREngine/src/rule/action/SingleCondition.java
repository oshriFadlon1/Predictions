package rule.action;

import exceptions.GeneralException;
import interfaces.IConditionComponent;
import necessaryVariables.NecessaryVariablesImpl;
import property.PropertyInstance;

import java.io.Serializable;

public class SingleCondition implements IConditionComponent, Serializable {

    private String expressionValue;
    private String operator;
    private String valueToCompare;

    public SingleCondition(String propertyName, String operator, String valueToCompare) {
        this.expressionValue = propertyName;
        this.operator = operator;
        this.valueToCompare = valueToCompare;
    }

    public String getExpressionValue() {
        return expressionValue;
    }

    public void setExpressionValue(String expressionValue) {
        this.expressionValue = expressionValue;
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
    public int getNumberOfCondition() {
        return 1;
    }

    @Override
    public boolean getResultFromCondition(NecessaryVariablesImpl context) throws GeneralException {

        Float downCastProp = 1.0F, downCastCompereTo = 1.0F;


        Object valueCompareTo = null, valueFromExpression = null;

        try{
            valueFromExpression = context.getValueFromString(expressionValue);
            valueCompareTo = context.getValueFromString(valueToCompare);
            return getResultByOperator(valueFromExpression, valueCompareTo, operator);
        }
        catch (GeneralException e){
            throw e;
        }
    }

    private boolean getResultByOperator(Object valueFromExpression, Object valueCompareTo, String operator) throws GeneralException {
        if (operator.equalsIgnoreCase("bt"))
        {
            if ((valueFromExpression instanceof Number) && (valueCompareTo instanceof Number)){
                return ((Number) valueFromExpression).floatValue() > ((Number) valueCompareTo).floatValue();
            }
            else {
                throw new GeneralException("operator "+ this.operator + " can't compare between to diffrent types");
            }
        }
        if (operator.equalsIgnoreCase("lt")){
            if ((valueFromExpression instanceof Number) && (valueCompareTo instanceof Number)){
                return ((Number) valueFromExpression).floatValue() < ((Number) valueCompareTo).floatValue();
            }
            else {
                throw new GeneralException("operator "+ this.operator + " can't compare between to diffrent types");
            }
        }
        if (operator.equalsIgnoreCase("=")){
            if ((valueFromExpression instanceof Number) && (valueCompareTo instanceof Number)){
                return ((Number) valueFromExpression).floatValue() == ((Number) valueCompareTo).floatValue();
            } else if ((valueFromExpression instanceof Boolean) && (valueCompareTo instanceof Boolean)){
                return ((Boolean) valueFromExpression).equals((Boolean) valueCompareTo);
            }
            else if ((valueFromExpression instanceof String) && (valueCompareTo instanceof String)){
                return ((String) valueFromExpression).equals((String) valueCompareTo);
            }
            else {
                throw new GeneralException("operator "+ this.operator + " can't compare between to diffrent types");
            }
        }
        if (operator.equalsIgnoreCase("!=")){
            if ((valueFromExpression instanceof Number) && (valueCompareTo instanceof Number)){
                return ((Number) valueFromExpression).floatValue() != ((Number) valueCompareTo).floatValue();
            } else if ((valueFromExpression instanceof Boolean) && (valueCompareTo instanceof Boolean)){
                return !((Boolean) valueFromExpression).equals((Boolean) valueCompareTo);
            }
            else if ((valueFromExpression instanceof String) && (valueCompareTo instanceof String)){
                return !((String) valueFromExpression).equals((String) valueCompareTo);
            }
            else {
                throw new GeneralException("operator "+ this.operator + " can't compare between to diffrent types");
            }
        }
        return false;
    }
}
