package property;

public class PropertyInstance {
    private PropertyDefinition propertyDefinition;
    private Value propValue;

    public PropertyInstance(PropertyDefinition propertyDefinition, Value propValue) {
        this.propertyDefinition = propertyDefinition;
        this.propValue = propValue;
    }

    public PropertyInstance(PropertyDefinition propertyDefinition) {
        this.propertyDefinition = propertyDefinition;
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
        return "\r\npropertyDefinition: " + propertyDefinition +
                "\r\npropValue: " + propValue;
    }
}
