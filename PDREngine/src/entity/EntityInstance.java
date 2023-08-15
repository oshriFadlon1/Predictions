package entity;

import exceptions.GeneralException;
import property.PropertyInstance;

import java.util.HashMap;
import java.util.Map;

public class EntityInstance {

    private int Id;
    private Map<String, PropertyInstance> allProperties;

    private EntityDefinition definitionOfEntity;

    public EntityInstance(EntityDefinition definitionOfEntity,int Id) {
        this.Id = Id;
        this.allProperties = new HashMap<>();
        this.definitionOfEntity = definitionOfEntity;
    }

    public EntityInstance(int id, Map<String, PropertyInstance> allProperties) {
        Id = id;
        this.allProperties = allProperties;
        this.definitionOfEntity = null;
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

//        if(allProperties.containsKey(propertyName)){
//            throw new GeneralException(String.format("Property name %s already exists in entity %s.", propertyName, this.definitionOfEntity.getEntityName()));
//        }

        this.allProperties.put(propertyName, propertyToAdd);
    }

}
