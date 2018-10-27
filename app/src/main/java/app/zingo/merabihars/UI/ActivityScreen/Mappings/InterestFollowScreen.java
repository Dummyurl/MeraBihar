package app.zingo.merabihars.UI.ActivityScreen.Mappings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import app.zingo.merabihars.Adapter.CategoryFollowingList;
import app.zingo.merabihars.Adapter.InterestFollowAdapter;
import app.zingo.merabihars.Model.Category;
import app.zingo.merabihars.Model.Interest;
import app.zingo.merabihars.Model.InterestProfileMapping;
import app.zingo.merabihars.Model.UserProfile;
import app.zingo.merabihars.R;
import app.zingo.merabihars.Util.PreferenceHandler;
import app.zingo.merabihars.Util.ThreadExecuter;
import app.zingo.merabihars.Util.Util;
import app.zingo.merabihars.WebApi.CategoryApi;
import app.zingo.merabihars.WebApi.InterestAPI;
import app.zingo.merabihars.WebApi.InterestProfileAPI;
import app.zingo.merabihars.WebApi.ProfileFollowAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
//        return inflater.inflate(R.layout.fragment_interest_follow_screen, container, false);

public class InterestFollowScreen extends Fragment {

    RecyclerView recyclerView;
    private ProgressBar mProgressBar;

    ArrayList<Interest> categories;
    ArrayList<Interest> followings;
    int profileId;


    public InterestFollowScreen() {
        // Required empty public constructor
    }

    public static InterestFollowScreen newInstance() {
        InterestFollowScreen fragment = new InterestFollowScreen();
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
            View view = inflater.inflate(R.layout.fragment_interest_follow_screen, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.interest_following_list);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            mProgressBar.setVisibility(View.GONE);

            profileId = PreferenceHandler.getInstance(getActivity()).getUserId();


            getInterestByProfileId(profileId);


            return view;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

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
                                    recyclerView.setAdapter(adapter);
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
