package app.zingo.merabihar.UI.ActivityScreen.Contents;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.zingo.merabihar.Adapter.AutocompleteCustomArrayAdapter;
import app.zingo.merabihar.Adapter.SubCategoryListAdapter;
import app.zingo.merabihar.CustomViews.CustomAutoComplete;
import app.zingo.merabihar.CustomViews.CustomGridView;
import app.zingo.merabihar.Model.ContentImages;
import app.zingo.merabihar.Model.Contents;
import app.zingo.merabihar.Model.Interest;
import app.zingo.merabihar.Model.InterestAndContents;
import app.zingo.merabihar.Model.InterestContentMapping;
import app.zingo.merabihar.Model.InterestContentResponse;
import app.zingo.merabihar.Model.SubCategories;
import app.zingo.merabihar.R;
import app.zingo.merabihar.UI.ActivityScreen.MainTabHostActivity.TabMainActivity;
import app.zingo.merabihar.Util.Action;
import app.zingo.merabihar.Util.Constants;
import app.zingo.merabihar.Util.PreferenceHandler;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.ContentAPI;
import app.zingo.merabihar.WebApi.InterestAPI;
import app.zingo.merabihar.WebApi.InterestContentAPI;
import app.zingo.merabihar.WebApi.SubCategoryAPI;
import app.zingo.merabihar.WebApi.UploadApi;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostVideoYoutubeContent extends AppCompatActivity {

    LinearLayout mTaglay,mActivityLay;
    EditText mTitle,mShort,mLong,mURL;
    CustomGridView customGridView;
    CustomAutoComplete mTags;
    Button mSave;

    SubCategories activity;
    SubCategoryListAdapter adapter;

    private ArrayList<SubCategories> activities;
    ArrayList<Interest> tlist;
    ArrayList<Integer> initerestId;
    int initerestIds = 0;



    private int activityId;
    
    ArrayList<String> interestList;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_post_video_youtube_content);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            setTitle("New Story");

            mTitle = (EditText) findViewById(R.id.blog_title);
            mShort = (EditText) findViewById(R.id.short_desc_blog);
            mLong = (EditText) findViewById(R.id.long_desc_blog);
            mURL = (EditText) findViewById(R.id.youtube_url);
            mTaglay = (LinearLayout) findViewById(R.id.tag_layout);
            mActivityLay = (LinearLayout) findViewById(R.id.activity_list);
            mSave = (Button) findViewById(R.id.create_blogs);
            customGridView = (CustomGridView) findViewById(R.id.interest_grid_view);
            mTags = (CustomAutoComplete) findViewById(R.id.tagss_blog);

            getActivities();
            getInterest();
            initerestId = new ArrayList<>();

            mTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    mTags.setText("#"+tlist.get(position).getInterestName());

                    // initerestId.add(tlist.get(position).getZingoInterestId());
                    initerestIds = tlist.get(position).getZingoInterestId();
                }
            });


           

            mSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //  System.out.println("Selected Image List "+selectedImageList.size());

                    if(activities!=null&&activities.size()!=0){
                        int i = adapter.getCount();
                        String selected = "";

                        activity = new SubCategories();
                        for (int j=0;j<i;j++)
                        {
                            //System.out.println();
                            if(((LinearLayout)customGridView.getChildAt(j)).isActivated())
                            {

                                activity = activities.get(j);
                                activityId = activities.get(j).getSubCategoriesId();
                                break;

                            }
                        }

                        if(activityId!=0){

                            System.out.println("URL VIdeo = "+extractYTId(mURL.getText().toString()));
                           validate();
                        }else{

                            Toast.makeText(PostVideoYoutubeContent.this, "Please Select atleast one tags", Toast.LENGTH_SHORT).show();
                        }

                    }






                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }

    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                PostVideoYoutubeContent.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getActivities()
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                final SubCategoryAPI activityApi = Util.getClient().create(SubCategoryAPI.class);
                Call<ArrayList<SubCategories>> getCat = activityApi.getSubCategoriesByCityId(Constants.CITY_ID);

                getCat.enqueue(new Callback<ArrayList<SubCategories>>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<ArrayList<SubCategories>> call, Response<ArrayList<SubCategories>> response) {

                        if (response.code() == 200)
                        {


                            if(response.body()!=null&&response.body().size()!=0){

                                activities = response.body();

                                adapter = new SubCategoryListAdapter(PostVideoYoutubeContent.this,activities);
                                customGridView.setAdapter(adapter);


                            }else{

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<SubCategories>> call, Throwable t) {


                        Toast.makeText(PostVideoYoutubeContent.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });
    }

    public void validate(){

        String title = mTitle.getText().toString();
        // String shortDesc = mShort.getText().toString();
        String longDesc = mLong.getText().toString();
        String tags = mTags.getText().toString();
        String url = mURL.getText().toString();

        System.out.println(" Tags id = "+tags);
        System.out.println(" Tags id = "+interestList.contains(tags));


        if(title==null||title.isEmpty()){
            Toast.makeText(PostVideoYoutubeContent.this, "Should not be Empty", Toast.LENGTH_SHORT).show();
        }else if(url==null||url.isEmpty()){
            Toast.makeText(PostVideoYoutubeContent.this, "Should not be Empty", Toast.LENGTH_SHORT).show();
        }else if(longDesc==null||longDesc.isEmpty()){
            Toast.makeText(PostVideoYoutubeContent.this, "Should not be Empty", Toast.LENGTH_SHORT).show();
        }else{

            try
            {

                SimpleDateFormat sdf  = new SimpleDateFormat("MM/dd/yyyy");

                Contents blogs = new Contents();
                blogs.setTitle(mTitle.getText().toString());
                blogs.setDescription(mLong.getText().toString());
                blogs.setContentType("Video");
                blogs.setContentURL(extractYTId(mURL.getText().toString()));
                blogs.setCreatedBy(PreferenceHandler.getInstance(PostVideoYoutubeContent.this).getUserFullName());
                blogs.setCreatedDate(""+new SimpleDateFormat("MM/dd/yyyyy").format(new Date()));
                blogs.setUpdatedDate(""+new SimpleDateFormat("MM/dd/yyyyy").format(new Date()));
                blogs.setProfileId(PreferenceHandler.getInstance(PostVideoYoutubeContent.this).getUserId());
                blogs.setSubCategoriesId(activityId);


                if(mTags.getText().toString()!=null||!mTags.getText().toString().isEmpty()){

                    if(interestList.contains(mTags.getText().toString())){

                        if(initerestIds!=0){


                            postBlogsWithInterest(blogs);

                        }else{
                            ArrayList<Interest> interests = new ArrayList<>();

                            Interest interest = new Interest();
                            interest.setInterestName(mTags.getText().toString().replace("#",""));
                            interest.setDescription(mTags.getText().toString().replace("#",""));
                            interests.add(interest);

                            InterestAndContents interestAndBlogs = new InterestAndContents();
                            interestAndBlogs.setContent(blogs);
                            interestAndBlogs.setInterestList(interests);
                            postBlogsAndNewInterest(interestAndBlogs);
                        }
                    }else{

                        ArrayList<Interest> interests = new ArrayList<>();

                        Interest interest = new Interest();
                        interest.setInterestName(mTags.getText().toString().replace("#",""));
                        interest.setDescription(mTags.getText().toString().replace("#",""));
                        interests.add(interest);

                        InterestAndContents interestAndBlogs = new InterestAndContents();
                        interestAndBlogs.setContent(blogs);
                        interestAndBlogs.setInterestList(interests);
                        postBlogsAndNewInterest(interestAndBlogs);
                    }

                }else{
                    initerestIds = 0;
                    postBlogs(blogs);
                }
                //blogs.setActivitiesId(90);





            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

        }
    }





    private void postBlogs(final Contents blogs) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.show();



        //System.out.println(sub.getCategoriesName()+","+sub.getDescription()+","+sub.getOrderNo()+","+sub.getCityId());

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ContentAPI blogsAPI = Util.getClient().create(ContentAPI.class);
                Call<Contents> response = blogsAPI.postContent(blogs);
                response.enqueue(new Callback<Contents>() {
                    @Override
                    public void onResponse(Call<Contents> call, Response<Contents> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {



                            Toast.makeText(PostVideoYoutubeContent.this,"Story created Successfull",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PostVideoYoutubeContent.this, TabMainActivity.class);
                            startActivity(intent);
                            PostVideoYoutubeContent.this.finish();
                        }
                        else
                        {
                            Toast.makeText(PostVideoYoutubeContent.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Contents> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(PostVideoYoutubeContent.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }




    private void postBlogsWithInterest(final Contents blogs) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.show();



        //System.out.println(sub.getCategoriesName()+","+sub.getDescription()+","+sub.getOrderNo()+","+sub.getCityId());

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ContentAPI blogsAPI = Util.getClient().create(ContentAPI.class);
                Call<Contents> response = blogsAPI.postContent(blogs);
                response.enqueue(new Callback<Contents>() {
                    @Override
                    public void onResponse(Call<Contents> call, Response<Contents> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {
                            InterestContentMapping interestBlogMapping = new InterestContentMapping();
                            interestBlogMapping.setContentId(response.body().getContentId());
                            interestBlogMapping.setZingoInterestId(initerestIds);
                            postBlogInterestMapping(interestBlogMapping);

                        }
                        else
                        {
                            Toast.makeText(PostVideoYoutubeContent.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Contents> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(PostVideoYoutubeContent.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    private void postBlogInterestMapping(final InterestContentMapping blogs) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.show();



        //System.out.println(sub.getCategoriesName()+","+sub.getDescription()+","+sub.getOrderNo()+","+sub.getCityId());

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                InterestContentAPI blogsAPI = Util.getClient().create(InterestContentAPI.class);
                Call<InterestContentMapping> response = blogsAPI.postInterestContent(blogs);
                response.enqueue(new Callback<InterestContentMapping>() {
                    @Override
                    public void onResponse(Call<InterestContentMapping> call, Response<InterestContentMapping> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {
                            Toast.makeText(PostVideoYoutubeContent.this,"Story created Successfull",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PostVideoYoutubeContent.this, TabMainActivity.class);
                            startActivity(intent);
                            PostVideoYoutubeContent.this.finish();
                        }
                        else
                        {
                            Toast.makeText(PostVideoYoutubeContent.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<InterestContentMapping> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(PostVideoYoutubeContent.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }


    private void postBlogsAndNewInterest(final InterestAndContents blogs) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.show();



        //System.out.println(sub.getCategoriesName()+","+sub.getDescription()+","+sub.getOrderNo()+","+sub.getCityId());

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ContentAPI blogsAPI = Util.getClient().create(ContentAPI.class);
                Call<ArrayList<InterestContentResponse>> response = blogsAPI.postContentsWithMultipleNewInterest(blogs);
                response.enqueue(new Callback<ArrayList<InterestContentResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<InterestContentResponse>> call, Response<ArrayList<InterestContentResponse>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {
                            Toast.makeText(PostVideoYoutubeContent.this,"Story created Successfull",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PostVideoYoutubeContent.this, TabMainActivity.class);
                            startActivity(intent);
                            PostVideoYoutubeContent.this.finish();
                        }
                        else
                        {
                            Toast.makeText(PostVideoYoutubeContent.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<InterestContentResponse>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(PostVideoYoutubeContent.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }


    public void getInterest()
    {

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


                        if(response.code() == 200)
                        {
                            tlist = response.body();

                            interestList  = new ArrayList<>();

                            if(tlist!=null&&tlist.size()!=0){
                                for(int i =0;i<tlist.size();i++){

                                    interestList.add("#"+tlist.get(i).getInterestName());
                                }
                                AutocompleteCustomArrayAdapter autocompleteCustomArrayAdapter =
                                        new AutocompleteCustomArrayAdapter(PostVideoYoutubeContent.this,R.layout.interest_row,tlist);
                                mTags.setThreshold(1);
                                mTags.setAdapter(autocompleteCustomArrayAdapter);
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Interest>> call, Throwable t) {


                        Toast.makeText(PostVideoYoutubeContent.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    public static String extractYTId(String youtubeUrl) {
        if (TextUtils.isEmpty(youtubeUrl)) {
            return "";
        }
        String video_id = "";

        String expression = "^.*((youtu.be" + "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
        CharSequence input = youtubeUrl;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            String groupIndex1 = matcher.group(7);
            if (groupIndex1 != null && groupIndex1.length() == 11)
                video_id = groupIndex1;
        }
        if (TextUtils.isEmpty(video_id)) {
            if (youtubeUrl.contains("youtu.be/")  ) {
                String spl = youtubeUrl.split("youtu.be/")[1];
                if (spl.contains("\\?")) {
                    video_id = spl.split("\\?")[0];
                }else {
                    video_id =spl;
                }

            }
        }

        return video_id;
    }

}
