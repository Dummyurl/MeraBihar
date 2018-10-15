package app.zingo.merabihar.UI.ActivityScreen.Events;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import app.zingo.merabihar.Adapter.MultiImageAdapter;
import app.zingo.merabihar.Adapter.ViewPagerAdapter;
import app.zingo.merabihar.Model.ActivityImages;
import app.zingo.merabihar.Model.Category;
import app.zingo.merabihar.Model.UserProfile;
import app.zingo.merabihar.R;
import app.zingo.merabihar.UI.ActivityScreen.AccountScreens.ProfileActivity;
import app.zingo.merabihar.UI.ActivityScreen.LandingScreen.LandingScreenActivity;
import app.zingo.merabihar.Util.Constants;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.ActivityApi;
import app.zingo.merabihar.WebApi.CategoryApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContentSearchScreen extends AppCompatActivity {

    ListView mImagesList;
    LinearLayout mCategoryLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_content_search_screen);

            mImagesList = (ListView) findViewById(R.id.image_list);
            mCategoryLayout = (LinearLayout) findViewById(R.id.category_layout);

            getCategories();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getActivityImages();
    }

    public void getActivityImages()
    {
        final ProgressDialog dialog = new ProgressDialog(ContentSearchScreen.this);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ActivityApi categoryAPI = Util.getClient().create(ActivityApi.class);
                Call<ArrayList<ActivityImages>> getCat = categoryAPI.getActivityImages();
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<ActivityImages>>() {

                    @Override
                    public void onResponse(Call<ArrayList<ActivityImages>> call, Response<ArrayList<ActivityImages>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        if(response.code() == 200)
                        {

                            if(response.body() != null && response.body().size() != 0)
                            {


                                ArrayList<ArrayList<ActivityImages>> activityImage = new ArrayList<>();
                                int count = 0;
                                ArrayList<ActivityImages> actImages = new ArrayList<>();

                                for(int i=0;i<response.body().size();i++){



                                    actImages.add(response.body().get(i));
                                    count++;
                                    if(count==5){

                                        activityImage.add(actImages);
                                        activityImage.add(actImages);
                                        count= 0;
                                        actImages = new ArrayList<>();

                                    }

                                }

                                if(activityImage!=null&&activityImage.size()!=0){

                                    MultiImageAdapter blogAdapter = new MultiImageAdapter(ContentSearchScreen.this,activityImage);//,pagerModelArrayList);
                                    mImagesList.setAdapter(blogAdapter);
                                    setListViewHeightBasedOnChildren(mImagesList);
                                    mImagesList.setSelection(0);
                                }


                            }
                            else
                            {

                            }
//                            loadRoomCategoriesSpinner();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<ActivityImages>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        Toast.makeText(ContentSearchScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    public void getCategories()
    {
        final ProgressDialog dialog = new ProgressDialog(ContentSearchScreen.this);
        dialog.setMessage("Loading Categories");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final CategoryApi categoryAPI = Util.getClient().create(CategoryApi.class);
                Call<ArrayList<Category>> getCat = categoryAPI.getCategoriesByCityId(2);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<Category>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        if(response.code() == 200)
                        {

                            if(response.body() != null && response.body().size() != 0)
                            {

                                for(int i=0;i< response.body().size();i++){

                                    addView(response.body().get(i));
                                }

                            }
                            else
                            {
                                mCategoryLayout.setVisibility(View.GONE);

                            }
                        }else{
                            mCategoryLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(ContentSearchScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        mCategoryLayout.setVisibility(View.GONE);
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    public void addView(Category category)
    {
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.content_category_adapter, null);
        TextView mCategoryName = (TextView)v.findViewById(R.id.collection_name);
        ImageView mCategoryImage = (ImageView) v.findViewById(R.id.collection_image);

        if(category !=null)
        {

            mCategoryName.setText(""+category.getCategoriesName());



            if(category.getCategoriesImage()!=null){

                Picasso.with(ContentSearchScreen.this).load(category.getCategoriesImage()).placeholder(R.drawable.no_image).
                        error(R.drawable.no_image).into(mCategoryImage);
            }

            mCategoryLayout.addView(v);

        }


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
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1))+20;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
