package app.zingo.merabihars.Camera;

/**
 * Created by ZingoHotels Tech on 05-10-2018.
 */

public class TimeLimitReachedException extends Exception {

    public TimeLimitReachedException() {
        super("You've reached the time limit without starting a recording.");
    }
}
