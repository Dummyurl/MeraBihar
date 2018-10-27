package app.zingo.merabihars.Camera.ActivityCamera;

import android.app.Fragment;
import android.support.annotation.NonNull;

import app.zingo.merabihars.Camera.FragmentsCamera.CameraFragment;

/**
 * Created by ZingoHotels Tech on 05-10-2018.
 */

public class CaptureActivity extends BaseCaptureActivity {

    @Override
    @NonNull
    public Fragment getFragment() {
        return CameraFragment.newInstance();
    }
}