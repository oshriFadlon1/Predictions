package entity;

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

    public List<PropertyForEntity> getProprties() {
        return properties;
    }

    public void setProprties(List<PropertyForEntity> proprties) {
        this.properties = proprties;
    }
}
