package rule.action;

import entity.EntityInstance;
import enums.Operation;

public class ActionSet extends Action {

    private String entityName;
    private String propertyName;
    private Object value;

    public ActionSet( String entityName, String propertyName, Object value, String operationType) {
        super(operationType);
        this.entityName = entityName;
        this.propertyName = propertyName;
        this.value = value;
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

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public void Invoke(EntityInstance entityInstance) {

    }
}
