package dto;

import property.PropertyDefinitionEntity;
import java.util.List;

public class DtoResponseEntities {

    private String entityName;
    private int Population;

    private List<PropertyDefinitionEntity> propertyDefinitionEntityList;

    public DtoResponseEntities(String entityName, int population, List<PropertyDefinitionEntity> propertyDefinitionEntityList) {
        this.entityName = entityName;
        Population = population;
        this.propertyDefinitionEntityList = propertyDefinitionEntityList;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public int getPopulation() {
        return Population;
    }

    public void setPopulation(int population) {
        Population = population;
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
                "\r\nPopulation: " + Population +
                "\r\nproperties: " + str.toString();
    }
}
