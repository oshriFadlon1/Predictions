package rule.action;

import entity.EntityDefinition;
import entity.SecondEntity;
import enums.Operation;
import exceptions.GeneralException;
import necessaryVariables.NecessaryVariablesImpl;

public class ActionProximity extends AbstractAction {
    private String envDepth;

    public ActionProximity(EntityDefinition entityDefinition) {
        super(Operation.PROXIMITY, entityDefinition);
    }

    public ActionProximity(EntityDefinition entityDefinition, SecondEntity secondaryEntity) {
        super(Operation.PROXIMITY, entityDefinition, secondaryEntity);
    }

    @Override
    public void invoke(NecessaryVariablesImpl context) throws GeneralException {

    }

    @Override
    public EntityDefinition getContextEntity() {
        return null;
    }
}
