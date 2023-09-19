package property;

import java.io.Serializable;

public class PropertyInstance implements Serializable {
    private PropertyDefinition propertyDefinition;
    private Object propValue;
    private int currentTicksWithoutChange;
    private int numberOfReset;
    private int totalTickWithoutChange;
    private boolean isPropertyChangedInCurrTick;

    public PropertyInstance(PropertyDefinition propertyDefinition, Object propValue) {
        this.propertyDefinition = propertyDefinition.getCopyOfPropertyDefinition();
        this.propValue = propValue;
        this.currentTicksWithoutChange = 0;
        this.numberOfReset = 0;
        this.totalTickWithoutChange = 0;
        this.isPropertyChangedInCurrTick = false;
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
        if (this.propertyDefinition.getPropertyType().equalsIgnoreCase("float")){
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

    public void updatePropertyValue(Object propValue){
        if (this.propertyDefinition.getPropertyType().equalsIgnoreCase("float")){
            if (this.propertyDefinition.checkIfValueInRange(propValue)){
                float resultInF = ((Float) propValue).floatValue();
                float propertyValueInF = ((Float) this.propValue).floatValue();
                if (resultInF != propertyValueInF) {
                    this.propValue = propValue;
                    isPropertyChangedInCurrTick = true;
                }
            } else {
                this.propValue = this.propertyDefinition.getValueInRange(this.propertyDefinition.getPropertyType(),propValue);
            }
        }
        else {
            if (propertyDefinition.getPropertyType().equalsIgnoreCase("boolean")) {
                boolean resultInB = ((Boolean) propValue).booleanValue();
                boolean propertyValueInB = ((Boolean) this.propValue).booleanValue();
                if (resultInB != propertyValueInB) {
                    this.propValue = propValue;
                    isPropertyChangedInCurrTick = true;
                }
            } else {
                String resultInS = ((String) propValue);
                String propertyValueInS = ((String) this.propValue);
                if (!resultInS.equals(propertyValueInS)) {
                    this.propValue = propValue;
                    isPropertyChangedInCurrTick = true;
                }
            }
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

    public void setIsPropertyChangedInCurrTick(boolean value){
        this.isPropertyChangedInCurrTick = value;
    }

    public boolean getIsPropertyChangedInCurrTick(){
        return this.isPropertyChangedInCurrTick;
    }

    public float calculateAvgTicksWithoutChange(){
        return (float)this.totalTickWithoutChange / numberOfReset;
    }

    @Override
    public String toString() {
        return "\r\npropertyDefinition: " + propertyDefinition +
                "\r\npropValue: " + propValue;
    }

    public int getNumberOfReset() {
        return numberOfReset;
    }

    public void setNumberOfReset(int numberOfReset) {
        this.numberOfReset = numberOfReset;
    }

    public int getTotalTickWithoutChange() {
        return totalTickWithoutChange;
    }

    public void setTotalTickWithoutChange(int totalTickWithoutChange) {
        this.totalTickWithoutChange = totalTickWithoutChange;
    }

    public boolean isPropertyChangedInCurrTick() {
        return isPropertyChangedInCurrTick;
    }

    public void setPropertyChangedInCurrTick(boolean propertyChangedInCurrTick) {
        isPropertyChangedInCurrTick = propertyChangedInCurrTick;
    }
}
