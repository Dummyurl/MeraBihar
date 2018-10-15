package app.zingo.merabihar.UI.ActivityScreen.Events;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import app.zingo.merabihar.Adapter.BlogMainAdapterList;
import app.zingo.merabihar.Model.Blogs;
import app.zingo.merabihar.R;
import app.zingo.merabihar.Util.Constants;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.BlogApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlogListActivity extends AppCompatActivity {

    private static ListView mtopBlogs;

    ArrayList<Blogs> blogsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_blog_list);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            setTitle("Blogs List");


            mtopBlogs = (ListView) findViewById(R.id.top_blogs_viewpager);

            getBlogs();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void getBlogs()
    {
        final ProgressDialog dialog = new ProgressDialog(BlogListActivity.this);
        dialog.setMessage("Loading Blogs");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final BlogApi blogApi = Util.getClient().create(BlogApi.class);
                Call<ArrayList<Blogs>> getBlog = blogApi.getBlogsByCityId(Constants.CITY_ID);
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
                            blogsList = new ArrayList<>();
                            ArrayList<Blogs> blogsArrayList = response.body();

                            if(response.body()!=null&&response.body().size()!=0){

                                Collections.reverse(blogsArrayList);

                                if(blogsArrayList!=null&&blogsArrayList.size()!=0){

                                    for (int i = 0;i<blogsArrayList.size();i++){

                                        if(blogsArrayList.get(i).isApproved()&&blogsArrayList.get(i).getStatus().equalsIgnoreCase("Approved")){
                                            blogsList.add(blogsArrayList.get(i));
                                        }
                                    }

                                    if(blogsList!=null&&blogsList.size()!=0){
                                        BlogMainAdapterList blogAdapter = new BlogMainAdapterList(BlogListActivity.this,blogsList);//,pagerModelArrayList);
                                        mtopBlogs.setAdapter(blogAdapter);
                                    }else{
                                        Toast.makeText(BlogListActivity.this, "No Blogs", Toast.LENGTH_SHORT).show();
                                    }

                                  //  setListViewHeightBasedOnChildren(mtopBlogs);

                                }else{
                                    Toast.makeText(BlogListActivity.this, "No Blogs", Toast.LENGTH_SHORT).show();
                                }


                            }else{
                                Toast.makeText(BlogListActivity.this, "No Blogs", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Blogs>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        Toast.makeText(BlogListActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
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
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1))+100;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                BlogListActivity.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
