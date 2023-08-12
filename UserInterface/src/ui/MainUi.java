package ui;

import dto.*;
import engine.MainEngine;
import entity.EntityDefinition;
import environment.EnvironmentDefinition;
import environment.EnvironmentInstance;
import interfaces.InterfaceMenu;
import property.PropertyDefinition;
import property.PropertyDefinitionEntity;

import java.util.*;

public class MainUi {

    public static void main(String[] args) {
        InterfaceMenu interfaceMenu = new MainEngine();
        boolean userLoadFile = false;

        String xmlPath;

        System.out.println("Hello and welcome to the simulation runner app.");
        System.out.println("-----------------------------------------------");
        int userChoice = getUserChoice(1,5);
        while (userChoice != 5) {
            if ( 1 < userChoice && userChoice < 5)
            {
                if (userLoadFile) {
                    switchUserChoice(userChoice, interfaceMenu);
                }
                else {
                    System.out.println("Cannot do current action before loading XML file. Need to load XML file first.");
                }
            }

            if (userChoice == 1) {
                xmlPath = getFileXmlPath();
                System.out.println(interfaceMenu.createWorldDefinition(xmlPath));
                userLoadFile = true;
            }
            userChoice = getUserChoice(1,5);
        }

    }

    private static void switchUserChoice(int userChoice, InterfaceMenu interfaceMenu) {
            switch (userChoice){
                case 2:DtoResponsePreview previewOfSimulation = interfaceMenu.showCurrentSimulation();
                    printCurrentSimulationPreview(previewOfSimulation);
                    //System.out.println(interfaceMenu.showCurrentSimulation().toString());
                    break;
                case 3:
                    //run simulation
                    DtoEnvironments environmentsAvailable = interfaceMenu.sendEnvironmentsToUser();
                    Map<String, Object> environmentsForEngine = printAndValidateEnvironments(environmentsAvailable.getEnvironmentDefinitions(),interfaceMenu);
                    DtoResponseSimulationEnded dtoResponseSimulationEnded = interfaceMenu.runSimulations(environmentsForEngine);
                    //engineController.moveWorld();
                    System.out.println("current world is moved");
                    break;
                    case 4:
//                    DtoOldSimulationResponse response = interfaceMenu.getAllOldSimulationsInfo();
//                    System.out.println(response.getAllInfoSimulation());
//                    if(response.getNumberOfSimulations() == 0){
//                        System.out.println("No simulations to show.");
//                    }
//                    else{
//                        System.out.println(String.format("Please choose from the list above a simulation number between 1 to %d." ,response.getNumberOfSimulations()));
//                        int userSimulationChoice = getUserChoice(1,response.getNumberOfSimulations() );
//                        System.out.println("Please choose from the following display options: \n1. By quantity.\n2. By histogram");
//                        int userSimulationDisplayChoice = getUserChoice(1,2);
//                        interfaceMenu.printPastSimulation(userSimulationChoice,userSimulationDisplayChoice);
//                    }

                    //if there are no simulations existing, then write that there are no simulation and go on.
                    //we will ask for an input from the user in order to select the simulation that he wants to present
                    // simulation from the past
                    break;

            }
    }

    private static void printCurrentSimulationPreview(DtoResponsePreview simulationPreview){
        DtoResponseEntities allEntitiesDto = simulationPreview.getDtoResponseEntities();
        System.out.println("Entity:");
        System.out.println("   Entity name: " + allEntitiesDto.getEntityName());
        System.out.println("   Entity population: " + allEntitiesDto.getPopulation());
        System.out.println("Properties for entity");
        for (PropertyDefinitionEntity propertyDef: allEntitiesDto.getPropertyDefinitionEntityList()) {
            System.out.println("        Property name: " + propertyDef.getPropertyDefinition().getPropertyName());
            System.out.println("        Property type:" + propertyDef.getPropertyDefinition().getPropertyType());
            if(propertyDef.getPropertyDefinition().getPropertyRange() != null) {
                System.out.println("        Property range:" + propertyDef.getPropertyDefinition().getPropertyRange().getFrom() + " to " + propertyDef.getPropertyDefinition().getPropertyRange().getTo());
            }
            System.out.println("        Property randomly initialized:" + propertyDef.getPropValue().getRandomInit());
        }

        System.out.println("Rules");
        for(DtoResponseRules currRule: simulationPreview.getDtoResponseRules()){
            System.out.println("   Rule name: " + currRule.getRuleName());
            System.out.println("   Rule activition: " + currRule.getTicks() + " ticks, " + currRule.getProbability() + " probability");
            System.out.println("   Action count: " + currRule.getCountOfAction());
            System.out.println("   Actions:");
            for(String actionName: currRule.getActionNames()){
                System.out.println("        Action name: " + actionName);
            }
        }

        System.out.println("Simulation termination:");
        System.out.println("   Seconds: " + simulationPreview.getDtoResponseTermination().getSeconds());
        System.out.println("   Ticks: " + simulationPreview.getDtoResponseTermination().getTicks());
    }

    private static Map<String, Object> printAndValidateEnvironments(Map<String, EnvironmentDefinition> environmentDefinitions, InterfaceMenu interfaceMenu) {
        Map<String, Object> environmentList = new HashMap<>();
        String userInput;
        Scanner scanner = new Scanner(System.in);
        boolean isFirstTime = true;
        for (String environmentName: environmentDefinitions.keySet()){
            EnvironmentDefinition currEnvironmentDef = environmentDefinitions.get(environmentName);
            System.out.println("Environment name: " + environmentName);
            PropertyDefinition currPropertyDef = currEnvironmentDef.getEnvPropertyDefinition();
            System.out.println(currPropertyDef);

            if(!envHandleUserInput(currPropertyDef, interfaceMenu, currEnvironmentDef)){//environment is ranomly initialized
               Object initVal = interfaceMenu.initializeRandomEnvironmentValues(currEnvironmentDef, currPropertyDef);
               DtoEnvUiToEngine currEnvIns = new DtoEnvUiToEngine(currEnvironmentDef, initVal);
               environmentList.put(environmentName, currEnvIns);
            }
            else{//evironmet is not randomly initialized
                System.out.println("Enter your choice: ");
                userInput = scanner.nextLine();
                while(!isEnvInputValid(userInput, currPropertyDef, interfaceMenu)){

                    System.out.println("User input for environment variable is invalid. Please try again. ");
                    System.out.println("Enter your choice: ");
                    userInput = scanner.nextLine();
                }

                Object initVal = interfaceMenu.initializeValueFromUserInput(userInput, currPropertyDef);
                DtoEnvUiToEngine currEnvIns = new DtoEnvUiToEngine(currEnvironmentDef, initVal);
                environmentList.put(environmentName, currEnvIns);

            }
        }

        return environmentList;
    }

    private static boolean isEnvInputValid(String userInput, PropertyDefinition propDef, InterfaceMenu interfaceMenu) {
        return interfaceMenu.validateUserEnvInput(userInput, propDef);
    }

    private static boolean envHandleUserInput(PropertyDefinition propertyDef, InterfaceMenu interfaceMenu, EnvironmentDefinition currEnv) {
        EnvironmentInstance environmentFromUser = null;
        System.out.println("Would you like to insert a value for the following environment variable? y/n");
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();

        while(!userInput.equalsIgnoreCase("y") && !userInput.equalsIgnoreCase("n") ){
            System.out.println("Invalid input. Please enter y/n");
            userInput = scanner.nextLine();
        }

        if(userInput.equalsIgnoreCase("n")){
            return false;
        }

        return true;

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
            printMenu();
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
        System.out.println("-----------------------------------------------");
    }
}
