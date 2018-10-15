package app.zingo.merabihar.UI.ActivityScreen.Events;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import app.zingo.merabihar.Adapter.ExperienceAdapter;
import app.zingo.merabihar.Model.SubCategories;
import app.zingo.merabihar.R;
import app.zingo.merabihar.Util.Constants;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.SubCategoryAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryListScreen extends AppCompatActivity {

    private static ListView mAllExperience;

    ArrayList<SubCategories> subCategoriesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_sub_category_list_screen);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            setTitle("All Experience");

            mAllExperience = (ListView) findViewById(R.id.all_experience_list);

            getSubCategory();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void getSubCategory()
    {
        final ProgressDialog dialog = new ProgressDialog(SubCategoryListScreen.this);
        dialog.setMessage("Loading Experiences");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final SubCategoryAPI categoryApi = Util.getClient().create(SubCategoryAPI.class);
                Call<ArrayList<SubCategories>> getBlog = categoryApi.getSubCategoriesByCityId(Constants.CITY_ID);
                //Call<ArrayList<Blogs>> getBlog = blogApi.getBlogs();

                getBlog.enqueue(new Callback<ArrayList<SubCategories>>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<ArrayList<SubCategories>> call, Response<ArrayList<SubCategories>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        if (response.code() == 200)
                        {
                            //subCategoriesArrayList = new ArrayList<>();
                            subCategoriesArrayList = response.body();



                            if(subCategoriesArrayList!=null&&subCategoriesArrayList.size()!=0){



                                if(subCategoriesArrayList!=null&&subCategoriesArrayList.size()!=0){
                                    ExperienceAdapter adapter = new ExperienceAdapter(SubCategoryListScreen.this,subCategoriesArrayList);//,pagerModelArrayList);
                                    mAllExperience.setAdapter(adapter);
                                }else{
                                    Toast.makeText(SubCategoryListScreen.this, "No Collections", Toast.LENGTH_SHORT).show();
                                }

                                //  setListViewHeightBasedOnChildren(mtopBlogs);

                            }else{
                                Toast.makeText(SubCategoryListScreen.this, "No Collections", Toast.LENGTH_SHORT).show();
                            }




                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<SubCategories>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        Toast.makeText(SubCategoryListScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
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
                SubCategoryListScreen.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
