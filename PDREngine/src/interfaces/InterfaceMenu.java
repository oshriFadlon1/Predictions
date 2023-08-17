package interfaces;

import dto.*;
import environment.EnvironmentDefinition;
import environment.EnvironmentInstance;
import property.PropertyDefinition;
import world.WorldInstance;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface InterfaceMenu {

    // func 1
    DtoResponse createWorldDefinition(String xmlPath);
    //func 2
    DtoResponsePreview showCurrentSimulation();
    //func 3
    DtoResponseSimulationEnded runSimulations(DtoEnvUiToEngine envInputFromUser);
    //func 4
    DtoOldSimulationsMap getMapOfOldSimulation();
    //send environments
    DtoEnvironments sendEnvironmentsToUser();
    List<DtoReviewOldSimulation> fetchChosenWorld(int userSimulationChoice);

    Object initializeRandomEnvironmentValues(EnvironmentDefinition currEnv, PropertyDefinition propertyDef);

    boolean validateUserEnvInput(String userInput, PropertyDefinition propDef);

    Object initializeValueFromUserInput(String userInput, PropertyDefinition propDef);

    DtoResponse saveWorldState(String stringPath);

    DtoResponse loadWorldState(String stringPath);

}
