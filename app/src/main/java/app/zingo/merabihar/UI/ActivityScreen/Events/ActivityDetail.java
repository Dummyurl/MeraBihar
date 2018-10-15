package app.zingo.merabihar.UI.ActivityScreen.Events;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import app.zingo.merabihar.Adapter.ActivityAdapter;
import app.zingo.merabihar.Adapter.AutoScrollAdapter;
import app.zingo.merabihar.Adapter.BlogViewPagerAdapter;
import app.zingo.merabihar.Adapter.PackageAdapter;
import app.zingo.merabihar.CustomInterface.SortPackageDetails;
import app.zingo.merabihar.Model.ActivityModel;
import app.zingo.merabihar.Model.Blogs;
import app.zingo.merabihar.Model.Category;
import app.zingo.merabihar.Model.Maps;
import app.zingo.merabihar.Model.PackageDetails;
import app.zingo.merabihar.Model.SubCategories;
import app.zingo.merabihar.R;
import app.zingo.merabihar.Util.Constants;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.BlogApi;
import app.zingo.merabihar.WebApi.CategoryApi;
import app.zingo.merabihar.WebApi.SubCategoryAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityDetail extends AppCompatActivity {

    AutoScrollAdapter imagePager;
    //ViewPager imagePager;
    TextView mAbout,mMore,mLess,mMHigh,mLHigh,mHighLights,mName,mCate,
            mSell,mDisplay,mInterest,mBreif,mLocation,mPercent,mDuration,
            mAddress,mWhoCome,mWhatProvide,mWhatDo,mMoreBlogs;
    LinearLayout mPackageLayout;
    private static LinearLayout mBlogCollection;
    private static ViewPager mBlogs;
    ArrayList<String> activityImages;
    ArrayList<PackageDetails> activityPackages;
    RecyclerView recyclerView;
    private ActivityModel activityList;
    Button mLetsGo;
    ViewGroup rootContainer;

    int currentPage = 0,start = 0,end = 0;
    Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 1500;
    private ProgressDialog progressDialog;

    //maps related
    private GoogleMap mMap;
    MapView mapView;
    int activityId;
    Maps maps;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_detail);

            final Bundle bundle = getIntent().getExtras();
            if(bundle!=null){
                activityList = (ActivityModel)bundle.getSerializable(Constants.ACTIVITY);
            }

            imagePager = (AutoScrollAdapter) findViewById(R.id.activity_images_viewpager);
            imagePager.setStopScrollWhenTouch(true);


            mAbout = (TextView)findViewById(R.id.activity_about);
            mName = (TextView)findViewById(R.id.activity_name);
            mCate = (TextView)findViewById(R.id.activity_category);
            mSell = (TextView)findViewById(R.id.activity_sell_rate);
            mPercent = (TextView)findViewById(R.id.percentage_text);
            mDuration = (TextView)findViewById(R.id.duration);
            mDisplay = (TextView)findViewById(R.id.activity_display_rate);
            mInterest = (TextView)findViewById(R.id.activity_interest);
            mAddress = (TextView)findViewById(R.id.activity_details_activity_address);
            mBreif = (TextView)findViewById(R.id.activity_brief);
            //mLocation = (TextView)findViewById(R.id.activity_location);
            mHighLights = (TextView)findViewById(R.id.activity_highlights);
            mMore = (TextView)findViewById(R.id.read_more);
            mLess = (TextView)findViewById(R.id.read_less);
            mMHigh = (TextView)findViewById(R.id.read_more_cancel);
            mLHigh = (TextView)findViewById(R.id.read_less_cancel);
            mWhatProvide = (TextView)findViewById(R.id.activity_provide);
            mWhatDo = (TextView)findViewById(R.id.activity_do);
            mWhoCome = (TextView)findViewById(R.id.activity_come);
            mLetsGo = (Button) findViewById(R.id.lets_go);
            mapView = (MapView) findViewById(R.id.activity_details_activity_location);
            recyclerView = (RecyclerView) findViewById(R.id.package_details_recyclerview);
            mPackageLayout = (LinearLayout) findViewById(R.id.package_layout);
            mMoreBlogs = (TextView) findViewById(R.id.more_blogs);
            mMoreBlogs.setVisibility(View.GONE);
            mBlogCollection = (LinearLayout) findViewById(R.id.blogs_collection);
            mBlogs = (ViewPager) findViewById(R.id.blog_pager);
            mBlogs.setClipToPadding(false);
            mBlogs.setPageMargin(18);

        /*recyclerView.addOnItemTouchListener(new RecyclerTouchListener(ActivityDetail.this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                System.out.println("activityPackages = "+activityPackages.get(position).getName());
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));*/

            mapView.onCreate(savedInstanceState);
            mapView.onResume();

            try {
                MapsInitializer.initialize(ActivityDetail.this);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;


                    if (ActivityCompat.checkSelfPermission(ActivityDetail.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ActivityDetail.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    // mMap.getUiSettings().setZoomControlsEnabled(true);
                    //mMap.setMyLocationEnabled(true);
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mMap.getProjection().getVisibleRegion().latLngBounds.getCenter();

                    //getMapDetails();
                    try{
                        setActivty();
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            });



            mMore.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    mMore.setVisibility(View.GONE);
                    mLess.setVisibility(View.VISIBLE);
                    mAbout.setMaxLines(Integer.MAX_VALUE);

                }
            });

            mLess.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    mLess.setVisibility(View.GONE);
                    mMore.setVisibility(View.VISIBLE);
                    mAbout.setMaxLines(2);

                }
            });

            mMHigh.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    mMHigh.setVisibility(View.GONE);
                    mLHigh.setVisibility(View.VISIBLE);
                    mHighLights.setMaxLines(Integer.MAX_VALUE);

                }
            });

            mLHigh.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    mLHigh.setVisibility(View.GONE);
                    mMHigh.setVisibility(View.VISIBLE);
                    mHighLights.setMaxLines(2);

                }
            });

            mLetsGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  Intent service = new Intent(ActivityDetail.this,ActivityBook.class);
                    Bundle bundle1 = new Bundle();
                    bundle.putSerializable(Constants.ACTIVITY,activityList);
                    service.putExtras(bundle);
                    startActivity(service);*/
                }
            });



            // getAct();
        /*setActivty();*/

        /*PackageAdapter adapter = new PackageAdapter(ActivityDetail.this);
        recyclerView.setAdapter(adapter);*/

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityDetail.this.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

 /*   public void prepareExitTransition(Runnable finishRunnable) {
        if (isAlwaysFinishActivitiesEnabled() || !this.viewHolder.prepareExitTransition(finishRunnable)) {
            this.viewHolder.hideTourImage();
            if (VERSION.SDK_INT >= 21) {
                this.rootContainer.setTransitionGroup(true);
                getWindow().setTransitionBackgroundFadeDuration(0);
                getWindow().setReturnTransition(new Slide());
                getWindow().setSharedElementReturnTransition(null);
            }
            finishRunnable.run();
        }
    }*/


    private void setActivty() throws Exception{

        String validFrom = activityList.getValidFrom();
        String validTo = activityList.getValidTo();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfs = new SimpleDateFormat("MMM dd,yy");

        String duration = "",durations="";

        activityImages = new ArrayList<>();
        activityPackages =new ArrayList<>();// new ArrayList<>();

        if(validFrom!=null&&!validFrom.isEmpty()){

            if(validFrom.contains("T")){

                String arraySpilt[] = validFrom.split("T");
                String from = arraySpilt[0];
                Date fromDate = sdf.parse(from);
                duration = sdfs.format(fromDate);

            }
        }

        if(validTo!=null&&!validTo.isEmpty()){

            if(validTo.contains("T")){

                String arraySpilt[] = validTo.split("T");
                String to = arraySpilt[0];
                Date toDate = sdf.parse(to);
                durations = sdfs.format(toDate);

            }
        }

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){


            mAbout.setText(Html.fromHtml(activityList.getAboutTheActivity(), Html.FROM_HTML_MODE_COMPACT));
            mHighLights.setText(Html.fromHtml(activityList.getHighlightsOfTheActivity(), Html.FROM_HTML_MODE_COMPACT));
            mName.setText(activityList.getActivityName());
            mBreif.setText(Html.fromHtml(activityList.getDescription(), Html.FROM_HTML_MODE_COMPACT));

            mAddress.setText(Html.fromHtml(activityList.getAddress(), Html.FROM_HTML_MODE_COMPACT));
            mWhoCome.setText(Html.fromHtml(activityList.getWhoCanCome(), Html.FROM_HTML_MODE_COMPACT));
            mWhatDo.setText(Html.fromHtml(activityList.getWhatWeWillDo(), Html.FROM_HTML_MODE_COMPACT));
            mWhatProvide.setText(Html.fromHtml(activityList.getWhatIWillProvide(), Html.FROM_HTML_MODE_COMPACT));
            if(!duration.isEmpty()&&!durations.isEmpty()){

                mDuration.setText(duration+"-"+durations);
            }

        }else{


            mAbout.setText(Html.fromHtml(activityList.getAboutTheActivity()));
            mHighLights.setText(Html.fromHtml(activityList.getHighlightsOfTheActivity()));
            mName.setText(activityList.getActivityName());
            mBreif.setText(Html.fromHtml(activityList.getDescription()));

            mAddress.setText(Html.fromHtml(activityList.getAddress()));
            mWhoCome.setText(Html.fromHtml(activityList.getWhoCanCome()));
            mWhatDo.setText(Html.fromHtml(activityList.getWhatWeWillDo()));
            mWhatProvide.setText(Html.fromHtml(activityList.getWhatIWillProvide()));
            if(!duration.isEmpty()&&!durations.isEmpty()){

                mDuration.setText(duration+"-"+durations);
            }
        }

        getBlogs(activityList.getActivitiesId());





        if(activityList.getSubCategories()!=null){

            getCategory(activityList.getSubCategories().getCategoriesId(),activityList.getSubCategories().getSubCategoriesName());

        }else{
            getSubCategory(activityList.getSubCategoriesId());
        }



        final int packageSize = activityList.getPackageDetails().size();

        if(activityList.getActivityImages()!=null&&activityList.getActivityImages().size()!=0){

            for (int i=0;i<activityList.getActivityImages().size();i++)
            {
                activityImages.add(activityList.getActivityImages().get(i).getImages());
            }

            if(activityImages!=null&&activityImages.size()!=0){
                ActivityAdapter activityImagesadapter = new ActivityAdapter(ActivityDetail.this,activityImages);
                imagePager.setAdapter(activityImagesadapter);

                final int size = activityImages.size();

                final Handler handler = new Handler();
                final Runnable Update = new Runnable() {
                    public void run() {
                        if (currentPage == (size - 1)&& start == 0) {
                            currentPage = --currentPage;
                            start = 1;
                            end = 0;
                        }else if(currentPage < (size - 1) && currentPage != 0 && end == 0&& start == 1){
                            currentPage = --currentPage;
                        }else if(currentPage == 0 && end == 0 && start == 1){
                            currentPage = 0;
                            end = 1;
                            start = 0;
                        }else if(currentPage <= (size - 1) && start == 0){

                            currentPage = ++currentPage;
                        }else if(currentPage == 0&& start == 0){

                            currentPage = ++currentPage;
                        }else{

                        }
                        imagePager.setCurrentItem(currentPage, true);
                    }
                };

                timer = new Timer(); // This will create a new Thread
                timer .schedule(new TimerTask() { // task to be scheduled

                    @Override
                    public void run() {
                        handler.post(Update);
                    }
                }, DELAY_MS, PERIOD_MS);
            }


        }

      for (int i=0;i<packageSize;i++)
      {
          activityPackages.add(activityList.getPackageDetails().get(i));
          System.out.println("Package sizxe=="+activityPackages.size());
          Arrays.toString(activityPackages.toArray());
      }



        if(activityPackages != null && activityPackages.size() != 0)
        {
            Collections.sort(activityPackages,new SortPackageDetails());

            System.out.println("Package sizxe=="+activityPackages.size());

            mSell.setText("₹ "+activityPackages.get(0).getSellRate());//activityList.getSellingPrice());
            mDisplay.setText("₹ "+activityPackages.get(0).getDeclaredRate());//activityList.getDisplayPrice());
            double diff = (activityPackages.get(0).getDeclaredRate() - activityPackages.get(0).getSellRate())/activityPackages.get(0).getDeclaredRate();
            double per = diff*100;

            double diffs = (activityPackages.get(0).getDeclaredRate() -activityPackages.get(0).getSellRate())*1.0;
            double value =diffs/activityPackages.get(0).getDeclaredRate();
            double pers = value*100;
            System.out.println("per = "+per);
            DecimalFormat df = new DecimalFormat("#.##");
            mPercent.setText(df.format(pers)+" %");

            PackageAdapter adapter = new PackageAdapter(ActivityDetail.this,activityPackages);
            recyclerView.setAdapter(adapter);
           // mLetsGo.setVisibility(View.GONE);
        }else{
            mPackageLayout.setVisibility(View.GONE);
            mPercent.setText("0 %");
        }


        if(activityList.getMaps() != null && activityList.getMaps().size() != 0)
        {
            mMap.clear();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(convertToBitmap(getDrawable(R.drawable.location_icon),100,100));
                if(activityList.getActivityImages()!=null&&activityList.getActivityImages().size()!=0){

                    String url =activityList.getActivityImages().get(0).getImages();
                    icon = BitmapDescriptorFactory.fromBitmap(Util.getResizedBitmap(getBitmapFromURL(url),100));

                }

                LatLng latlng = new LatLng(Double.parseDouble(activityList.getMaps().get(0).getLatitude()),Double.parseDouble(activityList.getMaps().get(0).getLogitude()));
                marker = mMap.addMarker(new MarkerOptions()
                        .position(latlng)
                        .icon(icon));
                CameraPosition cameraPosition = new CameraPosition.Builder().zoom(14).target(latlng).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }else{
                LatLng latlng = new LatLng(Double.parseDouble(activityList.getMaps().get(0).getLatitude()),Double.parseDouble(activityList.getMaps().get(0).getLogitude()));
                marker = mMap.addMarker(new MarkerOptions()
                        .position(latlng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                CameraPosition cameraPosition = new CameraPosition.Builder().zoom(14).target(latlng).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }


        }

        //getMapDetails(activityList.getActivitiesId());

    }

    public void getBlogs(final int activityId)
    {
        final ProgressDialog dialog = new ProgressDialog(ActivityDetail.this);
        dialog.setMessage("Loading Blogs");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final BlogApi blogApi = Util.getClient().create(BlogApi.class);
                Call<ArrayList<Blogs>> getBlog = blogApi.getBlogsByActivityId(activityId);
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

                        if (response.code() == 200)
                        {

                            ArrayList<Blogs> blogsArrayList = response.body();



                                if(blogsArrayList!=null&&blogsArrayList.size()!=0){

                                    BlogViewPagerAdapter adapter = new BlogViewPagerAdapter(ActivityDetail.this,blogsArrayList);
                                    mBlogs.setAdapter(adapter);
                                    //  setListViewHeightBasedOnChildren(mtopBlogs);

                                }else{
                                    mBlogCollection.setVisibility(View.GONE);
                                }




                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Blogs>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        Toast.makeText(ActivityDetail.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });
    }

    public void getSubCategory(final int id){

      /*  final ProgressDialog dialog = new ProgressDialog(ActivityDetail.this);
        dialog.setMessage("Loading Packages");
        dialog.setCancelable(false);
        dialog.show();*/

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final SubCategoryAPI subCategoryAPI = Util.getClient().create(SubCategoryAPI.class);
                Call<SubCategories> getSub = subCategoryAPI.getSubCategoryById(id);
                //Call<ArrayList<Blogs>> getBlog = blogApi.getBlogs();

                getSub.enqueue(new Callback<SubCategories>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<SubCategories> call, Response<SubCategories> response) {
                       /* if(dialog != null)
                        {
                            dialog.dismiss();
                        }
*/
                        if (response.code() == 200)
                        {

                            SubCategories subCategories = response.body();


                            if(subCategories!=null){
                                getCategory(subCategories.getCategoriesId(),subCategories.getSubCategoriesName());
                            }else{
                                mCate.setVisibility(View.GONE);
                            }






                        }
                    }

                    @Override
                    public void onFailure(Call<SubCategories> call, Throwable t) {
                       /* if(dialog != null)
                        {
                            dialog.dismiss();
                        }
*/
                        Toast.makeText(ActivityDetail.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });
    }

    public void getCategory(final int id,final String subName){

      /*  final ProgressDialog dialog = new ProgressDialog(ActivityDetail.this);
        dialog.setMessage("Loading Packages");
        dialog.setCancelable(false);
        dialog.show();*/

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final CategoryApi subCategoryAPI = Util.getClient().create(CategoryApi.class);
                Call<Category> getcat = subCategoryAPI.getCategoryById(id);
                //Call<ArrayList<Blogs>> getBlog = blogApi.getBlogs();

                getcat.enqueue(new Callback<Category>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<Category> call, Response<Category> response) {
                       /* if(dialog != null)
                        {
                            dialog.dismiss();
                        }
*/
                        if (response.code() == 200)
                        {

                            Category categories = response.body();

                            if(categories!=null){
                                mCate.setText(""+categories.getCategoriesName()+" - "+subName);
                            }else{
                                mCate.setVisibility(View.GONE);
                            }








                        }
                    }

                    @Override
                    public void onFailure(Call<Category> call, Throwable t) {
                       /* if(dialog != null)
                        {
                            dialog.dismiss();
                        }
*/
                        Toast.makeText(ActivityDetail.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });
    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }

}
