package interfaces;

import dto.*;
import environment.EnvironmentDefinition;
import property.PropertyDefinition;
import simulationmanager.SimulationExecutionerManager;
import world.GeneralInformation;

import java.util.List;

public interface InterfaceMenu {

    // func 1
    DtoResponse createWorldDefinition(String xmlPath);
    //func 2
    DtoResponsePreview showCurrentSimulation();
    //func 3
//    DtoResponseSimulationEnded runSimulations(DtoUiToEngine envInputFromUser);
    //func 4
//    DtoOldSimulationsMap getMapOfOldSimulation();
    //send environments
    DtoEnvironments sendEnvironmentsToUser();
    DtoSimulationDetails fetchChosenWorld(int userSimulationChoice);

    Object initializeRandomEnvironmentValues(PropertyDefinition propertyDef);

    boolean validateUserEnvInput(String userInput, PropertyDefinition propDef);

    Object initializeValueFromUserInput(String userInput, PropertyDefinition propDef);
    SimulationExecutionerManager getExecutionsManager();

//    DtoResponse saveWorldState(String stringPath);
//
//    DtoResponse loadWorldState(String stringPath);
    void executeSimulation(DtoUiToEngine envInputFromUser);

    DtoSimulationDetails getSimulationById(int idOfCurrentSimulation);

}
