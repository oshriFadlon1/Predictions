package dto;

import environment.EnvironmentDefinition;

public class DtoEnvUiToEngine {
    private EnvironmentDefinition envDef;
    private Object envValue;

    public DtoEnvUiToEngine(EnvironmentDefinition envDef, Object envValue){
        this.envDef = envDef;
        this.envValue = envValue;
    }

    public EnvironmentDefinition getEnvDef(){
        return this.envDef;
    }

    public Object getEnvValue(){
        return this.envValue;
    }
}
