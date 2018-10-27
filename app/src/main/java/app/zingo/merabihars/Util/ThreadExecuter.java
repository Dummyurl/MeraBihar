package app.zingo.merabihars.Util;

import java.util.concurrent.Executor;

/**
 * Created by ZingoHotels Tech on 29-08-2018.
 */


public class ThreadExecuter implements Executor {
    @Override
    public void execute(Runnable command) {
        new Thread(command).start();
    }
}
