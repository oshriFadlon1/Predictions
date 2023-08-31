package ui;

import dto.*;
import engine.MainEngine;
import environment.EnvironmentDefinition;
import interfaces.InterfaceMenu;
import property.PropertyDefinition;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;

public class MainUi {

    public static void main(String[] args) {
        InterfaceMenu interfaceMenu = new MainEngine();
        boolean userLoadFile = false;

        String xmlPath;

        System.out.println("Hello and welcome to the simulation runner app.");
        printMenu();
        int userChoice = getUserChoice(1,7);
        while (userChoice != 5) {
            if ( 1 < userChoice && userChoice < 7)
            {
                if (userLoadFile) {
                    if (userChoice == 6){
                        System.out.println("Enter full path (up to the file name without the suffix) to file you liked to save the current state of the system");
                        DtoResponse dtoResponseForSave = interfaceMenu.saveWorldState((new Scanner(System.in)).nextLine());
                        System.out.println(dtoResponseForSave.getResponse());
                    } else {
                        switchUserChoice(userChoice, interfaceMenu);
                    }
                }
                else {
                    System.out.println("Cannot do current action before loading XML file. Need to load XML file first.");
                }
            }

            if (userChoice == 1) {
                xmlPath = getFileXmlPath();
                DtoResponse dtoResponse =interfaceMenu.createWorldDefinition(xmlPath);
                System.out.println(dtoResponse.getResponse());
                userLoadFile = dtoResponse.isSucceeded();
            }

            if (userChoice == 7){
                System.out.println("Enter full path (up to the file name without the suffix) to file you liked to load the current state from");
                DtoResponse dtoResponseForLoad = interfaceMenu.loadWorldState((new Scanner(System.in)).nextLine());
                System.out.println(dtoResponseForLoad.getResponse());
                userLoadFile = dtoResponseForLoad.isSucceeded();
            }
            printMenu();
            userChoice = getUserChoice(1,7);
        }

    }

    private static void switchUserChoice(int userChoice, InterfaceMenu interfaceMenu){
        switch (userChoice){
            case 2:DtoResponsePreview previewOfSimulation = interfaceMenu.showCurrentSimulation();
                printCurrentSimulationPreview(previewOfSimulation);
                break;
            case 3:
                //run simulation
                DtoEnvironments environmentsAvailable = interfaceMenu.sendEnvironmentsToUser();
                DtoUiToEngine environmentsForEngine = printAndValidateEnvironments(environmentsAvailable.getEnvironmentDefinitions(),interfaceMenu);
                DtoResponseSimulationEnded dtoResponseSimulationEnded = interfaceMenu.runSimulations(environmentsForEngine);
                System.out.println("Simulation run ended.");
                System.out.println("Simulation id: " + dtoResponseSimulationEnded.getSimulationId());
                if (dtoResponseSimulationEnded.getErrorMsg() == null)
                {
                    System.out.println("Ending cause: ");
                    System.out.println("Ticks: " + dtoResponseSimulationEnded.getEndingCause().isEndByTicks());
                    System.out.println("Seconds: " + dtoResponseSimulationEnded.getEndingCause().isEndBySeconds());
                } else {
                    System.out.println("The run fail because " + dtoResponseSimulationEnded.getErrorMsg());
                }

                break;
                case 4:
                DtoOldSimulationsMap response = interfaceMenu.getMapOfOldSimulation();
                if(response.getOldSimulationsMap().size() == 0){
                    System.out.println("No simulations to show.");
                }
                else{
                    System.out.println(String.format("Please choose from the list above a simulation number between 1 to %d." ,response.getOldSimulationsMap().size()));
                    displayMapToUser(response.getOldSimulationsMap());
                    int userSimulationChoice = getUserChoice(1,response.getOldSimulationsMap().size());

                    // send back to mainEngien to fetch user choice and get back a copy of the choose world
                    List<DtoReviewOldSimulation> theChooseWorld = interfaceMenu.fetchChosenWorld(userSimulationChoice);

                    displayChooseWorldSimulation(theChooseWorld);

                }
                break;
        }
    }

