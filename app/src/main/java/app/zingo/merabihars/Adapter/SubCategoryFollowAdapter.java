package app.zingo.merabihars.Adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.zingo.merabihars.Model.Category;
import app.zingo.merabihars.Model.InterestProfileMapping;
import app.zingo.merabihars.Model.ProfileSubCategoryMapping;
import app.zingo.merabihars.Model.SubCategories;
import app.zingo.merabihars.R;
import app.zingo.merabihars.Util.PreferenceHandler;
import app.zingo.merabihars.Util.ThreadExecuter;
import app.zingo.merabihars.Util.Util;
import app.zingo.merabihars.WebApi.InterestProfileAPI;
import app.zingo.merabihars.WebApi.ProfileSubCategoryAPI;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ZingoHotels Tech on 03-10-2018.
 */

public class SubCategoryFollowAdapter extends RecyclerView.Adapter<SubCategoryFollowAdapter.ViewHolder> {
    private Context context;
    private ArrayList<SubCategories> list;
    public SubCategoryFollowAdapter(Context context,ArrayList<SubCategories> list) {

        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        try{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_collection_follow, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final SubCategories profile = list.get(position);
        holder.mExperiences.setVisibility(View.GONE);

        if(profile!=null){

            holder.mName.setText(""+profile.getSubCategoriesName());
            String base=profile.getSubCategoriesImage();
            if(base != null && !base.isEmpty()){
                Picasso.with(context).load(base).placeholder(R.drawable.banner_background).
                        error(R.drawable.banner_background).into(holder.mPhoto);


            }

            holder.mFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(holder.mFollow.getText().toString().equalsIgnoreCase("Follow")){
                        holder.mFollow.setEnabled(false);
                        ProfileSubCategoryMapping pm = new ProfileSubCategoryMapping();
                        pm.setSubCategoryId(profile.getSubCategoriesId());
                        pm.setProfileId(PreferenceHandler.getInstance(context).getUserId());
                        profileSub(pm,holder.mFollow);
                    }else{

                        Toast.makeText(context, "Already you followed "+profile.getSubCategoriesName(), Toast.LENGTH_SHORT).show();
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
        AppCompatButton mFollow,mExperiences;
        ImageView mPhoto;


//

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);

            mName = (TextView)itemView.findViewById(R.id.banner_category_name);
            mFollow = (AppCompatButton) itemView.findViewById(R.id.follow_collection);
            mExperiences = (AppCompatButton) itemView.findViewById(R.id.follow_experience);
            mPhoto = (ImageView)itemView.findViewById(R.id.category_banner);


        }


    }

    private void profileSub(final ProfileSubCategoryMapping intrst, final AppCompatButton tv) {



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ProfileSubCategoryAPI mapApi = Util.getClient().create(ProfileSubCategoryAPI.class);
                Call<ProfileSubCategoryMapping> response = mapApi.postSubInterest(intrst);
                response.enqueue(new Callback<ProfileSubCategoryMapping>() {
                    @Override
                    public void onResponse(Call<ProfileSubCategoryMapping> call, Response<ProfileSubCategoryMapping> response) {

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
                    public void onFailure(Call<ProfileSubCategoryMapping> call, Throwable t) {

                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                        tv.setEnabled(true);
                    }
                });
            }
        });
    }

}
