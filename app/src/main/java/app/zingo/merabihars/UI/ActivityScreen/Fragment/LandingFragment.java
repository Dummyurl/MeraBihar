package app.zingo.merabihars.UI.ActivityScreen.Fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import app.zingo.merabihars.Adapter.BlogMainViewPager;
import app.zingo.merabihars.Adapter.MultiImageAdapter;
import app.zingo.merabihars.Adapter.NavigationListAdapter;
import app.zingo.merabihars.Adapter.TopActivitiesAdapter;
import app.zingo.merabihars.Adapter.ViewPagerAdapter;
import app.zingo.merabihars.Model.ActivityImages;
import app.zingo.merabihars.Model.ActivityModel;
import app.zingo.merabihars.Model.BlogDataModel;
import app.zingo.merabihars.Model.Blogs;
import app.zingo.merabihars.Model.Category;
import app.zingo.merabihars.Model.MasterSetups;
import app.zingo.merabihars.Model.NavBarItems;
import app.zingo.merabihars.Model.SubCategories;
import app.zingo.merabihars.Model.UserProfile;
import app.zingo.merabihars.R;
import app.zingo.merabihars.UI.ActivityScreen.AccountScreens.LoginScreen;
import app.zingo.merabihars.UI.ActivityScreen.AccountScreens.ProfileActivity;
import app.zingo.merabihars.UI.ActivityScreen.Events.AboutCity;
import app.zingo.merabihars.UI.ActivityScreen.Events.BlogListActivity;
import app.zingo.merabihars.UI.ActivityScreen.Events.CategoriesListScreen;
import app.zingo.merabihars.UI.ActivityScreen.Events.CategoryDetailsScreen;
import app.zingo.merabihars.UI.ActivityScreen.Events.SubCategoryDetails;
import app.zingo.merabihars.UI.ActivityScreen.Events.SubCategoryListScreen;
import app.zingo.merabihars.UI.ActivityScreen.Events.TermsPolicyScreen;
import app.zingo.merabihars.UI.ActivityScreen.Events.TouristMapImages;
import app.zingo.merabihars.UI.ActivityScreen.LandingScreen.LandingScreenActivity;
import app.zingo.merabihars.Util.Constants;
import app.zingo.merabihars.Util.Permission;
import app.zingo.merabihars.Util.PreferenceHandler;
import app.zingo.merabihars.Util.ThreadExecuter;
import app.zingo.merabihars.Util.Util;
import app.zingo.merabihars.WebApi.ActivityApi;
import app.zingo.merabihars.WebApi.BlogApi;
import app.zingo.merabihars.WebApi.CategoryApi;
import app.zingo.merabihars.WebApi.MasterAPI;
import app.zingo.merabihars.WebApi.ProfileAPI;
import app.zingo.merabihars.WebApi.SubCategoryAPI;
import app.zingo.merabihars.WebApi.UploadApi;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v4.view.GravityCompat.START;

/**
 * A simple {@link Fragment} subclass.
 */
public class LandingFragment extends Fragment {

    String TAG = "LandingScreenActivity";
    ViewPager vpPager,mtop_activities_viewpager;//mtopBlogs
    ListView mtopBlogs,mImagesList;
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout appBarLayout;
    TextView mPickInterest,moreCategories,mFirstCategory,mSecondCategory,mThirdCategory,mMoreCategory,mMoreBlogs,mMoreBlogTitles
            ,mFirstCategorySub,mSecondCategorySub,mThirdCategorySub,mMoreCategorySub,
            mAppName,mSubTitle,mWelcomeTitle,mWelcomeMessage;
    ImageView mFirstBanner,mSecondBanner,mThirdBanner,mMoreBanner;
    ImageView mFirstBannerSub,mSecondBannerSub,mThirdBannerSub,mMoreBannerSub;
    FrameLayout mFirstBannerFrame,mSecondBannerFrame,mThirdBannerFrame,mMoreBannerFrame;
    FrameLayout mFirstBannerFrameSub,mSecondBannerFrameSub,mThirdBannerFrameSub,mMoreBannerFrameSub;
    LinearLayout mCategoryLayout,mSubCategoryLayout,mActivityLayout,mBlogLayout;


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



    public LandingFragment() {
        // Required empty public constructor
    }

