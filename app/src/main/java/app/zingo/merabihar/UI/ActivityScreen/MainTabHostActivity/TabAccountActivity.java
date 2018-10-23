package app.zingo.merabihar.UI.ActivityScreen.MainTabHostActivity;

import android.content.Intent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.zingo.merabihar.Adapter.ProfileFollowedAdapter;
import app.zingo.merabihar.Adapter.ProfileFollowingAdapter;
import app.zingo.merabihar.Model.UserProfile;
import app.zingo.merabihar.R;
import app.zingo.merabihar.UI.ActivityScreen.AccountScreens.LoginScreen;
import app.zingo.merabihar.UI.ActivityScreen.AccountScreens.SignUpScreen;
import app.zingo.merabihar.UI.ActivityScreen.Mappings.FollowingMainScreen;
import app.zingo.merabihar.Util.PreferenceHandler;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.ProfileAPI;
import app.zingo.merabihar.WebApi.ProfileFollowAPI;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabAccountActivity extends AppCompatActivity {

    NestedScrollView mProfileMainLay;
    LinearLayout mNotLay,mDataLay,mSettingLay,mLogoutLay;
    TabLayout tabLayout;
    ViewPager viewPager;
    RecyclerView recyclerView;

    AppCompatButton mLogin,mFollow;
    TextView mSignUp,mName,mDesc,mFollowers,mPost,mFollowing;
    CircleImageView mProfilePhoto;
    ImageView mLogout;
    private ProgressBar mProgressBar;

    BottomSheetBehavior sheetBehavior;

    UserProfile userProfile;
    ArrayList<UserProfile> userProfileArrayList;
    ArrayList<UserProfile> followings;
    ArrayList<UserProfile> followers;
    private int[] tabIcons = {
            R.drawable.gallery,
            R.drawable.follower,
            R.drawable.following
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_tab_account);


            mProfileMainLay = (NestedScrollView) findViewById(R.id.profile_main_layout);
            mNotLay = (LinearLayout) findViewById(R.id.not_login_layout);

            mDataLay = (LinearLayout) findViewById(R.id.linLayout);
            mSettingLay = (LinearLayout) findViewById(R.id.setting_menu);
            mLogoutLay = (LinearLayout) findViewById(R.id.log_out_lay);
            mLogin = (AppCompatButton) findViewById(R.id.loginAccount);
            mFollow = (AppCompatButton) findViewById(R.id.btn_follow);
            mSignUp = (TextView) findViewById(R.id.link_signup);
            mName = (TextView) findViewById(R.id.display_name);
            mDesc = (TextView) findViewById(R.id.description);
            mFollowers = (TextView) findViewById(R.id.tvFollowers);
            mFollowing = (TextView) findViewById(R.id.tvFollowing);
            mPost = (TextView) findViewById(R.id.tvPosts);
            mProfilePhoto = (CircleImageView) findViewById(R.id.profile_cover_image);
            mLogout = (ImageView) findViewById(R.id.logout);
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
            mProgressBar.setVisibility(View.GONE);

            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.addTab(tabLayout.newTab().setText(""));
            tabLayout.addTab(tabLayout.newTab().setText(""));
            tabLayout.addTab(tabLayout.newTab().setText(""));
            recyclerView = (RecyclerView) findViewById(R.id.following_list);
            //recyclerView.setNestedScrollingEnabled(false);

            tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            tabLayout.getTabAt(1).setIcon(tabIcons[1]);
            tabLayout.getTabAt(2).setIcon(tabIcons[2]);

            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    userProfileArrayList = new ArrayList<>();

                    if(tab.getPosition()==1){

                        recyclerView.removeAllViews();

                        if(followers!=null){
                            ProfileFollowedAdapter adapter = new ProfileFollowedAdapter(TabAccountActivity.this,followers);
                            recyclerView.setAdapter(adapter);
                        }else{
                            getFollowerByProfileId(userProfile.getProfileId(),"TAB");
                        }


                    }else if(tab.getPosition()==2){

                        recyclerView.removeAllViews();
                        if(followings!=null){

                            ProfileFollowingAdapter adapter = new ProfileFollowingAdapter(TabAccountActivity.this,followings);
                            recyclerView.setAdapter(adapter);

                        }else{
                            getFollowingByProfileId(userProfile.getProfileId(),"TAB");
                        }


                    }else if(tab.getPosition()==0) {

                        recyclerView.removeAllViews();
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            int profileId = PreferenceHandler.getInstance(TabAccountActivity.this).getUserId();




            if(profileId!=0){

                mNotLay.setVisibility(View.GONE);
                mProfileMainLay.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                getProfile(profileId);

            }else{

                mNotLay.setVisibility(View.VISIBLE);
                mProfileMainLay.setVisibility(View.GONE);
            }

            mLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent login = new Intent(TabAccountActivity.this, LoginScreen.class);
                    startActivity(login);
                }
            });

            mSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent login = new Intent(TabAccountActivity.this,SignUpScreen.class);
                    startActivity(login);
                }
            });

            mFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent follow = new Intent(TabAccountActivity.this,FollowingMainScreen.class);
                    startActivity(follow);
                }
            });

            mLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(mSettingLay.getVisibility() == View.VISIBLE){
                        mSettingLay.setVisibility(View.GONE);
                    }else{

                        mSettingLay.setVisibility(View.VISIBLE);
                    }


                }
            });

            mLogoutLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PreferenceHandler.getInstance(TabAccountActivity.this).clear();
                    Intent logout = new Intent(TabAccountActivity.this,LoginScreen.class);
                    logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(logout);
                }
            });





        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void getProfile(final int id){


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

                        mProgressBar.setVisibility(View.GONE);

                        if (response.code() == 200)
                        {
                            System.out.println("Inside api");

                            userProfile = response.body();

                            setData(userProfile);



                        }else{
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {

                        mProgressBar.setVisibility(View.GONE);

                        Toast.makeText(TabAccountActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });
    }

    public  void setData(UserProfile profile){

        try{

            if(profile.getFullName()!=null){
                mName.setText(""+profile.getFullName());
            }

            if(profile.getPrefix()!=null){
                mDesc.setText(""+profile.getPrefix());
            }

            getFollowingByProfileId(profile.getProfileId());
            getFollowerByProfileId(profile.getProfileId());

            String base=profile.getProfilePhoto();
            if(base != null && !base.isEmpty()){
                Picasso.with(TabAccountActivity.this).load(base).placeholder(R.drawable.profile_image).
                        error(R.drawable.profile_image).into(mProfilePhoto);

                Picasso.with(TabAccountActivity.this).load(base).placeholder(R.drawable.banner).
                        error(R.drawable.banner).into(mProfilePhoto);

                mProfilePhoto.setVisibility(View.VISIBLE);

            }
            mFollow.setVisibility(View.VISIBLE);
            mDataLay.setVisibility(View.VISIBLE);

        }catch (Exception e){
            e.printStackTrace();
        }



    }

    private void getFollowingByProfileId(final int id){

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

                        mProgressBar.setVisibility(View.GONE);

                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {

                                followings = response.body();
                                mFollowing.setText(""+responseProfile.size());


                            }
                            else
                            {


                            }
                        }
                        else
                        {

                            Toast.makeText(TabAccountActivity.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed

                        mProgressBar.setVisibility(View.GONE);

                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    private void getFollowerByProfileId(final int id){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileFollowAPI apiService =
                        Util.getClient().create(ProfileFollowAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getFollowersByProfileId(id);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        mProgressBar.setVisibility(View.GONE);

                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();


                            if(responseProfile != null && responseProfile.size()!=0 )
                            {

                                followers = response.body();
                                mFollowers.setText(""+responseProfile.size());


                            }
                            else
                            {


                            }
                        }
                        else
                        {

                            Toast.makeText(TabAccountActivity.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed

                        mProgressBar.setVisibility(View.GONE);

                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    private void getFollowerByProfileId(final int id,final String type){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileFollowAPI apiService =
                        Util.getClient().create(ProfileFollowAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getFollowersByProfileId(id);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        mProgressBar.setVisibility(View.GONE);

                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {

                                ProfileFollowedAdapter adapter = new ProfileFollowedAdapter(TabAccountActivity.this,responseProfile);
                                recyclerView.setAdapter(adapter);


                            }
                            else
                            {


                            }
                        }
                        else
                        {

                            Toast.makeText(TabAccountActivity.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed

                        mProgressBar.setVisibility(View.GONE);

                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    private void getFollowingByProfileId(final int id,String type){

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

                        mProgressBar.setVisibility(View.GONE);

                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {

                                ProfileFollowingAdapter adapter = new ProfileFollowingAdapter(TabAccountActivity.this,responseProfile);
                                recyclerView.setAdapter(adapter);


                            }
                            else
                            {


                            }
                        }
                        else
                        {

                            Toast.makeText(TabAccountActivity.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed

                        mProgressBar.setVisibility(View.GONE);

                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    private void goBack()
    {

        Intent intent = new Intent(TabAccountActivity.this,TabMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        TabAccountActivity.this.finish();
    }

}
