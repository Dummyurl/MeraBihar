package app.zingo.merabihars.Camera.Internal;

/**
 * Created by ZingoHotels Tech on 05-10-2018.
 */

public interface ICallback {

    /**
     * It is called when the background operation completes. If the operation is successful, {@code
     * exception} will be {@code null}.
     */
    void done(Exception exception);
}