    private static void displayChooseWorldSimulation(List<DtoReviewOldSimulation> theChooseWorld) {
        
        // display all entities in thw world the user choose
        int index = 0;
        System.out.println("Please choose the entity you like to display information on:");
        for (DtoReviewOldSimulation dtoReviewOldSimulation : theChooseWorld) {
            System.out.println((index+1) + ": " + theChooseWorld.get(index).getEntityDefinition().getEntityName());
            index ++ ;
        }
        int userSimulationDisplayChoice = getUserChoice(1,index);
        DtoReviewOldSimulation entityTheUserChoose = theChooseWorld.get(userSimulationDisplayChoice - 1);

        System.out.println("Please choose from the following display options: \n1. By quantity.\n2. By histogram");
        userSimulationDisplayChoice = getUserChoice(1,2);
        if (userSimulationDisplayChoice == 1){
            displayByQuantity(entityTheUserChoose);
        }
         else {
             if (entityTheUserChoose.getEntityInstanceList().size() != 0){
            System.out.println("you choose to display by histogram");
            Set<String> proprtyNamesInSet = fetchPropertiesNames(entityTheUserChoose.getEntityInstanceList());
            List<String> proprtyNamesInList = new ArrayList<>(proprtyNamesInSet);
            System.out.println("Choose a property from the list below, which we will present the histogram by.");
            index = 0;
            for (String name : proprtyNamesInList) {
                System.out.println((index+1) + ": " + proprtyNamesInList.get(index));
                index ++;
            }
            userSimulationDisplayChoice = getUserChoice(1,proprtyNamesInList.size());

            String NameOfThePropertyUserChoose = proprtyNamesInList.get(userSimulationDisplayChoice - 1);
            System.out.println("The property you choose is: " + NameOfThePropertyUserChoose);
            List<Object> allResult = new ArrayList<>();
            for (DtoEntityInstanceToUi entityInstance: entityTheUserChoose.getEntityInstanceList()) {
                allResult.add(entityInstance.getNameOfProperty2Value().get(NameOfThePropertyUserChoose));
            }
            countPropertyByValue(allResult,
                    entityTheUserChoose
                            .getEntityDefinition()
                            .getPropertyDefinition()
                            .get(NameOfThePropertyUserChoose)
                            .getPropertyDefinition()
                            .getPropertyType());
            }
            else{
                System.out.println("There are no instances to present.");
            }
         }

    }

    private static void countPropertyByValue(List<Object> allResult, String propertyType) {
        switch (propertyType.toLowerCase()) {
            case "decimal":
                printHistogramInt(allResult);
                break;
            case "float":
                printHistogramFloat(allResult);
                break;
            case "boolean":
                printHistogramBoolean(allResult);
                break;
            case "string":
                printHistogramString(allResult);
                break;
        }
    }

    private static void printHistogramString(List<Object> allResult) {
        String valueInString;
        Map<String, Integer> histogram = new HashMap<>();
        for (Object o : allResult) {
            valueInString = ((String) o).toString();
            if (!histogram.containsKey(valueInString))
            {
                histogram.put(valueInString,1);
            } else{
                histogram.put(valueInString, histogram.get(valueInString) + 1);
            }
        }

        for (String str : histogram.keySet()) {
            System.out.println("value " + str + ": " + histogram.get(str));
        }
    }

    private static void printHistogramBoolean(List<Object> allResult) {
        boolean valueInBoolean;
        Map<Boolean, Integer> histogram = new HashMap<>();
        for (Object o : allResult) {
            valueInBoolean = ((Boolean) o).booleanValue();
            if (!histogram.containsKey(valueInBoolean))
            {
                histogram.put(valueInBoolean,1);
            } else{
                histogram.put(valueInBoolean, histogram.get(valueInBoolean) + 1);
            }
        }

        for (Boolean aBoolean : histogram.keySet()) {
            System.out.println("value " + aBoolean + ": " + histogram.get(aBoolean));
        }
    }

    private static void printHistogramFloat(List<Object> allResult) {
        float valueInFloat;
        Map<Float, Integer> histogram = new HashMap<>();
        for (Object o : allResult) {
            valueInFloat = ((Float) o).floatValue();
            if (!histogram.containsKey(valueInFloat))
            {
                histogram.put(valueInFloat,1);
            } else{
                histogram.put(valueInFloat, histogram.get(valueInFloat) + 1);
            }
        }

        for (Float aFloat : histogram.keySet()) {
            System.out.println("value " + aFloat + ": " + histogram.get(aFloat));
        }
    }

