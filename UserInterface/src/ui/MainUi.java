package ui;

import constans.Constans;
import dto.DtoResponse;
import engine.MainEngine;

import java.util.InputMismatchException;
import java.util.Scanner;

import static tests.TestClass.createWorld;

public class MainUi {
    public static void main(String[] args) {

        boolean userLoadFile = true;
        MainEngine engine = new MainEngine();
        System.out.println("Hello and welcome to the simulation runner app.");
        System.out.println("-----------------------------------------------");
        int userChoice = getUserChoice();
        while (userChoice != 5) {
            if ( 1 < userChoice && userChoice < 5)
            {
                if (userLoadFile) {
                    switchUserChoice(engine, userChoice);
                }
                else {
                    System.out.println("Cannot do current action before loading XML file. Need to load XML file first.");
                }
            }

            if (userChoice == 1) {
//                DtoResponse response = engine.parseXmlToSimulation(getFileXmlPath());
//               //only if we succeeded loiading xml file.
//                if (response.getResponse().equals(Constans.SUCCEED_LOAD_FILE))
//                {
//                    userLoadFile = true;
//                }
//                System.out.println(response.getResponse());
                createWorld(engine);
                System.out.println("create simulation");
            }


            userChoice = getUserChoice();
        }



        //main loop
        //user chose 1 read xml, call function

    }

    private static void switchUserChoice(MainEngine engine, int userChoice) {
            switch (userChoice){
                case 2:
                    System.out.println(engine.printCurrentWorld());
                    break;
                case 3:
                    //run simulation
                    engine.moveWorld();
                    System.out.println("current world are moved");
                    break;
                case 4:
                    System.out.println(engine.getOldSimulationsInfo());
                    //present simulation from the past
                    break;

            }
    }


    private static String getFileXmlPath() {
        System.out.println("Please enter path for the wanted Xml file");
        return (new Scanner(System.in)).nextLine();
    }


    private static int getUserChoice(){
        int userChoice, counter = 0;

        do {
            if (counter != 0)
            {
                System.out.println("please choose number between 1 to 5");
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

        }while(!(0 < userChoice && userChoice < 6)); // if we do the bonus need to be 8

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
