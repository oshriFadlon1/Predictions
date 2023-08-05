package rule.action;

import entity.EntityInstance;
import enums.Operation;

public abstract  class Action {
    private Operation operationType;

    public Action(String operationType) {
        this.operationType = Operation.valueOf(operationType.toUpperCase());
    }

    public Operation getOperationType() {
        return operationType;
    }

    public void setOperationType(Operation operationType) {
        this.operationType = operationType;
    }

    public abstract void Invoke(EntityInstance entityInstance);
}
