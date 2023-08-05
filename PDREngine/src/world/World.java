package world;

import entity.EntityDefinition;
import entity.EntityInstance;
import exceptions.GeneralException;
import property.Environment;
import rule.Rule;
import termination.Termination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World {
    private Map<String, Environment> allEnvironments;
    private Map<EntityDefinition,List<EntityInstance>> allEntities;
    private Map<String, Rule> allRules;
    private Termination termination;


    public World(Termination termination) {
        this.allEntities = new HashMap<>();
        this.allRules = new HashMap<>();
        this.allEnvironments = new HashMap<>();
        this.termination = termination;
    }

    public World(Map<String, Environment> allEnvironments, Map<EntityDefinition, List<EntityInstance>> allEntities,
                 Map<String, Rule> allRules,  Termination termination) {
        this.allEntities = allEntities;
        this.allRules = allRules;
        this.allEnvironments = allEnvironments;
        this.termination = termination;
    }

    public Map<EntityDefinition, List<EntityInstance>> getAllEntities() {
        return allEntities;
    }

    public void setAllEntities(Map<EntityDefinition, List<EntityInstance>> allEntities) {
        this.allEntities = allEntities;
    }

    public Map<String, Rule> getAllRules() {
        return allRules;
    }

    public void setAllRules(Map<String, Rule> allRules) {
        this.allRules = allRules;
    }

    public Map<String, Environment> getAllEnvironments() {
        return allEnvironments;
    }

    public void setAllEnvironments(Map<String, Environment> allEnvironments) {
        this.allEnvironments = allEnvironments;
    }

    public Termination getTermination() {
        return termination;
    }

    public void setTermination(Termination termination) {
        this.termination = termination;
    }

    public void addEnvironment(Environment environmentDataMember) throws GeneralException {
        if(this.allEnvironments.containsKey(environmentDataMember.getEnvPropertyDefinition().getPropertyName()))
        {
            throw new GeneralException("Current environment already exists");
        }
        
        this.allEnvironments.put(environmentDataMember.getEnvPropertyDefinition().getPropertyName(),environmentDataMember);
    }

//    public void addEntity(EntityInstance ent) throws GeneralException{
//        if(this.allEntities.contains(ent))
//            throw new GeneralException("Current entity already exists");
//        this.allEntities.add(ent);
//    }

//    @Override
//    public String toString() {
//        StringBuilder result = new StringBuilder();
//        int entityCount = 1;
//        int ruleCount = 1;
//        for(EntityInstance currEntity: allEntities){
//            result.append(String.format("Entity #%d: \n", entityCount));
//            result.append(currEntity.toString()).append("\n");
//            entityCount++;
//        }
//        result.append("\n");
//        for(Rule currRule: allRules) {
//            result.append(String.format("Rule #%d: \n", ruleCount));
//            result.append(currRule.toString()).append("\n");
//            ruleCount++;
//        }
//
//        result.append(termination.toString());
//        return result.toString();
//    }
}
