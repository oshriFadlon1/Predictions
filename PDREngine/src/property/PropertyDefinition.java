package property;

import enums.Type;
import range.Range;

public class PropertyDefinition {
    private String propertyName;

    private Type propertyType;

    private Range propertyRange;

    public PropertyDefinition(String propertyName, String propertyType, Range propertyRange) {
        this.propertyName = propertyName;
        this.propertyType = Type.valueOf(propertyType.toUpperCase());
        this.propertyRange = new Range(propertyRange.getFrom(),propertyRange.getTo());
    }

    public PropertyDefinition(String propertyName, String propertyType) {
        this.propertyName = propertyName;
        this.propertyType = Type.valueOf(propertyType.toUpperCase());
        this.propertyRange = null;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Type getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(Type propertyType) {
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
        String str =this.propertyRange!=null?"\nRange: " + propertyRange.toString():"";
        return  "\nName: " + propertyName +
                "\nType: " + propertyType + str;

    }
}
