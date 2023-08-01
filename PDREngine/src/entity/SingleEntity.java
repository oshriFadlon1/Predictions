package entity;

import property.Property;
import property.PropertyForEntity;

import java.util.ArrayList;
import java.util.List;

public class SingleEntity {

    private List<PropertyForEntity> allProperties;
    public SingleEntity() {
        this.allProperties = new ArrayList<PropertyForEntity>();
    }

    public List<PropertyForEntity> getAllProperties() {
        return allProperties;
    }

    public void setAllProperties(List<PropertyForEntity> allProperties) {
        this.allProperties = allProperties;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        int propCounter = 1;
        for(PropertyForEntity currentProperty: allProperties){
            result.append("Propery #" + propCounter + "\n");
            result.append(currentProperty.toString());
            propCounter++;
        }

        return result.toString();
    }
}
