package app.zingo.merabihar.UI.ActivityScreen.Events;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.zingo.merabihar.Adapter.ActivityListAdapter;
import app.zingo.merabihar.Adapter.BlogViewPagerAdapter;
import app.zingo.merabihar.Model.ActivityModel;
import app.zingo.merabihar.Model.Blogs;
import app.zingo.merabihar.Model.SubCategories;
import app.zingo.merabihar.R;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.ActivityApi;
import app.zingo.merabihar.WebApi.BlogApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryDetails extends AppCompatActivity {

    SubCategories subCategories;
    private static TextView mSubCategoryActivity,mCategoryDesc,mMoreBlogs,mSubDesc;
    private static ViewPager mBlogs;
    private static LinearLayout mActivityLayout,mBlogCollection;
    private static ListView mActivityList;
    CoordinatorLayout mMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_sub_category_details);

            // Get Toolbar component.
            Toolbar toolbar = (Toolbar) findViewById(R.id.collapsing_toolbar);

            mActivityLayout = (LinearLayout) findViewById(R.id.subcategory_collection);
            mMain = (CoordinatorLayout) findViewById(R.id.main_layout);
            mCategoryDesc = (TextView) findViewById(R.id.subcategory_desc);
            mSubDesc = (TextView) findViewById(R.id.sub_desc_name);
            mActivityList = (ListView) findViewById(R.id.events_list);
            mSubCategoryActivity = (TextView) findViewById(R.id.subcategory_activity);
            mMoreBlogs = (TextView) findViewById(R.id.more_blogs);
            mBlogCollection = (LinearLayout) findViewById(R.id.blogs_collection);
            mBlogs = (ViewPager) findViewById(R.id.blog_pager);
            mBlogs.setClipToPadding(false);
            mBlogs.setPageMargin(18);
            Animation uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
            mMain.setAnimation(uptodown);
            // Use Toolbar to replace default activity action bar.
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();


            if (actionBar != null) {
                // Display home menu item.
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            Bundle bundle = getIntent().getExtras();

            if (bundle != null) {

                subCategories = (SubCategories) bundle.getSerializable("SubCategory");

            }

            // Set collapsing tool bar title.
            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
            // Set collapsing tool bar image.
            ImageView collapsingToolbarImageView = (ImageView) findViewById(R.id.collapsing_toolbar_image_view);
            //collapsingToolbarImageView.setImageResource(R.drawable.img1);

            if (subCategories != null) {
                collapsingToolbarLayout.setTitle("" + subCategories.getSubCategoriesName());
                Picasso.with(SubCategoryDetails.this).load(subCategories.getSubCategoriesImage()).placeholder(R.drawable.no_image).
                        error(R.drawable.no_image).into(collapsingToolbarImageView);
                mSubCategoryActivity.setText("Activites in " + subCategories.getSubCategoriesName());
                mSubDesc.setText("About " + subCategories.getSubCategoriesName());
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {


                    mCategoryDesc.setText(Html.fromHtml(subCategories.getDescription(), Html.FROM_HTML_MODE_COMPACT));

                }else {


                    mCategoryDesc.setText(Html.fromHtml(subCategories.getDescription()));
                }


                getBlogs(subCategories.getSubCategoriesId());
                getActivities();
            } else {
                collapsingToolbarLayout.setTitle("Category Detail");
                Picasso.with(SubCategoryDetails.this).load(subCategories.getSubCategoriesImage()).placeholder(R.drawable.no_image).
                        error(R.drawable.no_image).into(collapsingToolbarImageView);
            }

            mMoreBlogs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent blogs = new Intent(SubCategoryDetails.this, BlogListActivity.class);
                    startActivity(blogs);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getActivities()
    {
        final ProgressDialog dialog = new ProgressDialog(SubCategoryDetails.this);
        dialog.setMessage("Loading Places");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ActivityApi activityApi = Util.getClient().create(ActivityApi.class);
                Call<ArrayList<ActivityModel>> getCat = activityApi.getActivityBySubCategoryId(subCategories.getSubCategoriesId());

                getCat.enqueue(new Callback<ArrayList<ActivityModel>>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<ArrayList<ActivityModel>> call, Response<ArrayList<ActivityModel>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        if (response.code() == 200)
                        {
                            ArrayList<ActivityModel> activityModels = response.body();

                            if(activityModels!=null&&activityModels.size()!=0){



                                ActivityListAdapter eventpagerAdapter = new ActivityListAdapter(SubCategoryDetails.this,activityModels);//,pagerModelArrayList);
                                mActivityList.setAdapter(eventpagerAdapter);
                                setListViewHeightBasedOnChildren(mActivityList);

                                //getBlogs();

                            }else{
                                mActivityLayout.setVisibility(View.GONE);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<ActivityModel>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(SubCategoryDetails.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });
    }

    public void getBlogs(final int catId)
    {
        final ProgressDialog dialog = new ProgressDialog(SubCategoryDetails.this);
        dialog.setMessage("Loading Blogs");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final BlogApi blogApi = Util.getClient().create(BlogApi.class);
                Call<ArrayList<Blogs>> getBlog = blogApi.getBlogsBySubCategoryId(catId);
                //Call<ArrayList<Blogs>> getBlog = blogApi.getBlogs();

                getBlog.enqueue(new Callback<ArrayList<Blogs>>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<ArrayList<Blogs>> call, Response<ArrayList<Blogs>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

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

                                    BlogViewPagerAdapter adapter = new BlogViewPagerAdapter(SubCategoryDetails.this,blogsArrayList);
                                    mBlogs.setAdapter(adapter);
                                }else{
                                    mBlogCollection.setVisibility(View.GONE);
                                }

                            }else{
                                mBlogCollection.setVisibility(View.GONE);
                            }




                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Blogs>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        Toast.makeText(SubCategoryDetails.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1))+120;
        listView.setLayoutParams(params);
        listView.requestLayout();
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
