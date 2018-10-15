package app.zingo.merabihar.UI.ActivityScreen.Fragment;


import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import app.zingo.merabihar.R;
import app.zingo.merabihar.Util.Util;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.fragment_gallery_fragment_post
 */
public class GalleryFragmentPost extends Fragment {


    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    List<String> imagesEncodedList;

    static int REQUEST_GALLERY = 200;
    static String selectedImageList[];



    public GalleryFragmentPost() {
        // Required empty public constructor
    }

    public static GalleryFragmentPost newInstance() {
        GalleryFragmentPost fragment = new GalleryFragmentPost();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            View view = inflater.inflate(R.layout.fragment_gallery_fragment_post, container, false);

            gotoGallery();
            return view;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private void gotoGallery() {

        Intent i = new Intent("luminous.ACTION_MULTIPLE_PICK");
        getParentFragment().startActivityForResult(i, 200);
    }



    public static void onSelectImageFromGalleryResult(Intent data) {


        try{
            String[] all_path = data.getStringArrayExtra("all_path");
            selectedImageList = all_path;
            for (String s:all_path)
            {
                //System.out.println(s);
                File imgFile = new  File(s);
                if(imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    //addView(null,Util.getResizedBitmap(myBitmap,400));
                    //addView(null, Util.getResizedBitmap(myBitmap,700));
                    System.out.println("IMage "+imgFile.getAbsolutePath());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    @Override
    public void onResume() {
        super.onResume();

    }


}