    private static void printHistogramInt(List<Object> allResult) {
        int valueInInt;
        Map<Integer, Integer> histogram = new HashMap<>();
        for (Object o : allResult) {
            valueInInt = ((Integer) o).intValue();
            if (!histogram.containsKey(valueInInt))
            {
                histogram.put(valueInInt,1);
            } else{
                histogram.put(valueInInt, histogram.get(valueInInt) + 1);
            }
        }

        for (Integer integer : histogram.keySet()) {
            System.out.println("value " + integer + ": " + histogram.get(integer));
        }
    }

    private static void displayByQuantity(DtoReviewOldSimulation entityTheUserChoose) {
        System.out.println("you choose to display by quantity");
        System.out.println("Entity name: " + entityTheUserChoose.getEntityDefinition().getEntityName());
        System.out.println("Population in the start of the simulation: " + entityTheUserChoose.getEntityDefinition().getStartPo());
        System.out.println("Population in the end of the simulation: " + entityTheUserChoose.getEntityDefinition().getEndPro());
    }

    private static Set<String> fetchPropertiesNames(List<DtoEntityInstanceToUi> entityInstanceList) {
        Set<String> nameOfProperties = new HashSet<>();
        for (DtoEntityInstanceToUi EntityInstance : entityInstanceList) {
            for (String nameOfProperty : EntityInstance.getNameOfProperty2Value().keySet()) {
                nameOfProperties.add(nameOfProperty);
            }
        }
        return nameOfProperties;
    }

    private static void displayMapToUser(Map<Integer, String> oldSimulationsMap) {
        for (Integer integer : oldSimulationsMap.keySet()) {
            System.out.println(integer + ") " + oldSimulationsMap.get(integer));
        }
    }

    private static void printCurrentSimulationPreview(DtoResponsePreview simulationPreview){
//        DtoResponseEntities allEntitiesDto = simulationPreview.getDtoResponseEntities();
//        System.out.println("Entity: ");
//        System.out.println("--------");
//        System.out.println("   Entity name: " + allEntitiesDto.getEntityName());
//        System.out.println("   Entity population: " + allEntitiesDto.getPopulation());
//        System.out.println("Properties for entity: ");
//        System.out.println("-----------------------");
//        for (PropertyDefinitionEntity propertyDef: allEntitiesDto.getPropertyDefinitionEntityList()) {
//            System.out.println("        Property name: " + propertyDef.getPropertyDefinition().getPropertyName());
//            System.out.println("        Property type:" + propertyDef.getPropertyDefinition().getPropertyType());
//            if(propertyDef.getPropertyDefinition().getPropertyRange() != null) {
//                System.out.println("        Property range:" + propertyDef.getPropertyDefinition().getPropertyRange().getFrom() + " to " + propertyDef.getPropertyDefinition().getPropertyRange().getTo());
//            }
//            System.out.println("        Property randomly initialized:" + propertyDef.getPropValue().getRandomInit());
//            System.out.println();
//        }
//
//        System.out.println("Rules:");
//        System.out.println("-------");
//        for(DtoResponseRules currRule: simulationPreview.getDtoResponseRules()){
//            System.out.println("   Rule name: " + currRule.getRuleName());
//            System.out.println("   Rule activition: " + currRule.getTicks() + " ticks, " + currRule.getProbability() + " probability");
//            System.out.println("   Action count: " + currRule.getCountOfAction());
//            System.out.println("   Actions:");
//            for(String actionName: currRule.getActionNames()){
//                System.out.println("        Action name: " + actionName);
//            }
//            System.out.println();
//        }
//
//        System.out.println("Simulation termination:");
//        System.out.println("------------------------");
//        System.out.println("   Seconds: " + simulationPreview.getDtoResponseTermination().getSeconds());
//        System.out.println("   Ticks: " + simulationPreview.getDtoResponseTermination().getTicks());
    }

