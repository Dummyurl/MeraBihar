package app.zingo.merabihar.UI.ActivityScreen.Events;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.zingo.merabihar.Adapter.ActivityInOtherAdapter;
import app.zingo.merabihar.Adapter.BlogViewPagerAdapter;
import app.zingo.merabihar.Adapter.SubCategoryGridAdapter;
import app.zingo.merabihar.Model.ActivityModel;
import app.zingo.merabihar.Model.Blogs;
import app.zingo.merabihar.Model.Category;
import app.zingo.merabihar.R;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.ActivityApi;
import app.zingo.merabihar.WebApi.BlogApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryDetailsScreen extends AppCompatActivity {

    Category category;
    private static TextView mCategoryActivity,mMoreActivity,mCategoryDesc,mMoreBlogs,mCateDes;
    private static ViewPager mActivityView,mBlogs;
    private static LinearLayout mActivityLayout;
    private static GridView mSubCategory;
    private static LinearLayout mBlogCollection,mDesLay;
    private static NestedScrollView mMain;
    private static Animation uptodown,downtoup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_category_details_screen);
            // Get Toolbar component.
            Toolbar toolbar = (Toolbar)findViewById(R.id.collapsing_toolbar);

            mActivityLayout = (LinearLayout) findViewById(R.id.activiy_collection);
            mMain = (NestedScrollView) findViewById(R.id.main_layout);
            mCateDes = (TextView) findViewById(R.id.category_desc_name);
            mCategoryDesc = (TextView) findViewById(R.id.category_desc);
            mCategoryActivity = (TextView) findViewById(R.id.category_activity);
            mMoreActivity = (TextView) findViewById(R.id.more_activity_title);
            mActivityView = (ViewPager) findViewById(R.id.top_activities_viewpager_category);
            mSubCategory = (GridView) findViewById(R.id.sub_category_grid);
            mMoreBlogs = (TextView) findViewById(R.id.more_blogs);
            mBlogCollection = (LinearLayout) findViewById(R.id.blogs_collection);
            mDesLay = (LinearLayout) findViewById(R.id.des_lay);
            uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
            downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
            mDesLay.setAnimation(downtoup);
            mBlogs = (ViewPager) findViewById(R.id.blog_pager);
            mBlogs.setClipToPadding(false);
            mBlogs.setPageMargin(18);
            // Use Toolbar to replace default activity action bar.
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();

            mActivityView.setClipToPadding(false);
            // mtopBlogs.setClipToPadding(false);
            mActivityView.setPageMargin(5);
            if(actionBar!=null)
            {
                // Display home menu item.
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            recyclerAnimation();

            Bundle bundle = getIntent().getExtras();

            if(bundle!=null){

                category = (Category)bundle.getSerializable("Category");

            }

            // Set collapsing tool bar title.
            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar_layout);
            // Set collapsing tool bar image.
            ImageView collapsingToolbarImageView = (ImageView)findViewById(R.id.collapsing_toolbar_image_view);
            //collapsingToolbarImageView.setImageResource(R.drawable.img1);

            if(category!=null){
                collapsingToolbarLayout.setTitle(""+category.getCategoriesName());
                Picasso.with(CategoryDetailsScreen.this).load(category.getCategoriesImage()).placeholder(R.drawable.no_image).
                        error(R.drawable.no_image).into(collapsingToolbarImageView);
                mCategoryActivity.setText("Activites in "+category.getCategoriesName());
                mCateDes.setText("About "+category.getCategoriesName());
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {


                    mCategoryDesc.setText(Html.fromHtml(category.getDescription(), Html.FROM_HTML_MODE_COMPACT));

                }else {


                    mCategoryDesc.setText(Html.fromHtml(category.getDescription()));
                }


                if(category.getSubCategoriesList()!=null&&category.getSubCategoriesList().size()!=0){

                    SubCategoryGridAdapter adapter = new SubCategoryGridAdapter(CategoryDetailsScreen.this,category.getSubCategoriesList());
                    mSubCategory.setAdapter(adapter);
                }
                getBlogs(category.getCategoriesId());
                getActivities();
            }else{
                collapsingToolbarLayout.setTitle("Category Detail");
                Picasso.with(CategoryDetailsScreen.this).load(category.getCategoriesImage()).placeholder(R.drawable.no_image).
                        error(R.drawable.no_image).into(collapsingToolbarImageView);
            }

            mMoreBlogs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDesLay.setAnimation(uptodown);
                    Intent blogs = new Intent(CategoryDetailsScreen.this,BlogListActivity.class);
                    startActivity(blogs);
                }
            });

            mMoreActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDesLay.setAnimation(uptodown);
                    Intent intent = new Intent(CategoryDetailsScreen.this, ListOfEventsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("title",category.getCategoriesName());
                    bundle.putInt("cat_id",category.getCategoriesId());
                    bundle.putSerializable("Category",category);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });


        }catch(Exception e){
            e.printStackTrace();
        }

    }

    //GetActivities By Category Id
    public void getActivities()
    {
        /*final ProgressDialog dialog = new ProgressDialog(CategoryDetailsScreen.this);
        dialog.setMessage("Loading Places");
        dialog.setCancelable(false);
        dialog.show();*/

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ActivityApi activityApi = Util.getClient().create(ActivityApi.class);
                Call<ArrayList<ActivityModel>> getCat = activityApi.getActivityByCategoryId(category.getCategoriesId());

                getCat.enqueue(new Callback<ArrayList<ActivityModel>>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<ArrayList<ActivityModel>> call, Response<ArrayList<ActivityModel>> response) {
                       /* if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/
                        if (response.code() == 200)
                        {
                            ArrayList<ActivityModel> activityModels = response.body();

                            if(activityModels!=null&&activityModels.size()!=0){



                                ActivityInOtherAdapter eventpagerAdapter = new ActivityInOtherAdapter(CategoryDetailsScreen.this,activityModels);//,pagerModelArrayList);
                                mActivityView.setAdapter(eventpagerAdapter);


                                //getBlogs();

                            }else{
                                mActivityLayout.setVisibility(View.GONE);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<ActivityModel>> call, Throwable t) {
                       /* if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/
                        Toast.makeText(CategoryDetailsScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });
    }

    public void getBlogs(final int catId)
    {
      /*  final ProgressDialog dialog = new ProgressDialog(CategoryDetailsScreen.this);
        dialog.setMessage("Loading Blogs");
        dialog.setCancelable(false);
        dialog.show();*/

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final BlogApi blogApi = Util.getClient().create(BlogApi.class);
                Call<ArrayList<Blogs>> getBlog = blogApi.getBlogsByCategoryId(catId);
                //Call<ArrayList<Blogs>> getBlog = blogApi.getBlogs();

                getBlog.enqueue(new Callback<ArrayList<Blogs>>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<ArrayList<Blogs>> call, Response<ArrayList<Blogs>> response) {
                       /* if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/

                        if (response.code() == 200)
                        {

                            ArrayList<Blogs> blogsArrayList = response.body();
                            ArrayList<Blogs> approvedBlogs = new ArrayList<>();



                            if(blogsArrayList!=null&&blogsArrayList.size()!=0){

                                for(int i=0;i<blogsArrayList.size();i++){
                                    if(blogsArrayList.get(i).isApproved()){
                                        approvedBlogs.add(blogsArrayList.get(i));
                                    }
                                }

                                if(approvedBlogs!=null && approvedBlogs.size()!=0){

                                    BlogViewPagerAdapter adapter = new BlogViewPagerAdapter(CategoryDetailsScreen.this,blogsArrayList);
                                    mBlogs.setAdapter(adapter);
                                }else{
                                    mBlogCollection.setVisibility(View.GONE);
                                }


                                //  setListViewHeightBasedOnChildren(mtopBlogs);

                            }else{
                                mBlogCollection.setVisibility(View.GONE);
                            }




                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Blogs>> call, Throwable t) {
                     /*   if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/

                        Toast.makeText(CategoryDetailsScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });
    }

    public void recyclerAnimation(){
        mMain.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        mMain.getViewTreeObserver().removeOnPreDrawListener(this);

                        for (int i = 0; i < mMain.getChildCount(); i++) {
                            View v = mMain.getChildAt(i);
                            v.setAlpha(0.0f);
                            v.animate().alpha(1.0f)
                                    .setDuration(300)
                                    .setStartDelay(i * 50)
                                    .start();
                        }

                        return true;
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCateDes.requestFocus();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        // When user click home menu item then quit this activity.
        if(itemId==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
