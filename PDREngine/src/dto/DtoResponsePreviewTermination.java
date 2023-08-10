package dto;

public class DtoResponsePreviewTermination {
    private int ticks;
    private int seconds;

    public DtoResponsePreviewTermination(int ticks, int seconds) {
        this.ticks = ticks;
        this.seconds = seconds;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    @Override
    public String toString() {
        return "\r\nticks: " + ticks +
                "\r\nseconds: " + seconds;
    }
}
