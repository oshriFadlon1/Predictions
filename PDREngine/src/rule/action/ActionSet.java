package rule.action;

import dto.DtoActionResponse;
import entity.EntityDefinition;
import entity.SecondEntity;
import enums.Operation;
import exceptions.GeneralException;
import necessaryVariables.NecessaryVariablesImpl;
import property.PropertyInstance;

import java.io.Serializable;

public class ActionSet extends AbstractAction implements Serializable {

    private String propertyName;
    private String value;

    public ActionSet(EntityDefinition entityDefinition, String propertyName, String value, SecondEntity secondEntity) {
        super(Operation.SET,entityDefinition, secondEntity);
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
    public void invoke(NecessaryVariablesImpl context) throws GeneralException {
//        if (!context.getPrimaryEntityInstance().getDefinitionOfEntity().getEntityName().equalsIgnoreCase(super.getEntityDefinition().getEntityName())){
//            return;
//        }

        PropertyInstance propertyInstance = context.getPrimaryEntityInstance().getPropertyByName(propertyName);
        String type = propertyInstance.getPropertyDefinition().getPropertyType().toLowerCase();
        Object result = null;
        try {
            result = context.getValueFromString(this.value);

            switch (type) {
                case "float":
                    if (!(result instanceof Float)) {
                        throw new GeneralException("Set action can't operate on float with none float value");
                    }
                    break;
                case "boolean":
                    if (!(result instanceof Boolean)) {
                        throw new GeneralException("Set action can't operate on boolean with none boolean value");
                    }
                    break;
                case "string":
                    if (!(result instanceof String)) {
                        throw new GeneralException("Set action can't operate on String with none String value");
                    }
                    break;
            }
            propertyInstance.updatePropertyValue(result);
        } catch (GeneralException e) {
            throw e;
        }
    }

    @Override
    public Operation getOperationType() {
        return Operation.SET;
    }

    @Override
    public EntityDefinition getContextEntity() {
        return super.getEntityDefinition();
    }

    @Override
    public DtoActionResponse getActionResponse() {
        DtoActionResponse actionResponse = super.getActionResponse();
        actionResponse.setActionName("Set");
        actionResponse.setActionProperty(this.propertyName);
        actionResponse.setActionValue(this.value);
        return actionResponse;
    }
}
