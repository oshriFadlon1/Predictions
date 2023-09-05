package dto;

import entity.EntityDefinition;
import entity.SecondEntity;
import environment.EnvironmentDefinition;
import shema.generated.PRDAction;
import java.util.List;
import java.util.Map;

public class DtoActionArgsContainer {

    private List<EntityDefinition> entityDefinitionList;
    private Map<String, EnvironmentDefinition> environments;
    private SecondEntity secondEntity;
    private List<EntityDefinition> entitiesInContext;

    public DtoActionArgsContainer(List<EntityDefinition> entityDefinitionList,
                                  Map<String, EnvironmentDefinition> environments,
                                  SecondEntity secondEntity,
                                  List<EntityDefinition> entitiesInContext) {
        this.entityDefinitionList = entityDefinitionList;
        this.environments = environments;
        this.secondEntity = secondEntity;
        this.entitiesInContext = entitiesInContext;
    }

    public List<EntityDefinition> getEntityDefinitionList() {
        return entityDefinitionList;
    }

    public void setEntityDefinitionList(List<EntityDefinition> entityDefinitionList) {
        this.entityDefinitionList = entityDefinitionList;
    }

    public Map<String, EnvironmentDefinition> getEnvironments() {
        return environments;
    }

    public void setEnvironments(Map<String, EnvironmentDefinition> environments) {
        this.environments = environments;
    }

    public SecondEntity getSecondEntity() {
        return secondEntity;
    }

    public void setSecondEntity(SecondEntity secondEntity) {
        this.secondEntity = secondEntity;
    }

    public List<EntityDefinition> getEntitiesInContext() {
        return entitiesInContext;
    }

    public void setEntitiesInContext(List<EntityDefinition> entitiesInContext) {
        this.entitiesInContext = entitiesInContext;
    }
}
