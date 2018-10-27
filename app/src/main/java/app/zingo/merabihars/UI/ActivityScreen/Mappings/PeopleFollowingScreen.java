package app.zingo.merabihars.UI.ActivityScreen.Mappings;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.Auth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.zingo.merabihars.Adapter.EventListAdapter;
import app.zingo.merabihars.Adapter.ProfileListAdapter;
import app.zingo.merabihars.Model.UserProfile;
import app.zingo.merabihars.Model.UserRole;
import app.zingo.merabihars.R;
import app.zingo.merabihars.UI.ActivityScreen.AccountScreens.LoginScreen;
import app.zingo.merabihars.UI.ActivityScreen.Events.ListOfEventsActivity;
import app.zingo.merabihars.UI.ActivityScreen.Fragment.SearchFragment;
import app.zingo.merabihars.UI.ActivityScreen.LandingScreen.HomeLandingBottomScreen;
import app.zingo.merabihars.Util.Constants;
import app.zingo.merabihars.Util.PreferenceHandler;
import app.zingo.merabihars.Util.ThreadExecuter;
import app.zingo.merabihars.Util.Util;
import app.zingo.merabihars.WebApi.ProfileAPI;
import app.zingo.merabihars.WebApi.ProfileFollowAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleFollowingScreen extends Fragment {

    RecyclerView recyclerView;
    private ProgressBar mProgressBar;

    ArrayList<UserProfile> userProfiles;
    ArrayList<UserProfile> followings;
    int profileId;
    public PeopleFollowingScreen() {
        // Required empty public constructor
    }

    public static PeopleFollowingScreen newInstance() {
        PeopleFollowingScreen fragment = new PeopleFollowingScreen();
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
            View view = inflater.inflate(R.layout.fragment_people_following_screen, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.people_following_list);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            mProgressBar.setVisibility(View.GONE);

             profileId = PreferenceHandler.getInstance(getActivity()).getUserId();

            if(profileId!=0){


                mProgressBar.setVisibility(View.VISIBLE);

                getFollowingByProfileId(profileId);

            }else{


            }

            return view;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void getProfileByUserRoleId(final int id){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileAPI apiService =
                        Util.getClient().create(ProfileAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getProfiles();

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        mProgressBar.setVisibility(View.GONE);

                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();
                            userProfiles = new ArrayList<>();
                            if(responseProfile != null && responseProfile.size()!=0 )
                            {
                                for (int i=0;i<responseProfile.size();i++){

                                    if(profileId!=responseProfile.get(i).getProfileId()){

                                        userProfiles.add(responseProfile.get(i));
                                    }

                                }

                                if(userProfiles!=null&&userProfiles.size()!=0){
                                    System.out.println("Before Size ="+userProfiles.size());
                                    System.out.println("Before Size ="+followings.size());


                                    if(followings.size()!=0){
                                        userProfiles.removeAll(followings);
                                    }
                                   // followings.removeAll(userProfiles);

                                    System.out.println("Before Size ="+userProfiles.size());
                                    if(userProfiles!=null&&userProfiles.size()!=0){
                                        ProfileListAdapter adapter = new ProfileListAdapter(getActivity(),userProfiles);
                                        recyclerView.setAdapter(adapter);
                                    }

                                }


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
                            followings = response.body();
                            if(followings != null && followings.size()!=0 )
                            {

                                getProfileByUserRoleId(1);

                            }
                            else
                            {
                                followings = new ArrayList<>();
                                getProfileByUserRoleId(1);

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
}
