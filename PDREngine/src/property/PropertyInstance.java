package property;

import java.io.Serializable;

public class PropertyInstance implements Serializable {
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
        if (this.propertyDefinition.getPropertyType().equalsIgnoreCase("float")||
                this.propertyDefinition.getPropertyType().equalsIgnoreCase("decimal")){
            if (this.propertyDefinition.checkIfValueInRange(propValue)){
                this.propValue = propValue;
            } else {
                this.propValue = this.propertyDefinition.getValueInRange(this.propertyDefinition.getPropertyType(),propValue);
            }
        }
        else {
            this.propValue = propValue;
        }
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
