package dto;

import environment.EnvironmentDefinition;
import property.PropertyDefinition;
import property.PropertyDefinitionEntity;
import range.Range;

import java.util.HashMap;
import java.util.Map;

public class DtoEnvironments {
    private Map<String, EnvironmentDefinition> environmentDefinitions;

    public DtoEnvironments(Map<String, EnvironmentDefinition> environmentDefinitions) {
        this.environmentDefinitions = new HashMap<>();
        PropertyDefinition newPropDef = null;
        for(String environmentName: environmentDefinitions.keySet()){
            EnvironmentDefinition currEnvDef = environmentDefinitions.get(environmentName);
            PropertyDefinition propertyDefinition = currEnvDef.getEnvPropertyDefinition();
            if (propertyDefinition.getPropertyType().equalsIgnoreCase("decimal") || propertyDefinition.getPropertyType().equalsIgnoreCase("float")){
                newPropDef = new PropertyDefinition(propertyDefinition.getPropertyName(), propertyDefinition.getPropertyType(), new Range(propertyDefinition.getPropertyRange().getFrom(), propertyDefinition.getPropertyRange().getTo()));
            }
            else{
                newPropDef = new PropertyDefinition(propertyDefinition.getPropertyName(), propertyDefinition.getPropertyType());
            }
            EnvironmentDefinition newEnvDef = new EnvironmentDefinition();
            newEnvDef.setEnvPropertyDefinition(newPropDef);
            this.environmentDefinitions.put(environmentName, newEnvDef);
        }
    }

    public Map<String, EnvironmentDefinition> getEnvironmentDefinitions() {
        return environmentDefinitions;
    }
}