package app.zingo.merabihar.UI.ActivityScreen.Contents;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import app.zingo.merabihar.Adapter.ActivityAdapter;
import app.zingo.merabihar.Adapter.AutoScrollAdapter;
import app.zingo.merabihar.Adapter.BlogViewPagerAdapter;
import app.zingo.merabihar.Model.ActivityModel;
import app.zingo.merabihar.Model.BlogImages;
import app.zingo.merabihar.Model.Blogs;
import app.zingo.merabihar.Model.ContentImages;
import app.zingo.merabihar.Model.Contents;
import app.zingo.merabihar.Model.UserProfile;
import app.zingo.merabihar.R;
import app.zingo.merabihar.UI.ActivityScreen.Events.AboutBlogs;
import app.zingo.merabihar.UI.ActivityScreen.Events.AboutCity;
import app.zingo.merabihar.Util.Constants;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.ProfileAPI;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConentDetailScreen extends AppCompatActivity implements YouTubePlayer.OnInitializedListener{

    private AutoScrollAdapter mBlogViews;
    private TextView mBlogName,mProfileName,mBlogDesc;
    private CircleImageView mProfileImage;

    private YouTubePlayerFragment playerFragmentTop;
    private YouTubePlayer mPlayer;
    private String YouTubeKey = Constants.YOUTUBE,url;

    //auto slide
    int currentPage = 0,start = 0,end = 0;
    Timer timer;
    final long DELAY_MS = 5000;
    final long PERIOD_MS = 3500;
    Bitmap thumbnail = null;

    Contents blogDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_conent_detail_screen);

            mBlogViews = (AutoScrollAdapter) findViewById(R.id.content_images);
            mBlogViews.setStopScrollWhenTouch(true);

            mBlogName = (TextView) findViewById(R.id.blog_name_about);
            mBlogDesc = (TextView) findViewById(R.id.blog_name_desc);
            mProfileName = (TextView) findViewById(R.id.user_name);
            mProfileImage = (CircleImageView) findViewById(R.id.user_profile_photo);
            playerFragmentTop = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_player_fragment_top);

            final Bundle bundle = getIntent().getExtras();
            if(bundle!=null){
                blogDataModel = (Contents) bundle.getSerializable("Contents");
            }

            if(blogDataModel!=null){
                setViewPager();
            }else{
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

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
                        Picasso.with(ConentDetailScreen.this).load(base).placeholder(R.drawable.profile_image).
                                error(R.drawable.profile_image).into(mProfileImage);
                        //mImage.setImageBitmap(
                        //      BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

                    }
                }

                SimpleDateFormat sdfs = new SimpleDateFormat("MMM dd,yyyy");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                String blogName = blogDataModel.getTitle();
                String blogShort = blogDataModel.getDescription();


                ArrayList<ContentImages> blogImages = blogDataModel.getContentImage();

                if(blogDataModel.getContentType().equalsIgnoreCase("Video")){

                    if(blogDataModel.getContentURL()!=null&&!blogDataModel.getContentURL().isEmpty()){

                        url = blogDataModel.getContentURL();

                        playerFragmentTop.initialize(YouTubeKey, ConentDetailScreen.this);

                    }else{
                        playerFragmentTop.getView().setVisibility(View.GONE);
                        mBlogViews.setVisibility(View.VISIBLE);
                        if(blogImages!=null&&blogImages.size()!=0){

                            ArrayList<String> blogImage = new ArrayList<>();

                            URL url = new URL(blogImages.get(0).getImages());
                            //thumbnail = BitmapFactory.decodeStream(url.openConnection().getInputStream());


                            for (int i=0;i<blogImages.size();i++)
                            {
                                blogImage.add(blogImages.get(i).getImages());

                            }

                            ActivityAdapter activityImagesadapter = new ActivityAdapter(ConentDetailScreen.this,blogImage);
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
                    }

                }else{
                    playerFragmentTop.getView().setVisibility(View.GONE);
                    mBlogViews.setVisibility(View.VISIBLE);
                    if(blogImages!=null&&blogImages.size()!=0){

                        ArrayList<String> blogImage = new ArrayList<>();

                        URL url = new URL(blogImages.get(0).getImages());
                        //thumbnail = BitmapFactory.decodeStream(url.openConnection().getInputStream());


                        for (int i=0;i<blogImages.size();i++)
                        {
                            blogImage.add(blogImages.get(i).getImages());

                        }

                        ActivityAdapter activityImagesadapter = new ActivityAdapter(ConentDetailScreen.this,blogImage);
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
                }



                mBlogName.setText(blogName + "");
                mBlogDesc.setText(blogShort + "");




            }




        }catch (Exception e){
            e.printStackTrace();
        }


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
                                    Picasso.with(ConentDetailScreen.this).load(base).placeholder(R.drawable.profile_image).
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
            player.loadVideo(url);
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

}