    private static DtoUiToEngine printAndValidateEnvironments(Map<String, EnvironmentDefinition> environmentDefinitions, InterfaceMenu interfaceMenu) {
        Map<String, Object> environmentList = new HashMap<>();

        int userChoiceEnv;
        Scanner scanner = new Scanner(System.in);
        Map<Integer, String> numberToEnvironmentMap = createNumberToEnvironmentMap(environmentDefinitions);
        Map<Integer, Boolean> idToIsInitializedByUser = createIdToIsInitializedMap(numberToEnvironmentMap);

        printAllEnvironmentsAvailable(numberToEnvironmentMap);

        System.out.println("Out of the following environment variables, please choose the one that you would like to initialize by yourself, or press " + (numberToEnvironmentMap.size() + 1) + " to continue");
        userChoiceEnv = getUserChoice(1, numberToEnvironmentMap.size() + 1);
        //handling user input and initializing environments from user input
        int mapSize = numberToEnvironmentMap.size() + 1;
        while (userChoiceEnv != mapSize) {

            while (!validateWrongUserInput(userChoiceEnv, numberToEnvironmentMap.size() + 1)) {
                System.out.println("Invalid choice. Please choose again.");
                userChoiceEnv = getUserChoice(1, mapSize);
            }

            String environmentNameFromUser = numberToEnvironmentMap.get(userChoiceEnv);
            EnvironmentDefinition currEnvDef = environmentDefinitions.get(environmentNameFromUser);
            PropertyDefinition currPropDef = currEnvDef.getEnvPropertyDefinition();
            presentPropertyDetails(currPropDef);
            System.out.println("Please state your init value for current environment: " + environmentNameFromUser);
            String userInput = scanner.next();
            while (!isEnvInputValid(userInput, currPropDef, interfaceMenu)) {
                System.out.println("User input for environment variable is invalid. Please try again. ");
                System.out.println("Enter your choice: ");
                userInput = scanner.next();
            }
            idToIsInitializedByUser.put(userChoiceEnv, true);
            Object initVal = interfaceMenu.initializeValueFromUserInput(userInput, currPropDef);
            environmentList.put(environmentNameFromUser, initVal);
            printAllEnvironmentsAvailable(numberToEnvironmentMap);

            System.out.println("Out of the following environment variables, please choose the one that you would like to initialize by yourself, or press " + mapSize + " to continue");
            userChoiceEnv = scanner.nextInt();
        }

        //iterating over whatever environment definitions we have left, and random initializing each one
        for(Integer id: idToIsInitializedByUser.keySet()){
            if(idToIsInitializedByUser.get(id) == false) {
                String envName = numberToEnvironmentMap.get(id);
                EnvironmentDefinition envDef = environmentDefinitions.get(envName);
                Object initVal = interfaceMenu.initializeRandomEnvironmentValues(envDef.getEnvPropertyDefinition());
                environmentList.put(envName, initVal);
            }
        }

        DtoUiToEngine allEnvInstances = new DtoUiToEngine(environmentList,100,100);
        printEnvValueToUser(allEnvInstances);

        return allEnvInstances;
    }

    private static void printEnvValueToUser(DtoUiToEngine allEnvInstances) {
        Object value;
        for (String key : allEnvInstances.getEnvironmentToValue().keySet()) {
            value = allEnvInstances.getEnvironmentToValue().get(key);

            if (value instanceof Integer)
            {
                int valueInInt = ((Integer) value).intValue();
                System.out.println("The value of "+key+": "+valueInInt);
            }
            if (value instanceof Float)
            {
                float valueInFloat = ((Float) value).floatValue();
                System.out.println("The value of "+key+": "+valueInFloat);
            }
            if (value instanceof Boolean)
            {
                boolean valueInBool = ((Boolean) value).booleanValue();
                System.out.println("The value of "+key+": "+valueInBool);
            }
            if (value instanceof String)
            {
                String valueInString = ((String) value).toString();
                System.out.println("The value of "+key+": "+valueInString);
            }
        }
    }

