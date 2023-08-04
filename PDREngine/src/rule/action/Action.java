package rule.action;

import entity.EntityInstance;
import enums.Operation;

public abstract  class Action {
    private Operation operationType;

    public Action(Operation operationType) {
        this.operationType = operationType;
    }

    public Operation getOperationType() {
        return operationType;
    }

    public void setOperationType(Operation operationType) {
        this.operationType = operationType;
    }

    public abstract void Invoke(EntityInstance entityInstance);
}
