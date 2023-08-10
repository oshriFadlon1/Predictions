package property;

public class PropertyInstance {
    private PropertyDefinition propertyDefinition;
    private Object propValue;

    public PropertyInstance(PropertyDefinition propertyDefinition, Object propValue) {
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

    public void setPropValue(Object propValue) {
        this.propValue = propValue;
    }

    public Object getPropValue() {
        return propValue;
    }

    @Override
    public String toString() {
        return "\r\npropertyDefinition: " + propertyDefinition +
                "\r\npropValue: " + propValue;
    }
}
