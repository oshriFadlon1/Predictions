package world;

import entity.Entity;
import property.Property;
import rule.Rule;
import termination.Termination;

import java.util.List;

public class World {
    private List<Entity> allEntities;
    private List<Rule> allRules;
    private List<Property> allEnviroments;
    private Termination termination;

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
        for(Rule currRule: allRules) {
            result.append(String.format("Rule #%d: \n", ruleCount));
            result.append(currRule.toString()).append("\n");
            ruleCount++;
        }

        result.append(termination.toString());
        return result.toString();
    }
}
