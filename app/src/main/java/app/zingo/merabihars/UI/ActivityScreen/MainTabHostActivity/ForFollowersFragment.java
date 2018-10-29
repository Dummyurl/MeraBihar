package app.zingo.merabihars.UI.ActivityScreen.MainTabHostActivity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import app.zingo.merabihars.Adapter.ContentCategoryRecyclerAdapter;
import app.zingo.merabihars.Model.Contents;
import app.zingo.merabihars.Model.UserProfile;
import app.zingo.merabihars.R;
import app.zingo.merabihars.Util.PreferenceHandler;
import app.zingo.merabihars.Util.ThreadExecuter;
import app.zingo.merabihars.Util.Util;
import app.zingo.merabihars.WebApi.InterestProfileAPI;
import app.zingo.merabihars.WebApi.ProfileFollowAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass. fragment_for_you
 */
public class ForFollowersFragment extends Fragment {

    LinearLayout mLoginLay;
    NestedScrollView mFolloersContent;
    SwipeRefreshLayout pullToRefresh;
    RecyclerView mFollowerContent,mFollowingContent,mInterestContent;

    int profileId = 0;

    ContentCategoryRecyclerAdapter adapter;
    boolean value = false;


    public ForFollowersFragment() {
        // Required empty public constructor
    }

    public static ForFollowersFragment newInstance() {
        ForFollowersFragment fragment = new ForFollowersFragment();
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

            View view =  inflater.inflate(R.layout.fragment_for_followers, container, false);

            mLoginLay = (LinearLayout)view.findViewById(R.id.not_login_layout);
            mFolloersContent = (NestedScrollView) view.findViewById(R.id.followers_content);
            pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);

            mFollowerContent = (RecyclerView) view.findViewById(R.id.all_followers_contents);
            mFollowerContent.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mFollowerContent.setNestedScrollingEnabled(false);

            mFollowingContent = (RecyclerView)  view.findViewById(R.id.all_following_contents);
            mFollowingContent.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mFollowingContent.setNestedScrollingEnabled(false);

            mInterestContent = (RecyclerView)  view.findViewById(R.id.all_interest_contents);
            mInterestContent.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mInterestContent.setNestedScrollingEnabled(false);



            profileId = PreferenceHandler.getInstance(getActivity()).getUserId();


            if(profileId!=0){

                mLoginLay.setVisibility(View.GONE);
                mFolloersContent.setVisibility(View.VISIBLE);

                Thread follower = new Thread(){

                    public void run(){
                        getFollowingByProfileId(profileId);
                    }
                };
                Thread following = new Thread(){

                    public void run(){
                        getFollowerByProfileId(profileId);
                    }
                };

                Thread interest = new Thread(){

                    public void run(){
                        getContentsofInterest(profileId);
                    }
                };

                follower.start();
                following.start();
                interest.start();

            }else{
                mLoginLay.setVisibility(View.VISIBLE);
                mFolloersContent.setVisibility(View.GONE);
            }


            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                int Refreshcounter = 1; //Counting how many times user have refreshed the layout

                @Override
                public void onRefresh() {
                    if(profileId!=0){

                        mLoginLay.setVisibility(View.GONE);
                        mFolloersContent.setVisibility(View.VISIBLE);

                        mFollowerContent.removeAllViews();
                        mFollowingContent.removeAllViews();
                        mInterestContent.removeAllViews();

                        Thread follower = new Thread(){

                            public void run(){
                                getFollowingByProfileId(profileId);
                            }
                        };
                        Thread following = new Thread(){

                            public void run(){
                                getFollowerByProfileId(profileId);
                            }
                        };

                        Thread interest = new Thread(){

                            public void run(){
                                getContentsofInterest(profileId);
                            }
                        };

                        follower.start();
                        following.start();
                        interest.start();

                    }else{
                        mLoginLay.setVisibility(View.VISIBLE);
                        mFolloersContent.setVisibility(View.GONE);
                    }



                    pullToRefresh.setRefreshing(false);
                }
            });

            return  view;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void getFollowingByProfileId(final int id){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileFollowAPI apiService =
                        Util.getClient().create(ProfileFollowAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getFollowersContentByProfileId(id);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();



                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();
                            ArrayList<Contents> followingContents = new ArrayList<>();

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {

                                for (UserProfile profile:responseProfile) {

                                    if(profile.getContents()!=null&&profile.getContents().size()!=0){

                                        for (Contents content:profile.getContents()) {

                                            followingContents.add(content);

                                        }


                                    }

                                }

                                if(followingContents!=null&& followingContents.size()!=0){
                                    Collections.shuffle(followingContents);
                                    value = true;
                                    adapter = new ContentCategoryRecyclerAdapter(getActivity(),followingContents);
                                    mFollowerContent.setAdapter(adapter);
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

    private void getFollowerByProfileId(final int id){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileFollowAPI apiService =
                        Util.getClient().create(ProfileFollowAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getFollowingContentByProfileId(id);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();



                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();
                            ArrayList<Contents> followingContents = new ArrayList<>();

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {

                                for (UserProfile profile:responseProfile) {

                                    if(profile.getContents()!=null&&profile.getContents().size()!=0){

                                        for (Contents content:profile.getContents()) {

                                            followingContents.add(content);

                                        }


                                    }

                                }

                                if(followingContents!=null&& followingContents.size()!=0){
                                    Collections.shuffle(followingContents);
                                    value = true;
                                    adapter = new ContentCategoryRecyclerAdapter(getActivity(),followingContents);
                                    mFollowingContent.setAdapter(adapter);
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

    public void getContentsofInterest(final int id)
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final InterestProfileAPI categoryAPI = Util.getClient().create(InterestProfileAPI.class);
                Call<ArrayList<Contents>> getCat = categoryAPI.getContentofInterestByProfileId(id);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<Contents>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Contents>> call, Response<ArrayList<Contents>> response) {



                        if(response.code() == 200)
                        {

                            ArrayList<Contents> contentsInterestList = response.body();

                            if(contentsInterestList != null && contentsInterestList.size() != 0)
                            {

                                Collections.shuffle(contentsInterestList);
                                value = true;
                                adapter = new ContentCategoryRecyclerAdapter(getActivity(),contentsInterestList);
                                mInterestContent.setAdapter(adapter);


                            }
                            else
                            {


                            }
                        }else{

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Contents>> call, Throwable t) {



                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

}
