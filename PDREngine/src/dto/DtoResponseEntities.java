package dto;

import property.PropertyDefinitionEntity;
import java.util.List;

public class DtoResponseEntities {

    private String entityName;

    private List<PropertyDefinitionEntity> propertyDefinitionEntityList;

    public DtoResponseEntities(String entityName, List<PropertyDefinitionEntity> propertyDefinitionEntityList) {
        this.entityName = entityName;
        this.propertyDefinitionEntityList = propertyDefinitionEntityList;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public List<PropertyDefinitionEntity> getPropertyDefinitionEntityList() {
        return propertyDefinitionEntityList;
    }

    public void setPropertyDefinitionEntityList(List<PropertyDefinitionEntity> propertyDefinitionEntityList) {
        this.propertyDefinitionEntityList = propertyDefinitionEntityList;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (PropertyDefinitionEntity propertyDefinitionEntity:
        propertyDefinitionEntityList) {
            str.append("\r\n").append(propertyDefinitionEntity.toString());
        }
        return "\r\nName:'" + entityName +
                "\r\nproperties: " + str.toString();
    }
}
