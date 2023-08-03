package entity;

import exceptions.GeneralException;
import property.PropertyInstance;

import java.util.HashMap;
import java.util.Map;

public class EntityInstance {

    private Map<String, PropertyInstance> allProperties;

    private EntityDefinition definitionOfEntity;

    public EntityInstance(EntityDefinition definitionOfEntity) {
        this.allProperties = new HashMap<>();
        this.definitionOfEntity = definitionOfEntity;
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

    public void addProperty(PropertyInstance propertyToAdd) throws GeneralException {

        String propertyName = propertyToAdd.getPropertyDefinition().getPropertyName();

        if(allProperties.containsKey(propertyName)){
            throw new GeneralException(String.format("Property name %s already exists in entity %s.", propertyName, this.definitionOfEntity.getEntityName()));
        }

        this.allProperties.put(propertyName, propertyToAdd);
    }
//    @Override
//    public String toString() {
//        StringBuilder res = new StringBuilder();
//
//        for(String key: allProperties.keySet()){
//            res.append(key).append(": ");
//            String value = allProperties.get(key).getPropValue().toString();
//            res.append(value);
//            res.append("\n");
//        }
//        for(PropertyForEntity currentProperty: allProperties){
//            result.append("Propery #" + propCounter + "\n");
//            result.append(currentProperty.toString());
//            propCounter++;
//        }
//
//        return result.toString();
//    }
}
