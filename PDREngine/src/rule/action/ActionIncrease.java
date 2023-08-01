package rule.action;

import enums.Operation;

public class ActionIncrease extends Action{

    private float increaseBy;
    private String entityName;
    private String propertyName;

    public ActionIncrease(float increaseBy, String entityName, String propertyName, Operation operationType){
        super(operationType);
        this.increaseBy = increaseBy;
        this.entityName = entityName;
        this.propertyName = propertyName;
    }

    public float getIncreaseBy(){
        return this.increaseBy;
    }

    public void setIncreaseBy(float increaseBy){
        this.increaseBy = increaseBy;
    }

    public String getEntityName(){
        return this.entityName;
    }

    public void setEntityName(String entityName){
        this.entityName = entityName;
    }

    public String getPropertyName(){
        return this.propertyName;
    }

    public void setPropertyName(String propertyName){
        this.propertyName = propertyName;
    }

}
