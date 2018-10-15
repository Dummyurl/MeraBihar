package app.zingo.merabihar.UI.ActivityScreen.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.zingo.merabihar.Adapter.ProfileFollowedAdapter;
import app.zingo.merabihar.Adapter.ProfileFollowingAdapter;
import app.zingo.merabihar.Adapter.ProfileListAdapter;
import app.zingo.merabihar.Model.UserProfile;
import app.zingo.merabihar.R;
import app.zingo.merabihar.UI.ActivityScreen.AccountScreens.LoginScreen;
import app.zingo.merabihar.UI.ActivityScreen.AccountScreens.ProfileActivity;
import app.zingo.merabihar.UI.ActivityScreen.AccountScreens.SignUpScreen;
import app.zingo.merabihar.UI.ActivityScreen.LandingScreen.LandingScreenActivity;
import app.zingo.merabihar.UI.ActivityScreen.Mappings.CategoryFollowScreen;
import app.zingo.merabihar.UI.ActivityScreen.Mappings.FollowingMainScreen;
import app.zingo.merabihar.UI.ActivityScreen.Mappings.InterestFollowScreen;
import app.zingo.merabihar.UI.ActivityScreen.Mappings.PeopleFollowingScreen;
import app.zingo.merabihar.UI.ActivityScreen.Mappings.SubCategoryFollowScreen;
import app.zingo.merabihar.Util.PreferenceHandler;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.ProfileAPI;
import app.zingo.merabihar.WebApi.ProfileFollowAPI;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountViewFragment extends Fragment {

    ScrollView mProfileMainLay;
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
    private int[] tabIcons = {
            R.drawable.gallery,
            R.drawable.follower,
            R.drawable.following
    };


    public AccountViewFragment() {
        // Required empty public constructor
    }
    public static AccountViewFragment newInstance(String param1, String param2) {
        AccountViewFragment fragment = new AccountViewFragment();
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
            View view = inflater.inflate(R.layout.fragment_account_view, container, false);

            mProfileMainLay = (ScrollView)view.findViewById(R.id.profile_main_layout);
            mNotLay = (LinearLayout) view.findViewById(R.id.not_login_layout);

            mDataLay = (LinearLayout) view.findViewById(R.id.linLayout);
            mSettingLay = (LinearLayout) view.findViewById(R.id.setting_menu);
            mLogoutLay = (LinearLayout) view.findViewById(R.id.log_out_lay);
            mLogin = (AppCompatButton) view.findViewById(R.id.loginAccount);
            mFollow = (AppCompatButton) view.findViewById(R.id.btn_follow);
            mSignUp = (TextView) view.findViewById(R.id.link_signup);
            mName = (TextView) view.findViewById(R.id.display_name);
            mDesc = (TextView) view.findViewById(R.id.description);
            mFollowers = (TextView) view.findViewById(R.id.tvFollowers);
            mFollowing = (TextView) view.findViewById(R.id.tvFollowing);
            mPost = (TextView) view.findViewById(R.id.tvPosts);
            tabLayout = (TabLayout) view.findViewById(R.id.tabs);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.addTab(tabLayout.newTab().setText(""));
            tabLayout.addTab(tabLayout.newTab().setText(""));
            tabLayout.addTab(tabLayout.newTab().setText(""));
            recyclerView = (RecyclerView) view.findViewById(R.id.following_list);
//            sheetBehavior = BottomSheetBehavior.from(mSetting);

            //viewPager = (ViewPager) view.findViewById(R.id.viewpager);

            /*LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            viewPager.setAdapter(new ViewPagerAdapter(getFragmentManager(), tabLayout.getTabCount()));
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.setupWithViewPager(viewPager);*/
            tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            tabLayout.getTabAt(1).setIcon(tabIcons[1]);
            tabLayout.getTabAt(2).setIcon(tabIcons[2]);
            //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                   // viewPager.setCurrentItem(tab.getPosition());
                  //  Toast.makeText(getActivity(), "Tab "+tab.getPosition(), Toast.LENGTH_SHORT).show();
                    userProfileArrayList = new ArrayList<>();

                    if(tab.getPosition()==1){

                        recyclerView.removeAllViews();
                        getFollowerByProfileId(userProfile.getProfileId(),"TAB");

                    }else if(tab.getPosition()==2){

                        recyclerView.removeAllViews();
                        getFollowingByProfileId(userProfile.getProfileId(),"TAB");

                    }else {

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
            mProfilePhoto = (CircleImageView) view.findViewById(R.id.profile_cover_image);
            mLogout = (ImageView) view.findViewById(R.id.logout);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            mProgressBar.setVisibility(View.GONE);
            int profileId = PreferenceHandler.getInstance(getActivity()).getUserId();




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
                    Intent login = new Intent(getActivity(), LoginScreen.class);
                    startActivity(login);
                }
            });

            mSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent login = new Intent(getActivity(),SignUpScreen.class);
                    startActivity(login);
                }
            });

            mFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent follow = new Intent(getActivity(),FollowingMainScreen.class);
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

                    PreferenceHandler.getInstance(getActivity()).clear();
                    Intent logout = new Intent(getActivity(),LoginScreen.class);
                    logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(logout);
                }
            });



