package app.zingo.merabihars.UI.ActivityScreen.Events;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import app.zingo.merabihars.Adapter.CollectionsAdapter;
import app.zingo.merabihars.Model.Category;
import app.zingo.merabihars.R;
import app.zingo.merabihars.Util.Constants;
import app.zingo.merabihars.Util.ThreadExecuter;
import app.zingo.merabihars.Util.Util;
import app.zingo.merabihars.WebApi.CategoryApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesListScreen extends AppCompatActivity {

    private static ListView mAllCollections;

    ArrayList<Category> categoryArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_categories_list_screen);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            setTitle("All Collections");

            mAllCollections = (ListView) findViewById(R.id.all_collections_list);

            getCategories();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void getCategories()
    {
        final ProgressDialog dialog = new ProgressDialog(CategoriesListScreen.this);
        dialog.setMessage("Loading Collections");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final CategoryApi categoryApi = Util.getClient().create(CategoryApi.class);
                Call<ArrayList<Category>> getBlog = categoryApi.getCategoriesByCityId(Constants.CITY_ID);
                //Call<ArrayList<Blogs>> getBlog = blogApi.getBlogs();

                getBlog.enqueue(new Callback<ArrayList<Category>>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        if (response.code() == 200)
                        {
                            //categoryArrayList = new ArrayList<>();
                            categoryArrayList = response.body();



                                if(categoryArrayList!=null&&categoryArrayList.size()!=0){



                                    if(categoryArrayList!=null&&categoryArrayList.size()!=0){
                                        CollectionsAdapter adapter = new CollectionsAdapter(CategoriesListScreen.this,categoryArrayList);//,pagerModelArrayList);
                                        mAllCollections.setAdapter(adapter);
                                    }else{
                                        Toast.makeText(CategoriesListScreen.this, "No Collections", Toast.LENGTH_SHORT).show();
                                    }

                                    //  setListViewHeightBasedOnChildren(mtopBlogs);

                                }else{
                                    Toast.makeText(CategoriesListScreen.this, "No Collections", Toast.LENGTH_SHORT).show();
                                }




                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        Toast.makeText(CategoriesListScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                CategoriesListScreen.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
