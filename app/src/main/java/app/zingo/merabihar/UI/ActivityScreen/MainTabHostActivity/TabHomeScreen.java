package app.zingo.merabihar.UI.ActivityScreen.MainTabHostActivity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.zingo.merabihar.Adapter.CategoryContentRecyclerAdapter;
import app.zingo.merabihar.Adapter.ContentCityHomePager;
import app.zingo.merabihar.Adapter.ContentCollectionAdapter;
import app.zingo.merabihar.Adapter.ExperienceContentAdapter;
import app.zingo.merabihar.Adapter.ProfileListAdapter;
import app.zingo.merabihar.Adapter.ViewPagerAdapter;
import app.zingo.merabihar.Model.Category;
import app.zingo.merabihar.Model.Contents;
import app.zingo.merabihar.Model.SubCategories;
import app.zingo.merabihar.R;
import app.zingo.merabihar.UI.ActivityScreen.LandingScreen.LandingScreenActivity;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.CategoryApi;
import app.zingo.merabihar.WebApi.ContentAPI;
import app.zingo.merabihar.WebApi.SubCategoryAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabHomeScreen extends AppCompatActivity {

    ViewPager mContentsCityPager;
    ProgressBar mContentProgress;
    RecyclerView mCategoryContents,mCollection,mExperience;
    LinearLayout mCollectionShow,mExperienceShow;

    ArrayList<Contents> contentsList;
    ArrayList<Category> categoryList;
    ArrayList<SubCategories> subcategoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_tab_home_screen);

            mContentsCityPager = (ViewPager)findViewById(R.id.content_pager_city);
            mContentProgress = (ProgressBar) findViewById(R.id.progressBar_content);
            mCollectionShow = (LinearLayout) findViewById(R.id.collection_show_layout);
            mExperienceShow = (LinearLayout) findViewById(R.id.experience_show_layout);
            mCategoryContents = (RecyclerView) findViewById(R.id.content_based_category);
            mCategoryContents.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mCategoryContents.setNestedScrollingEnabled(false);

            mCollection = (RecyclerView) findViewById(R.id.all_collections_contents);
            mCollection.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            mCollection.setNestedScrollingEnabled(false);

            mExperience = (RecyclerView) findViewById(R.id.all_experience_contents);
            mExperience.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            mExperience.setNestedScrollingEnabled(false);


            mContentsCityPager.setClipToPadding(false);
            mContentsCityPager.setPadding(30,10,30,0);
            mContentsCityPager.setPageMargin(10);


            getContents();
            getCategories();
            getSubCategories();


        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void getContents()
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ContentAPI categoryAPI = Util.getClient().create(ContentAPI.class);
                Call<ArrayList<Contents>> getCat = categoryAPI.getContentsByCityId(2);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<Contents>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Contents>> call, Response<ArrayList<Contents>> response) {

                        mContentProgress.setVisibility(View.GONE);

                        if(response.code() == 200)
                        {

                            contentsList = response.body();

                            if(contentsList != null && contentsList.size() != 0)
                            {


                                ContentCityHomePager adapter = new ContentCityHomePager(TabHomeScreen.this,contentsList);
                                mContentsCityPager.setAdapter(adapter);


                            }
                            else
                            {

                                mContentsCityPager.setVisibility(View.GONE);
                            }
                        }else{
                            mContentsCityPager.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Contents>> call, Throwable t) {

                        mContentProgress.setVisibility(View.GONE);
                        mContentsCityPager.setVisibility(View.GONE);

                        Toast.makeText(TabHomeScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    public void getCategories()
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final CategoryApi categoryAPI = Util.getClient().create(CategoryApi.class);
                Call<ArrayList<Category>> getCat = categoryAPI.getCategoriesByCityId(2);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<Category>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {

                        mContentProgress.setVisibility(View.GONE);

                        if(response.code() == 200)
                        {

                            categoryList = response.body();

                            if(categoryList != null && categoryList.size() != 0)
                            {

                                CategoryContentRecyclerAdapter adapter = new CategoryContentRecyclerAdapter(TabHomeScreen.this,categoryList);
                                mCategoryContents.setAdapter(adapter);

                                mCollectionShow.setVisibility(View.VISIBLE);

                                ContentCollectionAdapter adapters = new ContentCollectionAdapter(TabHomeScreen.this,categoryList);
                                mCollection.setAdapter(adapters);



                            }
                            else
                            {

                                mCategoryContents.setVisibility(View.GONE);
                                mCollectionShow.setVisibility(View.GONE);
                            }
                        }else{
                            mCategoryContents.setVisibility(View.GONE);
                            mCollectionShow.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Category>> call, Throwable t) {

                        mCategoryContents.setVisibility(View.GONE);
                        mCollectionShow.setVisibility(View.GONE);

                        Toast.makeText(TabHomeScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    public void getSubCategories()
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final SubCategoryAPI categoryAPI = Util.getClient().create(SubCategoryAPI.class);
                Call<ArrayList<SubCategories>> getCat = categoryAPI.getSubCategoriesByCityId(2);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<SubCategories>>() {

                    @Override
                    public void onResponse(Call<ArrayList<SubCategories>> call, Response<ArrayList<SubCategories>> response) {



                        if(response.code() == 200)
                        {

                            subcategoryList = response.body();

                            if(subcategoryList != null && subcategoryList.size() != 0)
                            {



                                mExperienceShow.setVisibility(View.VISIBLE);

                                ExperienceContentAdapter adapters = new ExperienceContentAdapter(TabHomeScreen.this,subcategoryList);
                                mExperience.setAdapter(adapters);



                            }
                            else
                            {


                                mExperienceShow.setVisibility(View.GONE);
                            }
                        }else{

                            mExperienceShow.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<SubCategories>> call, Throwable t) {


                        mExperienceShow.setVisibility(View.GONE);

                        Toast.makeText(TabHomeScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }
}
