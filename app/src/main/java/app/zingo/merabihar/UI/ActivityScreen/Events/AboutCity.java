package app.zingo.merabihar.UI.ActivityScreen.Events;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import app.zingo.merabihar.Adapter.BiharViewAdapter;
import app.zingo.merabihar.Model.BiharData;
import app.zingo.merabihar.Model.MasterSetups;
import app.zingo.merabihar.R;
import app.zingo.merabihar.Util.Constants;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.MasterAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutCity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener{


    private static ViewPager mBiharViews;
    LinearLayout mBlogDes;

    private YouTubePlayerFragment playerFragment,playerFragmentTop;
    private YouTubePlayer mPlayer;
    private String YouTubeKey = Constants.YOUTUBE,url;

    //auto slide
    int currentPage = 0,start = 0,end = 0;
    Timer timer;
    final long DELAY_MS = 5000;
    final long PERIOD_MS = 3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_about_city);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            setTitle("ABOUT BIHAR");

            mBiharViews = (ViewPager) findViewById(R.id.about_bihar_viewpage);
            mBlogDes = (LinearLayout) findViewById(R.id.blog_desc);
           // playerFragment = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_player_fragment);
            playerFragmentTop = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_player_fragment_top);

           // url = "vUUuzfPbrJA";

          //  playerFragment.initialize(YouTubeKey, this);
            getContent("525372873773");

            setViewPager();



        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        mPlayer = player;

        //Enables automatic control of orientation
        mPlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);

        //Show full screen in landscape mode always
        //mPlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);

        //System controls will appear automatically
        mPlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);

        if (!wasRestored) {
            player.cueVideo(url);
            //mPlayer.loadVideo("9rLZYyMbJic");
        }
        else
        {
            mPlayer.play();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        mPlayer = null;
    }

    private void setViewPager() {

        TypedArray icons = getResources().obtainTypedArray(R.array.bihar_images);
        String[] title  = getResources().getStringArray(R.array.bihar_title);
        String[] desc  = getResources().getStringArray(R.array.bihar_desc);

        final ArrayList<BiharData> biharData = new ArrayList<>();

        for (int i=0;i<title.length;i++)
        {
            BiharData biharDataItem = new BiharData(title[i],desc[i],icons.getResourceId(i, -1));
            biharData.add(biharDataItem);
        }

        BiharViewAdapter activityImagesadapter = new BiharViewAdapter(AboutCity.this,biharData);
        mBiharViews.setAdapter(activityImagesadapter);

        final int size = title.length;

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == (size - 1)&& start == 0) {
                    currentPage = --currentPage;
                    start = 1;
                    end = 0;
                }else if(currentPage < (size - 1) && currentPage != 0 && end == 0&& start == 1){
                    currentPage = --currentPage;
                }else if(currentPage == 0 && end == 0 && start == 1){
                    currentPage = 0;
                    end = 1;
                    start = 0;
                }else if(currentPage <= (size - 1) && start == 0){

                    currentPage = ++currentPage;
                }else if(currentPage == 0&& start == 0){

                    currentPage = ++currentPage;
                }else{

                }
                mBiharViews.setCurrentItem(currentPage, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer .schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);


    }

    public void getContent(final String uniqueid)
    {
        final ProgressDialog dialog = new ProgressDialog(AboutCity.this);
        dialog.setMessage("Loading content");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                //  System.out.println(TAG+" thread started");
                final MasterAPI categoryAPI = Util.getClient().create(MasterAPI.class);
                Call<ArrayList<MasterSetups>> getCat = categoryAPI.getContentByUniqueId(uniqueid);
                //Call<ArrayList<Category>> getCat = categoryAPI.getCategories();

                getCat.enqueue(new Callback<ArrayList<MasterSetups>>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<ArrayList<MasterSetups>> call, Response<ArrayList<MasterSetups>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        //System.out.println(TAG+" thread inside on response cATEGORY");
                        System.out.println("Response code == "+response.code());
                        if(response.code() == 200)
                        {
                            ArrayList<MasterSetups> masterSetups = response.body();

                            if(response.body()!=null&&response.body().size()!=0){


                                String welcome = masterSetups.get(0).getWelcomeMessageDescription();

                                if(welcome!=null&&!welcome.isEmpty()&&welcome.contains("<h1-content>")){

                                    String[] message = welcome.split("<h1-content>");
                                    // mWelcomeMessage.setText(""+message[0]);

                                    for(int i = 1;i<message.length;i++){

                                        addView(message[i]);
                                    }

                                }

                                url=masterSetups.get(0).getURL();
                                if(url==null||url.isEmpty()){
                                    playerFragmentTop.getView().setVisibility(View.GONE);
                                }else{
                                    playerFragmentTop.initialize(YouTubeKey, AboutCity.this);
                                }



                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<MasterSetups>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(AboutCity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });

    }

    public void addView(String text)
    {
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try{
            View v = vi.inflate(R.layout.adapter_string_spilt, null);
            TextView mPara = (TextView)v.findViewById(R.id.para);

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                mPara.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));

            }else{
                mPara.setText(Html.fromHtml(text));
            }
           // mPara.setText(""+text);

            mBlogDes.addView(v);

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
                AboutCity.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
