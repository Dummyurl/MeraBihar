package app.zingo.merabihar.UI.ActivityScreen.MainTabHostActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import app.zingo.merabihar.Adapter.CategoryContentRecyclerAdapter;
import app.zingo.merabihar.Adapter.MultiContentImageAdapter;
import app.zingo.merabihar.Adapter.MultiImageAdapter;
import app.zingo.merabihar.Model.ActivityImages;
import app.zingo.merabihar.Model.Category;
import app.zingo.merabihar.Model.CategoryAndContentList;
import app.zingo.merabihar.Model.ContentImages;
import app.zingo.merabihar.Model.Contents;
import app.zingo.merabihar.R;
import app.zingo.merabihar.UI.ActivityScreen.SearchScreens.SearchActivity;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.CategoryApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabSearchActivity extends AppCompatActivity {

    ListView mImagesList;
    LinearLayout mCategoryLayout,mSearchLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_tab_search);

            mImagesList = (ListView) findViewById(R.id.image_list);
            mCategoryLayout = (LinearLayout) findViewById(R.id.category_layout);
            mSearchLay = (LinearLayout) findViewById(R.id.search_layout);

            mSearchLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent search = new Intent(TabSearchActivity.this, SearchActivity.class);
                    startActivity(search);
                }
            });

            getCategoryAndContent();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void getCategoryAndContent()
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final CategoryApi categoryAPI = Util.getClient().create(CategoryApi.class);
                Call<ArrayList<CategoryAndContentList>> getCat = categoryAPI.getContentAndCategoryByCityId(2);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<CategoryAndContentList>>() {

                    @Override
                    public void onResponse(Call<ArrayList<CategoryAndContentList>> call, Response<ArrayList<CategoryAndContentList>> response) {



                        if(response.code() == 200)
                        {

                            ArrayList<CategoryAndContentList> categoryAndContentList = response.body();

                            if(categoryAndContentList != null && categoryAndContentList.size() != 0)
                            {


                                ArrayList<Category> categories = new ArrayList<>();
                                ArrayList<Contents> contents = new ArrayList<>();
                                ArrayList<ContentImages> contentImage = new ArrayList<>();
                                int count = 0;
                                ArrayList<ArrayList<ContentImages>> contentImageList = new ArrayList<>();

                                for(int i=0;i<categoryAndContentList.size();i++){

                                    categories.add(categoryAndContentList.get(i).getCategories());

                                    if(categoryAndContentList.get(i).getContentList()!=null&&categoryAndContentList.get(i).getContentList().size()!=0){


                                        for (Contents content:categoryAndContentList.get(i).getContentList()) {

                                            contents.add(content);

                                            if(content.getContentImage()!=null&&content.getContentImage().size()!=0){

                                                for (ContentImages contentImages:content.getContentImage()) {

                                                    contentImage.add(contentImages);
                                                    count++;
                                                    if(count==5){

                                                        contentImageList.add(contentImage);

                                                        count= 0;
                                                        contentImage = new ArrayList<>();

                                                    }
                                                }
                                            }

                                        }

                                            if(contentImageList!=null&&contentImageList.size()!=0){

                                                MultiContentImageAdapter blogAdapter = new MultiContentImageAdapter(TabSearchActivity.this,contentImageList);//,pagerModelArrayList);
                                                mImagesList.setAdapter(blogAdapter);
                                                setListViewHeightBasedOnChildren(mImagesList);
                                                mSearchLay.requestFocus();

                                            }

                                    }
                                }




                            }
                            else
                            {



                            }
                        }else{

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<CategoryAndContentList>> call, Throwable t) {



                        Toast.makeText(TabSearchActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();

       /* mImagesList.postDelayed(new Runnable() {
            @Override
            public void run() {
                mImagesList.setSelection(0);
                mImagesList.getChildAt(0).requestFocus();
            }
        }, 500);*/
    }

    public  void setListViewHeightBasedOnChildren(ListView listView) {
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
        //mImagesList.getChildAt(0).requestFocus();
    }
}
