package rule;

public class ActivationForRule {
    private int ticks;

    private float probability;

    public ActivationForRule(int ticks, float probability) {
        this.ticks = ticks;
        this.probability = probability;
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
}
