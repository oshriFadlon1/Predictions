package rule.action;

import entity.EntityDefinition;
import entity.SecondEntity;
import enums.CreationType;
import enums.Operation;
import exceptions.GeneralException;
import necessaryVariables.NecessaryVariablesImpl;

public class ActionReplace extends AbstractAction{
    private String entityToKill;
    private String entityToCreate;
    private CreationType creationType;
    public ActionReplace(EntityDefinition entityDefinition, String entityToKill, String entityToCreate, CreationType creationType){
        super(Operation.REPLACE, entityDefinition);
        this.entityToKill = entityToKill;
        this.entityToCreate = entityToCreate;
        this.creationType = creationType;
    }
    @Override
    public void invoke(NecessaryVariablesImpl context) throws GeneralException {

    }

    @Override
    public EntityDefinition getContextEntity() {
        return null;
    }
}
