package entity;

import exceptions.GeneralException;
import pointCoord.PointCoord;
import property.PropertyInstance;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EntityInstance implements Serializable {

    private int Id;
    private Map<String, PropertyInstance> allProperties;
    private PointCoord positionInWorld;

    private EntityDefinition definitionOfEntity;

    public EntityInstance(EntityDefinition definitionOfEntity,int Id) {
        this.Id = Id;
        this.allProperties = new HashMap<>();
        this.definitionOfEntity = definitionOfEntity;
    }

    public EntityInstance(int id, PointCoord positionInWorld, EntityDefinition definitionOfEntity) {
        Id = id;
        this.positionInWorld = positionInWorld;
        this.definitionOfEntity = definitionOfEntity;
        this.allProperties = new HashMap<>();
    }


    public Map<String, PropertyInstance> getAllProperties() {
        return allProperties;
    }

    public void setAllProperties(Map<String, PropertyInstance> allProperties) {
        this.allProperties = allProperties;
    }

    public EntityDefinition getDefinitionOfEntity() {
        return definitionOfEntity;
    }

    public void setDefinitionOfEntity(EntityDefinition definitionOfEntity) {
        this.definitionOfEntity = definitionOfEntity;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public PropertyInstance getPropertyByName(String name)
    {
        return allProperties.get(name);
    }

    public void addProperty(PropertyInstance propertyToAdd){ //throws GeneralException {

        String propertyName = propertyToAdd.getPropertyDefinition().getPropertyName();

        this.allProperties.put(propertyName, propertyToAdd);
    }

    public PointCoord getPositionInWorld() {
        return positionInWorld;
    }

    public void setPositionInWorld(PointCoord positionInWorld) {
        this.positionInWorld = positionInWorld;
    }
}
