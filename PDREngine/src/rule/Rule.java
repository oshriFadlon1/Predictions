package rule;


import rule.action.IAction;

import java.io.Serializable;
import java.util.*;
import java.util.List;

public class Rule implements Serializable {
    private final String ruleName;

    private final ActivationForRule activation;

    private final List<IAction> actions;

    public Rule(String ruleName, ActivationForRule activation, List<IAction> actions) {
        this.ruleName = ruleName;
        this.activation = activation;
        this.actions = actions;
    }

    public String getRuleName() {
        return ruleName;
    }

//    public void setRuleName(String ruleName) {
//        this.ruleName = ruleName;
//    }

    public ActivationForRule getActivation() {
        return activation;
    }

//    public void setActivation(ActivationForRule activation) {
//        this.activation = activation;
//    }

    public List<IAction> getActions() {
        return actions;
    }

//    public void setActions(List<IAction> actions) {
//        this.actions = actions;
//    }

    @Override
    public String toString(){
        int actionCounter = 1;
        StringBuilder result = new StringBuilder(String.format("Rule name: %s", this.ruleName));
        Set<IAction> withoutDuplicates = new HashSet<>(actions);

        result.append("\nActivition ticks:").append(this.activation.getTicks());
        result.append("\nActivition probability: ").append(this.activation.getProbability());
        result.append(String.format("\nActions number: %d", actions.size()));

        for(IAction currAction: withoutDuplicates) {
            result.append(String.format("\nAction #%d: %s", actionCounter, currAction.getOperationType()));
            actionCounter++;
        }
        return result.toString();
     }
}
