package entity;

import exceptions.GeneralException;
import property.PropertyForEntity;

import java.util.ArrayList;
import java.util.List;

public class Entity {
    private String entityName;
    private int population;
    private List<PropertyForEntity> properties;

    public Entity(String name, int population) {
        this.entityName = name;

        this.population = population;
        this.properties = new ArrayList<PropertyForEntity>();
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public List<PropertyForEntity> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyForEntity> properties) {
        this.properties = properties;
    }

    public void addProperty(PropertyForEntity propertyToAdd) throws GeneralException{

        for (PropertyForEntity property: this.properties) {
            if (property.getPropertyName().equals(propertyToAdd.getPropertyName()))
            {
                throw new GeneralException("Property name already exsits for entity " + this.entityName);
            }
        }
        this.properties.add(propertyToAdd);
    }
    @Override
    public String toString() {
        return "Name: " + entityName +
                "\npopulation: " + population +
                "\nproperties: " + allProperties() ;
    }

    private String allProperties() {
        StringBuilder str = new StringBuilder();
        for (PropertyForEntity property: properties) {
            str.append("\nproperty for entity:");
            str.append(property.toString());
        }

        return str.toString();
    }
}
