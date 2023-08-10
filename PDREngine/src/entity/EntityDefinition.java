package entity;


import property.PropertyDefinitionEntity;

import java.util.Map;

public class EntityDefinition {
    private String entityName;
    private int startPopulation;
    private int endPopulation;
    private Map<String, PropertyDefinitionEntity> propertyDefinition;

    public EntityDefinition(String entityName, int startPopulation, Map<String, PropertyDefinitionEntity> propertyDefinition) {
        this.entityName = entityName;
        this.startPopulation = startPopulation;
        this.endPopulation = startPopulation;
        this.propertyDefinition = propertyDefinition;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public int getStartPopulation() {
        return startPopulation;
    }

    public int getEndPopulation() {
        return endPopulation;
    }

    public void setEndPopulation(int endPopulation) {
        this.endPopulation = endPopulation;
    }

    public void setStartPopulation(int startPopulation) {
        this.startPopulation = startPopulation;
    }

    public Map<String, PropertyDefinitionEntity> getPropertyDefinition() {
        return propertyDefinition;
    }

    public void setPropertyDefinition(Map<String, PropertyDefinitionEntity> propertyDefinition) {
        this.propertyDefinition = propertyDefinition;
    }

    public boolean isPropertyNameExist(String propertryName){
        for (String key : propertyDefinition.keySet()) {
            PropertyDefinitionEntity propertyDefinitionEntity = propertyDefinition.get(key);
            if (propertyDefinitionEntity.getPropertyDefinition().getPropertyName().equals(propertryName))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Name: " + entityName +
                "\npopulation: " + startPopulation;
    }
}