    private static Map<Integer, Boolean> createIdToIsInitializedMap(Map<Integer, String> numberToEnvironmentMap) {
        Map<Integer, Boolean> idToIsInit = new HashMap<>();

        for(Integer currId: numberToEnvironmentMap.keySet()){
            idToIsInit.put(currId, false);
        }

        return idToIsInit;
    }

    private static Map<Integer, String> updateNumbersMapAfterRemoving(Map<Integer, String> oldNumberToEnv){
        Map<Integer, String> newNumberToEnv = new HashMap<>();
        int currNumber = 1;

        for(Integer currId: oldNumberToEnv.keySet()){
            newNumberToEnv.put(currNumber, oldNumberToEnv.get(currId));
            currNumber++;
        }

        return newNumberToEnv;
    }

//            if(!envHandleUserInput(currPropertyDef, interfaceMenu, currEnvironmentDef)){//environment is ranomly initialized
//               Object initVal = interfaceMenu.initializeRandomEnvironmentValues(currEnvironmentDef, currPropertyDef);
////               DtoEnvUiToEngine currEnvIns = new DtoEnvUiToEngine(currEnvironmentDef, initVal);
//               environmentList.put(environmentName, initVal);
//            }
//            else{//evironmet is not randomly initialized
//                System.out.println("Enter your choice: ");
//                userInput = scanner.nextLine();
//                while(!isEnvInputValid(userInput, currPropertyDef, interfaceMenu)){
//
//                    System.out.println("User input for environment variable is invalid. Please try again. ");
//                    System.out.println("Enter your choice: ");
//                    userInput = scanner.nextLine();
//                }
//
//                Object initVal = interfaceMenu.initializeValueFromUserInput(userInput, currPropertyDef);
//                DtoEnvUiToEngine currEnvIns = new DtoEnvUiToEngine(currEnvironmentDef, initVal);
//                environmentList.put(environmentName, currEnvIns);



    private static Map<Integer, String> createNumberToEnvironmentMap(Map<String, EnvironmentDefinition> envDefs) {
        Map<Integer, String> numberToEnv = new HashMap<>();
        int counter = 1;

        for(String envName: envDefs.keySet()) {
            numberToEnv.put(counter, envName);
            counter++;
        }

        return numberToEnv;
    }

    private static void printAllEnvironmentsAvailable(Map<Integer, String> allEnv) {
        System.out.println("Environment variables available:");
        for(Integer currId: allEnv.keySet()){
            System.out.println("    " + currId + ": Environment name: " + allEnv.get(currId));
        }
    }




    private static boolean validateWrongUserInput(int userChoice, int envListSize) {
        return userChoice >= 1 && userChoice <= envListSize;
    }

    private static void presentPropertyDetails(PropertyDefinition propDef){
        System.out.println("Name: " + propDef.getPropertyName());
        System.out.println("Property: " + propDef.getPropertyType());
        if (propDef.getPropertyRange() != null){
            System.out.println("Range: " + propDef.getPropertyRange());
        }

    }

    private static boolean isEnvInputValid(String userInput, PropertyDefinition propDef, InterfaceMenu interfaceMenu) {
        return interfaceMenu.validateUserEnvInput(userInput, propDef);
    }


    private static String getFileXmlPath() {
        System.out.println("Please enter path for the wanted Xml file");
        return (new Scanner(System.in)).nextLine();
    }


    private static int getUserChoice(int from, int to){
        int userChoice, counter = 0;
        do {
            if (counter != 0)
            {
                System.out.println(String.format("Please choose number between %d to %d",from,to));
            }
            //printMenu();
            try {
                Scanner scanner = new Scanner(System.in);
                userChoice = scanner.nextInt();
            }
            catch (InputMismatchException e) {
                userChoice = -1;
            }
            counter++;

        }while(!(from <= userChoice && userChoice <= to)); // if we do the bonus need to be 8

        return userChoice;
    }



    private static void printMenu() {
        System.out.println("-----------------------------------------------");
        System.out.println("1. Load XML file");
        System.out.println("2. Show simulation information");
        System.out.println("3. Start simulation");
        System.out.println("4. Show previous simulation");
        System.out.println("5. Exit");
        System.out.println("6. Save current system state");
        System.out.println("7. Load system from file");
        System.out.println("-----------------------------------------------");
    }
}
