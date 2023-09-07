package environment;

import java.io.Serializable;

public class EnvironmentInstance implements Serializable {
    private EnvironmentDefinition environmentDefinition;

    private Object envValue;

    public EnvironmentInstance(EnvironmentDefinition environmentDefition, Object envValue) {
        this.environmentDefinition = environmentDefition.createCloneOfEnvironmentDefinition();
        this.envValue = envValue;
    }

    public EnvironmentDefinition getEnvironmentDefinition() {
        return environmentDefinition;
    }

    public void setEnvironmentDefinition(EnvironmentDefinition environmentDefinition) {
        this.environmentDefinition = environmentDefinition;
    }

    public Object getEnvValue() {
        return envValue;
    }

    public void setEnvValue(Object envValue) {
        this.envValue = envValue;
    }

}
