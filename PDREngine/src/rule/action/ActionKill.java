package rule.action;

import entity.EntityDefinition;
import entity.SecondEntity;
import enums.Operation;
import exceptions.GeneralException;
import necessaryVariables.NecessaryVariablesImpl;

import java.io.Serializable;

public class ActionKill extends AbstractAction implements Serializable {

    private String entityName;

    public ActionKill(EntityDefinition entityDefinition, String entityName, SecondEntity secondEntity) {
        super(Operation.KILL, entityDefinition, secondEntity);
        this.entityName = entityName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    @Override
    public void invoke(NecessaryVariablesImpl context) throws GeneralException {
        context.removeEntity(context.getPrimaryEntityInstance());
    }

    @Override
    public Operation getOperationType() {
        return Operation.KILL;
    }

    @Override
    public EntityDefinition getContextEntity() {
        return super.getEntityDefinition();
    }
}
