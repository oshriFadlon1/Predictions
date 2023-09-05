package dto;

import java.util.List;

public class DtoResponseRules {
    private String ruleName;
    private int ticks;
    private float probability;
    private List<DtoActionResponse> actionNames;

    public DtoResponseRules(String ruleName, int ticks, float probability, List<DtoActionResponse> actionNames) {
        this.ruleName = ruleName;
        this.ticks = ticks;
        this.probability = probability;
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

    public List<DtoActionResponse> getActionNames() {
        return actionNames;
    }

    public void setActionNames(List<DtoActionResponse> actionNames) {
        this.actionNames = actionNames;
    }

    @Override
    public String toString() {
        StringBuilder actionName = new StringBuilder();
        for (DtoActionResponse str: actionNames) {
            actionName.append("\r\n").append(str);
        }
        return  "\r\nruleName: " + ruleName +
                "\r\nticks: " + ticks +
                "\r\nprobability: " + probability +
                "\r\naction Names: " + actionName.toString();
    }
}
