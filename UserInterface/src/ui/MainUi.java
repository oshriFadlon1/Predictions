package ui;

import dto.DtoOldSimulationResponse;
import engineContorller.EngineController;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MainUi {

    public static void main(String[] args) {
        EngineController engineController = new EngineController();
        boolean userLoadFile = true;

        String xmlPath;

        System.out.println("Hello and welcome to the simulation runner app.");
        System.out.println("-----------------------------------------------");
        int userChoice = getUserChoice(1,5);
        while (userChoice != 5) {
            if ( 1 < userChoice && userChoice < 5)
            {
                if (userLoadFile) {
                    switchUserChoice(userChoice, engineController);
                }
                else {
                    System.out.println("Cannot do current action before loading XML file. Need to load XML file first.");
                }
            }

            if (userChoice == 1) {
                xmlPath = getFileXmlPath();
                engineController.checkXmlFileValidation(xmlPath);

            }
            userChoice = getUserChoice(1,5);
        }

    }

    private static void switchUserChoice(int userChoice, EngineController engineController) {
            switch (userChoice){
                case 2:
                    System.out.println(engineController.showCurrentSimulation());
                    break;
                case 3:
                    //run simulation
                    //engineController.moveWorld();
                    System.out.println("current world are moved");
                    break;
                case 4:
                    DtoOldSimulationResponse response = engineController.getAllOldSimulationsInfo();
                    System.out.println(response.getAllInfoSimulation());
                    if(response.getNumberOfSimulations() == 0){
                        System.out.println("No simulations to show.");
                    }
                    else{
                        System.out.println(String.format("Please choose from the list above a simulation number between 1 to %d." ,response.getNumberOfSimulations()));
                        int userSimulationChoice = getUserChoice(1,response.getNumberOfSimulations() );
                        System.out.println("Please choose from the following display options: \n1. By quantity.\n2. By histogram");
                        int userSimulationDisplayChoice = getUserChoice(1,2);
                        engineController.printPastSimulation(userSimulationChoice,userSimulationDisplayChoice);
                    }

                    //if there are no simulations existing, then write that there are no simulation and go on.
                    //we will ask for an input from the user in order to select the simulation that he wants to present
                    // simulation from the past
                    break;

            }
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
        System.out.println("1. Load XML file");
        System.out.println("2. Show simulation information");
        System.out.println("3. Start simulation");
        System.out.println("4. Show previous simulation");
        System.out.println("5. Exit");
    }
}
