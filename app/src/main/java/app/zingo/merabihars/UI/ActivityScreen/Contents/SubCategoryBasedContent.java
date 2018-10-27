package app.zingo.merabihars.UI.ActivityScreen.Contents;

import android.app.ProgressDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import app.zingo.merabihars.Adapter.ContentCategoryRecyclerAdapter;
import app.zingo.merabihars.Model.Category;
import app.zingo.merabihars.Model.Contents;
import app.zingo.merabihars.Model.SubCategories;
import app.zingo.merabihars.R;
import app.zingo.merabihars.Util.ThreadExecuter;
import app.zingo.merabihars.Util.Util;
import app.zingo.merabihars.WebApi.ContentAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//activity_sub_category_based_content

public class SubCategoryBasedContent extends AppCompatActivity {

    SubCategories category;
    RecyclerView mContentList;

    private static Animation uptodown,downtoup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_sub_category_based_content);

            // Get Toolbar component.
            Toolbar toolbar = (Toolbar)findViewById(R.id.collapsing_toolbar);

            mContentList = (RecyclerView)findViewById(R.id.content_by_category);

            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();
            if(actionBar!=null)
            {
                // Display home menu item.
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            Bundle bundle = getIntent().getExtras();

            if(bundle!=null){

                category = (SubCategories) bundle.getSerializable("SubCategory");

            }

            // Set collapsing tool bar title.
            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar_layout);
            // Set collapsing tool bar image.
            ImageView collapsingToolbarImageView = (ImageView)findViewById(R.id.collapsing_toolbar_image_view);
            //collapsingToolbarImageView.setImageResource(R.drawable.img1);

            if(category!=null){

                collapsingToolbarLayout.setTitle(""+category.getSubCategoriesName());
                Picasso.with(SubCategoryBasedContent.this).load(category.getSubCategoriesImage()).placeholder(R.drawable.no_image).
                        error(R.drawable.no_image).into(collapsingToolbarImageView);

                getContents(category.getSubCategoriesId());

            }else{
                collapsingToolbarLayout.setTitle("Category Detail");
                Picasso.with(SubCategoryBasedContent.this).load(category.getSubCategoriesName()).placeholder(R.drawable.no_image).
                        error(R.drawable.no_image).into(collapsingToolbarImageView);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void getContents(final int id)
    {
        final ProgressDialog dialog = new ProgressDialog(SubCategoryBasedContent.this);
        dialog.setMessage("Loading Stories");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ContentAPI categoryAPI = Util.getClient().create(ContentAPI.class);
                Call<ArrayList<Contents>> getCat = categoryAPI.getContentsBySubCatId(id);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<Contents>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Contents>> call, Response<ArrayList<Contents>> response) {

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        if(response.code() == 200)
                        {

                            ArrayList<Contents> contentsList = response.body();

                            if(contentsList != null && contentsList.size() != 0)
                            {

                                Collections.shuffle(contentsList);
                                ContentCategoryRecyclerAdapter adapter = new ContentCategoryRecyclerAdapter(SubCategoryBasedContent.this,contentsList);
                                mContentList.setAdapter(adapter);


                            }
                            else
                            {


                            }
                        }else{


                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Contents>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }



                        Toast.makeText(SubCategoryBasedContent.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

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
