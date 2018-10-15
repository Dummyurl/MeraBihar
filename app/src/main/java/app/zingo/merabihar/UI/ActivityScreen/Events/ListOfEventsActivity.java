package app.zingo.merabihar.UI.ActivityScreen.Events;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import app.zingo.merabihar.Adapter.EventListAdapter;
import app.zingo.merabihar.CustomInterface.RecyclerTouchListener;
import app.zingo.merabihar.Model.ActivityModel;
import app.zingo.merabihar.R;
import app.zingo.merabihar.Util.Constants;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.ActivityApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListOfEventsActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<ActivityModel> ActivityArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_list_of_events);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            recyclerView = (RecyclerView) findViewById(R.id.events_list);

            Bundle bundle = getIntent().getExtras();
            if(bundle != null)
            {
                String title = bundle.getString("title");
                int id = bundle.getInt("cat_id");
                if(title != null)
                {
                    setTitle(title);
                    if(id != 0)
                    {
                        getActivities(id);
                    }
                }
            }

           /* EventListAdapter adapter = new EventListAdapter(ListOfEventsActivity.this,new ArrayList<ActivityModel>());//,response.body());
            recyclerView.setAdapter(adapter);*/

            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(ListOfEventsActivity.this, recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Intent intent = new Intent(ListOfEventsActivity.this, ActivityDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.ACTIVITY,ActivityArrayList.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void getActivities(final int id)
    {
        final ProgressDialog dialog = new ProgressDialog(ListOfEventsActivity.this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ActivityApi activityApi = Util.getClient().create(ActivityApi.class);
                Call<ArrayList<ActivityModel>> getCat = activityApi.getActivityByCategoryId(id);

                getCat.enqueue(new Callback<ArrayList<ActivityModel>>() {

                    @Override
                    public void onResponse(Call<ArrayList<ActivityModel>> call, Response<ArrayList<ActivityModel>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        try{
                            if (response.code() == 200 && response.body() != null )
                            {
                                ActivityArrayList = response.body();
                                if(ActivityArrayList!=null&&ActivityArrayList.size() != 0)
                                {
                                    EventListAdapter adapter = new EventListAdapter(ListOfEventsActivity.this,response.body());
                                    recyclerView.setAdapter(adapter);
                                }
                                else
                                {
                                    Toast.makeText(ListOfEventsActivity.this,"No activities found for this caegory",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        //System.out.println(TAG+" thread inside on response");

                    }

                    @Override
                    public void onFailure(Call<ArrayList<ActivityModel>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        //System.out.println(TAG+" thread inside on fail");
                        Toast.makeText(ListOfEventsActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
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
                ListOfEventsActivity.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
