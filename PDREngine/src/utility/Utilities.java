package utility;

import environment.EnvironmentDefinition;
import environment.EnvironmentInstance;
import exceptions.GeneralException;
import property.PropertyDefinition;
import property.PropertyInstance;
import range.Range;

import java.util.Map;
import java.util.Random;

public class Utilities {

    public static boolean initializeRandomBoolean(){
        Random random = new Random();
        boolean randomBoolean = random.nextBoolean();
        return randomBoolean;
    }

    public static int initializeRandomInt(int from,int to){
        int fromCasted = from;
        int toCasted = to;
        Random random = new Random();
        int randomNumber = random.nextInt(toCasted - fromCasted + 1) + fromCasted;
        return randomNumber;
    }

    public static float initializeRandomFloat(Range rangeOfProperty){
        float from = rangeOfProperty.getFrom();
        float to = rangeOfProperty.getTo();
        Random random = new Random();
        float randomNumber = from + random.nextFloat() * (to - from);
        return randomNumber;
    }

    public static String initializeRandomString(){
        int min = 1;
        int max = 50;
        Random random = new Random();
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!?,_-(). ";
        int randomStrLength = random.nextInt(max - min + 1) + min;
        String randomInitializedString = "";
        for(int i = 0; i < randomStrLength; i++){
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            randomInitializedString += randomChar;
        }

        return randomInitializedString;
    }

    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isFloat(String input) {
        try {
            Float.parseFloat(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean verifyNumericPropertyTYpe(PropertyInstance propertyValue) {
        return propertyValue.getPropertyDefinition().getPropertyType().equalsIgnoreCase("DECIMAL") ||
                propertyValue.getPropertyDefinition().getPropertyType().equalsIgnoreCase("FLOAT");
    }


    public static void isValueCalculationNumeric(String valueBy, Map<String, EnvironmentDefinition> environmentDefinitionMap, EntityDefinition currEntity) throws GeneralException {

        String copyOfValueBy = valueBy;

        int openingParenthesisIndex = copyOfValueBy.indexOf("(");

        int closingParenthesisIndex = copyOfValueBy.indexOf(")");

        if (openingParenthesisIndex != -1 && closingParenthesisIndex != -1) {

            // Extract the word "random" before the opening parenthesis
            String word = copyOfValueBy.substring(0, openingParenthesisIndex).toLowerCase();
            // Extract the number between the parentheses
            String valueString = copyOfValueBy.substring(openingParenthesisIndex + 1, closingParenthesisIndex);
            if (word.equals("random")) {
                if (isInteger(valueString)) {
                    return;
                } else {
                    throw new GeneralException("The function Random required numeric input but got" + valueString);
                }
            } else if (word.equals("environment")) {
                EnvironmentDefinition requiredEnv = environmentDefinitionMap.get(valueString);
                if (requiredEnv == null) {
                    throw new GeneralException("Environment " + valueString + " doesnt exist");
                } else {
                    if (!requiredEnv.getEnvPropertyDefinition().getPropertyType().equalsIgnoreCase("decimal") &&
                            !requiredEnv.getEnvPropertyDefinition().getPropertyType().equalsIgnoreCase("float")) {
                        throw new GeneralException("Environment " + valueString + " is not of a numeric type");
                    }
                    return;
                }
            }
        }
        PropertyDefinitionEntity propertyDefinitionEntity = currEntity.getPropertyDefinition().get(valueBy);
        if(propertyDefinitionEntity != null && (propertyDefinitionEntity.getPropertyDefinition().getPropertyType().equalsIgnoreCase("float") ||
                propertyDefinitionEntity.getPropertyDefinition().getPropertyType().equalsIgnoreCase("decimal"))){
            return;
        }

        if((isInteger(valueBy)) || isFloat(valueBy)){
            return;
        }
        throw new GeneralException("the value " + valueBy + " is not a numeric or call to function in required form");
    }

    public static boolean isOperatorFromSingleCondition(String operator){
        return operator.equals("=") || operator.equals("!=") ||
                operator.equalsIgnoreCase("bt") || operator.equalsIgnoreCase("lt");
    }

    public static boolean isOperatorFromMultipleCondition(String operator){
        return operator.equalsIgnoreCase("and") || operator.equalsIgnoreCase("or");
    }

}

