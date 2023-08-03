package property;

import range.Range;

public class PropertyDefinition {
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
        String str =this.propertyRange!=null?"\nRange: " + propertyRange.toString():"";
        return  "\nName: " + propertyName +
                "\nType: " + propertyType + str;

    }
}
