package dto;

import environment.EnvironmentDefinition;

import java.util.Map;

public class DtoEnvironments {
    private Map<String, EnvironmentDefinition> environmentDefinitions;

    public DtoEnvironments(Map<String, EnvironmentDefinition> environmentDefinitions) {
        this.environmentDefinitions = environmentDefinitions;
    }

    public Map<String, EnvironmentDefinition> getEnvironmentDefinitions() {
        return environmentDefinitions;
    }
}