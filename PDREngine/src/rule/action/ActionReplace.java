package rule.action;

import dto.DtoActionResponse;
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

    public String getEntityToKill(){
        return this.entityToKill;
    }

    public void setEntityToKill(String entityToKill) {
        this.entityToKill = entityToKill;
    }

    public String getEntityToCreate() {
        return entityToCreate;
    }

    public void setEntityToCreate(String entityToCreate) {
        this.entityToCreate = entityToCreate;
    }

    public CreationType getCreationType() {
        return creationType;
    }

    public void setCreationType(CreationType creationType) {
        this.creationType = creationType;
    }

    @Override
    public EntityDefinition getContextEntity() {
        return super.getEntityDefinition();
    }
    @Override
    public void invoke(NecessaryVariablesImpl context) throws GeneralException {
//        if (!context.getPrimaryEntityInstance().getDefinitionOfEntity().getEntityName().equalsIgnoreCase(super.getEntityDefinition().getEntityName())){
//            return;
//        }
        EntityDefinition currentEntityToCreate = null;
        for (EntityDefinition entityDefinition:context.getEntityDefinitions()) {
            if (entityDefinition.getEntityName().equalsIgnoreCase(this.entityToCreate)){
                currentEntityToCreate = entityDefinition;
                break;
            }
        }
        context.killAndCreateEntity(context.getPrimaryEntityInstance(), currentEntityToCreate, this.creationType);
    }

    @Override
    public DtoActionResponse getActionResponse() {
        DtoActionResponse actionResponse = super.getActionResponse();
        actionResponse.setActionName("Replace");
        actionResponse.setPrimEntityName(this.entityToKill);
        actionResponse.setSecEntityName(this.entityToCreate);
        if (this.creationType.toString().equalsIgnoreCase("scratch")){
            actionResponse.setActionProperty("scratch");
        } else {
            actionResponse.setActionProperty("derived");
        }
        return actionResponse;
    }
}
