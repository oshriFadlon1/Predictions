package property;

public class Value {

    private Boolean randomInit;

    private Object init;

    public Value(Boolean randomInit, Object init) {
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

    public Object getInit() {
        return init;
    }

    public void setInit(Object init) {
        this.init = init;
    }

    @Override
    public String toString() {
        return "\nrandominit:" + randomInit +
                ", init: " + init;
    }
}
