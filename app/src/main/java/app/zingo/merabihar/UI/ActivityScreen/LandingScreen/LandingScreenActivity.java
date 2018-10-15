package app.zingo.merabihar.UI.ActivityScreen.LandingScreen;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import app.zingo.merabihar.Adapter.BlogMainViewPager;
import app.zingo.merabihar.Adapter.MultiImageAdapter;
import app.zingo.merabihar.Adapter.NavigationListAdapter;
import app.zingo.merabihar.Adapter.TopActivitiesAdapter;
import app.zingo.merabihar.Adapter.ViewPagerAdapter;
import app.zingo.merabihar.Model.ActivityImages;
import app.zingo.merabihar.Model.ActivityModel;
import app.zingo.merabihar.Model.BlogDataModel;
import app.zingo.merabihar.Model.Blogs;
import app.zingo.merabihar.Model.Category;
import app.zingo.merabihar.Model.MasterSetups;
import app.zingo.merabihar.Model.NavBarItems;
import app.zingo.merabihar.Model.SubCategories;
import app.zingo.merabihar.Model.UserProfile;
import app.zingo.merabihar.R;
import app.zingo.merabihar.UI.ActivityScreen.AccountScreens.LoginScreen;
import app.zingo.merabihar.UI.ActivityScreen.AccountScreens.ProfileActivity;
import app.zingo.merabihar.UI.ActivityScreen.Events.AboutCity;
import app.zingo.merabihar.UI.ActivityScreen.Events.BlogListActivity;
import app.zingo.merabihar.UI.ActivityScreen.Events.CategoriesListScreen;
import app.zingo.merabihar.UI.ActivityScreen.Events.CategoryDetailsScreen;
import app.zingo.merabihar.UI.ActivityScreen.Events.SubCategoryDetails;
import app.zingo.merabihar.UI.ActivityScreen.Events.SubCategoryListScreen;
import app.zingo.merabihar.UI.ActivityScreen.Events.TermsPolicyScreen;
import app.zingo.merabihar.UI.ActivityScreen.Events.TouristMapImages;
import app.zingo.merabihar.Util.Constants;
import app.zingo.merabihar.Util.Permission;
import app.zingo.merabihar.Util.PreferenceHandler;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.ActivityApi;
import app.zingo.merabihar.WebApi.BlogApi;
import app.zingo.merabihar.WebApi.CategoryApi;
import app.zingo.merabihar.WebApi.MasterAPI;
import app.zingo.merabihar.WebApi.ProfileAPI;
import app.zingo.merabihar.WebApi.SubCategoryAPI;
import app.zingo.merabihar.WebApi.UploadApi;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v4.view.GravityCompat.START;

public class LandingScreenActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener{

    //ViewPager vpPager;
    String TAG = "LandingScreenActivity";
    private static ViewPager vpPager,mtop_activities_viewpager;//mtopBlogs
    private static ListView mtopBlogs,mImagesList;
    private static CollapsingToolbarLayout collapsingToolbarLayout;
    private static AppBarLayout appBarLayout;
    private static CircleImageView mProfileImage;
    private static TextView mPickInterest,moreCategories,mFirstCategory,mSecondCategory,mThirdCategory,mMoreCategory,mUserName,mUserEmail,mMoreBlogs,mMoreBlogTitles
            ,mFirstCategorySub,mSecondCategorySub,mThirdCategorySub,mMoreCategorySub,
                   mAppName,mSubTitle,mWelcomeTitle,mWelcomeMessage;
    private static ImageView mFirstBanner,mSecondBanner,mThirdBanner,mMoreBanner;
    private static ImageView mFirstBannerSub,mSecondBannerSub,mThirdBannerSub,mMoreBannerSub;
    private static DrawerLayout drawer;
    private static ListView navbar;
    private static FrameLayout mFirstBannerFrame,mSecondBannerFrame,mThirdBannerFrame,mMoreBannerFrame;
    private static FrameLayout mFirstBannerFrameSub,mSecondBannerFrameSub,mThirdBannerFrameSub,mMoreBannerFrameSub;
    private static LinearLayout mCategoryLayout,mSubCategoryLayout,mActivityLayout,mBlogLayout,mUserLayout;


    ArrayList<Category> categories;
    ArrayList<SubCategories> subcategories;
    ArrayList<Integer> categoriesId ;
    ArrayList<Integer> activityIds ;
    ArrayList<BlogDataModel> blogDataModels;
    ArrayList<Blogs> blogsList;
    public static final int MY_PERMISSIONS_REQUEST_RESULT = 1;

   // private YouTubePlayerFragment playerFragment;
    private YouTubePlayer mPlayer;
    private String YouTubeKey = Constants.YOUTUBE;
    int userId=0;
    String userName="",userEmail="";

    UserProfile profile;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String status,selectedImage;

    //Google Login or GoogleSignUp
    String profileType="",googleImage="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            setContentView(R.layout.activity_landing_screen);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            navbar = (ListView) findViewById(R.id.navbar_list);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            checkPermission();

            Bundle bundle = getIntent().getExtras();
            if(bundle!=null){

                profile = (UserProfile)bundle.getSerializable("Profile");
                profileType = bundle.getString("ProfileType");
                googleImage = bundle.getString("ProfilePhoto");
            }

