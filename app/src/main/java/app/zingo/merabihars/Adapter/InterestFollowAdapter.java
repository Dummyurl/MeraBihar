package app.zingo.merabihars.Adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.zingo.merabihars.Model.Category;
import app.zingo.merabihars.Model.Interest;
import app.zingo.merabihars.Model.InterestProfileMapping;
import app.zingo.merabihars.Model.ProfileFollowMapping;
import app.zingo.merabihars.R;
import app.zingo.merabihars.Util.PreferenceHandler;
import app.zingo.merabihars.Util.ThreadExecuter;
import app.zingo.merabihars.Util.Util;
import app.zingo.merabihars.WebApi.InterestProfileAPI;
import app.zingo.merabihars.WebApi.ProfileFollowAPI;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ZingoHotels Tech on 03-10-2018.
 */

public class InterestFollowAdapter  extends RecyclerView.Adapter<InterestFollowAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Interest> list;
    public InterestFollowAdapter(Context context,ArrayList<Interest> list) {

        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        try{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_interest_map, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Interest profile = list.get(position);

        if(profile!=null){

            holder.mName.setText(""+profile.getInterestName());

            holder.mFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(holder.mFollow.getText().toString().equalsIgnoreCase("Follow")){
                        holder.mFollow.setEnabled(false);
                        InterestProfileMapping pm = new InterestProfileMapping();
                        pm.setZingoInterestId(profile.getZingoInterestId());
                        pm.setProfileId(PreferenceHandler.getInstance(context).getUserId());
                        profileInterest(pm,holder.mFollow);
                    }else{

                        Toast.makeText(context, "Already you followed "+profile.getInterestName(), Toast.LENGTH_SHORT).show();
                    }


                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder  {

        TextView mName;
        AppCompatButton mFollow;



//

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);

            mName = (TextView)itemView.findViewById(R.id.interest_name);
            mFollow = (AppCompatButton) itemView.findViewById(R.id.follow_interest);



        }


    }

    private void profileInterest(final InterestProfileMapping intrst,final AppCompatButton tv) {



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                InterestProfileAPI mapApi = Util.getClient().create(InterestProfileAPI.class);
                Call<InterestProfileMapping> response = mapApi.postInterest(intrst);
                response.enqueue(new Callback<InterestProfileMapping>() {
                    @Override
                    public void onResponse(Call<InterestProfileMapping> call, Response<InterestProfileMapping> response) {

                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {

                            tv.setText("Following");
                            tv.setEnabled(true);

                        }
                        else
                        {
                            tv.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<InterestProfileMapping> call, Throwable t) {

                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                        tv.setEnabled(true);
                    }
                });
            }
        });
    }

}
