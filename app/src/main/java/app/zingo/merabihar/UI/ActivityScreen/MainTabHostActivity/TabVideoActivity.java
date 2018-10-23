package app.zingo.merabihar.UI.ActivityScreen.MainTabHostActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Vector;

import app.zingo.merabihar.Adapter.ContentCityHomePager;
import app.zingo.merabihar.Adapter.VideoAdapter;
import app.zingo.merabihar.Model.Contents;
import app.zingo.merabihar.Model.YoutubeVideo;
import app.zingo.merabihar.R;
import app.zingo.merabihar.Util.Constants;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.ContentAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabVideoActivity extends AppCompatActivity {

    //RECYCLER VIEW FIELD
    RecyclerView recyclerView;
    ProgressBar mContentProgress;

    //VECTOR FOR VIDEO URLS
    Vector<YoutubeVideo> youtubeVideos = new Vector<YoutubeVideo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_tab_video);

            recyclerView = (RecyclerView) findViewById(R.id.video_youtube_recyclerView);
            mContentProgress = (ProgressBar) findViewById(R.id.progressBar_content);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager( new LinearLayoutManager(this));

            getContents();


        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void getContents()
    {


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ContentAPI categoryAPI = Util.getClient().create(ContentAPI.class);
                Call<ArrayList<Contents>> getCat = categoryAPI.getContentsByCityId(Constants.CITY_ID);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<Contents>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Contents>> call, Response<ArrayList<Contents>> response) {

                        mContentProgress.setVisibility(View.GONE);

                        if(response.code() == 200)
                        {

                            ArrayList<Contents> contentsList = response.body();

                            if(contentsList != null && contentsList.size() != 0)
                            {

                                //Load video List

                                for (Contents content :contentsList) {

                                    if(content.getContentType().equalsIgnoreCase("Video")){

                                        if(content.getContentURL()!=null&&!content.getContentURL().isEmpty()){

                                            youtubeVideos.add( new YoutubeVideo("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/"+content.getContentURL()+"\" frameborder=\"0\" allowfullscreen></iframe>"));
                                            //youtubeVideos.add( new YoutubeVideo("https://www.youtube.com/watch?v="+content.getContentURL()));

                                        }
                                    }

                                }


                                if(youtubeVideos!=null&&youtubeVideos.size()!=0){

                                    VideoAdapter videoAdapter = new VideoAdapter(youtubeVideos);

                                    recyclerView.setAdapter(videoAdapter);

                                }






                            }
                            else
                            {

                                Toast.makeText(TabVideoActivity.this, "No video", Toast.LENGTH_SHORT).show();

                            }
                        }else{

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Contents>> call, Throwable t) {

                        mContentProgress.setVisibility(View.GONE);

                        Toast.makeText(TabVideoActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }
}
