package rule.action;

import entity.EntityDefinition;
import enums.CreationType;
import enums.Operation;
import exceptions.GeneralException;
import necessaryVariables.NecessaryVariablesImpl;

import java.io.Serializable;

public class ActionReplace extends AbstractAction implements Serializable{
    private String entityToKill;
    private String entityToCreate;
    private CreationType creationType;

    public ActionReplace(EntityDefinition entityDefinition, String entityToKill, String entityToCreate, CreationType creationType){
        super(Operation.REPLACE,entityDefinition);
        this.entityToKill = entityToKill;
        this.entityToCreate = entityToCreate;
        this.creationType = creationType;
    }





    @Override
    public EntityDefinition getContextEntity() {
        return super.getEntityDefinition();
    }
    @Override
    public void invoke(NecessaryVariablesImpl context) throws GeneralException {
        context.killAndCreateEntity(context.getPrimaryEntityInstance(), this.entityToCreate, this.creationType);
    }
}
