package app.zingo.merabihar.UI.ActivityScreen.SearchScreens;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;

import app.zingo.merabihar.Adapter.InterestFollowAdapter;
import app.zingo.merabihar.Adapter.ProfileSearchAdapter;
import app.zingo.merabihar.Model.FollowerNonFollowers;
import app.zingo.merabihar.Model.Interest;
import app.zingo.merabihar.Model.InterestProfileMapping;
import app.zingo.merabihar.Model.UserProfile;
import app.zingo.merabihar.R;
import app.zingo.merabihar.Util.PreferenceHandler;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.InterestAPI;
import app.zingo.merabihar.WebApi.InterestProfileAPI;
import app.zingo.merabihar.WebApi.ProfileAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.fragment_interest_search
 */
public class InterestSearchFragment extends Fragment {

    RecyclerView recyclerViewFolloers,recyclerViewNonFolloers,recyclerProfile;
    private ProgressBar mProgressBar;
    EditText mSearchText;
    ScrollView mInterestScroll;
    LinearLayout mNon,mOn;

    ArrayList<Interest> categories;
    ArrayList<Interest> followings;
    int profileId;
    public InterestSearchFragment() {
        // Required empty public constructor
    }

    public static InterestSearchFragment newInstance() {
        InterestSearchFragment fragment = new InterestSearchFragment();
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
            View view = inflater.inflate(R.layout.fragment_interest_search, container, false);
            recyclerViewFolloers = (RecyclerView) view.findViewById(R.id.people_following_list);
            recyclerViewNonFolloers = (RecyclerView) view.findViewById(R.id.people_non_following_list);
            recyclerProfile = (RecyclerView) view.findViewById(R.id.people_list);
            mInterestScroll = (ScrollView) view.findViewById(R.id.scroll_profile);
            mNon = (LinearLayout) view.findViewById(R.id.non_layout);
            mOn = (LinearLayout) view.findViewById(R.id.on_layout);
            mSearchText = (EditText) view.findViewById(R.id.search_editText);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            mProgressBar.setVisibility(View.GONE);

            profileId = PreferenceHandler.getInstance(getActivity()).getUserId();

            if(profileId!=0){


                mProgressBar.setVisibility(View.VISIBLE);



            }else{


            }

            mSearchText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    String text = mSearchText.getText().toString();

                    if(text.isEmpty()||text.equalsIgnoreCase("")){

                        mInterestScroll.setVisibility(View.VISIBLE);
                        recyclerProfile.setVisibility(View.GONE);

                    }else{
                        //filterProfiles(charSequence.toString().toLowerCase());

                    }




                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            return view;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

  /*  private void filterProfiles(String s) {

        ArrayList<UserProfile> filteredList = new ArrayList<>();
        mInterestScroll.setVisibility(View.GONE);
        recyclerProfile.setVisibility(View.VISIBLE);

        try{
            for(int i=0;i<userProfile.size();i++)
            {

                String fullName = "";


                if(userProfile.get(i).getFullName()!=null){
                    fullName= userProfile.get(i).getFullName().toLowerCase();
                }

                if(fullName.contains(s))
                {
                    filteredList.add(userProfile.get(i));
                }



            }

            ProfileSearchAdapter adapter = new ProfileSearchAdapter(getActivity(),filteredList);
            recyclerProfile.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("OTA Error "+e.getMessage());

        }




    }*/

   /* private void getOtherProfiles(final int id){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileAPI apiService =
                        Util.getClient().create(ProfileAPI.class);

                Call<FollowerNonFollowers> call = apiService.getOtherProfiles(id);

                call.enqueue(new Callback<FollowerNonFollowers>() {
                    @Override
                    public void onResponse(Call<FollowerNonFollowers> call, Response<FollowerNonFollowers> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        mProgressBar.setVisibility(View.GONE);

                        if(statusCode == 200 || statusCode == 204)
                        {

                            FollowerNonFollowers userProfiles = response.body();

                            if(userProfiles!=null){


                                ArrayList<UserProfile> folloers = new ArrayList<>();
                                ArrayList<UserProfile> nonfolloers = new ArrayList<>();

                                if (userProfiles.getFollowers()!=null&&userProfiles.getFollowers().size()!=0){


                                    System.out.println("User 1Size "+userProfile.size());
                                    folloers = userProfiles.getFollowers();
                                    for (UserProfile usr:folloers) {

                                        userProfile.add(usr);

                                    }
                                    ProfileSearchAdapter adapter = new ProfileSearchAdapter(getActivity(),folloers);
                                    recyclerViewFolloers.setAdapter(adapter);

                                }else{

                                    mOn.setVisibility(View.GONE);

                                }

                                if (userProfiles.getNonFollowers()!=null&&userProfiles.getNonFollowers().size()!=0){

                                    System.out.println("User 2Size "+userProfile.size());
                                    nonfolloers = userProfiles.getNonFollowers();
                                    for (UserProfile usr:nonfolloers) {

                                        userProfile.add(usr);

                                    }
                                    ProfileSearchAdapter adapter = new ProfileSearchAdapter(getActivity(),nonfolloers);
                                    recyclerViewNonFolloers.setAdapter(adapter);

                                }else{

                                    mNon.setVisibility(View.GONE);

                                }


                            }

                        }
                        else
                        {

                            Toast.makeText(getActivity(),response.message(),Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<FollowerNonFollowers> call, Throwable t) {
                        // Log error here since request failed

                        mProgressBar.setVisibility(View.GONE);

                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }*/

    public void getInterest()
    {
        mProgressBar.setVisibility(View.VISIBLE);
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                final InterestAPI categoryAPI = Util.getClient().create(InterestAPI.class);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategoriesByCityId(Constants.CITY_ID);
                Call<ArrayList<Interest>> getCat = categoryAPI.getInterest();
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<Interest>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Interest>> call, Response<ArrayList<Interest>> response) {
                        mProgressBar.setVisibility(View.GONE);

                        if(response.code() == 200)
                        {
                            categories = response.body();

                            if(categories!=null&&categories.size()!=0){
                                if(followings.size()!=0){
                                    categories.removeAll(followings);
                                }

                                if(categories!=null&&categories.size()!=0){
                                    InterestFollowAdapter adapter = new InterestFollowAdapter(getActivity(),categories);
                                    recyclerViewNonFolloers.setAdapter(adapter);
                                }

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Interest>> call, Throwable t) {
                        mProgressBar.setVisibility(View.GONE);

                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    private void getInterestByProfileId(final int id){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                InterestProfileAPI apiService =
                        Util.getClient().create(InterestProfileAPI.class);

                Call<ArrayList<InterestProfileMapping>> call = apiService.getInterestByProfileId(id);

                call.enqueue(new Callback<ArrayList<InterestProfileMapping>>() {
                    @Override
                    public void onResponse(Call<ArrayList<InterestProfileMapping>> call, Response<ArrayList<InterestProfileMapping>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        mProgressBar.setVisibility(View.GONE);

                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<InterestProfileMapping> responseProfile = response.body();
                            followings = new ArrayList<>();

                            for(int i=0;i<responseProfile.size();i++){

                                followings.add(responseProfile.get(i).getZingoInterest());
                            }
                            if(followings != null && followings.size()!=0 )
                            {

                                getInterest();

                            }
                            else
                            {
                                followings = new ArrayList<>();
                                getInterest();

                            }
                        }
                        else
                        {

                            Toast.makeText(getActivity(),response.message(),Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<InterestProfileMapping>> call, Throwable t) {
                        // Log error here since request failed

                        mProgressBar.setVisibility(View.GONE);

                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }


}

