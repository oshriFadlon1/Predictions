package rule.action;

import entity.EntityInstance;
import enums.Operation;

public class ActionDecrease extends Action {

    private float increaseBy;
    private String entityName;
    private String propertyName;

    public ActionDecrease(Operation operationType, float increaseBy, String entityName, String propertyName) {
        super(operationType);
        this.increaseBy = increaseBy;
        this.entityName = entityName;
        this.propertyName = propertyName;
    }

    public float getIncreaseBy() {
        return increaseBy;
    }

    public void setIncreaseBy(float increaseBy) {
        this.increaseBy = increaseBy;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public void Invoke(EntityInstance entityInstance) {
        System.out.println("bla");
    }
}
