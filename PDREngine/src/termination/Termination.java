package termination;

import java.io.Serializable;

public class Termination implements Serializable {
    private final int ticks;
    private final int seconds;
    private final boolean byUser;

    public Termination(int ticks, int seconds, boolean byUser) {
        this.ticks = ticks;
        this.seconds = seconds;
        this.byUser = byUser;
    }


    public int getTicks() {
        return ticks;
    }

//    public void setTicks(int ticks) {
//        this.ticks = ticks;
//    }

    public int getSeconds() {
        return seconds;
    }

//    public void setSeconds(int seconds) {
//        this.seconds = seconds;
//    }

    public boolean isSecondsActive(long currentSeconds){
        boolean result = false;
        if (this.seconds != -1){
            if (currentSeconds/1000 < this.seconds)
            {
                result = true;
            }
        } else {
            result = true;
        }

        return result;
    }

    public boolean isTicksActive(int countTicks){
        boolean result = false;
        if (this.ticks != -1){
            if (countTicks <= this.ticks)
            {
                result = true;
            }
        } else {
            result = true;
        }

        return result;
    }

//    public void setByUser(boolean b) {
//        this.byUser = b;
//    }

    public boolean getBUser(){
        return this.byUser;
    }

    @Override
    public String toString() {
        return "\nticks: " + ticks +
                "\nseconds: " + seconds;
    }
}
