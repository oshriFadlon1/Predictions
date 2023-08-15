package dto;

import property.PropertyDefinitionEntity;

import java.util.Map;

public class DtoEntityDefitionToDisplay {
    private String EntityName;
    private int StartPo;
    private int endPro;

    private Map<String, PropertyDefinitionEntity> propertyDefinition;

    public DtoEntityDefitionToDisplay(String entityName, int startPo, int endPro, Map<String, PropertyDefinitionEntity> propertyDefinition) {
        EntityName = entityName;
        StartPo = startPo;
        this.endPro = endPro;
        this.propertyDefinition = propertyDefinition;
    }

    public String getEntityName() {
        return EntityName;
    }

    public void setEntityName(String entityName) {
        EntityName = entityName;
    }

    public int getStartPo() {
        return StartPo;
    }

    public void setStartPo(int startPo) {
        StartPo = startPo;
    }

    public int getEndPro() {
        return endPro;
    }

    public void setEndPro(int endPro) {
        this.endPro = endPro;
    }

    public Map<String, PropertyDefinitionEntity> getPropertyDefinition() {
        return propertyDefinition;
    }

    public void setPropertyDefinition(Map<String, PropertyDefinitionEntity> propertyDefinition) {
        this.propertyDefinition = propertyDefinition;
    }
}
