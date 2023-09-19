package ui.presenter;

public class HistogramPresenter {
    private String propertyValue;
    private Integer countOfProperty;

    public HistogramPresenter(String propertyValue, Integer countOfProperty) {
        this.propertyValue = propertyValue;
        this.countOfProperty = countOfProperty;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public Integer getCountOfProperty() {
        return countOfProperty;
    }

    public void setCountOfProperty(Integer countOfProperty) {
        this.countOfProperty = countOfProperty;
    }
}
