package app.zingo.merabihar.UI.ActivityScreen.Fragment;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import app.zingo.merabihar.Adapter.SubCategorySpinner;
import app.zingo.merabihar.Camera.MaterialCamera;
import app.zingo.merabihar.Model.SubCategories;
import app.zingo.merabihar.R;
import app.zingo.merabihar.UI.ActivityScreen.AccountScreens.ProfileActivity;
import app.zingo.merabihar.UI.ActivityScreen.LandingScreen.LandingScreenActivity;
import app.zingo.merabihar.Util.Action;
import app.zingo.merabihar.Util.Constants;
import app.zingo.merabihar.Util.FilePaths;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.SubCategoryAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.fragment_post_new_story
 */
public class PostNewStory extends Fragment {

  EditText mTitle,mStory,mTags;
  Spinner mSubList;
  LinearLayout mImageLay,mUploadImage;
  Button mPost;

  ArrayList<SubCategories> subCategories;

  ArrayList<String> selectedImageList;
  int childcount = 0,count = 0,imageCount=0;

    private static final int REQUEST_CAMERA = 1,REQUEST_GALLERY = 2;

    public PostNewStory() {
        // Required empty public constructor
    }

    public static PostNewStory newInstance() {
        PostNewStory fragment = new PostNewStory();
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
        try{
            View view = inflater.inflate(R.layout.fragment_post_new_story, container, false);

            mTitle = (EditText)view.findViewById(R.id.blog_title);
            mStory = (EditText)view.findViewById(R.id.long_desc_blog);
            mTags = (EditText)view.findViewById(R.id.tags_blog);
            mSubList = (Spinner) view.findViewById(R.id.sub_list);
            mImageLay = (LinearLayout) view.findViewById(R.id.blog_images);
            mUploadImage = (LinearLayout) view.findViewById(R.id.image_layout);
            mPost = (Button) view.findViewById(R.id.create_blogs);

           // init();

            getSubCategories();

            mUploadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    selectImage();

                }
            });

            mTags.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    String tags = mTags.getText().toString();
                    if(tags.contains("#")){

                        Toast.makeText(getActivity(), "this is tags", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


            return view;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void selectImage()
    {
        try{
            //final String[] imageSelectionArray = {"Gallery","Take Photo","Cancel"};
            final String[] imageSelectionArray = {"Gallery","Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Select Photo");
            builder.setCancelable(false);
            builder.setItems(imageSelectionArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if(imageSelectionArray[which].equals("Gallery"))
                    {
                        boolean result= Util.checkPermissionOfCamera(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE
                                ,"This App Needs Storage Permission");
                        if(result)
                        {
                            gotoGallery();
                        }
                    }

                    else
                    {
                        dialog.dismiss();
                    }
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }



    }

    private void gotoGallery() {

        Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
        startActivityForResult(i, 2);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
        {

            if(requestCode == REQUEST_GALLERY)
            {
                onSelectImageFromGalleryResult(data);
            }
        }
    }

    private void onSelectImageFromGalleryResult(Intent data) {

        selectedImageList = new ArrayList<>();

        try{
            String[] all_path = data.getStringArrayExtra("all_path");
            if(all_path.length!=0){

                for(int i =0;i<all_path.length;i++){
                    selectedImageList.add(all_path[i]);

                }
            }
            //selectedImageList = all_path;
            mImageLay.removeAllViews();
            for (String s:all_path)
            {
                //System.out.println(s);
                File imgFile = new  File(s);
                if(imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    //addView(null,Util.getResizedBitmap(myBitmap,400));
                    addView(null,Util.getResizedBitmap(myBitmap,700));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void addView(String uri,Bitmap bitmap)
    {
        final LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try{
            View v = vi.inflate(R.layout.gallery_layout, null);
            ImageView blogs =(ImageView) v.findViewById(R.id.blog_images);



            if(uri != null)
            {

            }
            else if(bitmap != null)
            {
                blogs.setImageBitmap(bitmap);
            }


            mImageLay.addView(v);
        }catch (Exception e){
            e.printStackTrace();
        }




    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void getSubCategories()
    {
       /* final ProgressDialog dialog = new ProgressDialog(LandingScreenActivity.this);
        dialog.setMessage("Loading Categories");
        dialog.setCancelable(false);
        dialog.show();*/

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final SubCategoryAPI categoryAPI = Util.getClient().create(SubCategoryAPI.class);
                Call<ArrayList<SubCategories>> getCat = categoryAPI.getSubCategoriesByCityId(2);


                getCat.enqueue(new Callback<ArrayList<SubCategories>>() {

                    @Override
                    public void onResponse(Call<ArrayList<SubCategories>> call, Response<ArrayList<SubCategories>> response) {


                        if(response.code() == 200)
                        {
                            subCategories = response.body();
                            if(subCategories!=null&&subCategories.size()!=0){



                                SubCategorySpinner adapters = new SubCategorySpinner(getActivity(),subCategories);
                                mSubList.setAdapter(adapters);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<SubCategories>> call, Throwable t) {

                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });




            }

        });

    }

}
