package dto;

public class DtoResponseTermination {

    private boolean isEndByTicks;
    private boolean isEndBySeconds;

    public DtoResponseTermination(boolean isEndByTicks, boolean isEndBySeconds) {
        this.isEndByTicks = isEndByTicks;
        this.isEndBySeconds = isEndBySeconds;
    }

    public boolean isEndByTicks() {
        return isEndByTicks;
    }

    public void setEndByTicks(boolean endByTicks) {
        isEndByTicks = endByTicks;
    }

    public boolean isEndBySeconds() {
        return isEndBySeconds;
    }

    public void setEndBySeconds(boolean endBySeconds) {
        isEndBySeconds = endBySeconds;
    }
}
