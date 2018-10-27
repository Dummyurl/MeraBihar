package app.zingo.merabihars.UI.ActivityScreen.AccountScreens;

import android.app.ProgressDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import app.zingo.merabihars.Adapter.BlogMainAdapterList;
import app.zingo.merabihars.Model.Blogs;
import app.zingo.merabihars.Model.UserProfile;
import app.zingo.merabihars.R;
import app.zingo.merabihars.Util.ThreadExecuter;
import app.zingo.merabihars.Util.Util;
import app.zingo.merabihars.WebApi.BlogApi;
import app.zingo.merabihars.WebApi.ProfileAPI;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileBlogList extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    CircleImageView collapsingToolbarImageView;
    private static ListView mtopBlogs;

    ArrayList<Blogs> blogsList;

    private static Animation uptodown,downtoup;
    UserProfile profile;
    int profileId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_profile_blog_list);

            Toolbar toolbar = (Toolbar)findViewById(R.id.collapsing_toolbar);

            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();

            if(actionBar!=null)
            {
                // Display home menu item.
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            // Set collapsing tool bar title.
            collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar_layout);
            // Set collapsing tool bar image.
            collapsingToolbarImageView = (CircleImageView)findViewById(R.id.collapsing_toolbar_image_view);
            //collapsingToolbarImageView.setImageResource(R.drawable.img1);

            mtopBlogs = (ListView)findViewById(R.id.profile_blog_list);

            Bundle bundle = getIntent().getExtras();

            if(bundle!=null){

                profile = (UserProfile)bundle.getSerializable("Profile");
                profileId = bundle.getInt("ProfileId");
            }

            if(profile!=null){
                collapsingToolbarLayout.setTitle(""+profile.getFullName());
                Picasso.with(ProfileBlogList.this).load(profile.getProfilePhoto()).placeholder(R.drawable.profile_image).
                        error(R.drawable.profile_image).into(collapsingToolbarImageView);

                getBlogs(profile.getProfileId());
            }else{
                getProfile(profileId);
                getBlogs(profileId);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void getProfile(final int id){

      /*  final ProgressDialog dialog = new ProgressDialog(ActivityDetail.this);
        dialog.setMessage("Loading Packages");
        dialog.setCancelable(false);
        dialog.show();*/

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ProfileAPI subCategoryAPI = Util.getClient().create(ProfileAPI.class);
                Call<UserProfile> getProf = subCategoryAPI.getProfileById(id);
                //Call<ArrayList<Blogs>> getBlog = blogApi.getBlogs();

                getProf.enqueue(new Callback<UserProfile>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                       /* if(dialog != null)
                        {
                            dialog.dismiss();
                        }
*/
                        if (response.code() == 200)
                        {
                            System.out.println("Inside api");

                            UserProfile profile = response.body();

                            collapsingToolbarLayout.setTitle(""+profile.getFullName());

                            if(profile.getProfilePhoto()!=null){

                                String base=profile.getProfilePhoto();
                                if(base != null && !base.isEmpty()){
                                    Picasso.with(ProfileBlogList.this).load(base).placeholder(R.drawable.profile_image).
                                            error(R.drawable.profile_image).into(collapsingToolbarImageView);
                                    //mImage.setImageBitmap(
                                    //      BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

                                }
                            }




                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {
                       /* if(dialog != null)
                        {
                            dialog.dismiss();
                        }
*/
                        //Toast.makeText(.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });
    }

    public void getBlogs(final  int profileIds)
    {
        final ProgressDialog dialog = new ProgressDialog(ProfileBlogList.this);
        dialog.setMessage("Loading Blogs");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final BlogApi blogApi = Util.getClient().create(BlogApi.class);
                Call<ArrayList<Blogs>> getBlog = blogApi.getBlogsByStatusProfileId("Approved",profileIds);
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
                            //blogsList = new ArrayList<>();
                            ArrayList<Blogs> blogsArrayList = response.body();
                            blogsList = response.body();

                            if(response.body()!=null&&response.body().size()!=0){

                                Collections.reverse(blogsArrayList);
                                if(blogsArrayList!=null&&blogsArrayList.size()!=0){

                                    /*for (int i = 0;i<blogsArrayList.size();i++){

                                        if(blogsArrayList.get(i).isApproved()&&blogsArrayList.get(i).getStatus().equalsIgnoreCase("Approved")){
                                            blogsList.add(blogsArrayList.get(i));
                                        }
                                    }*/

                                    if(blogsList!=null&&blogsList.size()!=0){
                                        BlogMainAdapterList blogAdapter = new BlogMainAdapterList(ProfileBlogList.this,blogsList);//,pagerModelArrayList);
                                        mtopBlogs.setAdapter(blogAdapter);
                                        setListViewHeightBasedOnChildren(mtopBlogs);
                                    }else{
                                        Toast.makeText(ProfileBlogList.this, "No Blogs", Toast.LENGTH_SHORT).show();
                                    }

                                    //  setListViewHeightBasedOnChildren(mtopBlogs);

                                }else{
                                    Toast.makeText(ProfileBlogList.this, "No Blogs", Toast.LENGTH_SHORT).show();
                                }


                            }else{
                                Toast.makeText(ProfileBlogList.this, "No Blogs", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Blogs>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        Toast.makeText(ProfileBlogList.this,t.getMessage(),Toast.LENGTH_SHORT).show();
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
            ProfileBlogList.this.finish();
        }
        return super.onOptionsItemSelected(item);
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
}
