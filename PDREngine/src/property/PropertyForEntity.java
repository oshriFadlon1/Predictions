package property;

import range.Range;

public class PropertyForEntity extends Property{
    private Value propValue;

    public PropertyForEntity(String propertyName, String propertyType, Range propertyRange, Value propValue) {
        super(propertyName, propertyType, propertyRange);
        this.propValue = propValue;
    }

    public PropertyForEntity(String propertyName, String propertyType, Value propValue) {
        super(propertyName, propertyType);
        this.propValue = propValue;
    }

    public Value getPropValue() {
        return propValue;
    }

    public void setPropValue(Value propValue) {
        this.propValue = propValue;
    }

    @Override
    public String toString() {
        return super.toString() + this.propValue.toString();
    }
}
