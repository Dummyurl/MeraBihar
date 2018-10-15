package app.zingo.merabihar.UI.ActivityScreen.Events;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import app.zingo.merabihar.Adapter.ActivityAdapter;
import app.zingo.merabihar.Adapter.AutoScrollAdapter;
import app.zingo.merabihar.Adapter.BlogViewPagerAdapter;
import app.zingo.merabihar.Model.ActivityModel;
import app.zingo.merabihar.Model.BlogImages;
import app.zingo.merabihar.Model.Blogs;
import app.zingo.merabihar.Model.MasterSetups;
import app.zingo.merabihar.Model.UserProfile;
import app.zingo.merabihar.R;
import app.zingo.merabihar.UI.ActivityScreen.AccountScreens.ProfileBlogList;
import app.zingo.merabihar.UI.ActivityScreen.LandingScreen.LandingScreenActivity;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.BlogApi;
import app.zingo.merabihar.WebApi.MasterAPI;
import app.zingo.merabihar.WebApi.ProfileAPI;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutBlogs extends AppCompatActivity {

    private static AutoScrollAdapter mBlogViews;
    private static ViewPager mBlogs;
    private static TextView mBlogName,mBlogCreatedBy,mBlogCreateDate,mMoreBlogs,mProfileName;
    private static LinearLayout mBlogCollection,mProfileBlog;
    private AppCompatButton mFaceBookShare;
    private CircleImageView mProfileImage;
    LinearLayout mBlogDes;

    //auto slide
    int currentPage = 0,start = 0,end = 0;
    Timer timer;
    final long DELAY_MS = 5000;
    final long PERIOD_MS = 3500;

    Blogs blogDataModel;

    //FaceBook Share
    ShareDialog shareDialog;
    Bitmap thumbnail = null;
    SharePhotoContent content;
    CallbackManager callbackManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_about_blogs);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            setTitle("Blog Detail");

            FacebookSdk.sdkInitialize(getApplicationContext());
            AppEventsLogger.activateApp(this);

           // fbLoginManager = LoginManager.getInstance();
            callbackManager = CallbackManager.Factory.create();


            mBlogViews = (AutoScrollAdapter) findViewById(R.id.about_blogs);
            mBlogViews.setStopScrollWhenTouch(true);

            mBlogName = (TextView) findViewById(R.id.blog_name_about);
            mProfileName = (TextView) findViewById(R.id.user_name);
            mProfileImage = (CircleImageView) findViewById(R.id.user_profile_photo);
            mFaceBookShare = (AppCompatButton) findViewById(R.id.share_facebook);
            mFaceBookShare.setVisibility(View.GONE);
            mBlogDes = (LinearLayout) findViewById(R.id.blog_desc);
            mBlogCreatedBy = (TextView) findViewById(R.id.blog_created_by);
            mBlogCreateDate = (TextView) findViewById(R.id.blog_date);
            mBlogCollection = (LinearLayout) findViewById(R.id.blogs_collection);
            mProfileBlog = (LinearLayout) findViewById(R.id.profile_blog);
            mMoreBlogs = (TextView) findViewById(R.id.more_blogs);
            mBlogs = (ViewPager) findViewById(R.id.blog_pager);
            mBlogs.setClipToPadding(false);
            mBlogs.setPageMargin(18);

            shareDialog = new ShareDialog(this);

            final Bundle bundle = getIntent().getExtras();
            if(bundle!=null){
                blogDataModel = (Blogs)bundle.getSerializable("Blogs");
            }
            if(blogDataModel!=null){
                setViewPager();
            }else{
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            mMoreBlogs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent blog = new Intent(AboutBlogs.this,BlogListActivity.class);
                    startActivity(blog);
                }
            });

            mProfileBlog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle profileBundle = new Bundle();
                    profileBundle.putSerializable("Profile",blogDataModel.getProfile());
                    profileBundle.putInt("ProfileId",blogDataModel.getProfileId());
                    Intent blog = new Intent(AboutBlogs.this,ProfileBlogList.class);
                    blog.putExtras(profileBundle);
                    startActivity(blog);
                }
            });

           /* ShareDialog(thumbnail);

            if(content!=null){
                mFaceBookShare.setShareContent(content);
            }*/

           mFaceBookShare.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {


                 /*  ShareLinkContent content = new ShareLinkContent.Builder()
                           .setContentUrl(Uri.parse("http://bihartourism.org/index.php?page_name=blogdetails&blogId="+blogDataModel.getBlogId()))
                           .setQuote(blogDataModel.getTitle()+" - "+blogDataModel.getShortDesc())
                           .build();
*/
                   SharePhoto photo = new SharePhoto.Builder()
                           .setBitmap(thumbnail)
                           .setCaption("Bihar")
                           .build();

                   content = new SharePhotoContent.Builder()
                           .addPhoto(photo)
                          .setRef("http://bihartourism.org/index.php?page_name=blogdetails&blogId="+blogDataModel.getBlogId())
                           .build();



                   shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);




               }
           });



        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setViewPager() {

        try{

            if(blogDataModel!=null){

                if(blogDataModel.getProfile()==null){
                    getProfile(blogDataModel.getProfileId());
                }else{

                    mProfileName.setText(""+blogDataModel.getProfile().getFullName());
                    String base=blogDataModel.getProfile().getProfilePhoto();
                    if(base != null && !base.isEmpty()){
                        Picasso.with(AboutBlogs.this).load(base).placeholder(R.drawable.profile_image).
                                error(R.drawable.profile_image).into(mProfileImage);
                        //mImage.setImageBitmap(
                        //      BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

                    }
                }

                SimpleDateFormat sdfs = new SimpleDateFormat("MMM dd,yyyy");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                String blogName = blogDataModel.getTitle();
                String blogShort = blogDataModel.getShortDesc();
                String longDesc = blogDataModel.getLongDesc();
                String createdBy = blogDataModel.getCreatedUser();
                String createdDate = blogDataModel.getCreateDate();

                ArrayList<BlogImages> blogImages = blogDataModel.getBlogImages();
                ActivityModel activity = blogDataModel.getActivities();




                if(blogName!=null&&!blogName.isEmpty()&&blogShort!=null&&!blogShort.isEmpty()){

                    mBlogName.setText(blogName+" - "+blogShort);
                }else if((blogName!=null&&!blogName.isEmpty())&&(blogShort==null||blogShort.isEmpty())) {
                    mBlogName.setText(blogName + "");
                }


                if(longDesc!=null&&!longDesc.isEmpty()){
                    //mBlogDes.setText(blogDataModel.getLongDesc());

                   // ArrayList<String> spiltString = new ArrayList<>();
                    if(blogDataModel.getLongDesc().contains("<br>")){

                        String[] arraySpilt = blogDataModel.getLongDesc().split("<br>");
                        int size = arraySpilt.length;

                        if(size!=0){

                            for(int i=0;i<size;i++){

                                //spiltString.add(arraySpilt[i]);
                                addView(arraySpilt[i]);
                            }

                            /*if(spiltString!=null&&spiltString.size()!=0){

                                TextSpiltAdapter blogAdapter = new TextSpiltAdapter(AboutBlogs.this,spiltString);//,pagerModelArrayList);
                                mBlogDes.setAdapter(blogAdapter);
                                setListViewHeightBasedOnChildren(mBlogDes);
                            }*/

                        }
                    }else{

                        //spiltString.add(blogDataModel.getLongDesc());
                        addView(blogDataModel.getLongDesc());

                        /*TextSpiltAdapter blogAdapter = new TextSpiltAdapter(AboutBlogs.this,spiltString);//,pagerModelArrayList);
                        mBlogDes.setAdapter(blogAdapter);
                        setListViewHeightBasedOnChildren(mBlogDes);*/
                    }
                }

                if(createdBy!=null&&!createdBy.isEmpty()){
                    mBlogCreatedBy.setText("By "+blogDataModel.getCreatedUser());
                }

                if(createdDate!=null&&!createdDate.isEmpty()){


                    if(createdDate.contains("T")){
                        String create[] = createdDate.split("T");
                        Date df = sdf.parse(create[0]);
                        mBlogCreateDate.setText(""+sdfs.format(df));

                    }



                }


                if(blogImages!=null&&blogImages.size()!=0){

                    ArrayList<String> blogImage = new ArrayList<>();

                    URL url = new URL(blogImages.get(0).getImage());
                    thumbnail = BitmapFactory.decodeStream(url.openConnection().getInputStream());


                    for (int i=0;i<blogImages.size();i++)
                    {
                        blogImage.add(blogImages.get(i).getImage());

                    }

                    ActivityAdapter activityImagesadapter = new ActivityAdapter(AboutBlogs.this,blogImage);
                    mBlogViews.setAdapter(activityImagesadapter);

                    final int size = blogImage.size();

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
                            mBlogViews.setCurrentItem(currentPage, true);
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


                if(activity!=null){

                    if(activity.getBlogsList()!=null&&activity.getBlogsList().size()!=0){

                        ArrayList<Blogs> blogsArrayList = activity.getBlogsList();
                        ArrayList<Blogs> activityBlogs = new ArrayList<>();



                        if(blogsArrayList!=null&&blogsArrayList.size()!=0) {

                            for (int i = 0; i < blogsArrayList.size(); i++) {

                                if (blogsArrayList.get(i).getBlogId() != blogDataModel.getBlogId()) {
                                    activityBlogs.add(blogsArrayList.get(i));
                                }
                            }


                            if (activityBlogs != null && activityBlogs.size() != 0) {
                                BlogViewPagerAdapter adapter = new BlogViewPagerAdapter(AboutBlogs.this, activityBlogs);
                                mBlogs.setAdapter(adapter);
                            } else {
                                mBlogCollection.setVisibility(View.GONE);
                            }
                        }


                        }else{
                        getBlogs(activity.getActivitiesId());
                    }





                }else{
                    getBlogs(blogDataModel.getActivitiesId());
                }




            }




        }catch (Exception e){
            e.printStackTrace();
        }






    }

    public void getBlogs(final int activityId)
    {
        final ProgressDialog dialog = new ProgressDialog(AboutBlogs.this);
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
                            ArrayList<Blogs> activityBlogs = new ArrayList<>();



                            if(blogsArrayList!=null&&blogsArrayList.size()!=0){

                                for(int i=0;i<blogsArrayList.size();i++){

                                    if(blogsArrayList.get(i).getBlogId()!=blogDataModel.getBlogId()){
                                        activityBlogs.add(blogsArrayList.get(i));
                                    }
                                }


                                if(activityBlogs!=null&&activityBlogs.size()!=0){
                                    BlogViewPagerAdapter adapter = new BlogViewPagerAdapter(AboutBlogs.this,activityBlogs);
                                    mBlogs.setAdapter(adapter);
                                }else{
                                    mBlogCollection.setVisibility(View.GONE);
                                }



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

                        Toast.makeText(AboutBlogs.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                AboutBlogs.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Call callbackManager.onActivityResult to pass login result to the LoginManager via callbackManager.
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void ShareDialog(Bitmap imagePath){
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(imagePath)
                .setCaption("Hi..")
                .build();
        content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        shareDialog.show(content);
    }


    public void getProfile(final int id){

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

                            UserProfile profile = response.body();

                            mProfileName.setText(""+profile.getFullName());

                                if(profile.getProfilePhoto()!=null){

                                    String base=profile.getProfilePhoto();
                                    if(base != null && !base.isEmpty()){
                                        Picasso.with(AboutBlogs.this).load(base).placeholder(R.drawable.profile_image).
                                                error(R.drawable.profile_image).into(mProfileImage);
                                        //mImage.setImageBitmap(
                                        //      BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

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


    public void share(){


        Uri uri = Uri.parse("http://bihartourism.org/index.php?page_name=blogdetails&blogId="+blogDataModel.getBlogId());

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, blogDataModel.getTitle());
        //shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, (String) AboutBlogs.this.getTag(R.drawable.ic_launcher));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

        PackageManager pm = getApplicationContext().getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
        for (final ResolveInfo app : activityList)
        {
            if ((app.activityInfo.name).startsWith("com.facebook.katana"))
            {
                final ActivityInfo activity = app.activityInfo;
                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                shareIntent.setComponent(name);
                getApplicationContext().startActivity(shareIntent);
                break;
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




    public void addView(String text)
    {
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try{
            View v = vi.inflate(R.layout.adapter_string_spilt, null);
            TextView mPara = (TextView)v.findViewById(R.id.para);


            mPara.setText(""+text);


            mBlogDes.addView(v);
        }catch (Exception e){
            e.printStackTrace();
        }




    }

}
