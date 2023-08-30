package entity;

import interfaces.IConditionComponent;
import rule.action.IAction;

import java.util.List;

public class SecondEntity {
    private EntityDefinition entity;
    private String count;
    private IConditionComponent condition;

    public SecondEntity(EntityDefinition entity, String count, IConditionComponent condition) {
        this.entity = entity;
        this.count = count;
        this.condition = condition;
    }

    public EntityDefinition getEntity() {
        return entity;
    }

    public void setEntity(EntityDefinition entity) {
        this.entity = entity;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public IConditionComponent getCondition() {
        return condition;
    }

    public void setCondition(IConditionComponent condition) {
        this.condition = condition;
    }
}
