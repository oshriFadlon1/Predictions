package dto;

import java.util.List;

public class DtoResponseRules {
    private String ruleName;
    private int ticks;
    private float probability;
    private int countOfAction;
    private List<String> actionNames;

    public DtoResponseRules(String ruleName, int ticks, float probability, int countOfAction, List<String> actionNames) {
        this.ruleName = ruleName;
        this.ticks = ticks;
        this.probability = probability;
        this.countOfAction = countOfAction;
        this.actionNames = actionNames;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public float getProbability() {
        return probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }

    public int getCountOfAction() {
        return countOfAction;
    }

    public void setCountOfAction(int countOfAction) {
        this.countOfAction = countOfAction;
    }

    public List<String> getActionNames() {
        return actionNames;
    }

    public void setActionNames(List<String> actionNames) {
        this.actionNames = actionNames;
    }

    @Override
    public String toString() {
        StringBuilder actionName = new StringBuilder();
        for (String str: actionNames) {
            actionName.append("\r\n").append(str);
        }
        return  "\r\nruleName: " + ruleName +
                "\r\nticks: " + ticks +
                "\r\nprobability: " + probability +
                "\r\ncount Of Action: " + countOfAction +
                "\r\naction Names: " + actionName.toString();
    }
}