    public static LandingFragment newInstance(String param1, String param2) {
        LandingFragment fragment = new LandingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        try{
            View view = inflater.inflate(R.layout.fragment_landing, container, false);

            vpPager = (ViewPager) view.findViewById(R.id.pager);
            Animation downtoup = AnimationUtils.loadAnimation(getActivity(),R.anim.downtoup);

            //  drawer.setAnimation(downtoup);

            mtop_activities_viewpager = (ViewPager) view.findViewById(R.id.top_activities_viewpager);
            mtopBlogs = (ListView) view.findViewById(R.id.top_blogs_viewpager);
            mImagesList = (ListView) view.findViewById(R.id.image_list);
            mtop_activities_viewpager.setClipToPadding(false);
            // mtopBlogs.setClipToPadding(false);
            mtop_activities_viewpager.setPageMargin(18);

            //mtopBlogs.setPageMargin(12);

            mPickInterest = (TextView) view.findViewById(R.id.interest);
            mAppName = (TextView) view.findViewById(R.id.app_name);
            mSubTitle = (TextView) view.findViewById(R.id.sub_title);
            mWelcomeMessage = (TextView) view.findViewById(R.id.welcome_message);
            mWelcomeTitle = (TextView) view.findViewById(R.id.welcome_title);
            mMoreBlogs = (TextView) view.findViewById(R.id.more_blogs);
            mMoreBlogTitles = (TextView) view.findViewById(R.id.more_blog_title);

            mFirstCategory = (TextView) view.findViewById(R.id.first_banner_category_name);
            mSecondCategory = (TextView) view.findViewById(R.id.second_banner_category_name);
            mThirdCategory = (TextView) view.findViewById(R.id.third_banner_category_name);
            mMoreCategory = (TextView) view.findViewById(R.id.more_banner_category_name);
            mFirstCategorySub = (TextView) view.findViewById(R.id.first_banner_category_name_sub);
            mSecondCategorySub = (TextView) view.findViewById(R.id.second_banner_category_name_sub);
            mThirdCategorySub = (TextView) view.findViewById(R.id.third_banner_category_name_sub);
            mMoreCategorySub = (TextView) view.findViewById(R.id.more_banner_category_name_sub);

            mFirstBanner = (ImageView) view.findViewById(R.id.first_category_banner);
            mSecondBanner = (ImageView) view.findViewById(R.id.second_category_banner);
            mThirdBanner = (ImageView) view.findViewById(R.id.third_category_banner);
            mMoreBanner = (ImageView) view.findViewById(R.id.more_category_banner);
            mFirstBannerSub = (ImageView) view.findViewById(R.id.first_category_banner_sub);
            mSecondBannerSub = (ImageView) view.findViewById(R.id.second_category_banner_sub);
            mThirdBannerSub = (ImageView) view.findViewById(R.id.third_category_banner_sub);
            mMoreBannerSub = (ImageView) view.findViewById(R.id.more_category_banner_sub);

            mFirstBannerFrame = (FrameLayout) view.findViewById(R.id.first_banner_category_frame);
            mSecondBannerFrame = (FrameLayout) view.findViewById(R.id.second_banner_category_frame);
            mThirdBannerFrame = (FrameLayout) view.findViewById(R.id.third_banner_category_frame);
            mMoreBannerFrame = (FrameLayout) view.findViewById(R.id.more_banner_category_frame);
            mFirstBannerFrameSub = (FrameLayout) view.findViewById(R.id.first_banner_category_frame_sub);
            mSecondBannerFrameSub = (FrameLayout) view.findViewById(R.id.second_banner_category_frame_sub);
            mThirdBannerFrameSub = (FrameLayout) view.findViewById(R.id.third_banner_category_frame_sub);
            mMoreBannerFrameSub = (FrameLayout) view.findViewById(R.id.more_banner_category_frame_sub);

            mCategoryLayout = (LinearLayout) view.findViewById(R.id.category_collection);
            mSubCategoryLayout = (LinearLayout) view.findViewById(R.id.sub_category_collection);
            mActivityLayout = (LinearLayout) view.findViewById(R.id.activity_collection);
            mBlogLayout = (LinearLayout) view.findViewById(R.id.blogs_collection);


            mFirstBannerFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent = new Intent(getActivity(), ListOfEventsActivity.class);
                    Intent intent = new Intent(getActivity(), CategoryDetailsScreen.class);
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
                    //   Intent intent = new Intent(getActivity(), ListOfEventsActivity.class);
                    Intent intent = new Intent(getActivity(), CategoryDetailsScreen.class);
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
                    //Intent intent = new Intent(getActivity(), ListOfEventsActivity.class);
                    Intent intent = new Intent(getActivity(), CategoryDetailsScreen.class);
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
                    Intent intent = new Intent(getActivity(), SubCategoryDetails.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("SubCategory",subcategories.get(0));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            mSecondBannerFrameSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //   Intent intent = new Intent(getActivity(), ListOfEventsActivity.class);
                    Intent intent = new Intent(getActivity(), SubCategoryDetails.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("SubCategory",subcategories.get(1));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            mThirdBannerFrameSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent = new Intent(getActivity(), ListOfEventsActivity.class);
                    Intent intent = new Intent(getActivity(), SubCategoryDetails.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("SubCategory",subcategories.get(2));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            mMoreBlogs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent blogs = new Intent(getActivity(), BlogListActivity.class);
                    startActivity(blogs);
                }
            });

            mMoreBlogTitles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent blogs = new Intent(getActivity(), BlogListActivity.class);
                    startActivity(blogs);
                }
            });

            mPickInterest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* Intent intent = new Intent(getActivity(),InterestActivity.class);
                    startActivity(intent);*/
                }
            });
            mMoreBannerFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mintent = new Intent(getActivity(),CategoriesListScreen.class);
                    startActivity(mintent);
                }
            });

            mMoreBannerFrameSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mintent = new Intent(getActivity(),SubCategoryListScreen.class);
                    startActivity(mintent);
                }
            });

            vpPager.setClipToPadding(false);
            vpPager.setPageMargin(18);
            getContent("525372873773");
            getCategories();
            getSubCategories();


            setup();




            return view;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }


    private void setup() {

        getActivities();
        getBlogs();
        //getActivityImages();


    }



    public void getContent(final String uniqueid)
    {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
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
                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    public void getCategories()
    {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
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
                                    ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity(),categories);
                                    vpPager.setAdapter(adapter);

                                    if(categories.size() == 1)
                                    {
                                        Picasso.with(getActivity()).load(categories.get(0).getCategoriesImage()).placeholder(R.drawable.no_image).
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

                                        Picasso.with(getActivity()).load(categories.get(0).getCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mFirstBanner);

                                        //mFirstBanner.setImageBitmap(Util.decodeBase64(categories.get(0).getCategoriesImage()));
                                        mFirstCategory.setText(categories.get(0).getCategoriesName());

                                        Picasso.with(getActivity()).load(categories.get(1).getCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mSecondBanner);
                                        // mSecondBanner.setImageBitmap(Util.decodeBase64(categories.get(1).getCategoriesImage()));
                                        mSecondCategory.setText(categories.get(1).getCategoriesName());
                                        mThirdBannerFrame.setVisibility(View.GONE);
                                        mMoreBannerFrame.setVisibility(View.GONE);

                                    }
                                    else if(categories.size() == 3)
                                    {
                                        Picasso.with(getActivity()).load(categories.get(0).getCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mFirstBanner);
                                        // mFirstBanner.setImageBitmap(Util.decodeBase64(categories.get(0).getCategoriesImage()));
                                        mFirstCategory.setText(categories.get(0).getCategoriesName());

                                        Picasso.with(getActivity()).load(categories.get(1).getCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mSecondBanner);
                                        //mSecondBanner.setImageBitmap(Util.decodeBase64(categories.get(1).getCategoriesImage()));
                                        mSecondCategory.setText(categories.get(1).getCategoriesName());

                                        Picasso.with(getActivity()).load(categories.get(2).getCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mThirdBanner);
                                        // mThirdBanner.setImageBitmap(Util.decodeBase64(categories.get(2).getCategoriesImage()));
                                        mThirdCategory.setText(categories.get(2).getCategoriesName());

                                        mMoreBannerFrame.setVisibility(View.GONE);
                                    }
                                    else if(categories.size() > 3)
                                    {
                                        Picasso.with(getActivity()).load(categories.get(0).getCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mFirstBanner);
                                        //mFirstBanner.setImageBitmap(Util.decodeBase64(categories.get(0).getCategoriesImage()));
                                        mFirstCategory.setText(categories.get(0).getCategoriesName());

                                        Picasso.with(getActivity()).load(categories.get(1).getCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mSecondBanner);
                                        //  mSecondBanner.setImageBitmap(Util.decodeBase64(categories.get(1).getCategoriesImage()));
                                        mSecondCategory.setText(categories.get(1).getCategoriesName());

                                        Picasso.with(getActivity()).load(categories.get(2).getCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mThirdBanner);
                                        // mThirdBanner.setImageBitmap(Util.decodeBase64(categories.get(2).getCategoriesImage()));
                                        mThirdCategory.setText(categories.get(2).getCategoriesName());

                                        Picasso.with(getActivity()).load(categories.get(3).getCategoriesImage()).placeholder(R.drawable.no_image).
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
                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    public void getActivityImages()
    {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
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

                                    MultiImageAdapter blogAdapter = new MultiImageAdapter(getActivity(),activityImage);//,pagerModelArrayList);
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
                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    public void getSubCategories()
    {
       /* final ProgressDialog dialog = new ProgressDialog(getActivity());
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
                                        Picasso.with(getActivity()).load(subcategories.get(0).getSubCategoriesImage()).placeholder(R.drawable.no_image).
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

                                        Picasso.with(getActivity()).load(subcategories.get(0).getSubCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mFirstBannerSub);

                                        //mFirstBanner.setImageBitmap(Util.decodeBase64(categories.get(0).getCategoriesImage()));
                                        mFirstCategorySub.setText(subcategories.get(0).getSubCategoriesName());

                                        Picasso.with(getActivity()).load(subcategories.get(1).getSubCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mSecondBannerSub);
                                        // mSecondBanner.setImageBitmap(Util.decodeBase64(categories.get(1).getCategoriesImage()));
                                        mSecondCategorySub.setText(subcategories.get(1).getSubCategoriesName());
                                        mThirdBannerFrameSub.setVisibility(View.GONE);
                                        mMoreBannerFrameSub.setVisibility(View.GONE);

                                    }
                                    else if(subcategories.size() == 3)
                                    {
                                        Picasso.with(getActivity()).load(subcategories.get(0).getSubCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mFirstBannerSub);
                                        // mFirstBanner.setImageBitmap(Util.decodeBase64(categories.get(0).getCategoriesImage()));
                                        mFirstCategorySub.setText(subcategories.get(0).getSubCategoriesName());

                                        Picasso.with(getActivity()).load(subcategories.get(1).getSubCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mSecondBannerSub);
                                        //mSecondBanner.setImageBitmap(Util.decodeBase64(categories.get(1).getCategoriesImage()));
                                        mSecondCategorySub.setText(subcategories.get(1).getSubCategoriesName());

                                        Picasso.with(getActivity()).load(subcategories.get(2).getSubCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mThirdBannerSub);
                                        // mThirdBanner.setImageBitmap(Util.decodeBase64(categories.get(2).getCategoriesImage()));
                                        mThirdCategorySub.setText(subcategories.get(2).getSubCategoriesName());

                                        mMoreBannerFrameSub.setVisibility(View.GONE);
                                    }
                                    else if(subcategories.size() > 3)
                                    {
                                        Picasso.with(getActivity()).load(subcategories.get(0).getSubCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mFirstBannerSub);
                                        //mFirstBanner.setImageBitmap(Util.decodeBase64(categories.get(0).getCategoriesImage()));
                                        mFirstCategorySub.setText(subcategories.get(0).getSubCategoriesName());

                                        Picasso.with(getActivity()).load(subcategories.get(1).getSubCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mSecondBannerSub);
                                        //  mSecondBanner.setImageBitmap(Util.decodeBase64(categories.get(1).getCategoriesImage()));
                                        mSecondCategorySub.setText(subcategories.get(1).getSubCategoriesName());

                                        Picasso.with(getActivity()).load(subcategories.get(2).getSubCategoriesImage()).placeholder(R.drawable.no_image).
                                                error(R.drawable.no_image).into(mThirdBannerSub);
                                        // mThirdBanner.setImageBitmap(Util.decodeBase64(categories.get(2).getCategoriesImage()));
                                        mThirdCategorySub.setText(subcategories.get(2).getSubCategoriesName());

                                        Picasso.with(getActivity()).load(subcategories.get(3).getSubCategoriesImage()).placeholder(R.drawable.no_image).
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
                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    public void getActivities()
    {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
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

                                    TopActivitiesAdapter eventpagerAdapter = new TopActivitiesAdapter(getActivity(),activityModels);//,pagerModelArrayList);
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
                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });
    }

    public void getBlogs()
    {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
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

                                    BlogMainViewPager blogAdapter = new BlogMainViewPager(getActivity(),blogMultiList);//,pagerModelArrayList);
                                    mtopBlogs.setAdapter(blogAdapter);
                                    setListViewHeightBasedOnChildren(mtopBlogs);

                                }else{

                                    mBlogLayout.setVisibility(View.GONE);

                                }



                                       /* if(blogsList!=null&&blogsList.size()!=0){

                                              BlogMainAdapterList blogAdapter = new BlogMainAdapterList(getActivity(),blogsList);//,pagerModelArrayList);
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
                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });
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
    public void onResume() {
        super.onResume();
        vpPager.requestFocus();
    }


}
