package interfaces;

import dto.DtoEnvironments;
import dto.DtoResponsePreview;
import dto.DtoResponseSimulationEnded;
import environment.EnvironmentDefinition;
import environment.EnvironmentInstance;
import property.PropertyDefinition;

import java.util.Map;

public interface InterfaceMenu {

    // func 1
    String createWorldDefinition(String xmlPath);
    //func 2
    DtoResponsePreview showCurrentSimulation();
    //func 3
    DtoResponseSimulationEnded runSimulations(Map<String, Object> environmentsForEngine);
    //func 4
    String printPastSimulation(int indexOfSimulation, int userDisplay);
    //send environments
    DtoEnvironments sendEnvironmentsToUser();

    Object initializeRandomEnvironmentValues(EnvironmentDefinition currEnv, PropertyDefinition propertyDef);

    boolean validateUserEnvInput(String userInput, PropertyDefinition propDef);

    Object initializeValueFromUserInput(String userInput, PropertyDefinition propDef);
}
