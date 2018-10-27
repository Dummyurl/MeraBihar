package app.zingo.merabihars.UI.ActivityScreen.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.zingo.merabihars.Adapter.MultiImageAdapter;
import app.zingo.merabihars.Model.ActivityImages;
import app.zingo.merabihars.Model.Category;
import app.zingo.merabihars.R;
import app.zingo.merabihars.UI.ActivityScreen.Events.ContentSearchScreen;
import app.zingo.merabihars.UI.ActivityScreen.SearchScreens.SearchActivity;
import app.zingo.merabihars.Util.ThreadExecuter;
import app.zingo.merabihars.Util.Util;
import app.zingo.merabihars.WebApi.ActivityApi;
import app.zingo.merabihars.WebApi.CategoryApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    ListView mImagesList;
    LinearLayout mCategoryLayout,mSearchLay;


    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try{
            View view = inflater.inflate(R.layout.fragment_search, container, false);

            mImagesList = (ListView) view.findViewById(R.id.image_list);
            mCategoryLayout = (LinearLayout) view.findViewById(R.id.category_layout);
            mSearchLay = (LinearLayout) view.findViewById(R.id.search_layout);

            newInstance();

           getCategories();
           getActivityImages();


           mSearchLay.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   Intent search = new Intent(getActivity(), SearchActivity.class);
                   startActivity(search);
               }
           });

            return view;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void getActivityImages()
    {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
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

                                    MultiImageAdapter blogAdapter = new MultiImageAdapter(getActivity(),activityImage);//,pagerModelArrayList);
                                    mImagesList.setAdapter(blogAdapter);
                                    setListViewHeightBasedOnChildren(mImagesList);

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

                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    public void getCategories()
    {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
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
                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                        mCategoryLayout.setVisibility(View.GONE);
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    public void addView(Category category)
    {
        LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.content_category_adapter, null);
        TextView mCategoryName = (TextView)v.findViewById(R.id.collection_name);
        ImageView mCategoryImage = (ImageView) v.findViewById(R.id.collection_image);

        if(category !=null)
        {

            mCategoryName.setText(""+category.getCategoriesName());



            if(category.getCategoriesImage()!=null){

                Picasso.with(getActivity()).load(category.getCategoriesImage()).placeholder(R.drawable.no_image).
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
