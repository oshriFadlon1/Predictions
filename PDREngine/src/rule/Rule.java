package rule;


import java.util.List;

public class Rule {
    private String RuleName;

    private ActivationForRule activation;

    private List<Action> actions;

    public Rule(String ruleName, ActivationForRule activation, List<Action> actions) {
        RuleName = ruleName;
        this.activation = activation;
        this.actions = actions;
    }

    public String getRuleName() {
        return RuleName;
    }

    public void setRuleName(String ruleName) {
        RuleName = ruleName;
    }

    public ActivationForRule getActivation() {
        return activation;
    }

    public void setActivation(ActivationForRule activation) {
        this.activation = activation;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }
}
