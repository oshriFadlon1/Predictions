package property;

import java.io.Serializable;

public class PropertyInstance implements Serializable {
    private PropertyDefinition propertyDefinition;
    private Object propValue;
    private int currentTicksWithoutChange;
    private int numberOfReset;
    private int totalTickWithoutChange;

    public PropertyInstance(PropertyDefinition propertyDefinition, Object propValue) {
        this.propertyDefinition = propertyDefinition;
        this.propValue = propValue;
        this.currentTicksWithoutChange = 0;
        this.numberOfReset = 0;
        this.totalTickWithoutChange = 0;
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

    public void increaseTick(){
        this.currentTicksWithoutChange++;

    }
    public void resetTicksToZero(){
        this.totalTickWithoutChange += this.currentTicksWithoutChange;
        this.currentTicksWithoutChange = 0;
        this.numberOfReset++;

    }

    public void resetAllTicks(){
        this.totalTickWithoutChange = this.currentTicksWithoutChange = this.numberOfReset = 0;
    }
    public int getCurrentTicksWithoutChange() {
        return currentTicksWithoutChange;
    }

    public void setCurrentTicksWithoutChange(int currentTicksWithoutChange) {
        this.currentTicksWithoutChange = currentTicksWithoutChange;
    }

    public float calculateAvgTicksWithoutChange(){
        return (float)this.totalTickWithoutChange / numberOfReset;
    }

    @Override
    public String toString() {
        return "\r\npropertyDefinition: " + propertyDefinition +
                "\r\npropValue: " + propValue;
    }
}
