package app.zingo.merabihars.UI.ActivityScreen.MainTabHostActivity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import app.zingo.merabihars.Adapter.CategoryContentRecyclerAdapter;
import app.zingo.merabihars.Adapter.ContentCategoryRecyclerAdapter;
import app.zingo.merabihars.Adapter.ContentCityHomePager;
import app.zingo.merabihars.Adapter.ContentCollectionAdapter;
import app.zingo.merabihars.Adapter.ExperienceContentAdapter;
import app.zingo.merabihars.Model.Category;
import app.zingo.merabihars.Model.CategoryAndContentList;
import app.zingo.merabihars.Model.Contents;
import app.zingo.merabihars.Model.SubCategories;
import app.zingo.merabihars.Model.UserProfile;
import app.zingo.merabihars.R;
import app.zingo.merabihars.Util.Constants;
import app.zingo.merabihars.Util.PreferenceHandler;
import app.zingo.merabihars.Util.ThreadExecuter;
import app.zingo.merabihars.Util.Util;
import app.zingo.merabihars.WebApi.CategoryApi;
import app.zingo.merabihars.WebApi.ContentAPI;
import app.zingo.merabihars.WebApi.InterestProfileAPI;
import app.zingo.merabihars.WebApi.ProfileFollowAPI;
import app.zingo.merabihars.WebApi.SubCategoryAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabHomeScreen extends AppCompatActivity {

    ViewPager mContentsCityPager;
    ProgressBar mContentProgress;
    RecyclerView mCategoryContents,mCollection,mExperience,mFollowerContent,mFollowingContent,mInterestContent;
    LinearLayout mCollectionShow,mExperienceShow,mFollowerContentLayout;

    ArrayList<Contents> contentsList;
    ArrayList<Category> categoryList;
    ArrayList<CategoryAndContentList> categoryAndContentList;
    ArrayList<SubCategories> subcategoryList;

    ContentCategoryRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_tab_home_screen);

            mContentsCityPager = (ViewPager)findViewById(R.id.content_pager_city);
            mContentProgress = (ProgressBar) findViewById(R.id.progressBar_content);
            mCollectionShow = (LinearLayout) findViewById(R.id.collection_show_layout);
            mExperienceShow = (LinearLayout) findViewById(R.id.experience_show_layout);
            mFollowerContentLayout = (LinearLayout) findViewById(R.id.follower_content_show_layout);
            mCategoryContents = (RecyclerView) findViewById(R.id.content_based_category);
            mCategoryContents.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mCategoryContents.setNestedScrollingEnabled(false);

            mCollection = (RecyclerView) findViewById(R.id.all_collections_contents);
            mCollection.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            mCollection.setNestedScrollingEnabled(false);

            mExperience = (RecyclerView) findViewById(R.id.all_experience_contents);
            mExperience.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            mExperience.setNestedScrollingEnabled(false);

            mFollowerContent = (RecyclerView) findViewById(R.id.all_followers_contents);
            mFollowerContent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mFollowerContent.setNestedScrollingEnabled(false);

            mFollowingContent = (RecyclerView) findViewById(R.id.all_following_contents);
            mFollowingContent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mFollowingContent.setNestedScrollingEnabled(false);

            mInterestContent = (RecyclerView) findViewById(R.id.all_interest_contents);
            mInterestContent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mInterestContent.setNestedScrollingEnabled(false);


            mContentsCityPager.setClipToPadding(false);
            mContentsCityPager.setPadding(30,10,30,0);
            mContentsCityPager.setPageMargin(10);

            getCategoryAndContent();
            int userId = PreferenceHandler.getInstance(TabHomeScreen.this).getUserId();
            //int userId = 49;

            if(userId!=0){
                getFollowingByProfileId(userId);
                getFollowerByProfileId(userId);
                getContentsofInterest(userId);
            }else{
                mFollowerContentLayout.setVisibility(View.GONE);
            }

            //getContents();
            getCategories();
            getSubCategories();


        }catch(Exception e){
            e.printStackTrace();
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
                                    adapter = new ContentCategoryRecyclerAdapter(TabHomeScreen.this,followingContents);
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
                                    adapter = new ContentCategoryRecyclerAdapter(TabHomeScreen.this,followingContents);
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


    public void getContents()
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ContentAPI categoryAPI = Util.getClient().create(ContentAPI.class);
                Call<ArrayList<Contents>> getCat = categoryAPI.getContentsByCityId(Constants.CITY_ID);
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
                                adapter = new ContentCategoryRecyclerAdapter(TabHomeScreen.this,contentsInterestList);
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
                Call<ArrayList<Category>> getCat = categoryAPI.getCategoriesByCityId(Constants.CITY_ID);
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

                               /* CategoryContentRecyclerAdapter adapter = new CategoryContentRecyclerAdapter(TabHomeScreen.this,categoryList);
                                mCategoryContents.setAdapter(adapter);*/

                                mCollectionShow.setVisibility(View.VISIBLE);

                                ContentCollectionAdapter adapters = new ContentCollectionAdapter(TabHomeScreen.this,categoryList);
                                mCollection.setAdapter(adapters);



                            }
                            else
                            {

                             /*   mCategoryContents.setVisibility(View.GONE);*/
                                mCollectionShow.setVisibility(View.GONE);
                            }
                        }else{
                         /*   mCategoryContents.setVisibility(View.GONE);*/
                            mCollectionShow.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Category>> call, Throwable t) {

                     /*   mCategoryContents.setVisibility(View.GONE);*/
                        mCollectionShow.setVisibility(View.GONE);

                        Toast.makeText(TabHomeScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

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
                                CategoryContentRecyclerAdapter adapter = new CategoryContentRecyclerAdapter(TabHomeScreen.this,categoryAndContentList);
                                mCategoryContents.setAdapter(adapter);



                                if(contentsList!=null&&contentsList.size()!=0){
                                   ContentCityHomePager adapters = new ContentCityHomePager(TabHomeScreen.this,contentsList);
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
                Call<ArrayList<SubCategories>> getCat = categoryAPI.getSubCategoriesByCityId(Constants.CITY_ID);
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
