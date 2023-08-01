package rule.action;

import entity.Entity;
import enums.Operation;
import property.PropertyForEntity;

public class Action {
    private Entity OperationOnEntity;

    private Operation operationType;

    //private PropertyForEntity propertyForEntity;

//    public Action(Entity operationOnEntity, Operation operationType, PropertyForEntity propertyForEntity) {
//        OperationOnEntity = operationOnEntity;
//        this.operationType = operationType;
//        this.propertyForEntity = propertyForEntity;
//    }
//
//    public Entity getOperationOnEntity() {
//        return OperationOnEntity;
//    }
//
//    public void setOperationOnEntity(Entity operationOnEntity) {
//        OperationOnEntity = operationOnEntity;
//    }
//
//
//    public PropertyForEntity getPropertyForEntity() {
//        return propertyForEntity;
//    }
//
//    public void setPropertyForEntity(PropertyForEntity propertyForEntity) {
//        this.propertyForEntity = propertyForEntity;
//    }
//
//    public Operation getOperationType() {
//        return operationType;
//    }
//
//    public void setOperationType(Operation operationType) {
//        this.operationType = operationType;
//    }
//
//    @Override
//    public String toString() {
//        return "Action{" +
//                "OperationOnEntity=" + OperationOnEntity +
//                ", operationType=" + operationType +
//                ", propertyForEntity=" + propertyForEntity +
//                '}';
//    }


    public Action(Operation operationType) {
        this.operationType = operationType;
    }

    public Operation getOperationType() {
        return operationType;
    }

    public void setOperationType(Operation operationType) {
        this.operationType = operationType;
    }
}