            vpPager = (ViewPager) findViewById(R.id.pager);
            Animation downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);

          //  drawer.setAnimation(downtoup);

            mtop_activities_viewpager = (ViewPager) findViewById(R.id.top_activities_viewpager);
            mtopBlogs = (ListView) findViewById(R.id.top_blogs_viewpager);
            mImagesList = (ListView) findViewById(R.id.image_list);
            mtop_activities_viewpager.setClipToPadding(false);
           // mtopBlogs.setClipToPadding(false);
            mtop_activities_viewpager.setPageMargin(18);

            //mtopBlogs.setPageMargin(12);

            mPickInterest = (TextView) findViewById(R.id.interest);
            mAppName = (TextView) findViewById(R.id.app_name);
            mSubTitle = (TextView) findViewById(R.id.sub_title);
            mWelcomeMessage = (TextView) findViewById(R.id.welcome_message);
            mWelcomeTitle = (TextView) findViewById(R.id.welcome_title);
            mUserName = (TextView) findViewById(R.id.main_user_name);
            mUserEmail = (TextView) findViewById(R.id.user_mail);
            mProfileImage = (CircleImageView) findViewById(R.id.user_image);
            mMoreBlogs = (TextView) findViewById(R.id.more_blogs);
            mMoreBlogTitles = (TextView) findViewById(R.id.more_blog_title);

            mFirstCategory = (TextView) findViewById(R.id.first_banner_category_name);
            mSecondCategory = (TextView) findViewById(R.id.second_banner_category_name);
            mThirdCategory = (TextView) findViewById(R.id.third_banner_category_name);
            mMoreCategory = (TextView) findViewById(R.id.more_banner_category_name);
            mFirstCategorySub = (TextView) findViewById(R.id.first_banner_category_name_sub);
            mSecondCategorySub = (TextView) findViewById(R.id.second_banner_category_name_sub);
            mThirdCategorySub = (TextView) findViewById(R.id.third_banner_category_name_sub);
            mMoreCategorySub = (TextView) findViewById(R.id.more_banner_category_name_sub);

            mFirstBanner = (ImageView) findViewById(R.id.first_category_banner);
            mSecondBanner = (ImageView) findViewById(R.id.second_category_banner);
            mThirdBanner = (ImageView) findViewById(R.id.third_category_banner);
            mMoreBanner = (ImageView) findViewById(R.id.more_category_banner);
            mFirstBannerSub = (ImageView) findViewById(R.id.first_category_banner_sub);
            mSecondBannerSub = (ImageView) findViewById(R.id.second_category_banner_sub);
            mThirdBannerSub = (ImageView) findViewById(R.id.third_category_banner_sub);
            mMoreBannerSub = (ImageView) findViewById(R.id.more_category_banner_sub);

            mFirstBannerFrame = (FrameLayout) findViewById(R.id.first_banner_category_frame);
            mSecondBannerFrame = (FrameLayout) findViewById(R.id.second_banner_category_frame);
            mThirdBannerFrame = (FrameLayout) findViewById(R.id.third_banner_category_frame);
            mMoreBannerFrame = (FrameLayout) findViewById(R.id.more_banner_category_frame);
            mFirstBannerFrameSub = (FrameLayout) findViewById(R.id.first_banner_category_frame_sub);
            mSecondBannerFrameSub = (FrameLayout) findViewById(R.id.second_banner_category_frame_sub);
            mThirdBannerFrameSub = (FrameLayout) findViewById(R.id.third_banner_category_frame_sub);
            mMoreBannerFrameSub = (FrameLayout) findViewById(R.id.more_banner_category_frame_sub);

            mCategoryLayout = (LinearLayout) findViewById(R.id.category_collection);
            mSubCategoryLayout = (LinearLayout) findViewById(R.id.sub_category_collection);
            mActivityLayout = (LinearLayout) findViewById(R.id.activity_collection);
            mBlogLayout = (LinearLayout) findViewById(R.id.blogs_collection);
            mUserLayout = (LinearLayout) findViewById(R.id.user_layout);

           // playerFragment =
            //        (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_player_fragment);



            userId = PreferenceHandler.getInstance(LandingScreenActivity.this).getUserId();
            userName = PreferenceHandler.getInstance(LandingScreenActivity.this).getUserFullName();
            userEmail = PreferenceHandler.getInstance(LandingScreenActivity.this).getUserName();

            if(userName!=null&&!userName.isEmpty()){

               // mUserEmail.setVisibility(View.VISIBLE);
               // mUserEmail.setText(""+userEmail);
                mUserName.setText(""+userName);
            }

            if(userEmail!=null&&!userEmail.isEmpty()){

                mUserEmail.setVisibility(View.VISIBLE);
                mUserEmail.setText(""+userEmail);
              //  mUserName.setText(""+userName);
            }

            //userEmail!=null&&!userEmail.isEmpty()

            mFirstBannerFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent = new Intent(LandingScreenActivity.this, ListOfEventsActivity.class);
                    Intent intent = new Intent(LandingScreenActivity.this, CategoryDetailsScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("title",mFirstCategory.getText().toString());
                    bundle.putInt("cat_id",categories.get(0).getCategoriesId());
                    bundle.putSerializable("Category",categories.get(0));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            mSecondBannerFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   Intent intent = new Intent(LandingScreenActivity.this, ListOfEventsActivity.class);
                    Intent intent = new Intent(LandingScreenActivity.this, CategoryDetailsScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("title",mSecondCategory.getText().toString());
                    bundle.putInt("cat_id",categories.get(1).getCategoriesId());
                    bundle.putSerializable("Category",categories.get(1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            mThirdBannerFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent = new Intent(LandingScreenActivity.this, ListOfEventsActivity.class);
                    Intent intent = new Intent(LandingScreenActivity.this, CategoryDetailsScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("title",mThirdCategory.getText().toString());
                    bundle.putInt("cat_id",categories.get(2).getCategoriesId());
                    bundle.putSerializable("Category",categories.get(2));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });


            mFirstBannerFrameSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LandingScreenActivity.this, SubCategoryDetails.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("SubCategory",subcategories.get(0));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            mSecondBannerFrameSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //   Intent intent = new Intent(LandingScreenActivity.this, ListOfEventsActivity.class);
                    Intent intent = new Intent(LandingScreenActivity.this, SubCategoryDetails.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("SubCategory",subcategories.get(1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            mThirdBannerFrameSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent = new Intent(LandingScreenActivity.this, ListOfEventsActivity.class);
                    Intent intent = new Intent(LandingScreenActivity.this, SubCategoryDetails.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("SubCategory",subcategories.get(2));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            mMoreBlogs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent blogs = new Intent(LandingScreenActivity.this, BlogListActivity.class);
                    startActivity(blogs);
                }
            });

            mMoreBlogTitles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent blogs = new Intent(LandingScreenActivity.this, BlogListActivity.class);
                    startActivity(blogs);
                }
            });

            mPickInterest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* Intent intent = new Intent(LandingScreenActivity.this,InterestActivity.class);
                    startActivity(intent);*/
                }
            });
            mMoreBannerFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mintent = new Intent(LandingScreenActivity.this,CategoriesListScreen.class);
                    startActivity(mintent);
                }
            });

            mMoreBannerFrameSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mintent = new Intent(LandingScreenActivity.this,SubCategoryListScreen.class);
                    startActivity(mintent);
                }
            });

            vpPager.setClipToPadding(false);
            vpPager.setPageMargin(18);
            getContent("525372873773");
            getCategories();
            getSubCategories();


            setup();

            setUpNavigationDrawer();

            mUserLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(userId!=0&&userName!=null&&!userName.isEmpty()&&userEmail!=null&&!userEmail.isEmpty()){

                    }else{
                        Intent login = new Intent(LandingScreenActivity.this, LoginScreen.class);
                        startActivity(login);
                    }

                }
            });

            mUserEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (profile != null) {

                        Intent edit = new Intent(LandingScreenActivity.this, ProfileActivity.class);
                        edit.putExtra("Profile",profile);
                        startActivity(edit);

                    }else if(userId!=0&&userName!=null&&!userName.isEmpty()&&userEmail!=null&&!userEmail.isEmpty()){

                        getProfile(userId,"Edit");

                    }else{
                        Intent login = new Intent(LandingScreenActivity.this, LoginScreen.class);
                        startActivity(login);
                    }

                }
            });

            mUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (profile != null) {

                        Intent edit = new Intent(LandingScreenActivity.this, ProfileActivity.class);
                        edit.putExtra("Profile",profile);
                        startActivity(edit);

                    }else if(userId!=0&&(userName!=null&&!userName.isEmpty())||(userEmail!=null&&!userEmail.isEmpty())){

                        getProfile(userId,"Edit");

                    }else{
                        Intent login = new Intent(LandingScreenActivity.this, LoginScreen.class);
                        startActivity(login);
                    }

                }
            });
            mProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (profile != null) {

                        selectImage();

                    }else if(userId!=0&&userName!=null&&!userName.isEmpty()&&userEmail!=null&&!userEmail.isEmpty()){

                        getProfile(userId,"Image");

                    }else{
                        Intent login = new Intent(LandingScreenActivity.this, LoginScreen.class);
                        startActivity(login);
                    }

                }
            });//sa00aac30955/001


            if(profile==null){
                if(userId!=0){
                    System.out.println("Going it");
                    getProfile(userId,"Empty");
                }

            }else{
                String base=profile.getProfilePhoto();
                if(base != null && !base.isEmpty()){
                    Picasso.with(LandingScreenActivity.this).load(base).placeholder(R.drawable.profile_image).
                            error(R.drawable.profile_image).into(mProfileImage);
                    //mImage.setImageBitmap(
                    //      BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setup() {

        getActivities();
        getBlogs();
        //getActivityImages();


    }

    private void setUpNavigationDrawer() {

        TypedArray icons = getResources().obtainTypedArray(R.array.navnar_item_images);
        String[] title  = getResources().getStringArray(R.array.navbar_items);

        final ArrayList<NavBarItems> navBarItemsList = new ArrayList<>();

        for (int i=0;i<title.length;i++)
        {
            NavBarItems navbarItem = new NavBarItems(title[i],icons.getResourceId(i, -1));
            navBarItemsList.add(navbarItem);
        }
        //final ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(title));
        NavigationListAdapter adapter = new NavigationListAdapter(getApplicationContext(),navBarItemsList);
        navbar.setAdapter(adapter);
        navbar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayView(navBarItemsList.get(position).getTitle());
            }
        });




    }

    public void getContent(final String uniqueid)
    {
        final ProgressDialog dialog = new ProgressDialog(LandingScreenActivity.this);
        dialog.setMessage("Loading content");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(TAG+" thread started");
                final MasterAPI categoryAPI = Util.getClient().create(MasterAPI.class);
                Call<ArrayList<MasterSetups>> getCat = categoryAPI.getContentByUniqueId(uniqueid);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<MasterSetups>>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<ArrayList<MasterSetups>> call, Response<ArrayList<MasterSetups>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(TAG+" thread inside on response cATEGORY");
                        System.out.println("Response code == "+response.code());
                        if(response.code() == 200)
                        {
                            ArrayList<MasterSetups> masterSetups = response.body();

                           if(response.body()!=null&&response.body().size()!=0){

                               mAppName.setText(""+masterSetups.get(0).getTitle());
                               mSubTitle.setText(""+masterSetups.get(0).getSubTitle());
                               mWelcomeTitle.setText(""+masterSetups.get(0).getWelcomeMessageTitle());

                               String welcome = masterSetups.get(0).getWelcomeMessageDescription();

                               if(welcome!=null&&!welcome.isEmpty()&&welcome.contains("<h1-content>")){

                                   String[] message = welcome.split("<h1-content>");
                                   mWelcomeMessage.setText(""+message[0]);
                               }


                           }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<MasterSetups>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(TAG+" thread inside on fail");
                        Toast.makeText(LandingScreenActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    public void getCategories()
    {
        final ProgressDialog dialog = new ProgressDialog(LandingScreenActivity.this);
        dialog.setMessage("Loading Categories");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(TAG+" thread started");
                final CategoryApi categoryAPI = Util.getClient().create(CategoryApi.class);
                Call<ArrayList<Category>> getCat = categoryAPI.getCategoriesByCityId(Constants.CITY_ID);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<Category>>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(TAG+" thread inside on response cATEGORY");
                        if(response.code() == 200)
                        {
                            categories = response.body();
                            //categories = new ArrayList<>();
                            categoriesId = new ArrayList<>();
                            if(response.body() != null && response.body().size() != 0)
                            {

                                /*for(int i=0;i<response.body().size();i++){
                                    if(response.body().get(i).getCityId()== Constants.CITY_ID){
                                        categories.add(response.body().get(i));
                                        categoriesId.add(response.body().get(i).getCategoriesId());

                                    }
                                }*/

                                if(categories!=null&&categories.size()!=0){

                                    System.out.println("categories = "+categories.size());
                                    System.out.println("categories = "+categoriesId.size());
                                    ViewPagerAdapter adapter = new ViewPagerAdapter(LandingScreenActivity.this,categories);
                                    vpPager.setAdapter(adapter);

                                    if(categories.size() == 1)
                                    {
                                        Picasso.with(LandingScreenActivity.this).load(categories.get(0).getCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mFirstBanner);
                                        // mFirstBanner.setImageBitmap(Util.decodeBase64(categories.get(0).getCategoriesImage()));
                                        //mFirstCategory.setAlpha(0.8f);
                                        mFirstCategory.setText(categories.get(0).getCategoriesName());
                                        mSecondBannerFrame.setVisibility(View.GONE);
                                        mThirdBannerFrame.setVisibility(View.GONE);
                                        mMoreBannerFrame.setVisibility(View.GONE);
                                    }
                                    else if(categories.size()  == 2)
                                    {

                                        Picasso.with(LandingScreenActivity.this).load(categories.get(0).getCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mFirstBanner);

                                        //mFirstBanner.setImageBitmap(Util.decodeBase64(categories.get(0).getCategoriesImage()));
                                        mFirstCategory.setText(categories.get(0).getCategoriesName());

                                        Picasso.with(LandingScreenActivity.this).load(categories.get(1).getCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mSecondBanner);
                                        // mSecondBanner.setImageBitmap(Util.decodeBase64(categories.get(1).getCategoriesImage()));
                                        mSecondCategory.setText(categories.get(1).getCategoriesName());
                                        mThirdBannerFrame.setVisibility(View.GONE);
                                        mMoreBannerFrame.setVisibility(View.GONE);

                                    }
                                    else if(categories.size() == 3)
                                    {
                                        Picasso.with(LandingScreenActivity.this).load(categories.get(0).getCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mFirstBanner);
                                        // mFirstBanner.setImageBitmap(Util.decodeBase64(categories.get(0).getCategoriesImage()));
                                        mFirstCategory.setText(categories.get(0).getCategoriesName());

                                        Picasso.with(LandingScreenActivity.this).load(categories.get(1).getCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mSecondBanner);
                                        //mSecondBanner.setImageBitmap(Util.decodeBase64(categories.get(1).getCategoriesImage()));
                                        mSecondCategory.setText(categories.get(1).getCategoriesName());

                                        Picasso.with(LandingScreenActivity.this).load(categories.get(2).getCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mThirdBanner);
                                        // mThirdBanner.setImageBitmap(Util.decodeBase64(categories.get(2).getCategoriesImage()));
                                        mThirdCategory.setText(categories.get(2).getCategoriesName());

                                        mMoreBannerFrame.setVisibility(View.GONE);
                                    }
                                    else if(categories.size() > 3)
                                    {
                                        Picasso.with(LandingScreenActivity.this).load(categories.get(0).getCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mFirstBanner);
                                        //mFirstBanner.setImageBitmap(Util.decodeBase64(categories.get(0).getCategoriesImage()));
                                        mFirstCategory.setText(categories.get(0).getCategoriesName());

                                        Picasso.with(LandingScreenActivity.this).load(categories.get(1).getCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mSecondBanner);
                                        //  mSecondBanner.setImageBitmap(Util.decodeBase64(categories.get(1).getCategoriesImage()));
                                        mSecondCategory.setText(categories.get(1).getCategoriesName());

                                        Picasso.with(LandingScreenActivity.this).load(categories.get(2).getCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mThirdBanner);
                                        // mThirdBanner.setImageBitmap(Util.decodeBase64(categories.get(2).getCategoriesImage()));
                                        mThirdCategory.setText(categories.get(2).getCategoriesName());

                                        Picasso.with(LandingScreenActivity.this).load(categories.get(3).getCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mMoreBanner);
                                        //mMoreBannerFrame.setVisibility(View.GONE);
                                    }



                                }else{
                                    mMoreBannerFrame.setVisibility(View.GONE);
                                    mThirdBannerFrame.setVisibility(View.GONE);
                                    mSecondBannerFrame.setVisibility(View.GONE);
                                    mFirstBannerFrame.setVisibility(View.GONE);
                                    mCategoryLayout.setVisibility(View.GONE);
                                    vpPager.setVisibility(View.GONE);
                                }
                                //categories = response.body();

                            }
                            else
                            {
                                mMoreBannerFrame.setVisibility(View.GONE);
                                mThirdBannerFrame.setVisibility(View.GONE);
                                mSecondBannerFrame.setVisibility(View.GONE);
                                mFirstBannerFrame.setVisibility(View.GONE);
                                mCategoryLayout.setVisibility(View.GONE);
                                vpPager.setVisibility(View.GONE);
                            }
//                            loadRoomCategoriesSpinner();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(TAG+" thread inside on fail");
                        Toast.makeText(LandingScreenActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    public void getActivityImages()
    {
        final ProgressDialog dialog = new ProgressDialog(LandingScreenActivity.this);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(TAG+" thread started");
                final ActivityApi categoryAPI = Util.getClient().create(ActivityApi.class);
                Call<ArrayList<ActivityImages>> getCat = categoryAPI.getActivityImages();
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<ActivityImages>>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<ArrayList<ActivityImages>> call, Response<ArrayList<ActivityImages>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(TAG+" thread inside on response cATEGORY");
                        if(response.code() == 200)
                        {

                            if(response.body() != null && response.body().size() != 0)
                            {


                                ArrayList<ArrayList<ActivityImages>> activityImage = new ArrayList<>();
                                int count = 0;
                                ArrayList<ActivityImages> actImages = new ArrayList<>();

                                for(int i=0;i<response.body().size();i++){



                                    actImages.add(response.body().get(i));
                                    count++;
                                    if(count==5){

                                        activityImage.add(actImages);
                                        count= 0;
                                        actImages = new ArrayList<>();

                                    }

                                }

                                if(activityImage!=null&&activityImage.size()!=0){

                                    MultiImageAdapter blogAdapter = new MultiImageAdapter(LandingScreenActivity.this,activityImage);//,pagerModelArrayList);
                                    mImagesList.setAdapter(blogAdapter);
                                    setListViewHeightBasedOnChildren(mImagesList);
                                }


                            }
                            else
                            {

                            }
//                            loadRoomCategoriesSpinner();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<ActivityImages>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(TAG+" thread inside on fail");
                        Toast.makeText(LandingScreenActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

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
                System.out.println(TAG+" thread started");
                final SubCategoryAPI categoryAPI = Util.getClient().create(SubCategoryAPI.class);
                Call<ArrayList<SubCategories>> getCat = categoryAPI.getSubCategoriesByCityId(Constants.CITY_ID);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<SubCategories>>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<ArrayList<SubCategories>> call, Response<ArrayList<SubCategories>> response) {
                       /* if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/
                        System.out.println(TAG+" thread inside on response cATEGORY");
                        if(response.code() == 200)
                        {
                            subcategories = response.body();
                            //categories = new ArrayList<>();
                           // categoriesId = new ArrayList<>();
                            if(response.body() != null && response.body().size() != 0)
                            {

                                /*for(int i=0;i<response.body().size();i++){
                                    if(response.body().get(i).getCityId()== Constants.CITY_ID){
                                        categories.add(response.body().get(i));
                                        categoriesId.add(response.body().get(i).getCategoriesId());

                                    }
                                }*/

                                if(subcategories!=null&&subcategories.size()!=0){

                                    System.out.println("categories = "+subcategories.size());
                                  //  System.out.println("categories = "+categoriesId.size());


                                    if(subcategories.size() == 1)
                                    {
                                        Picasso.with(LandingScreenActivity.this).load(subcategories.get(0).getSubCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mFirstBannerSub);
                                        // mFirstBanner.setImageBitmap(Util.decodeBase64(categories.get(0).getCategoriesImage()));
                                        //mFirstCategory.setAlpha(0.8f);
                                        mFirstCategorySub.setText(subcategories.get(0).getSubCategoriesName());
                                        mSecondBannerFrameSub.setVisibility(View.GONE);
                                        mThirdBannerFrameSub.setVisibility(View.GONE);
                                        mMoreBannerFrameSub.setVisibility(View.GONE);
                                    }
                                    else if(subcategories.size()  == 2)
                                    {

                                        Picasso.with(LandingScreenActivity.this).load(subcategories.get(0).getSubCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mFirstBannerSub);

                                        //mFirstBanner.setImageBitmap(Util.decodeBase64(categories.get(0).getCategoriesImage()));
                                        mFirstCategorySub.setText(subcategories.get(0).getSubCategoriesName());

                                        Picasso.with(LandingScreenActivity.this).load(subcategories.get(1).getSubCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mSecondBannerSub);
                                        // mSecondBanner.setImageBitmap(Util.decodeBase64(categories.get(1).getCategoriesImage()));
                                        mSecondCategorySub.setText(subcategories.get(1).getSubCategoriesName());
                                        mThirdBannerFrameSub.setVisibility(View.GONE);
                                        mMoreBannerFrameSub.setVisibility(View.GONE);

                                    }
                                    else if(subcategories.size() == 3)
                                    {
                                        Picasso.with(LandingScreenActivity.this).load(subcategories.get(0).getSubCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mFirstBannerSub);
                                        // mFirstBanner.setImageBitmap(Util.decodeBase64(categories.get(0).getCategoriesImage()));
                                        mFirstCategorySub.setText(subcategories.get(0).getSubCategoriesName());

                                        Picasso.with(LandingScreenActivity.this).load(subcategories.get(1).getSubCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mSecondBannerSub);
                                        //mSecondBanner.setImageBitmap(Util.decodeBase64(categories.get(1).getCategoriesImage()));
                                        mSecondCategorySub.setText(subcategories.get(1).getSubCategoriesName());

                                        Picasso.with(LandingScreenActivity.this).load(subcategories.get(2).getSubCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mThirdBannerSub);
                                        // mThirdBanner.setImageBitmap(Util.decodeBase64(categories.get(2).getCategoriesImage()));
                                        mThirdCategorySub.setText(subcategories.get(2).getSubCategoriesName());

                                        mMoreBannerFrameSub.setVisibility(View.GONE);
                                    }
                                    else if(subcategories.size() > 3)
                                    {
                                        Picasso.with(LandingScreenActivity.this).load(subcategories.get(0).getSubCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mFirstBannerSub);
                                        //mFirstBanner.setImageBitmap(Util.decodeBase64(categories.get(0).getCategoriesImage()));
                                        mFirstCategorySub.setText(subcategories.get(0).getSubCategoriesName());

                                        Picasso.with(LandingScreenActivity.this).load(subcategories.get(1).getSubCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mSecondBannerSub);
                                        //  mSecondBanner.setImageBitmap(Util.decodeBase64(categories.get(1).getCategoriesImage()));
                                        mSecondCategorySub.setText(subcategories.get(1).getSubCategoriesName());

                                        Picasso.with(LandingScreenActivity.this).load(subcategories.get(2).getSubCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mThirdBannerSub);
                                        // mThirdBanner.setImageBitmap(Util.decodeBase64(categories.get(2).getCategoriesImage()));
                                        mThirdCategorySub.setText(subcategories.get(2).getSubCategoriesName());

                                        Picasso.with(LandingScreenActivity.this).load(subcategories.get(3).getSubCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mMoreBannerSub);


                                        //mMoreBannerFrame.setVisibility(View.GONE);
                                    }



                                }else{
                                    mMoreBannerFrameSub.setVisibility(View.GONE);
                                    mThirdBannerFrameSub.setVisibility(View.GONE);
                                    mSecondBannerFrameSub.setVisibility(View.GONE);
                                    mFirstBannerFrameSub.setVisibility(View.GONE);
                                    mSubCategoryLayout.setVisibility(View.GONE);
                                }
                                //categories = response.body();

                            }
                            else
                            {
                                mMoreBannerFrameSub.setVisibility(View.GONE);
                                mThirdBannerFrameSub.setVisibility(View.GONE);
                                mSecondBannerFrameSub.setVisibility(View.GONE);
                                mFirstBannerFrameSub.setVisibility(View.GONE);
                                mSubCategoryLayout.setVisibility(View.GONE);
                            }
//                            loadRoomCategoriesSpinner();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<SubCategories>> call, Throwable t) {
                      /*  if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/
                        System.out.println(TAG+" thread inside on fail");
                        Toast.makeText(LandingScreenActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    public void getActivities()
    {
        final ProgressDialog dialog = new ProgressDialog(LandingScreenActivity.this);
        dialog.setMessage("Loading Places");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(TAG+" thread started");
                final ActivityApi activityApi = Util.getClient().create(ActivityApi.class);
                Call<ArrayList<ActivityModel>> getCat = activityApi.getActivityByCityId(Constants.CITY_ID);

                getCat.enqueue(new Callback<ArrayList<ActivityModel>>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<ArrayList<ActivityModel>> call, Response<ArrayList<ActivityModel>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(TAG+" thread inside on response");
                        if (response.code() == 200)
                        {
                            ArrayList<ActivityModel> activityModels = response.body();
                            activityIds = new ArrayList<>();

                            if(response.body()!=null&&response.body().size()!=0){

                                if(activityModels!=null&&activityModels.size()!=0){

                                    for(int i=0;i<activityModels.size();i++){

                                        activityIds.add(activityModels.get(i).getActivitiesId());
                                    }

                                    TopActivitiesAdapter eventpagerAdapter = new TopActivitiesAdapter(LandingScreenActivity.this,activityModels);//,pagerModelArrayList);
                                    mtop_activities_viewpager.setAdapter(eventpagerAdapter);


                                    //getBlogs();

                                }else{
                                    mActivityLayout.setVisibility(View.GONE);
                                }

                                /*if(categoriesId!=null&& categoriesId.size()!=0){
                                    for(int i=0;i<response.body().size();i++){
                                        for(int j=0;j<categoriesId.size();j++){

                                            if(categoriesId.get(j)==response.body().get(i).getSubCategories().getCategoriesId()){
                                                activityModels.add(response.body().get(i));
                                                activityModels.add(response.body().get(i));
                                            }
                                        }
                                    }



                                }*/
                            }else{
                                mActivityLayout.setVisibility(View.GONE);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<ActivityModel>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(TAG+" thread inside on fail");
                        Toast.makeText(LandingScreenActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });
    }

    public void getBlogs()
    {
        final ProgressDialog dialog = new ProgressDialog(LandingScreenActivity.this);
        dialog.setMessage("Loading Blogs");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(TAG+" thread started");
                final BlogApi blogApi = Util.getClient().create(BlogApi.class);
                Call<ArrayList<Blogs>> getBlog = blogApi.getBlogsByCityId(Constants.CITY_ID);
                //Call<ArrayList<Blogs>> getBlog = blogApi.getBlogs();

                getBlog.enqueue(new Callback<ArrayList<Blogs>>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<ArrayList<Blogs>> call, Response<ArrayList<Blogs>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(TAG+" thread inside on response");
                        if (response.code() == 200)
                        {
                            blogsList = new ArrayList<>();
                            ArrayList<Blogs> blogsArrayList = response.body();
                            ArrayList<Blogs> addList = new ArrayList<>();
                            ArrayList<ArrayList<Blogs>> blogMultiList = new ArrayList<>();

                            int count = 0;
                            int countValue =0;


                                if(blogsArrayList!=null&&blogsArrayList.size()!=0){

                                    //blogsArrayList.reverse
                                    Collections.reverse(blogsArrayList);

                                        for(int i=0;i<blogsArrayList.size();i++){

                                            if(blogsArrayList.get(i).isApproved()&&blogsArrayList.get(i).getStatus().equalsIgnoreCase("Approved")){

                                                blogsList.add(blogsArrayList.get(i));

                                                //multi list
                                                count = count+1;
                                                addList.add(blogsArrayList.get(i));

                                                if(blogsArrayList.size()==1){
                                                    blogMultiList.add(addList);
                                                    count = 0;
                                                    addList = new ArrayList<>();

                                                }else if(blogsArrayList.size()==2){

                                                    blogMultiList.add(addList);
                                                    count = 0;
                                                    addList = new ArrayList<>();

                                                }else if(blogsArrayList.size()==3){

                                                    if(count==2){
                                                        countValue = countValue+1;
                                                        blogMultiList.add(addList);
                                                       // count = 0;
                                                        addList = new ArrayList<>();

                                                        /*if(countValue >= 2){
                                                            break;
                                                        }*/

                                                    }else  if(count==3){

                                                        blogMultiList.add(addList);

                                                    }

                                                }else if(blogsArrayList.size()>=4){

                                                    if(count==2&&countValue<=2){
                                                        countValue = countValue+1;
                                                        blogMultiList.add(addList);
                                                        count = 0;
                                                        addList = new ArrayList<>();

                                                        if(countValue >= 2){
                                                            break;
                                                        }

                                                    }


                                                }

                                                /*if(count==2){
                                                    countValue = countValue+1;
                                                    blogMultiList.add(addList);
                                                    count = 0;
                                                    addList = new ArrayList<>();

                                                    if(countValue >= 2){
                                                        break;
                                                    }

                                                }*/


                                            }else{


                                            }


                                        }

                                        if (blogMultiList!=null&&blogMultiList.size()!=0){

                                            BlogMainViewPager blogAdapter = new BlogMainViewPager(LandingScreenActivity.this,blogMultiList);//,pagerModelArrayList);
                                            mtopBlogs.setAdapter(blogAdapter);
                                            setListViewHeightBasedOnChildren(mtopBlogs);

                                        }else{

                                            mBlogLayout.setVisibility(View.GONE);

                                        }



                                       /* if(blogsList!=null&&blogsList.size()!=0){

                                              BlogMainAdapterList blogAdapter = new BlogMainAdapterList(LandingScreenActivity.this,blogsList);//,pagerModelArrayList);
                                              mtopBlogs.setAdapter(blogAdapter);
                                            setListViewHeightBasedOnChildren(mtopBlogs);

                                        }else{
                                            mBlogLayout.setVisibility(View.GONE);
                                        }*/





                                }else{
                                    mBlogLayout.setVisibility(View.GONE);
                                }




                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Blogs>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(TAG+" thread inside on fail");
                        Toast.makeText(LandingScreenActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });
    }



    public void displayView(String i)
    {
        if(drawer != null)
            drawer.closeDrawer(START);


        switch (i)
        {
            case "Profile":

                Toast.makeText(LandingScreenActivity.this,"Coming soon",Toast.LENGTH_SHORT).show();
                break;

            case "Share App":

                Toast.makeText(LandingScreenActivity.this,"Coming soon",Toast.LENGTH_SHORT).show();
                break;

            case "About Bihar":
                Intent bihar = new Intent(LandingScreenActivity.this, AboutCity.class);
                startActivity(bihar);

                break;

            case "Terms & Privacy Policy":
                Intent privacy = new Intent(LandingScreenActivity.this, TermsPolicyScreen.class);
                startActivity(privacy);

                break;

            case "Categories":
                Intent mintent = new Intent(LandingScreenActivity.this,CategoriesListScreen.class);
                startActivity(mintent);

                break;

            case "Blogs":
                Intent blogs = new Intent(LandingScreenActivity.this, BlogListActivity.class);


                break;

            case "Tourist Map":
                Intent tmap = new Intent(LandingScreenActivity.this, TouristMapImages.class);
                startActivity(tmap);
                //Toast.makeText(LandingScreenActivity.this,"Bihar Coming soon",Toast.LENGTH_SHORT).show();
                break;


            case "Logout":

                Toast.makeText(LandingScreenActivity.this,"Logout",Toast.LENGTH_SHORT).show();
                break;

        }
    }

    public boolean checkPermission() {
        if ((ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
                &&(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.SEND_SMS))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE))
                    && (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CALL_PHONE))) {

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                android.Manifest.permission.SEND_SMS,
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_RESULT);
                Log.d("checkPermission if","false");

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.SEND_SMS,
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_RESULT);
                Log.d("checkPermission else","true");

            }
            return false;
        } else {
            Log.d("checkPermission else","trur");
            //mobileNumber = mCountryCode.getText().toString()+mobileNumber;
            //System.out.println("+++++"+mobileNumber);

            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RESULT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    Log.d("grantResults length", grantResults.length + "");
                    boolean sendSMsPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean accessLocation = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writeToStarage= grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean readStorage = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean call = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    if(sendSMsPermission)
                    {
                        //mobileNumber = mCountryCode.getText().toString()+mobileNumber;
                       /* SharedPreferenceHandler.getInstance(UserAuthenticationActivity.this).setUserPhoneNumber(mobileNumber);
                        Intent goToHomeIntent = new Intent(this,LandingScreenActivity.class);
                        startActivity(goToHomeIntent);*/

                    }
                }

                return;
            }
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1))+120;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        mPlayer = player;

        //Enables automatic control of orientation
        mPlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);

        //Show full screen in landscape mode always
        //mPlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);

        //System controls will appear automatically
        mPlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);

        if (!wasRestored) {
            player.cueVideo("p5UZVi3rVNs");
            //mPlayer.loadVideo("9rLZYyMbJic");
        }
        else
        {
            mPlayer.play();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        mPlayer = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        vpPager.requestFocus();
        //playerFragment.initialize(YouTubeKey, this);
    }


    public void getProfile(final int id,final String image){

      /*  final ProgressDialog dialog = new ProgressDialog(ActivityDetail.this);
        dialog.setMessage("Loading Packages");
        dialog.setCancelable(false);
        dialog.show();*/

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ProfileAPI subCategoryAPI = Util.getClient().create(ProfileAPI.class);
                Call<UserProfile> getProf = subCategoryAPI.getProfileById(id);
                //Call<ArrayList<Blogs>> getBlog = blogApi.getBlogs();

                getProf.enqueue(new Callback<UserProfile>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                       /* if(dialog != null)
                        {
                            dialog.dismiss();
                        }
*/
                        if (response.code() == 200)
                        {
                            System.out.println("Inside api");

                            profile = response.body();

                            if(image!=null&&!image.isEmpty()){

                                if(image.equalsIgnoreCase("Image")){

                                    selectImage();
                                }else if(image.equalsIgnoreCase("Edit")){
                                    Intent edit = new Intent(LandingScreenActivity.this, ProfileActivity.class);
                                    edit.putExtra("Profile",profile);
                                    startActivity(edit);
                                }else{
                                    if(profile.getProfilePhoto()!=null){

                                        String base=profile.getProfilePhoto();
                                        if(base != null && !base.isEmpty()){
                                            Picasso.with(LandingScreenActivity.this).load(base).placeholder(R.drawable.profile_image).
                                                    error(R.drawable.profile_image).into(mProfileImage);
                                            //mImage.setImageBitmap(
                                            //      BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

                                        }
                                    }
                                }
                            }else{

                                if(profile.getProfilePhoto()!=null){

                                    String base=profile.getProfilePhoto();
                                    if(base != null && !base.isEmpty()){
                                        Picasso.with(LandingScreenActivity.this).load(base).placeholder(R.drawable.profile_image).
                                                error(R.drawable.profile_image).into(mProfileImage);
                                        //mImage.setImageBitmap(
                                        //      BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

                                    }
                                }
                            }



                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {
                       /* if(dialog != null)
                        {
                            dialog.dismiss();
                        }
*/
                        //Toast.makeText(.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });
    }

    private void selectImage() {
        final CharSequence[] items = {"Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(LandingScreenActivity.this);
        builder.setTitle("Add Image!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Permission.checkPermission(LandingScreenActivity.this);
                if (items[item].equals("Choose from Library")) {

                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);

        }
    }

    private void onSelectFromGalleryResult(Intent data) {

        try{


            Uri selectedImageUri = data.getData( );
            String picturePath = getPath( LandingScreenActivity.this, selectedImageUri );
            Log.d("Picture Path", picturePath);
            String[] all_path = {picturePath};
            selectedImage = all_path[0];
            System.out.println("allpath === "+data.getPackage());
            for (String s:all_path)
            {
                //System.out.println(s);
                File imgFile = new  File(s);
                if(imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    //addView(null,Util.getResizedBitmap(myBitmap,400));
                    addImage(null,Util.getResizedBitmap(myBitmap,700));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public static String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }

    public void addImage(String uri,Bitmap bitmap)
    {
        try{


            if(uri != null)
            {

            }
            else if(bitmap != null)
            {
                mProfileImage.setImageBitmap(bitmap);

                if(selectedImage != null && !selectedImage.isEmpty())
                {
                    File file = new File(selectedImage);

                    if(file.length() <= 1*1024*1024)
                    {
                        FileOutputStream out = null;
                        String[] filearray = selectedImage.split("/");
                        final String filename = getFilename(filearray[filearray.length-1]);

                        out = new FileOutputStream(filename);
                        Bitmap myBitmap = BitmapFactory.decodeFile(selectedImage);

//          write the compressed bitmap at the destination specified by filename.
                        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                        uploadImage(filename,profile);



                    }
                    else
                    {
                        compressImage(selectedImage,profile);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void uploadImage(final String filePath,final UserProfile userProfile)
    {
        //String filePath = getRealPathFromURIPath(uri, ImageUploadActivity.this);

        final File file = new File(filePath);
        int size = 1*1024*1024;

        if(file != null)
        {
            if(file.length() > size)
            {
                System.out.println(file.length());
                compressImage(filePath,userProfile);
            }
            else
            {
                final ProgressDialog dialog = new ProgressDialog(LandingScreenActivity.this);
                dialog.setCancelable(false);
                dialog.setTitle("Uploading Image..");
                dialog.show();
                Log.d("Image Upload", "Filename " + file.getName());

                RequestBody mFile = RequestBody.create(MediaType.parse("image"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
                UploadApi uploadImage = Util.getClient().create(UploadApi.class);

                Call<String> fileUpload = uploadImage.uploadProfileImages(fileToUpload, filename);
                fileUpload.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        userProfile.setProfilePhoto(Constants.IMAGE_URL+response.body().toString());


                            updateProfile(userProfile);



                        if(filePath.contains("MyFolder/Images"))
                        {
                            file.delete();
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("UpdateCate", "Error " + t.getMessage());
                    }
                });
            }
        }
    }

    public String compressImage(String filePath,final  UserProfile userProfile) {

        //String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = actualHeight/2;//2033.0f;
        float maxWidth = actualWidth/2;//1011.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String[] filearray = filePath.split("/");
        final String filename = getFilename(filearray[filearray.length-1]);
        try {
            out = new FileOutputStream(filename);


//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            uploadImage(filename,userProfile);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public String getFilename(String filePath) {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        System.out.println("getFilePath = "+filePath);
        String uriSting;
        if(filePath.contains(".jpg"))
        {
            uriSting = (file.getAbsolutePath() + "/" + filePath);
        }
        else
        {
            uriSting = (file.getAbsolutePath() + "/" + filePath+".jpg" );
        }
        return uriSting;

    }

    private void updateProfile(final UserProfile userProfile) {


        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("Updating Image..");
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ProfileAPI auditApi = Util.getClient().create(ProfileAPI.class);
                Call<UserProfile> response = auditApi.updateProfile(userProfile.getProfileId(),userProfile);
                response.enqueue(new Callback<UserProfile>() {
                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {
                            Toast.makeText(LandingScreenActivity.this,"Profile Image Updated",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(LandingScreenActivity.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(LandingScreenActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LandingScreenActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
