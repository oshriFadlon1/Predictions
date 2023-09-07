package entity;


import property.PropertyDefinitionEntity;

import java.io.Serializable;
import java.util.Map;

public class EntityDefinition implements Serializable {
    private final String entityName;
    private final Map<String, PropertyDefinitionEntity> propertyDefinition;

    public EntityDefinition(String entityName, Map<String, PropertyDefinitionEntity> propertyDefinition) {
        this.entityName = entityName;
        this.propertyDefinition = propertyDefinition;
    }

    public String getEntityName() {
        return entityName;
    }
    public Map<String, PropertyDefinitionEntity> getPropertyDefinition() {
        return propertyDefinition;
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
        return "Name: " + entityName;
    }
}
