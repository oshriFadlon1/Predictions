package dto;

import entity.EntityDefinition;
import entity.EntityInstance;
import property.PropertyDefinition;
import property.PropertyDefinitionEntity;
import property.Value;
import range.Range;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DtoReviewOldSimulation {
    private DtoEntityDefitionToDisplay entityDefinition;
    private List<DtoEntityInstanceToUi> entityInstanceList;

    public DtoReviewOldSimulation(EntityDefinition entityDefinition, List<EntityInstance> entityInstanceList) {
        //this.entityDefinition = createCopyEntityDefition(entityDefinition);
        this.entityInstanceList = createCopysOfAllEntitiesInstances(entityInstanceList);
    }

    public DtoEntityDefitionToDisplay getEntityDefinition() {
        return entityDefinition;
    }

    public void setEntityDefinition(DtoEntityDefitionToDisplay entityDefinition) {
        this.entityDefinition = entityDefinition;
    }

    public List<DtoEntityInstanceToUi> getEntityInstanceList() {
        return entityInstanceList;
    }

    public void setEntityInstanceList(List<DtoEntityInstanceToUi> entityInstanceList) {
        this.entityInstanceList = entityInstanceList;
    }

//    private DtoEntityDefitionToDisplay createCopyEntityDefition(EntityDefinition entityDefinition) {
//        String Name = entityDefinition.getEntityName();
//        int startPopulation = entityDefinition.getStartPopulation();
//        int endPopulation = entityDefinition.getEndPopulation();
//        Map<String, PropertyDefinitionEntity> propertyDefinitions = new HashMap<>();
//        for (String propertyName: entityDefinition.getPropertyDefinition().keySet()) {
//            PropertyDefinitionEntity propertyDefinitionEntity = entityDefinition.getPropertyDefinition().get(propertyName);
//            Range range = propertyDefinitionEntity.getPropertyDefinition().getPropertyRange();
//            if (range != null)
//            {
//                propertyDefinitions.put(propertyName,
//                        new PropertyDefinitionEntity(new PropertyDefinition(propertyDefinitionEntity.getPropertyDefinition().getPropertyName(),
//                                propertyDefinitionEntity.getPropertyDefinition().getPropertyType(),
//                                range),
//                                new Value(propertyDefinitionEntity.getPropValue().getRandomInit(),
//                                        propertyDefinitionEntity.getPropValue().getInit())));
//            } else {
//                propertyDefinitions.put(propertyName,
//                        new PropertyDefinitionEntity(new PropertyDefinition(propertyDefinitionEntity.getPropertyDefinition().getPropertyName(),
//                                propertyDefinitionEntity.getPropertyDefinition().getPropertyType()),
//                                new Value(propertyDefinitionEntity.getPropValue().getRandomInit(),
//                                        propertyDefinitionEntity.getPropValue().getInit())));
//            }
//
//        }
//
//        return new DtoEntityDefitionToDisplay(Name, startPopulation, endPopulation, propertyDefinitions);
//    }

    private List<DtoEntityInstanceToUi> createCopysOfAllEntitiesInstances(List<EntityInstance> entityInstanceList) {
        List<DtoEntityInstanceToUi> copyOfEntityInstances = new ArrayList<>();
        for (EntityInstance entityInstance : entityInstanceList) {
            Map<String, Object> propertyInstanceCopy = new HashMap<>();
            for (String propertyName : entityInstance.getAllProperties().keySet()) {
                String propertyType = entityInstance.getAllProperties().get(propertyName).getPropertyDefinition().getPropertyType();
                Object propertyValue = createCopyOfValueProperty(entityInstance.getAllProperties().get(propertyName).getPropValue(), propertyType);
                propertyInstanceCopy.put(propertyName, propertyValue);
            }

            copyOfEntityInstances.add(new DtoEntityInstanceToUi(entityInstance.getId(),
                    propertyInstanceCopy));
        }
        return copyOfEntityInstances;
    }

    private Object createCopyOfValueProperty(Object propValue, String propertyType) {
        Object returnVlaue = null;
        switch (propertyType.toLowerCase()){
            case "decimal":
                int intValue = (Integer) propValue;
                int newIntValue = intValue;
                returnVlaue = newIntValue;
                break;
            case "float":
                float floatValue = (Float) propValue;
                float newFloatValue = floatValue;
                returnVlaue = newFloatValue;
                break;
            case "boolean":
                boolean booleanValue = (Boolean) propValue;
                boolean newBooleanValue = booleanValue;
                returnVlaue = newBooleanValue;
                break;
            case "string":
                String stringValue = (String) propValue;
                String newStringValue = stringValue;
                returnVlaue = newStringValue;
                break;
        }
        return returnVlaue;
    }


}
