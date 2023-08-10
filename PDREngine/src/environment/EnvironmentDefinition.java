package environment;

import property.PropertyDefinition;

public class EnvironmentDefinition {
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
