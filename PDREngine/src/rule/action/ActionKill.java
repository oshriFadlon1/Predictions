package rule.action;

import entity.EntityDefinition;
import enums.Operation;
import necessaryVariables.NecessaryVariablesImpl;

public class ActionKill extends AbstractAction {

    private String entityName;

    public ActionKill(EntityDefinition entityDefinition, String entityName) {
        super(Operation.KILL, entityDefinition);
        this.entityName = entityName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    @Override
    public void invoke(NecessaryVariablesImpl context) {
        context.removeEntity(context.getPrimaryEntityInstance());
    }

    @Override
    public Operation getActionType() {
        return Operation.KILL;
    }

    @Override
    public EntityDefinition getContextEntity() {
        return super.getEntityDefinition();
    }
}
