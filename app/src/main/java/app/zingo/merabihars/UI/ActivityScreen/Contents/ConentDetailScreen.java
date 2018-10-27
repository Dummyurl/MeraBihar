package app.zingo.merabihars.UI.ActivityScreen.Contents;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import app.zingo.merabihars.Adapter.ActivityAdapter;
import app.zingo.merabihars.Adapter.AutoScrollAdapter;
import app.zingo.merabihars.Adapter.BlogViewPagerAdapter;
import app.zingo.merabihars.Adapter.ContentCategoryRecyclerAdapter;
import app.zingo.merabihars.Model.ActivityModel;
import app.zingo.merabihars.Model.BlogImages;
import app.zingo.merabihars.Model.Blogs;
import app.zingo.merabihars.Model.Comments;
import app.zingo.merabihars.Model.ContentImages;
import app.zingo.merabihars.Model.Contents;
import app.zingo.merabihars.Model.Likes;
import app.zingo.merabihars.Model.ProfileFollowMapping;
import app.zingo.merabihars.Model.UserProfile;
import app.zingo.merabihars.Model.VideoTrackers;
import app.zingo.merabihars.R;
import app.zingo.merabihars.UI.ActivityScreen.AccountScreens.LoginScreen;
import app.zingo.merabihars.UI.ActivityScreen.AccountScreens.SignUpScreen;
import app.zingo.merabihars.UI.ActivityScreen.Events.AboutBlogs;
import app.zingo.merabihars.UI.ActivityScreen.Events.AboutCity;
import app.zingo.merabihars.Util.Constants;
import app.zingo.merabihars.Util.PreferenceHandler;
import app.zingo.merabihars.Util.ThreadExecuter;
import app.zingo.merabihars.Util.Util;
import app.zingo.merabihars.WebApi.CommentAPI;
import app.zingo.merabihars.WebApi.LikeAPI;
import app.zingo.merabihars.WebApi.ProfileAPI;
import app.zingo.merabihars.WebApi.ProfileFollowAPI;
import app.zingo.merabihars.WebApi.VideoTrackersAPI;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConentDetailScreen extends AppCompatActivity implements YouTubePlayer.OnInitializedListener{

    private AutoScrollAdapter mBlogViews;
    private TextView mBlogName,mProfileName,mBlogDesc,mViewCount,
            mLikeCount,mLikedId,mDislikeCount,mDislikedId,mCommentCount,mFollowing;
    private CircleImageView mProfileImage;
    ImageView imageView,mIcon,mLike,mLiked,mDislike,mDisliked
            ,mComment,mWhatsapp,mBack,mMenu;
    LinearLayout mFollowLay,mShareLay,mSettingLay;

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
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
            mBlogViews = (AutoScrollAdapter) findViewById(R.id.content_images);
            mBlogViews.setStopScrollWhenTouch(true);

            mBlogName = (TextView) findViewById(R.id.blog_name_about);
            mBlogDesc = (TextView) findViewById(R.id.blog_name_desc);
            mViewCount = (TextView) findViewById(R.id.view_count);
            mProfileName = (TextView) findViewById(R.id.user_name);
            mProfileImage = (CircleImageView) findViewById(R.id.user_profile_photo);
            mLikedId = (TextView) findViewById(R.id.liked_id);
            mDislikedId = (TextView) findViewById(R.id.disliked_id);
            mLike = (ImageView) findViewById(R.id.like_icon);
            mLiked = (ImageView) findViewById(R.id.liked_icon);
            mDislike = (ImageView) findViewById(R.id.dislike_icon);
            mDisliked = (ImageView) findViewById(R.id.disliked_icon);
            mComment = (ImageView) findViewById(R.id.comments_icon);
            mWhatsapp = (ImageView) findViewById(R.id.whatsaapp_share);
            mCommentCount = (TextView) findViewById(R.id.comments_count);
            mFollowing = (TextView) findViewById(R.id.follow);
            mLikeCount = (TextView) findViewById(R.id.like_count);
            mDislikeCount = (TextView) findViewById(R.id.dislike_count);
            mBack = (ImageView)findViewById(R.id.back);
            mMenu = (ImageView)findViewById(R.id.expanded_menu);
            mSettingLay = (LinearLayout) findViewById(R.id.setting_menu);
            mFollowLay = (LinearLayout) findViewById(R.id.follow_lay);
            mShareLay = (LinearLayout) findViewById(R.id.share_layout);
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

            mBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ConentDetailScreen.this.finish();
                }
            });

            mMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(mSettingLay.getVisibility() == View.VISIBLE){
                        mSettingLay.setVisibility(View.GONE);
                    }else{

                        mSettingLay.setVisibility(View.VISIBLE);
                    }


                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setViewPager() {

        try{

            final int profileId = PreferenceHandler.getInstance(ConentDetailScreen.this).getUserId();

            if(blogDataModel!=null){

                String vWatch = "W"+blogDataModel.getContentId();

                if(vWatch!=null&&!vWatch.isEmpty()){
                    getLiveCount(vWatch);
                }

                int watchId = PreferenceHandler.getInstance(ConentDetailScreen.this).getVideoId();

                if(watchId!=0){

                    //int profileId = PreferenceHandler.getInstance(ConentDetailScreen.this).getUserId();

                    if(profileId!=0){

                        VideoTrackers vd = new VideoTrackers();
                        vd.setTrackerId(watchId);
                        vd.setContentId(blogDataModel.getContentId());
                        vd.setProfileId(profileId);
                        vd.setVideoReferralCodeUsedToWatch("W"+blogDataModel.getContentId());
                        vd.setStatus("Watching");
                        updateVideo(vd);
                    }


                }else{

                   // int profileId = PreferenceHandler.getInstance(ConentDetailScreen.this).getUserId();

                    if(profileId!=0){

                        VideoTrackers vd = new VideoTrackers();
                        vd.setContentId(blogDataModel.getContentId());
                        vd.setProfileId(profileId);
                        vd.setVideoReferralCodeUsedToWatch("W"+blogDataModel.getContentId());
                        vd.setStatus("Watching");
                        postVideo(vd);

                    }else{

                    }


                }

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


                if(blogDataModel.getCommentsList()!=null&&blogDataModel.getCommentsList().size()!=0){

                    mCommentCount.setText(""+blogDataModel.getCommentsList().size());
                }

                if(blogDataModel.getLikes()!=null&&blogDataModel.getLikes().size()!=0){

                    ArrayList<Likes> liked = new ArrayList<>();
                    ArrayList<Likes> disliked = new ArrayList<>();

                    boolean profileLike = false;
                    boolean profileDislike = false;

                    for (Likes likes:blogDataModel.getLikes()) {

                        if(likes.isLiked()){
                            liked.add(likes);

                            if(profileId!=0){
                                if(likes.getProfileId()==profileId){
                                    profileLike = true;
                                    mLikedId.setText(""+likes.getLikeId());
                                }
                            }


                        }else{
                            disliked.add(likes);

                            if(profileId!=0){
                                if(likes.getProfileId()==profileId){
                                    profileDislike = true;
                                    mDislikedId.setText(""+likes.getLikeId());
                                }
                            }

                        }

                    }


                    if(liked!=null&&liked.size()!=0){

                        mLikeCount.setText(""+liked.size());

                    }

                    if(disliked!=null&&disliked.size()!=0){

                        mDislikeCount.setText(""+disliked.size());
                    }

                    if(profileLike){

                        mLiked.setVisibility(View.VISIBLE);
                        mLike.setVisibility(View.GONE);
                    }

                    if(profileDislike){

                        mDisliked.setVisibility(View.VISIBLE);
                        mDislike.setVisibility(View.GONE);
                    }
                }




            }


            mLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(profileId!=0){

                        mLike.setEnabled(false);
                        Likes likes = new Likes();
                        likes.setContentId(blogDataModel.getContentId());
                        likes.setProfileId(profileId);
                        likes.setLiked(true);

                        if(mDisliked.getVisibility()==View.VISIBLE){

                            if(mDislikedId.getText().toString()!=null&&!mDislikedId.getText().toString().isEmpty()){


                                deleteDisLike(likes,mLike,mLiked,mLikeCount,Integer.parseInt(mDislikedId.getText().toString()),mDisliked,mDislike,mDislikedId,mDislikeCount,mLikedId);
                            }
                        }else{

                            postLike(likes,mLike,mLiked,mLikeCount,mLikedId);
                        }



                    }else{

                        new AlertDialog.Builder(ConentDetailScreen.this)
                                .setMessage("Please login/Signup to Like the Story")
                                .setCancelable(false)
                                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent login = new Intent(ConentDetailScreen.this, LoginScreen.class);
                                        startActivity(login);

                                    }
                                })
                                .setNegativeButton("SignUp", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent signUp = new Intent(ConentDetailScreen.this, SignUpScreen.class);
                                        startActivity(signUp);

                                    }
                                })
                                .show();

                    }




                }
            });

            mDislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(profileId!=0){

                        mDislike.setEnabled(false);
                        Likes likes = new Likes();
                        likes.setContentId(blogDataModel.getContentId());
                        likes.setProfileId(profileId);
                        likes.setLiked(false);

                        if(mLiked.getVisibility()==View.VISIBLE){

                            if(mLikedId.getText().toString()!=null&&!mLikedId.getText().toString().isEmpty()){


                                deleteLike(likes,mDislike,mDisliked,mDislikeCount,Integer.parseInt(mLikedId.getText().toString()),mLiked,mLike,mLikedId,mLikeCount,mDislikedId);
                            }
                        }else{


                            postDisLike(likes,mDislike,mDisliked,mDislikeCount,mDislikedId);
                        }


                    }else{

                        new AlertDialog.Builder(ConentDetailScreen.this)
                                .setMessage("Please login/Signup to Dislike the Story")
                                .setCancelable(false)
                                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent login = new Intent(ConentDetailScreen.this, LoginScreen.class);
                                        startActivity(login);

                                    }
                                })
                                .setNegativeButton("SignUp", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent signUp = new Intent(ConentDetailScreen.this, SignUpScreen.class);
                                        startActivity(signUp);

                                    }
                                })
                                .show();
                    }




                }
            });

            mWhatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AsyncTask mMyTask;
                    if(blogDataModel.getContentType().equalsIgnoreCase("Video")) {

                        url = blogDataModel.getContentURL();


                        if (url != null && !url.isEmpty()) {
                            mMyTask = new DownloadTask()
                                    .execute(stringToURL(
                                            "https://img.youtube.com/vi/"+url+"/0.jpg"
                                    ));

                        }

                    }else{

                        mMyTask = new DownloadTask()
                                .execute(stringToURL(
                                        ""+blogDataModel.getContentImage().get(0).getImages()
                                ));
                    }
                }
            });

            

            mComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(profileId!=0){

                        Intent comments = new Intent(ConentDetailScreen.this, app.zingo.merabihars.UI.ActivityScreen.Contents.CommentsScreen.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Contents",blogDataModel);

                        comments.putExtras(bundle);
                        startActivity(comments);

                    }else{

                        new AlertDialog.Builder(ConentDetailScreen.this)
                                .setMessage("Please login/Signup to Like the Story")
                                .setCancelable(false)
                                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent login = new Intent(ConentDetailScreen.this, LoginScreen.class);
                                        startActivity(login);

                                    }
                                })
                                .setNegativeButton("SignUp", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent signUp = new Intent(ConentDetailScreen.this, SignUpScreen.class);
                                        startActivity(signUp);

                                    }
                                })
                                .show();

                    }




                }
            });

            mShareLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    AsyncTask mMyTasks;
                    if(blogDataModel.getContentType().equalsIgnoreCase("Video")) {

                        url = blogDataModel.getContentURL();


                        if (url != null && !url.isEmpty()) {
                            mMyTasks = new DownloadTasks()
                                    .execute(stringToURL(
                                            "https://img.youtube.com/vi/"+url+"/0.jpg"
                                    ));

                        }

                    }else{

                        mMyTasks = new DownloadTasks()
                                .execute(stringToURL(
                                        ""+blogDataModel.getContentImage().get(0).getImages()
                                ));
                    }

                }
            });

            if(profileId!=0){

                if(profileId==blogDataModel.getProfileId()){

                    mFollowLay.setVisibility(View.GONE);
                }else{
                    getFollowingByProfileId(profileId,mFollowLay,blogDataModel.getProfileId());

                }

            }


            mFollowLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(profileId!=0){

                        if(mFollowing.getText().toString().equalsIgnoreCase("Follow")){
                            mFollowing.setEnabled(false);
                            ProfileFollowMapping pm = new ProfileFollowMapping();
                            pm.setFollowerId(blogDataModel.getProfileId());
                            pm.setProfileId(PreferenceHandler.getInstance(ConentDetailScreen.this).getUserId());
                            profileFollow(pm,mFollowing);
                        }else{

                            Toast.makeText(ConentDetailScreen.this, "Already you followed "+blogDataModel.getCreatedBy(), Toast.LENGTH_SHORT).show();
                        }

                    }else{

                        new AlertDialog.Builder(ConentDetailScreen.this)
                                .setMessage("Please login/Signup to Like the Story")
                                .setCancelable(false)
                                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent login = new Intent(ConentDetailScreen.this, LoginScreen.class);
                                        startActivity(login);

                                    }
                                })
                                .setNegativeButton("SignUp", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent signUp = new Intent(ConentDetailScreen.this, SignUpScreen.class);
                                        startActivity(signUp);

                                    }
                                })
                                .show();

                    }
                }
            });

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


    public void getLiveCount(final String id){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final VideoTrackersAPI subCategoryAPI = Util.getClient().create(VideoTrackersAPI.class);
                Call<ArrayList<UserProfile>> getProf = subCategoryAPI.getLiveWatchers(id);

                getProf.enqueue(new Callback<ArrayList<UserProfile>>() {

                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {

                        if (response.code() == 200)
                        {
                            System.out.println("Inside api");

                            ArrayList<UserProfile> profile = response.body();

                            if(profile!=null&&profile.size()!=0){

                                mViewCount.setText(""+profile.size());
                                new CountDownTimer(30000, 1000) {

                                    public void onTick(long millisUntilFinished) {
                                        //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                                        //here you can have your logic to set text to edittext
                                    }

                                    public void onFinish() {
                                        //mTextField.setText("done!");
                                        getLiveCount(id);
                                    }

                                }.start();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {

                    }
                });

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

    private void postVideo(final VideoTrackers vd) {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                VideoTrackersAPI blogsAPI = Util.getClient().create(VideoTrackersAPI.class);
                Call<VideoTrackers> response = blogsAPI.postVideoTrackers(vd);
                response.enqueue(new Callback<VideoTrackers>() {
                    @Override
                    public void onResponse(Call<VideoTrackers> call, Response<VideoTrackers> response) {

                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {

                         /*   getContents(contents.getContentId());
                            comment.setText("");*/

                            if(response.body()!=null){

                                PreferenceHandler.getInstance(ConentDetailScreen.this).setVideoId(response.body().getTrackerId());
                                getLiveCount(response.body().getVideoReferralCodeUsedToWatch());
                            }

                        }
                        else
                        {
                            Toast.makeText(ConentDetailScreen.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoTrackers> call, Throwable t) {

                        Toast.makeText(ConentDetailScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }


    private void updateVideo(final VideoTrackers vd) {

        /*final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.show();*/



        //System.out.println(sub.getCategoriesName()+","+sub.getDescription()+","+sub.getOrderNo()+","+sub.getCityId());

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                VideoTrackersAPI blogsAPI = Util.getClient().create(VideoTrackersAPI.class);
                Call<VideoTrackers> response = blogsAPI.updateVideoTrackers(vd.getTrackerId(),vd);
                response.enqueue(new Callback<VideoTrackers>() {
                    @Override
                    public void onResponse(Call<VideoTrackers> call, Response<VideoTrackers> response) {
                      /*  if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/
                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {

                         /*   getContents(contents.getContentId());
                            comment.setText("");*/

                            if(response.body()!=null){

                                //PreferenceHandler.getInstance(ConentDetailScreen.this).setVideoId(response.body().getTrackerId());
                              //
                            }

                            getLiveCount(vd.getVideoReferralCodeUsedToWatch());

                        }
                        else
                        {
                            Toast.makeText(ConentDetailScreen.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoTrackers> call, Throwable t) {
                      /*  if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/
                        Toast.makeText(ConentDetailScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    private void postComment(final Comments blogs) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.show();



        //System.out.println(sub.getCategoriesName()+","+sub.getDescription()+","+sub.getOrderNo()+","+sub.getCityId());

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                CommentAPI blogsAPI = Util.getClient().create(CommentAPI.class);
                Call<Comments> response = blogsAPI.postComment(blogs);
                response.enqueue(new Callback<Comments>() {
                    @Override
                    public void onResponse(Call<Comments> call, Response<Comments> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {

                         /*   getContents(contents.getContentId());
                            comment.setText("");*/

                        }
                        else
                        {
                            Toast.makeText(ConentDetailScreen.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Comments> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(ConentDetailScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        int watchId = PreferenceHandler.getInstance(ConentDetailScreen.this).getVideoId();
        if(watchId!=0){

            int profileId = PreferenceHandler.getInstance(ConentDetailScreen.this).getUserId();

            if(profileId!=0){

                VideoTrackers vd = new VideoTrackers();
                vd.setTrackerId(watchId);
                vd.setContentId(blogDataModel.getContentId());
                vd.setProfileId(profileId);
                vd.setVideoReferralCodeUsedToWatch("W"+blogDataModel.getContentId());
                vd.setStatus("Watching");
                updateVideo(vd);
            }


        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        int watchId = PreferenceHandler.getInstance(ConentDetailScreen.this).getVideoId();
        if(watchId!=0){

            int profileId = PreferenceHandler.getInstance(ConentDetailScreen.this).getUserId();

            if(profileId!=0){

                VideoTrackers vd = new VideoTrackers();
                vd.setTrackerId(watchId);
                vd.setContentId(blogDataModel.getContentId());
                vd.setProfileId(profileId);
                vd.setVideoReferralCodeUsedToWatch("W"+blogDataModel.getContentId());
                vd.setStatus("Paused");
                updateVideo(vd);
            }


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        int watchId = PreferenceHandler.getInstance(ConentDetailScreen.this).getVideoId();
        if(watchId!=0){

            int profileId = PreferenceHandler.getInstance(ConentDetailScreen.this).getUserId();

            if(profileId!=0){

                VideoTrackers vd = new VideoTrackers();
                vd.setTrackerId(watchId);
                vd.setContentId(blogDataModel.getContentId());
                vd.setProfileId(profileId);
                vd.setVideoReferralCodeUsedToWatch("W"+blogDataModel.getContentId());
                vd.setStatus("Watched");
                updateVideo(vd);
            }


        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        int watchId = PreferenceHandler.getInstance(ConentDetailScreen.this).getVideoId();
        if(watchId!=0){

            int profileId = PreferenceHandler.getInstance(ConentDetailScreen.this).getUserId();

            if(profileId!=0){

                VideoTrackers vd = new VideoTrackers();
                vd.setTrackerId(watchId);
                vd.setContentId(blogDataModel.getContentId());
                vd.setProfileId(profileId);
                vd.setVideoReferralCodeUsedToWatch("W"+blogDataModel.getContentId());
                vd.setStatus("Stop");
                updateVideo(vd);
            }


        }
    }

    private void postLike(final Likes likes, final ImageView like,final ImageView liked,final TextView likeCount,final TextView likedId) {



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LikeAPI mapApi = Util.getClient().create(LikeAPI.class);
                Call<Likes> response = mapApi.postLikes(likes);
                response.enqueue(new Callback<Likes>() {
                    @Override
                    public void onResponse(Call<Likes> call, Response<Likes> response) {

                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {

                            like.setVisibility(View.GONE);
                            liked.setVisibility(View.VISIBLE);
                            likedId.setText(""+response.body().getLikeId());
                            String likeText = likeCount.getText().toString();
                            if(likeText!=null&&!likeText.isEmpty()){

                                int count = Integer.parseInt(likeText);
                                likeCount.setText(""+(count+1));
                            }

                        }
                        else
                        {
                            like.setEnabled(true);

                        }
                    }

                    @Override
                    public void onFailure(Call<Likes> call, Throwable t) {

                        Toast.makeText(ConentDetailScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        like.setEnabled(true);

                    }
                });
            }
        });
    }

    private void deleteDisLike(final Likes likes, final ImageView like,final ImageView liked,final TextView likeCount,final int likeId,final ImageView disliked,final ImageView dislike,final TextView dislikeId,final TextView dislikeCount,final TextView likedIds) {



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LikeAPI mapApi = Util.getClient().create(LikeAPI.class);
                Call<Likes> response = mapApi.deleteLikes(likeId);
                response.enqueue(new Callback<Likes>() {
                    @Override
                    public void onResponse(Call<Likes> call, Response<Likes> response) {

                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {

                            dislike.setVisibility(View.VISIBLE);
                            dislike.setEnabled(true);
                            disliked.setVisibility(View.GONE);
                            dislikeId.setText("");
                            String dislikeText = dislikeCount.getText().toString();
                            if(dislikeText!=null&&!dislikeText.isEmpty()){

                                int count = Integer.parseInt(dislikeText);

                                if(count<=0){

                                }else{
                                    dislikeCount.setText(""+(count-1));
                                }

                            }
                            postLike(likes,like,liked,likeCount,likedIds);
                        }
                        else
                        {
                            like.setEnabled(true);

                        }
                    }

                    @Override
                    public void onFailure(Call<Likes> call, Throwable t) {

                        Toast.makeText(ConentDetailScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        like.setEnabled(true);

                    }
                });
            }
        });
    }

    private void deleteLike(final Likes likes, final ImageView like,final ImageView liked,final TextView likeCount,final int likeId,final ImageView disliked,final ImageView dislike,final TextView dislikeId,final TextView dislikeCount,final TextView likedIds) {



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LikeAPI mapApi = Util.getClient().create(LikeAPI.class);
                Call<Likes> response = mapApi.deleteLikes(likeId);
                response.enqueue(new Callback<Likes>() {
                    @Override
                    public void onResponse(Call<Likes> call, Response<Likes> response) {

                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {

                            dislike.setVisibility(View.VISIBLE);
                            dislike.setEnabled(true);
                            disliked.setVisibility(View.GONE);
                            dislikeId.setText("");
                            String dislikeText = dislikeCount.getText().toString();
                            if(dislikeText!=null&&!dislikeText.isEmpty()){

                                int count = Integer.parseInt(dislikeText);

                                if(count<=0){

                                }else{
                                    dislikeCount.setText(""+(count-1));
                                }

                            }
                            postDisLike(likes,like,liked,likeCount,likedIds);
                        }
                        else
                        {
                            like.setEnabled(true);

                        }
                    }

                    @Override
                    public void onFailure(Call<Likes> call, Throwable t) {

                        Toast.makeText(ConentDetailScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        like.setEnabled(true);

                    }
                });
            }
        });
    }



    private void postDisLike(final Likes likes, final ImageView like,final ImageView liked,final TextView likeCount,final TextView dislikeId) {



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LikeAPI mapApi = Util.getClient().create(LikeAPI.class);
                Call<Likes> response = mapApi.postLikes(likes);
                response.enqueue(new Callback<Likes>() {
                    @Override
                    public void onResponse(Call<Likes> call, Response<Likes> response) {

                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {

                            like.setVisibility(View.GONE);
                            liked.setVisibility(View.VISIBLE);
                            dislikeId.setText(""+response.body().getLikeId());

                            String likeText = likeCount.getText().toString();
                            if(likeText!=null&&!likeText.isEmpty()){

                                int count = Integer.parseInt(likeText);
                                likeCount.setText(""+(count+1));
                            }

                        }
                        else
                        {
                            like.setEnabled(true);

                        }
                    }

                    @Override
                    public void onFailure(Call<Likes> call, Throwable t) {

                        Toast.makeText(ConentDetailScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        like.setEnabled(true);

                    }
                });
            }
        });
    }

    private class DownloadTask extends AsyncTask<URL,Void,Bitmap> {
        // Before the tasks execution
        protected void onPreExecute(){
            // Display the progress dialog on async task start
            //mProgressDialog.show();
            Toast.makeText(ConentDetailScreen.this, "Downloading image...", Toast.LENGTH_SHORT).show();
        }

        // Do the task in background/non UI thread
        protected Bitmap doInBackground(URL...urls){
            URL url = urls[0];
            HttpURLConnection connection = null;

            try{
                // Initialize a new http url connection
                connection = (HttpURLConnection) url.openConnection();

                // Connect the http url connection
                connection.connect();

                // Get the input stream from http url connection
                InputStream inputStream = connection.getInputStream();

                /*
                    BufferedInputStream
                        A BufferedInputStream adds functionality to another input stream-namely,
                        the ability to buffer the input and to support the mark and reset methods.
                */
                /*
                    BufferedInputStream(InputStream in)
                        Creates a BufferedInputStream and saves its argument,
                        the input stream in, for later use.
                */
                // Initialize a new BufferedInputStream from InputStream
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                /*
                    decodeStream
                        Bitmap decodeStream (InputStream is)
                            Decode an input stream into a bitmap. If the input stream is null, or
                            cannot be used to decode a bitmap, the function returns null. The stream's
                            position will be where ever it was after the encoded data was read.

                        Parameters
                            is InputStream : The input stream that holds the raw data
                                              to be decoded into a bitmap.
                        Returns
                            Bitmap : The decoded bitmap, or null if the image data could not be decoded.
                */
                // Convert BufferedInputStream to Bitmap object
                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);

                // Return the downloaded bitmap
                return bmp;

            }catch(IOException e){
                e.printStackTrace();
            }finally{
                // Disconnect the http url connection
                connection.disconnect();
            }
            return null;
        }

        // When all async task done
        protected void onPostExecute(Bitmap result){
            // Hide the progress dialog
            // mProgressDialog.dismiss();

            if(result!=null){
                // Display the downloaded image into ImageView
                //mImageView.setImageBitmap(result);

                // Save bitmap to internal storage

                // Set the ImageView image from internal storage
                //mImageViewInternal.setImageURI(imageInternalUri);

                try{



                    File sd = Environment.getExternalStorageDirectory();
                    String fileName = System.currentTimeMillis() + ".png";

                    File directory = new File(sd.getAbsolutePath()+"/Mera Bihar App/.Share/");
                    //create directory if not exist
                    if (!directory.exists() && !directory.isDirectory()) {
                        directory.mkdirs();
                    }

                    File file = new File(directory, fileName);

                    Intent shareIntent;


                    OutputStream out = null;
                    try {
                        out = new FileOutputStream(file);
                        mark(result).compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    fileName=file.getPath();

                    Uri bmpUri = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        bmpUri = FileProvider.getUriForFile(ConentDetailScreen.this, "app.zingo.merabihars.fileprovider", file);
                    }else{
                        bmpUri = Uri.parse("file://"+fileName);
                    }
                    // Uri bmpUri = Uri.parse("file://"+path);
                    shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    shareIntent.setPackage("com.whatsapp");
                    /*String sAux = "\n"+mBlogName.getText().toString()+"\n\n";
                    sAux = sAux + "to read more click here "+shortUrl+" \n\n"+"To get the latest update about Bihar Download our Bihar Tourism official mobile app by clicking goo.gl/rZfotV";*/
                    shareIntent.putExtra(Intent.EXTRA_TEXT,"Check");
                    shareIntent.setType("image/png");
                    try{

                        startActivity(shareIntent);

                    }catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(ConentDetailScreen.this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                    }
                    //context.startActivity(Intent.createChooser(shareIntent,"Share with"));

                }catch (Exception we)
                {
                    we.printStackTrace();
                    Toast.makeText(ConentDetailScreen.this, "Unable to send,Check Permission", Toast.LENGTH_SHORT).show();
                }

            }else {
                // Notify user that an error occurred while downloading image
                //Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private class DownloadTasks extends AsyncTask<URL,Void,Bitmap> {
        // Before the tasks execution
        protected void onPreExecute(){
            // Display the progress dialog on async task start
            //mProgressDialog.show();
            Toast.makeText(ConentDetailScreen.this, "Downloading image...", Toast.LENGTH_SHORT).show();
        }

        // Do the task in background/non UI thread
        protected Bitmap doInBackground(URL...urls){
            URL url = urls[0];
            HttpURLConnection connection = null;

            try{
                // Initialize a new http url connection
                connection = (HttpURLConnection) url.openConnection();

                // Connect the http url connection
                connection.connect();

                // Get the input stream from http url connection
                InputStream inputStream = connection.getInputStream();

                /*
                    BufferedInputStream
                        A BufferedInputStream adds functionality to another input stream-namely,
                        the ability to buffer the input and to support the mark and reset methods.
                */
                /*
                    BufferedInputStream(InputStream in)
                        Creates a BufferedInputStream and saves its argument,
                        the input stream in, for later use.
                */
                // Initialize a new BufferedInputStream from InputStream
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                /*
                    decodeStream
                        Bitmap decodeStream (InputStream is)
                            Decode an input stream into a bitmap. If the input stream is null, or
                            cannot be used to decode a bitmap, the function returns null. The stream's
                            position will be where ever it was after the encoded data was read.

                        Parameters
                            is InputStream : The input stream that holds the raw data
                                              to be decoded into a bitmap.
                        Returns
                            Bitmap : The decoded bitmap, or null if the image data could not be decoded.
                */
                // Convert BufferedInputStream to Bitmap object
                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);

                // Return the downloaded bitmap
                return bmp;

            }catch(IOException e){
                e.printStackTrace();
            }finally{
                // Disconnect the http url connection
                connection.disconnect();
            }
            return null;
        }

        // When all async task done
        protected void onPostExecute(Bitmap result){
            // Hide the progress dialog
            // mProgressDialog.dismiss();

            if(result!=null){
                // Display the downloaded image into ImageView
                //mImageView.setImageBitmap(result);

                // Save bitmap to internal storage

                // Set the ImageView image from internal storage
                //mImageViewInternal.setImageURI(imageInternalUri);

                try{



                    File sd = Environment.getExternalStorageDirectory();
                    String fileName = System.currentTimeMillis() + ".png";

                    File directory = new File(sd.getAbsolutePath()+"/Mera Bihar App/.Share/");
                    //create directory if not exist
                    if (!directory.exists() && !directory.isDirectory()) {
                        directory.mkdirs();
                    }

                    File file = new File(directory, fileName);

                    Intent shareIntent;


                    OutputStream out = null;
                    try {
                        out = new FileOutputStream(file);
                        mark(result).compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    fileName=file.getPath();

                    Uri bmpUri = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        bmpUri = FileProvider.getUriForFile(ConentDetailScreen.this, "app.zingo.merabihars.fileprovider", file);
                    }else{
                        bmpUri = Uri.parse("file://"+fileName);
                    }
                    // Uri bmpUri = Uri.parse("file://"+path);

                   /* Intent intent = new AppInviteInvitation.IntentBuilder("Share")
                            .setMessage("Check")
                            //.setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                            .setCustomImage(bmpUri)
                            .setCallToActionText("Check")
                            .build();
                    startActivityForResult(intent, 1);*/
                    shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);

                    shareIntent.putExtra(Intent.EXTRA_TEXT,"Check");
                    shareIntent.setType("image/png");

                    startActivity(Intent.createChooser(shareIntent,"Share with"));

                }catch (Exception we)
                {
                    we.printStackTrace();
                    Toast.makeText(ConentDetailScreen.this, "Unable to send,Check Permission", Toast.LENGTH_SHORT).show();
                }

            }else {
                // Notify user that an error occurred while downloading image
                //Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
            }
        }
    }

    // Custom method to convert string to url
    protected URL stringToURL(String urlString){
        try{
            URL url = new URL(urlString);
            return url;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }

    // Custom method to save a bitmap into internal storage
    public Bitmap mark(Bitmap src) {

        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.app_logo);
        int w = src.getWidth();
        int h = src.getHeight();
        int pw=w-w;
        int ph=h-h;
        int nw = (w * 10)/100;
        int nh = (h * 10)/100;
        Bitmap result = Bitmap.createBitmap(w, h, icon.getConfig());
        Canvas canvas = new Canvas(result);
        Bitmap resized = Bitmap.createScaledBitmap(icon, nw, nh, true);

        canvas.drawBitmap(src, 0, 0, null);
        Paint paint = new Paint();

        paint.setTextSize(30);
        paint.setAntiAlias(true);
        paint.setUnderlineText(false);
        canvas.drawBitmap(resized,pw,ph,paint);
        return result;
    }

    private void getFollowingByProfileId(final int id, final LinearLayout button, final int contentProfileId){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileFollowAPI apiService =
                        Util.getClient().create(ProfileFollowAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getFollowingByProfileId(id);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();


                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {

                                for (UserProfile profile:responseProfile) {

                                    if(profile.getProfileId()==contentProfileId){

                                        mFollowing.setText("Following");
                                        button.setEnabled(false);
                                        break;
                                    }

                                }



                            }
                            else
                            {


                            }
                        }
                        else
                        {


                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed



                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    private void profileFollow(final ProfileFollowMapping intrst, final TextView tv) {



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ProfileFollowAPI mapApi = Util.getClient().create(ProfileFollowAPI.class);
                Call<ProfileFollowMapping> response = mapApi.postInterest(intrst);
                response.enqueue(new Callback<ProfileFollowMapping>() {
                    @Override
                    public void onResponse(Call<ProfileFollowMapping> call, Response<ProfileFollowMapping> response) {

                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {

                            tv.setText("Following");
                            tv.setEnabled(true);

                        }
                        else
                        {
                            tv.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileFollowMapping> call, Throwable t) {

                        Toast.makeText(ConentDetailScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        tv.setEnabled(true);
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      //  Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                   // Log.d(TAG, "onActivityResult: sent invitation " + id);
                    System.out.print("Share onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
            }
        }
    }

}
