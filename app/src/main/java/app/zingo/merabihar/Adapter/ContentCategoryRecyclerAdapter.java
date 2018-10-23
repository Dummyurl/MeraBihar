package app.zingo.merabihar.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.zingo.merabihar.Model.Category;
import app.zingo.merabihar.Model.Contents;
import app.zingo.merabihar.Model.Likes;
import app.zingo.merabihar.Model.ProfileFollowMapping;
import app.zingo.merabihar.Model.UserProfile;
import app.zingo.merabihar.R;
import app.zingo.merabihar.UI.ActivityScreen.AccountScreens.LoginScreen;
import app.zingo.merabihar.UI.ActivityScreen.AccountScreens.SignUpScreen;
import app.zingo.merabihar.UI.ActivityScreen.Contents.ConentDetailScreen;
import app.zingo.merabihar.UI.ActivityScreen.Events.AboutBlogs;
import app.zingo.merabihar.UI.ActivityScreen.Events.AboutCity;
import app.zingo.merabihar.UI.ActivityScreen.LandingScreen.LandingScreenActivity;
import app.zingo.merabihar.UI.ActivityScreen.MainTabHostActivity.TabAccountActivity;
import app.zingo.merabihar.Util.Constants;
import app.zingo.merabihar.Util.PreferenceHandler;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.ContentAPI;
import app.zingo.merabihar.WebApi.LikeAPI;
import app.zingo.merabihar.WebApi.ProfileAPI;
import app.zingo.merabihar.WebApi.ProfileFollowAPI;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ZingoHotels Tech on 16-10-2018.
 */

public class ContentCategoryRecyclerAdapter extends RecyclerView.Adapter<ContentCategoryRecyclerAdapter.ViewHolder>  {

    private Context context;
    private ArrayList<Contents> list;

