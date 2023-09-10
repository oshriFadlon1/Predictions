package property;

import enums.Type;
import range.Range;

import java.io.Serializable;

public class PropertyDefinition implements Serializable {
    private String propertyName;

    private String propertyType;

    private Range propertyRange;

    public PropertyDefinition(String propertyName, String propertyType, Range propertyRange) {
        this.propertyName = propertyName;
        this.propertyType = propertyType;
        this.propertyRange = new Range(propertyRange.getFrom(),propertyRange.getTo());
    }

    public PropertyDefinition(String propertyName, String propertyType) {
        this.propertyName = propertyName;
        this.propertyType = propertyType;
        this.propertyRange = null;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public Range getPropertyRange() {
        return propertyRange;
    }

    public void setPropertyRange(Range propertyRange) {
        this.propertyRange = propertyRange;
    }

    @Override
    public String toString() {
        return  "\r\nName: " + propertyName +
                "\r\nType: " + propertyType +
                (this.propertyRange!=null?"\r\nRange: " + propertyRange.toString():"");

    }

    public boolean checkIfValueInRange(Object obj) {
        if (this.propertyRange != null) {
            if (obj instanceof Float || obj instanceof Integer) {
                float from = this.propertyRange.getFrom();
                float to = this.propertyRange.getTo();
                float value = ((Number) obj).floatValue();
                return from <= value && value <= to;
            }
        }
        return false;
    }

    public Object getValueInRange(String propertyType, Object propValue) {
        Object valueToReturn = null;
        if (propertyType.equalsIgnoreCase("decimal")){
            if (this.propertyRange.getFrom() > ((Number) propValue).floatValue())
            {
                Number floatToInt = (Number)this.propertyRange.getFrom();
                valueToReturn = floatToInt.intValue();
            } else {
                Number floatToInt = (Number)this.propertyRange.getTo();
                valueToReturn = floatToInt.intValue();
            }
        }
        else {
            if (this.propertyRange.getFrom() > ((Number) propValue).floatValue())
            {
                Number valueInFloat = (Number)this.propertyRange.getFrom();
                valueToReturn = valueInFloat.floatValue();
            } else {
                Number valueInFloat = (Number)this.propertyRange.getTo();
                valueToReturn = valueInFloat.floatValue();
            }
        }
        return valueToReturn;
    }

    public PropertyDefinition getCopyOfPropertyDefinition(){
        if (this.propertyRange == null){
            return new PropertyDefinition(this.propertyName,this.propertyType);
        }
        return new PropertyDefinition(this.propertyName, this.propertyType, this.propertyRange.getCopyOfRange());
    }
}
