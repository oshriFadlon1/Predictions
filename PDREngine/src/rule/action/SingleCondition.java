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
        

//        if (operator.equalsIgnoreCase("=")) {
//            switch (type){
//                case "decimal":
//                    if(valueCompareTo instanceof Integer)
//                    {
//                        result = ((Number)valueCompareTo).intValue() == ((Number)propertyInstance.getPropValue()).intValue();
//                    }else {
//                        throw new GeneralException("operator "+ this.operator + " can't compare between to diffrent types");
//                    }
//                    break;
//                case "float":
//                    if(valueCompareTo instanceof Float)
//                    {
//                        result = ((Number)valueCompareTo).floatValue() == ((Number)propertyInstance.getPropValue()).floatValue();
//                    }else {
//                        throw new GeneralException("operator "+ this.operator + " can't compare between to diffrent types");
//                    }
//                    break;
//                case "boolean":
//                    if(valueCompareTo instanceof Boolean)
//                    {
//                        result = ((Boolean)valueCompareTo).equals((Boolean)propertyInstance.getPropValue());
//                    }else {
//                        throw new GeneralException("operator "+ this.operator + " can't compare between to diffrent types");
//                    }
//                    break;
//                case "string":
//                    if(valueCompareTo instanceof String)
//                    {
//                        result = ((String)valueCompareTo).equalsIgnoreCase((String)propertyInstance.getPropValue());
//                    }else {
//                        throw new GeneralException("operator "+ this.operator + " can't compare between to diffrent types");
//                    }
//                    break;
//            }
//        } else if (operator.equalsIgnoreCase("!=")){
//            switch (type){
//                case "decimal":
//                    if(valueCompareTo instanceof Integer)
//                    {
//                        result = ((Number)valueCompareTo).intValue() != ((Number)propertyInstance.getPropValue()).intValue();
//                    }else {
//                        throw new GeneralException("operator "+ this.operator + " can't compare between to diffrent");
//                    }
//                    break;
//                case "float":
//                    if(valueCompareTo instanceof Float)
//                    {
//                        result = ((Number)valueCompareTo).floatValue() != ((Number)propertyInstance.getPropValue()).floatValue();
//                    }else {
//                        throw new GeneralException("operator "+ this.operator + " can't compare between to diffrent types");
//                    }
//                    break;
//                case "boolean":
//                    if(valueCompareTo instanceof Boolean)
//                    {
//                        result = !(((Boolean)valueCompareTo).equals((Boolean)propertyInstance.getPropValue()));
//                    }else {
//                        throw new GeneralException("operator "+ this.operator + " can't compare between to diffrent types");
//                    }
//                    break;
//                case "string":
//                    if(valueCompareTo instanceof String)
//                    {
//                        result = !((String)valueCompareTo).equalsIgnoreCase((String)propertyInstance.getPropValue());
//                    }else {
//                        throw new GeneralException("operator "+ this.operator + " can't compare between to diffrent types");
//                    }
//                    break;
//            }
//
//        } else if (operator.equalsIgnoreCase("bt")) {
//            if (type.equalsIgnoreCase("decimal") || type.equalsIgnoreCase("float")){
//                if(propertyInstance.getPropValue() instanceof Integer){
//                    downCastProp = ((Integer)propertyInstance.getPropValue()).floatValue();
//                }
//                 else if(propertyInstance.getPropValue() instanceof Float){
//                    downCastProp = ((Float) propertyInstance.getPropValue()).floatValue();
//                } else{
//                    throw new GeneralException("operator "+ this.operator + " can't compare between to diffrent types");
//                }
//                if(valueCompareTo instanceof Integer){
//                    downCastCompereTo = ((Integer)valueCompareTo).floatValue();
//                }
//                else if(valueCompareTo instanceof Float){
//                    downCastCompereTo = ((Float) valueCompareTo).floatValue();
//                } else {
//                    throw new GeneralException("operator "+ this.operator + " can't compare between to diffrent types");
//                }
//
//                result = downCastProp > downCastCompereTo;
//
//            } else {
//                throw new GeneralException("operator "+ this.operator + " can't compare between to diffrent types");
//            }
//
////            switch (type){
////                case "decimal":
////                    if(valueCompareTo instanceof Integer)
////                    {
////                        result = 0 > (((Integer)valueCompareTo).compareTo((Integer)propertyInstance.getPropValue()));
////                    }else {
////                        throw new GeneralException("operator "+ this.operator + " can't compare between to diffrent");
////                    }
////                    break;
////                case "float":
////                    if(valueCompareTo instanceof Float)
////                    {
////                        result = 0 > (((Float)valueCompareTo).compareTo((Float)propertyInstance.getPropValue()));
////                    }else {
////                        throw new GeneralException("operator "+ this.operator + " can't compare between to diffrent types");
////                    }
////                    break;
////            }
//
//        } else if (operator.equalsIgnoreCase("lt")) {
//            if (type.equalsIgnoreCase("decimal") || type.equalsIgnoreCase("float")){
//                if(propertyInstance.getPropValue() instanceof Integer){
//                    downCastProp = ((Integer)propertyInstance.getPropValue()).floatValue();
//                }
//                else if(propertyInstance.getPropValue() instanceof Float){
//                    downCastProp = ((Float) propertyInstance.getPropValue()).floatValue();
//                } else{
//                    throw new GeneralException("operator "+ this.operator + " can't compare between to diffrent types");
//                }
//                if(valueCompareTo instanceof Integer){
//                    downCastCompereTo = ((Integer)valueCompareTo).floatValue();
//                }
//                else if(valueCompareTo instanceof Float){
//                    downCastCompereTo = ((Float)valueCompareTo).floatValue();
//                } else {
//                    throw new GeneralException("operator "+ this.operator + " can't compare between to diffrent types");
//                }
//
//                result = downCastProp < downCastCompereTo;
//            } else {
//                throw new GeneralException("operator "+ this.operator + " can't compare between to diffrent types");
//            }
//
////            switch (type){
////                case "decimal":
////                    if(valueCompareTo instanceof Integer)
////                    {
////
////                        result = 0 < (((Integer)valueCompareTo).compareTo((Integer)propertyInstance.getPropValue()));
////                    }else {
////                        throw new GeneralException("operator "+ this.operator + "can't compare between to diffrent");
////                    }
////                    break;
////                case "float":
////                    if(valueCompareTo instanceof Float)
////                    {
////                        result = 0 < (((Float)valueCompareTo).compareTo((Float)propertyInstance.getPropValue()));
////                    }else {
////                        throw new GeneralException("operator "+ this.operator + "can't compare between to diffrent types");
////                    }
////                    break;
////            }
//
//        }
//        return result;
//    }

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
