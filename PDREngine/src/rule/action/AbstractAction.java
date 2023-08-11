package rule.action;

import entity.EntityDefinition;
import entity.EntityInstance;
import enums.Operation;
import exceptions.GeneralException;
import necessaryVariables.NecessaryVariablesImpl;

public abstract  class AbstractAction implements Action {
    private final Operation operationType;
    private final EntityDefinition entityDefinition;

    public AbstractAction(Operation operationType, EntityDefinition entityDefinition) {
        this.operationType = operationType;
        this.entityDefinition = entityDefinition;
    }

    public Operation getOperationType() {
        return operationType;
    }

    public EntityDefinition getEntityDefinition() {
        return entityDefinition;
    }

    public abstract void invoke(NecessaryVariablesImpl context) throws GeneralException;
}
