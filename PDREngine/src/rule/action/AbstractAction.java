package rule.action;

import entity.EntityDefinition;
import enums.Operation;
import exceptions.GeneralException;
import necessaryVariables.NecessaryVariablesImpl;

import java.io.Serializable;

public abstract  class AbstractAction implements IAction, Serializable {
    private final Operation operationType;
    private final EntityDefinition entityDefinition;

    public AbstractAction(Operation operationType, EntityDefinition entityDefinition) {
        this.operationType = operationType;
        this.entityDefinition = entityDefinition;
    }

    public Operation getOperationType() {
        return this.operationType;
    }

    public EntityDefinition getEntityDefinition() {
        return entityDefinition;
    }

    public abstract void invoke(NecessaryVariablesImpl context) throws GeneralException;
}
