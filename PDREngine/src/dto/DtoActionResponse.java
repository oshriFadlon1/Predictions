package dto;

public class DtoActionResponse {
    // get form abstract action
    private String actionName;
    private String primEntityName;
    private String secEntityName;

    private String ActionProperty;

    private String ActionValue; // in calculation use as arg1

    private String arg2;
    private String actionElse;

    public DtoActionResponse(String actionName, String primEntityName, String secEntityName,
                             String actionProperty, String actionValue, String arg2, String actionElse) {
        this.actionName = actionName;
        this.primEntityName = primEntityName;
        this.secEntityName = secEntityName;
        ActionProperty = actionProperty;
        ActionValue = actionValue;
        this.arg2 = arg2;
        this.actionElse = actionElse;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public void setPrimEntityName(String primEntityName) {
        this.primEntityName = primEntityName;
    }

    public void setSecEntityName(String secEntityName) {
        this.secEntityName = secEntityName;
    }

    public void setActionProperty(String actionProperty) {
        ActionProperty = actionProperty;
    }

    public void setActionValue(String actionValue) {
        ActionValue = actionValue;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    public void setActionElse(String actionElse) {
        this.actionElse = actionElse;
    }

    public String getActionName() {
        return actionName==null?"":this.actionName;
    }

    public String getPrimEntityName() {
        return primEntityName==null?"":this.primEntityName;
    }

    public String getSecEntityName() {
        return secEntityName==null?"":this.secEntityName;
    }

    public String getActionProperty() {
        return ActionProperty==null?"":this.ActionProperty;
    }

    public String getActionValue() {
        return ActionValue==null?"":this.ActionValue;
    }

    public String getArg2() {
        return arg2==null?"":this.arg2;
    }

    public String getActionElse() {
        return actionElse==null?"":this.actionElse;
    }
}
