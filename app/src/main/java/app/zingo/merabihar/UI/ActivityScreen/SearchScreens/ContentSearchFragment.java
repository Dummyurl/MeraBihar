package app.zingo.merabihar.UI.ActivityScreen.SearchScreens;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import java.util.ArrayList;

import app.zingo.merabihar.Adapter.CategorySearchAdapter;
import app.zingo.merabihar.Adapter.ContentSearchAdapter;
import app.zingo.merabihar.Model.Category;
import app.zingo.merabihar.Model.Contents;
import app.zingo.merabihar.Model.UserProfile;
import app.zingo.merabihar.R;
import app.zingo.merabihar.Util.PreferenceHandler;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.CategoryApi;
import app.zingo.merabihar.WebApi.ContentAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentSearchFragment extends Fragment {

    RecyclerView recyclerViewFolloers,recyclerViewNonFolloers,recyclerProfile;
    private ProgressBar mProgressBar;
    EditText mSearchText;
    ScrollView mPeopleScroll;
    LinearLayout mNon,mOn;

    ArrayList<Contents> categoryArrayList;
    ArrayList<UserProfile> followings;
    int profileId;


    public ContentSearchFragment() {
        // Required empty public constructor
    }

    public static ContentSearchFragment newInstance() {
        ContentSearchFragment fragment = new ContentSearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment fragment_content_search

        try{
            View view = inflater.inflate(R.layout.fragment_content_search, container, false);
            recyclerViewFolloers = (RecyclerView) view.findViewById(R.id.people_following_list);
            recyclerViewNonFolloers = (RecyclerView) view.findViewById(R.id.people_non_following_list);
            recyclerProfile = (RecyclerView) view.findViewById(R.id.people_list);
            mPeopleScroll = (ScrollView) view.findViewById(R.id.scroll_profile);
            mNon = (LinearLayout) view.findViewById(R.id.non_layout);
            mOn = (LinearLayout) view.findViewById(R.id.on_layout);
            mSearchText = (EditText) view.findViewById(R.id.search_editText);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            mProgressBar.setVisibility(View.GONE);








            getContentents(profileId);



            mSearchText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    String text = mSearchText.getText().toString();

                    if(text.isEmpty()||text.equalsIgnoreCase("")){

                        mPeopleScroll.setVisibility(View.VISIBLE);
                        recyclerProfile.setVisibility(View.GONE);

                    }else{
                        filterProfiles(charSequence.toString().toLowerCase());

                    }




                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            return view;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void filterProfiles(String s) {

        ArrayList<Contents> filteredList = new ArrayList<>();
        mPeopleScroll.setVisibility(View.GONE);
        recyclerProfile.setVisibility(View.VISIBLE);

        try{
            for(int i=0;i<categoryArrayList.size();i++)
            {

                String fullName = "";


                if(categoryArrayList.get(i).getTitle()!=null){
                    fullName= categoryArrayList.get(i).getTitle().toLowerCase();
                }

                if(fullName.contains(s))
                {
                    filteredList.add(categoryArrayList.get(i));
                }



            }

            ContentSearchAdapter adapter = new ContentSearchAdapter(getActivity(),filteredList);
            recyclerProfile.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("OTA Error "+e.getMessage());

        }




    }


    public void getContentents(final int id)
    {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading Collections");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ContentAPI categoryApi = Util.getClient().create(ContentAPI.class);
                Call<ArrayList<Contents>> getBlog = categoryApi.getContentsByCityId(2);
                //Call<ArrayList<Blogs>> getBlog = blogApi.getBlogs();

                getBlog.enqueue(new Callback<ArrayList<Contents>>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<ArrayList<Contents>> call, Response<ArrayList<Contents>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        if (response.code() == 200)
                        {
                            //categoryArrayList = new ArrayList<>();
                            categoryArrayList = response.body();



                            if(categoryArrayList!=null&&categoryArrayList.size()!=0){


                                profileId = PreferenceHandler.getInstance(getActivity()).getUserId();
                                if(profileId!=0){
                                    getCategoriesByProfileID(profileId,categoryArrayList);
                                }


                            }else{

                            }




                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Contents>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }


                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });
    }

    public void getCategoriesByProfileID(final int id,final ArrayList<Contents> categoryArrayList)
    {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading Collections");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ContentAPI categoryApi = Util.getClient().create(ContentAPI.class);
                Call<ArrayList<Contents>> getBlog = categoryApi.getFavContentsByProfileId(id);
                //Call<ArrayList<Blogs>> getBlog = blogApi.getBlogs();

                getBlog.enqueue(new Callback<ArrayList<Contents>>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<ArrayList<Contents>> call, Response<ArrayList<Contents>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        if (response.code() == 200)
                        {
                            //categoryArrayList = new ArrayList<>();
                            ArrayList<Contents> categoryFollow = response.body();



                            if(categoryFollow!=null&&categoryFollow.size()!=0){

                                ContentSearchAdapter adapter = new ContentSearchAdapter(getActivity(),categoryFollow);
                                recyclerViewFolloers.setAdapter(adapter);

                                if(categoryFollow.size()!=0){
                                    categoryArrayList.removeAll(categoryFollow);
                                }

                                if(categoryArrayList!=null&&categoryArrayList.size()!=0){
                                    ContentSearchAdapter adapters = new ContentSearchAdapter(getActivity(),categoryArrayList);
                                    recyclerViewNonFolloers.setAdapter(adapters);
                                }


                            }else{

                            }




                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Contents>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }


                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });
    }


}