    private YouTubePlayer mPlayer;
    String url;
    public ContentCategoryRecyclerAdapter(Context context,ArrayList<Contents> list) {

        this.context = context;
        this.list = list;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        try{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_content_recycle, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Contents contents = list.get(position);
        final int profileId = PreferenceHandler.getInstance(context).getUserId();

        if(contents!=null){


            holder.contentName.setText(contents.getTitle());

            if(profileId!=0){

                if(profileId==contents.getProfileId()){

                    holder.mFollowing.setVisibility(View.GONE);
                }else{
                    getFollowingByProfileId(profileId,holder.mFollowing,contents.getProfileId());

                }

            }

            if(contents.getProfile()==null){
                getProfile(contents.getProfileId(),holder.mProfilePhoto);
            }else{


                String base=contents.getProfile().getProfilePhoto();
                if(base != null && !base.isEmpty()){
                    Picasso.with(context).load(base).placeholder(R.drawable.profile_image).
                            error(R.drawable.profile_image).into(holder.mProfilePhoto);
                    //mImage.setImageBitmap(
                    //      BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

                }
            }


            if(contents.getContentType().equalsIgnoreCase("Video")){

                url = contents.getContentURL();

                holder.imageView.setVisibility(View.VISIBLE);
                holder.mIcon.setVisibility(View.VISIBLE);
                if(url!=null&&!url.isEmpty()){
                    String img = "https://img.youtube.com/vi/"+url+"/0.jpg";
                    if(img!=null&&!img.isEmpty()){
                        Picasso.with(context).load(img).placeholder(R.drawable.no_image).
                                error(R.drawable.no_image).into(holder.imageView);
                    }
                }





            }else{

                holder.imageView.setVisibility(View.VISIBLE);
                holder.mIcon.setVisibility(View.GONE);
                if(contents.getContentImage() != null && contents.getContentImage().size()!=0)
                {

                    String img = contents.getContentImage().get(0).getImages();

                    if(img!=null&&!img.isEmpty()){
                        Picasso.with(context).load(img).placeholder(R.drawable.no_image).
                                error(R.drawable.no_image).into(holder.imageView);
                    }else{
                        holder.imageView.setImageResource(R.drawable.no_image);
                    }



                }else{
                    holder.imageView.setImageResource(R.drawable.no_image);
                }

            }



            if(contents.getSubCategories()!=null){

                holder.contentCate.setText(""+contents.getSubCategories().getSubCategoriesName());
            }

            if(contents.getCreatedBy()!=null&&!contents.getCreatedBy().isEmpty()){

                holder.contentBy.setText(""+contents.getCreatedBy());
            }

            if(contents.getCommentsList()!=null&&contents.getCommentsList().size()!=0){

                holder.mCommentCount.setText(""+contents.getCommentsList().size());
            }

            if(contents.getLikes()!=null&&contents.getLikes().size()!=0){

                ArrayList<Likes> liked = new ArrayList<>();
                ArrayList<Likes> disliked = new ArrayList<>();

                boolean profileLike = false;
                boolean profileDislike = false;

                for (Likes likes:contents.getLikes()) {

                    if(likes.isLiked()){
                        liked.add(likes);

                        if(profileId!=0){
                            if(likes.getProfileId()==profileId){
                                profileLike = true;
                            }
                        }


                    }else{
                        disliked.add(likes);

                        if(profileId!=0){
                            if(likes.getProfileId()==profileId){
                                profileDislike = true;
                            }
                        }

                    }
                    
                }


                if(liked!=null&&liked.size()!=0){

                    holder.mLikeCount.setText(""+liked.size());

                }

                if(disliked!=null&&disliked.size()!=0){

                    holder.mDislikeCount.setText(""+disliked.size());
                }

                if(profileLike){

                    holder.mLiked.setVisibility(View.VISIBLE);
                    holder.mLike.setVisibility(View.GONE);
                }

                if(profileDislike){

                    holder.mDisliked.setVisibility(View.VISIBLE);
                    holder.mDislike.setVisibility(View.GONE);
                }
            }


            holder.mContentDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ConentDetailScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Contents",contents);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

            holder.mLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(profileId!=0){

                        holder.mLike.setEnabled(false);
                        Likes likes = new Likes();
                        likes.setContentId(contents.getContentId());
                        likes.setProfileId(profileId);
                        likes.setLiked(true);
                        postLike(likes,holder.mLike,holder.mLiked,holder.mLikeCount);

                    }else{

                        new AlertDialog.Builder(context)
                                .setMessage("Please login/Signup to Like the Story")
                                .setCancelable(false)
                                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent login = new Intent(context, LoginScreen.class);
                                        context.startActivity(login);

                                    }
                                })
                                .setNegativeButton("SignUp", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent signUp = new Intent(context, SignUpScreen.class);
                                        context.startActivity(signUp);

                                    }
                                })
                                .show();

                    }




                }
            });

            holder.mDislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(profileId!=0){

                        holder.mLike.setEnabled(false);
                        Likes likes = new Likes();
                        likes.setContentId(contents.getContentId());
                        likes.setProfileId(profileId);
                        likes.setLiked(false);
                        postDisLike(likes,holder.mDislike,holder.mDisliked,holder.mDislikeCount);

                    }else{

                        new AlertDialog.Builder(context)
                                .setMessage("Please login/Signup to Dislike the Story")
                                .setCancelable(false)
                                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent login = new Intent(context, LoginScreen.class);
                                        context.startActivity(login);

                                    }
                                })
                                .setNegativeButton("SignUp", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent signUp = new Intent(context, SignUpScreen.class);
                                        context.startActivity(signUp);

                                    }
                                })
                                .show();
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

        ImageView imageView,mIcon,mLike,mLiked,mDislike,mDisliked,mComment;
        TextView contentName,contentCate,contentBy,mLikeCount,mDislikeCount,mCommentCount;
        FrameLayout mContentDetail;
        CircleImageView mProfilePhoto;
        AppCompatButton mFollowing;

        //private YouTubePlayerFragment playerFragment;



        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);

            imageView = (ImageView) itemView.findViewById(R.id.content_images);
            mFollowing = (AppCompatButton) itemView.findViewById(R.id.follow_people);
            mProfilePhoto = (CircleImageView) itemView.findViewById(R.id.user_profile_photo);
            mContentDetail = (FrameLayout) itemView.findViewById(R.id.content_detail_layout);
            mIcon = (ImageView) itemView.findViewById(R.id.youtube_icon);
            contentName = (TextView) itemView.findViewById(R.id.content_name);
            contentCate = (TextView) itemView.findViewById(R.id.content_subcategory);
            contentBy = (TextView) itemView.findViewById(R.id.content_by);
            mLikeCount = (TextView) itemView.findViewById(R.id.like_count);
            mDislikeCount = (TextView) itemView.findViewById(R.id.dislike_count);
            mCommentCount = (TextView) itemView.findViewById(R.id.comments_count);
            mLike = (ImageView) itemView.findViewById(R.id.like_icon);
            mLiked = (ImageView) itemView.findViewById(R.id.liked_icon);
            mDislike = (ImageView) itemView.findViewById(R.id.dislike_icon);
            mDisliked = (ImageView) itemView.findViewById(R.id.disliked_icon);
            mComment = (ImageView) itemView.findViewById(R.id.comments_icon);




        }


    }


    private void postLike(final Likes likes, final ImageView like,final ImageView liked,final TextView likeCount) {



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LikeAPI mapApi = Util.getClient().create(LikeAPI.class);
                Call<Likes> response = mapApi.postLikes(likes);
                response.enqueue(new Callback<Likes>() {
                    @Override
                    public void onResponse(Call<Likes> call, Response<Likes> response) {

                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {

                         like.setVisibility(View.GONE);
                         liked.setVisibility(View.VISIBLE);
                         String likeText = likeCount.getText().toString();
                         if(likeText!=null&&!likeText.isEmpty()){

                             int count = Integer.parseInt(likeText);
                             likeCount.setText(""+(count+1));
                         }

                        }
                        else
                        {
                            like.setEnabled(true);

                        }
                    }

                    @Override
                    public void onFailure(Call<Likes> call, Throwable t) {

                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                        like.setEnabled(true);

                    }
                });
            }
        });
    }

    private void postDisLike(final Likes likes, final ImageView like,final ImageView liked,final TextView likeCount) {



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LikeAPI mapApi = Util.getClient().create(LikeAPI.class);
                Call<Likes> response = mapApi.postLikes(likes);
                response.enqueue(new Callback<Likes>() {
                    @Override
                    public void onResponse(Call<Likes> call, Response<Likes> response) {

                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {

                            like.setVisibility(View.GONE);
                            liked.setVisibility(View.VISIBLE);
                            String likeText = likeCount.getText().toString();
                            if(likeText!=null&&!likeText.isEmpty()){

                                int count = Integer.parseInt(likeText);
                                likeCount.setText(""+(count+1));
                            }

                        }
                        else
                        {
                            like.setEnabled(true);

                        }
                    }

                    @Override
                    public void onFailure(Call<Likes> call, Throwable t) {

                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                        like.setEnabled(true);

                    }
                });
            }
        });
    }

    public void getProfile(final int id,final CircleImageView cv){



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ProfileAPI subCategoryAPI = Util.getClient().create(ProfileAPI.class);
                Call<UserProfile> getProf = subCategoryAPI.getProfileById(id);
                //Call<ArrayList<Blogs>> getBlog = blogApi.getBlogs();

                getProf.enqueue(new Callback<UserProfile>() {

                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                       /* if(dialog != null)
                        {
                            dialog.dismiss();
                        }
*/
                        if (response.code() == 200)
                        {
                            System.out.println("Inside api");

                            UserProfile profile = response.body();



                            if(profile.getProfilePhoto()!=null){

                                String base=profile.getProfilePhoto();
                                if(base != null && !base.isEmpty()){
                                    Picasso.with(context).load(base).placeholder(R.drawable.profile_image).
                                            error(R.drawable.profile_image).into(cv);


                                }
                            }




                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {

                    }
                });




            }

        });
    }
    private void getFollowingByProfileId(final int id,final AppCompatButton button,final int contentProfileId){

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileFollowAPI apiService =
                        Util.getClient().create(ProfileFollowAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getFollowingByProfileId(id);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();


                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();

                            if(responseProfile != null && responseProfile.size()!=0 )
                            {

                                for (UserProfile profile:responseProfile) {

                                    if(profile.getProfileId()==contentProfileId){

                                        button.setText("Following");
                                        button.setEnabled(false);
                                        break;
                                    }

                                }



                            }
                            else
                            {


                            }
                        }
                        else
                        {


                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed



                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }


}
