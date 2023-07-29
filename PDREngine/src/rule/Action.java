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


    public PropertyForEntity getPropertyForEntity() {
        return propertyForEntity;
    }

    public void setPropertyForEntity(PropertyForEntity propertyForEntity) {
        this.propertyForEntity = propertyForEntity;
    }

    public Operation getOperationType() {
        return operationType;
    }

    public void setOperationType(Operation operationType) {
        this.operationType = operationType;
    }

    @Override
    public String toString() {
        return "Action{" +
                "OperationOnEntity=" + OperationOnEntity +
                ", operationType=" + operationType +
                ", propertyForEntity=" + propertyForEntity +
                '}';
    }
}