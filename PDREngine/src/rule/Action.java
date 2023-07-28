package rule;

import entity.Entity;
import property.PropertyForEntity;

public class Action {
    private Entity OperationOnEntity;

    private String typeOperation;

    private PropertyForEntity propertyForEntity;

    public Action(Entity operationOnEntity, String typeOperation, PropertyForEntity propertyForEntity) {
        OperationOnEntity = operationOnEntity;
        this.typeOperation = typeOperation;
        this.propertyForEntity = propertyForEntity;
    }

    public Entity getOperationOnEntity() {
        return OperationOnEntity;
    }

    public void setOperationOnEntity(Entity operationOnEntity) {
        OperationOnEntity = operationOnEntity;
    }

    public String getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(String typeOperation) {
        this.typeOperation = typeOperation;
    }

    public PropertyForEntity getPropertyForEntity() {
        return propertyForEntity;
    }

    public void setPropertyForEntity(PropertyForEntity propertyForEntity) {
        this.propertyForEntity = propertyForEntity;
    }
}