/*            sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    switch (newState) {
                        case BottomSheetBehavior.STATE_HIDDEN:
                            break;
                        case BottomSheetBehavior.STATE_EXPANDED: {
                            //mLogout.setText("Close");
                        }
                        break;
                        case BottomSheetBehavior.STATE_COLLAPSED: {
                            //btnBottomSheet.setText("Expand");
                        }
                        break;
                        case BottomSheetBehavior.STATE_DRAGGING:
                            break;
                        case BottomSheetBehavior.STATE_SETTLING:
                            break;
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            });*/

            return view;

        }catch (Exception e){
            e.printStackTrace();
            return null;
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

                        Toast.makeText(getActivity(),t.getMessage(), Toast.LENGTH_SHORT).show();
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
                Picasso.with(getActivity()).load(base).placeholder(R.drawable.profile_image).
                        error(R.drawable.profile_image).into(mProfilePhoto);

                Picasso.with(getActivity()).load(base).placeholder(R.drawable.banner).
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

                                mFollowing.setText(""+responseProfile.size());


                            }
                            else
                            {


                            }
                        }
                        else
                        {

                            Toast.makeText(getActivity(),response.message(),Toast.LENGTH_SHORT).show();
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

                                mFollowers.setText(""+responseProfile.size());


                            }
                            else
                            {


                            }
                        }
                        else
                        {

                            Toast.makeText(getActivity(),response.message(),Toast.LENGTH_SHORT).show();
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

                                ProfileFollowedAdapter adapter = new ProfileFollowedAdapter(getActivity(),responseProfile);
                                recyclerView.setAdapter(adapter);


                            }
                            else
                            {


                            }
                        }
                        else
                        {

                            Toast.makeText(getActivity(),response.message(),Toast.LENGTH_SHORT).show();
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

                                ProfileFollowingAdapter adapter = new ProfileFollowingAdapter(getActivity(),responseProfile);
                                recyclerView.setAdapter(adapter);


                            }
                            else
                            {


                            }
                        }
                        else
                        {

                            Toast.makeText(getActivity(),response.message(),Toast.LENGTH_SHORT).show();
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

   /* private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFrag(new PeopleFollowingScreen(), "ONE");
        adapter.addFrag(new InterestFollowScreen(), "TWO");
        adapter.addFrag(new CategoryFollowScreen(), "THREE");
        viewPager.setAdapter(adapter);
    }*/

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public ViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }


        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new InterestFollowScreen();
                case 1:
                    return new SubCategoryFollowScreen();
                case 2:
                    return new CategoryFollowScreen();

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

}
