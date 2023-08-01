package world;

import entity.Entity;
import property.Property;
import rule.Rule;
import termination.Termination;

import java.util.ArrayList;
import java.util.List;

public class World {
    private List<Entity> allEntities;
    //private List<Rule> allRules;
    private List<Property> allEnviroments;
    private Termination termination;

    public World(Termination termination) {
        this.allEntities = new ArrayList<>();
        //this.allRules = new ArrayList<>();
        this.allEnviroments = new ArrayList<>();
        this.termination = termination;
    }

    public World(List<Entity> allEntities, List<Rule> allRules, List<Property> allEnviroments) {
        this.allEntities = allEntities;
        //this.allRules = allRules;
        this.allEnviroments = allEnviroments;
        this.termination = termination;
    }

    public World(List<Entity> allEntities, List<Rule> allRules, List<Property> allEnviroments, Termination termination) {
        this.allEntities = allEntities;
        //this.allRules = allRules;
        this.allEnviroments = allEnviroments;
        this.termination = termination;
    }

    public List<Entity> getAllEntities() {
        return allEntities;
    }

    public void setAllEntities(List<Entity> allEntities) {
        this.allEntities = allEntities;
    }

//    public List<Rule> getAllRules() {
//        return allRules;
//    }
//
//    public void setAllRules(List<Rule> allRules) {
//        this.allRules = allRules;
//    }

    public List<Property> getAllEnviroments() {
        return allEnviroments;
    }

    public void setAllEnviroments(List<Property> allEnviroments) {
        this.allEnviroments = allEnviroments;
    }

    public Termination getTermination() {
        return termination;
    }

    public void setTermination(Termination termination) {
        this.termination = termination;
    }

    public void addEnviroment(Property env) throws GeneralException {
        if(this.allEnviroments.contains(env))
            throw new GeneralException("Current enviroment already exists");

        this.allEnviroments.add(env);
    }

    public void addEntity(Entity ent) throws GeneralException{
        if(this.allEntities.contains(ent))
            throw new GeneralException("Current entity already exists");
        this.allEntities.add(ent);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        int entityCount = 1;
        int ruleCount = 1;
        for(Entity currEntity: allEntities){
            result.append(String.format("Entity #%d: \n", entityCount));
            result.append(currEntity.toString()).append("\n");
            entityCount++;
        }
        result.append("\n");
//        for(Rule currRule: allRules) {
//            result.append(String.format("Rule #%d: \n", ruleCount));
//            result.append(currRule.toString()).append("\n");
//            ruleCount++;
//        }

        result.append(termination.toString());
        return result.toString();
    }
}
