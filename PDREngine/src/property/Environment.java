package property;

public class Environment {
    private PropertyDefinition envPropertyDefinition;

    public Environment(PropertyDefinition envPropertyDefinition) {
        this.envPropertyDefinition = envPropertyDefinition;
    }
    public Environment(){}

    public PropertyDefinition getEnvPropertyDefinition() {
        return envPropertyDefinition;
    }

    public void setEnvPropertyDefinition(PropertyDefinition envPropertyDefinition) {
        this.envPropertyDefinition = envPropertyDefinition;
    }


}
