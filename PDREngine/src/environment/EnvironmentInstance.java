package environment;

import property.PropertyDefinition;

public class EnvironmentInstance {
    private EnvironmentDefinition environmentDefition;

    private Object envValue;

    public EnvironmentInstance(EnvironmentDefinition environmentDefition, Object envValue) {
        this.environmentDefition = environmentDefition;
        this.envValue = envValue;
    }

    public EnvironmentDefinition getEnvironmentDefition() {
        return environmentDefition;
    }

    public void setEnvironmentDefition(EnvironmentDefinition environmentDefition) {
        this.environmentDefition = environmentDefition;
    }

    public Object getEnvValue() {
        return envValue;
    }

    public void setEnvValue(Object envValue) {
        this.envValue = envValue;
    }

}