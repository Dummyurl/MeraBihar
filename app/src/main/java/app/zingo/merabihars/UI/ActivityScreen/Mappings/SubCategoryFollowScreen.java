package app.zingo.merabihars.UI.ActivityScreen.Mappings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import app.zingo.merabihars.Adapter.CategoryFollowingList;
import app.zingo.merabihars.Adapter.SubCategoryFollowAdapter;
import app.zingo.merabihars.Model.Category;
import app.zingo.merabihars.Model.SubCategories;
import app.zingo.merabihars.R;
import app.zingo.merabihars.Util.Constants;
import app.zingo.merabihars.Util.PreferenceHandler;
import app.zingo.merabihars.Util.ThreadExecuter;
import app.zingo.merabihars.Util.Util;
import app.zingo.merabihars.WebApi.CategoryApi;
import app.zingo.merabihars.WebApi.SubCategoryAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubCategoryFollowScreen extends Fragment {

    RecyclerView recyclerView;
    private ProgressBar mProgressBar;

    ArrayList<SubCategories> categories;
    int profileId;


    public SubCategoryFollowScreen() {
        // Required empty public constructor
    }

    public static SubCategoryFollowScreen newInstance() {
        SubCategoryFollowScreen fragment = new SubCategoryFollowScreen();
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
            View view = inflater.inflate(R.layout.fragment_sub_category_follow_screen, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.sub_category_following_list);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            mProgressBar.setVisibility(View.GONE);

            profileId = PreferenceHandler.getInstance(getActivity()).getUserId();

            getSubCategories();

            return view;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void getSubCategories()
    {
        mProgressBar.setVisibility(View.VISIBLE);
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                final SubCategoryAPI categoryAPI = Util.getClient().create(SubCategoryAPI.class);
                Call<ArrayList<SubCategories>> getCat = categoryAPI.getSubCategoriesByCityId(Constants.CITY_ID);
                //Call<ArrayList<Category>> getCat = categoryAPI.getSubCategoriesByCityId(Constants.CITY_ID);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<SubCategories>>() {

                    @Override
                    public void onResponse(Call<ArrayList<SubCategories>> call, Response<ArrayList<SubCategories>> response) {
                        mProgressBar.setVisibility(View.GONE);

                        if(response.code() == 200)
                        {
                            categories = response.body();

                            if(categories!=null&&categories.size()!=0){

                                SubCategoryFollowAdapter adapter = new SubCategoryFollowAdapter(getActivity(),categories);
                                recyclerView.setAdapter(adapter);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<SubCategories>> call, Throwable t) {
                        mProgressBar.setVisibility(View.GONE);

                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }
}
