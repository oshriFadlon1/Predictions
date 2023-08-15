package dto;

import environment.EnvironmentDefinition;
import property.PropertyDefinition;
import range.Range;

import java.util.Map;

public class DtoEnvUiToEngine {
    Map<String, Object> environmentToValue;

    public DtoEnvUiToEngine(Map<String, Object> environmentToValue){
        this.environmentToValue = environmentToValue;
    }

    public Map<String, Object> getEnvironmentToValue(){
        return this.environmentToValue;
    }
}
