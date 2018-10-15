package app.zingo.merabihar.Adapter;

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

import app.zingo.merabihar.Model.ProfileFollowMapping;
import app.zingo.merabihar.Model.UserProfile;
import app.zingo.merabihar.R;
import app.zingo.merabihar.Util.PreferenceHandler;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.ProfileFollowAPI;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ZingoHotels Tech on 04-10-2018.
 */

public class ProfileFollowingAdapter extends RecyclerView.Adapter<ProfileFollowingAdapter.ViewHolder> {
    private Context context;
    private ArrayList<UserProfile> list;
    public ProfileFollowingAdapter(Context context,ArrayList<UserProfile> list) {

        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        try{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_following_list, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final UserProfile profile = list.get(position);

        if(profile!=null){

            holder.mName.setText(""+profile.getFullName());
            String base=profile.getProfilePhoto();
            if(base != null && !base.isEmpty()){
                Picasso.with(context).load(base).placeholder(R.drawable.profile_image).
                        error(R.drawable.profile_image).into(holder.mPhoto);


            }

            holder.mFollow.setVisibility(View.GONE);

            holder.mFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(holder.mFollow.getText().toString().equalsIgnoreCase("Follow")){
                        holder.mFollow.setEnabled(false);
                        ProfileFollowMapping pm = new ProfileFollowMapping();
                        pm.setFollowerId(profile.getProfileId());
                        pm.setProfileId(PreferenceHandler.getInstance(context).getUserId());
                        profileFollow(pm,holder.mFollow);
                    }else{

                        Toast.makeText(context, "Already you followed "+profile.getFullName(), Toast.LENGTH_SHORT).show();
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
        CircleImageView mPhoto;


//

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);

            mName = (TextView)itemView.findViewById(R.id.people_name);
            mFollow = (AppCompatButton) itemView.findViewById(R.id.follow_people);
            mPhoto = (CircleImageView)itemView.findViewById(R.id.people_profile_photo);


        }


    }

    private void profileFollow(final ProfileFollowMapping intrst,final AppCompatButton tv) {



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ProfileFollowAPI mapApi = Util.getClient().create(ProfileFollowAPI.class);
                Call<ProfileFollowMapping> response = mapApi.postInterest(intrst);
                response.enqueue(new Callback<ProfileFollowMapping>() {
                    @Override
                    public void onResponse(Call<ProfileFollowMapping> call, Response<ProfileFollowMapping> response) {

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
                    public void onFailure(Call<ProfileFollowMapping> call, Throwable t) {

                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                        tv.setEnabled(true);
                    }
                });
            }
        });
    }

}
