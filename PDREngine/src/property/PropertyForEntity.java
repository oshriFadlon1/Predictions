package property;

import range.Range;

public class PropertyForEntity extends Property{
    private Object propValue;

    private Boolean randomInit;

    private Object init;


    public PropertyForEntity(String propertyName, String propertyType, Range propertyRange, Object propValue,
                             Boolean randomInit, Object init) {
        super(propertyName, propertyType, propertyRange);
        this.propValue = propValue;
        this.randomInit = randomInit;
        this.init = init;
    }

    public Object getPropValue() {
        return propValue;
    }

    public void setPropValue(Object propValue) {
        this.propValue = propValue;
    }

    public Boolean getRandomInit() {
        return randomInit;
    }

    public void setRandomInit(Boolean randomInit) {
        this.randomInit = randomInit;
    }

    public Object getInit() {
        return init;
    }

    public void setInit(Object init) {
        this.init = init;
    }
}
