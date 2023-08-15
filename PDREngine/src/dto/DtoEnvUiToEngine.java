package dto;

import environment.EnvironmentDefinition;
import property.PropertyDefinition;
import range.Range;

import java.util.Map;

public class DtoEnvUiToEngine {
    private EnvironmentDefinition envDef;
    private Object envValue;

    public DtoEnvUiToEngine(Map<String, Object> environmentToValue){
        this.environmentToValue = environmentToValue;
    }

    public EnvironmentDefinition getEnvDef(){
        return this.envDef;
    }

    public Object getEnvValue(){
        return this.envValue;
    }
}
