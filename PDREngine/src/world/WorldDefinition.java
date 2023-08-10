package world;

import entity.EntityDefinition;
import entity.EntityInstance;
import environment.EnvironmentDefinition;
import rule.Rule;
import termination.Termination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldDefinition {
    private Map<String, EnvironmentDefinition> allEnvironments;

    private List<EntityDefinition> entityDefinitions;

    private Map<String, Rule> Rules;

    private Termination termination;

    public WorldDefinition(Map<String, EnvironmentDefinition> allEnvironments, List<EntityDefinition> entityDefinitions, Map<String, Rule> rules, Termination termination) {
        this.allEnvironments = allEnvironments;
        this.entityDefinitions = entityDefinitions;
        Rules = rules;
        this.termination = termination;
    }

    public WorldDefinition(Termination termination) {
        this.allEnvironments = new HashMap<>();
        this.entityDefinitions = new ArrayList<>();
        Rules = new HashMap<>();
        this.termination = termination;
    }

    public Map<String, EnvironmentDefinition> getAllEnvironments() {
        return allEnvironments;
    }

    public void setAllEnvironments(Map<String, EnvironmentDefinition> allEnvironments) {
        this.allEnvironments = allEnvironments;
    }

    public List<EntityDefinition> getEntityDefinitions() {
        return entityDefinitions;
    }

    public void setEntityDefinitions(List<EntityDefinition> entityDefinitions) {
        this.entityDefinitions = entityDefinitions;
    }

    public Map<String, Rule> getRules() {
        return Rules;
    }

    public void setRules(Map<String, Rule> rules) {
        Rules = rules;
    }

    public Termination getTermination() {
        return termination;
    }

    public void setTermination(Termination termination) {
        this.termination = termination;
    }


}
