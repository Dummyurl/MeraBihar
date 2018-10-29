package app.zingo.merabihars.UI.ActivityScreen.MainTabHostActivity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import app.zingo.merabihars.Adapter.CategoryContentRecyclerAdapter;
import app.zingo.merabihars.Adapter.ContentCityHomePager;
import app.zingo.merabihars.Model.CategoryAndContentList;
import app.zingo.merabihars.Model.Contents;
import app.zingo.merabihars.R;
import app.zingo.merabihars.UI.ActivityScreen.SearchScreens.PeopleSearchFragment;
import app.zingo.merabihars.Util.Constants;
import app.zingo.merabihars.Util.ThreadExecuter;
import app.zingo.merabihars.Util.Util;
import app.zingo.merabihars.WebApi.CategoryApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass. fragment_for_you
 */
public class ForYouFragment extends Fragment {

    ViewPager mContentsCityPager;
    ProgressBar mContentProgress;
    RecyclerView mCategoryContents;

    ArrayList<CategoryAndContentList> categoryAndContentList;
    ArrayList<Contents> contentsList;


    public ForYouFragment() {
        // Required empty public constructor
    }

    public static ForYouFragment newInstance() {
        ForYouFragment fragment = new ForYouFragment();
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

            View view = inflater.inflate(R.layout.fragment_for_you, container, false);

            mContentsCityPager = (ViewPager)view.findViewById(R.id.content_pager_city);
            mContentProgress = (ProgressBar) view.findViewById(R.id.progressBar_content);

            mCategoryContents = (RecyclerView) view.findViewById(R.id.content_based_category);
            mCategoryContents.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mCategoryContents.setNestedScrollingEnabled(false);

            mContentsCityPager.setClipToPadding(false);
            mContentsCityPager.setPadding(30,10,30,0);
            mContentsCityPager.setPageMargin(10);

            getCategoryAndContent();
            return view;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }


    public void getCategoryAndContent()
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final CategoryApi categoryAPI = Util.getClient().create(CategoryApi.class);
                Call<ArrayList<CategoryAndContentList>> getCat = categoryAPI.getContentAndCategoryByCityId(Constants.CITY_ID);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<CategoryAndContentList>>() {

                    @Override
                    public void onResponse(Call<ArrayList<CategoryAndContentList>> call, Response<ArrayList<CategoryAndContentList>> response) {

                        mContentProgress.setVisibility(View.GONE);

                        if(response.code() == 200)
                        {

                            ArrayList<CategoryAndContentList> body = response.body();
                            categoryAndContentList = new ArrayList<>();
                            contentsList = new ArrayList<>();


                            for (CategoryAndContentList categoryContent: body) {

                                if(categoryContent.getContentList()!=null&&categoryContent.getContentList().size()!=0){

                                    categoryAndContentList.add(categoryContent);

                                    for (Contents content:categoryContent.getContentList()) {

                                        contentsList.add(content);

                                    }
                                }

                            }

                            if(categoryAndContentList != null && categoryAndContentList.size() != 0)
                            {

                                Collections.shuffle(categoryAndContentList);
                                CategoryContentRecyclerAdapter adapter = new CategoryContentRecyclerAdapter(getActivity(),categoryAndContentList);
                                mCategoryContents.setAdapter(adapter);



                                if(contentsList!=null&&contentsList.size()!=0){
                                    ContentCityHomePager adapters = new ContentCityHomePager(getActivity(),contentsList);
                                    mContentsCityPager.setAdapter(adapters);
                                }




                            }
                            else
                            {

                                mCategoryContents.setVisibility(View.GONE);

                            }
                        }else{
                            mCategoryContents.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<CategoryAndContentList>> call, Throwable t) {

                        mCategoryContents.setVisibility(View.GONE);

                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }
}
