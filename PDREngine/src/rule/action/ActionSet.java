package rule.action;

import entity.EntityDefinition;
import entity.EntityInstance;
import enums.Operation;
import exceptions.GeneralException;
import necessaryVariables.NecessaryVariablesImpl;
import property.PropertyInstance;
import utility.Utilities;

import java.util.function.BooleanSupplier;

public class ActionSet extends AbstractAction {

    private String propertyName;
    private String value;

    public ActionSet(EntityDefinition entityDefinition, String propertyName, String value) {
        super(Operation.SET,entityDefinition);
        this.propertyName = propertyName;
        this.value = value;
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

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void invoke(NecessaryVariablesImpl context) {
        PropertyInstance propertyInstance = context.getPrimaryEntityInstance().getPropertyByName(propertyName);
        String type = propertyInstance.getPropertyDefinition().getPropertyType().toLowerCase();
        Object result = null;
        try {
            result = context.getValueFromString(this.value);

            switch (type){
                case "decimal":
                    if (! (result instanceof Integer)){
                        throw new GeneralException("Set action can't operate on decimal with none decimal value");
                    }
                    break;
                case "float":
                    if (! (result instanceof Float)){
                        throw new GeneralException("Set action can't operate on float with none float value");
                    }
                    break;
                case "boolean":
                    if (! (result instanceof Boolean)){
                        throw new GeneralException("Set action can't operate on boolean with none boolean value");
                    }
                    break;
                case "string":
                    if (! (result instanceof String)){
                        throw new GeneralException("Set action can't operate on String with none String value");
                    }
                    break;
            }
            propertyInstance.setPropValue(result);
        } catch (GeneralException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Operation getActionType() {
        return Operation.SET;
    }

    @Override
    public EntityDefinition getContextEntity() {
        return super.getEntityDefinition();
    }
}
