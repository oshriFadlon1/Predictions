package environment;

import property.PropertyDefinition;

import java.io.Serializable;

public class EnvironmentDefinition implements Serializable {
    private PropertyDefinition envPropertyDefinition;

    public EnvironmentDefinition(PropertyDefinition envPropertyDefinition) {
        this.envPropertyDefinition = envPropertyDefinition;
    }
    public EnvironmentDefinition(){}

    public PropertyDefinition getEnvPropertyDefinition() {
        return envPropertyDefinition;
    }

    public void setEnvPropertyDefinition(PropertyDefinition envPropertyDefinition) {
        this.envPropertyDefinition = envPropertyDefinition;
    }


}
