package app.zingo.merabihar.Camera.ActivityCamera;

import android.app.Fragment;
import android.support.annotation.NonNull;

import app.zingo.merabihar.Camera.FragmentsCamera.Camera2Fragment;

/**
 * Created by ZingoHotels Tech on 05-10-2018.
 */

public class CaptureActivity2 extends BaseCaptureActivity {

    @Override
    @NonNull
    public Fragment getFragment() {
        return Camera2Fragment.newInstance();
    }
}
