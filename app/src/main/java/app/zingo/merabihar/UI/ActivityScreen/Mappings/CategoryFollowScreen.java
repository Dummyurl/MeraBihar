package app.zingo.merabihar.UI.ActivityScreen.Mappings;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.zingo.merabihar.Adapter.CategoryFollowingList;
import app.zingo.merabihar.Adapter.ProfileListAdapter;
import app.zingo.merabihar.Adapter.ViewPagerAdapter;
import app.zingo.merabihar.Model.Category;
import app.zingo.merabihar.Model.UserProfile;
import app.zingo.merabihar.R;
import app.zingo.merabihar.Util.Constants;
import app.zingo.merabihar.Util.PreferenceHandler;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.CategoryApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFollowScreen extends Fragment {

    RecyclerView recyclerView;
    private ProgressBar mProgressBar;

    ArrayList<Category> categories;
    int profileId;


    public CategoryFollowScreen() {
        // Required empty public constructor
    }

    public static CategoryFollowScreen newInstance() {
        CategoryFollowScreen fragment = new CategoryFollowScreen();
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
            View view = inflater.inflate(R.layout.fragment_category_follow_screen, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.category_following_list);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            mProgressBar.setVisibility(View.GONE);

            profileId = PreferenceHandler.getInstance(getActivity()).getUserId();

            getCategories();

            return view;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void getCategories()
    {
        mProgressBar.setVisibility(View.VISIBLE);
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                final CategoryApi categoryAPI = Util.getClient().create(CategoryApi.class);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategoriesByCityId(Constants.CITY_ID);
                Call<ArrayList<Category>> getCat = categoryAPI.getCategoriesByCityId(2);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<Category>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                        mProgressBar.setVisibility(View.GONE);

                        if(response.code() == 200)
                        {
                            categories = response.body();

                            if(categories!=null&&categories.size()!=0){

                                CategoryFollowingList adapter = new CategoryFollowingList(getActivity(),categories);
                                recyclerView.setAdapter(adapter);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                        mProgressBar.setVisibility(View.GONE);

                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }
}
