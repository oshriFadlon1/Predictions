package property;

import java.io.Serializable;

public class PropertyDefinitionEntity implements Serializable {

    private PropertyDefinition propertyDefinition;
    private Value propValue;

    public PropertyDefinitionEntity(PropertyDefinition propertyDefinition, Value propValue) {
        this.propertyDefinition = propertyDefinition;
        this.propValue = propValue;
    }

    public PropertyDefinition getPropertyDefinition() {
        return propertyDefinition;
    }

    public void setPropertyDefinition(PropertyDefinition propertyDefinition) {
        this.propertyDefinition = propertyDefinition;
    }

    public Value getPropValue() {
        return propValue;
    }

    public void setPropValue(Value propValue) {
        this.propValue = propValue;
    }

    @Override
    public String toString() {
        return "\r\nproperty Definition: " + propertyDefinition.toString() +
                "\r\nproperty Value: " + propValue.toString();
    }
}
