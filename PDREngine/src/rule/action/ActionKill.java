package rule.action;

import entity.EntityInstance;
import enums.Operation;

public class ActionKill extends Action {

    private String entityName;


    public ActionKill(Operation operationType, String entityName) {
        super(operationType);
        this.entityName = entityName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    @Override
    public void Invoke(EntityInstance entityInstance) {

    }
}
