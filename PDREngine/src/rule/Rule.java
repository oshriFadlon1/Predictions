package rule;


import rule.action.AbstractAction;

import java.util.*;
import java.util.List;

public class Rule {
    private String ruleName;

    private ActivationForRule activation;

    private List<AbstractAction> actions;

    public Rule(String ruleName, ActivationForRule activation, List<AbstractAction> actions) {
        this.ruleName = ruleName;
        this.activation = activation;
        this.actions = actions;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public ActivationForRule getActivation() {
        return activation;
    }

    public void setActivation(ActivationForRule activation) {
        this.activation = activation;
    }

    public List<AbstractAction> getActions() {
        return actions;
    }

    public void setActions(List<AbstractAction> actions) {
        this.actions = actions;
    }

    @Override
    public String toString(){
        int actionCounter = 1;
        StringBuilder result = new StringBuilder(String.format("Rule name: %s", this.ruleName));
        Set<AbstractAction> withoutDuplicates = new HashSet<>(actions);

        result.append("\nActivition ticks:").append(this.activation.getTicks());
        result.append("\nActivition probability: ").append(this.activation.getProbability());
        result.append(String.format("\nActions number: %d", actions.size()));

        for(AbstractAction currAction: withoutDuplicates) {
            result.append(String.format("\nAction #%d: %s", actionCounter, currAction.getOperationType()));
            actionCounter++;
        }
        return result.toString();
     }
}
