package utility;

import property.PropertyDefinition;
import property.PropertyInstance;
import range.Range;

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


}

