package property;

public class Value {

    private Boolean randomInit;

    private String init;

    public Value(Boolean randomInit, String init) {
        this.randomInit = randomInit;
        this.init = init;
    }

    public Value(Boolean randomInit) {
        this.randomInit = randomInit;
        this.init = null;
    }

    public Boolean getRandomInit() {
        return randomInit;
    }

    public void setRandomInit(Boolean randomInit) {
        this.randomInit = randomInit;
    }

    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        this.init = init;
    }

    @Override
    public String toString() {
        return "\r\nrandom-init: " + randomInit +
                "\r\ninit: " + init;
    }
}
