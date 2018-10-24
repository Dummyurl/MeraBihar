package app.zingo.merabihar.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubePlayer;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import app.zingo.merabihar.Model.Contents;
import app.zingo.merabihar.Model.Likes;
import app.zingo.merabihar.Model.ProfileFollowMapping;
import app.zingo.merabihar.Model.UserProfile;
import app.zingo.merabihar.R;
import app.zingo.merabihar.UI.ActivityScreen.AccountScreens.LoginScreen;
import app.zingo.merabihar.UI.ActivityScreen.AccountScreens.SignUpScreen;
import app.zingo.merabihar.UI.ActivityScreen.Contents.CommentsScreen;
import app.zingo.merabihar.UI.ActivityScreen.Contents.ConentDetailScreen;
import app.zingo.merabihar.Util.PreferenceHandler;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Contents contents = list.get(position);
        final int profileId = PreferenceHandler.getInstance(context).getUserId();
        String img = null;

        if(contents!=null){


            holder.contentName.setText(contents.getTitle());
            holder.mContentDesc.setText(contents.getDescription());

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
                    img = "https://img.youtube.com/vi/"+url+"/0.jpg";
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

                    img = contents.getContentImage().get(0).getImages();

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
                                holder.mLikedId.setText(""+likes.getLikeId());
                            }
                        }


                    }else{
                        disliked.add(likes);

                        if(profileId!=0){
                            if(likes.getProfileId()==profileId){
                                profileDislike = true;
                                holder.mDislikedId.setText(""+likes.getLikeId());
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

            holder.mMore.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    holder.mMore.setVisibility(View.GONE);
                    holder.mLess.setVisibility(View.VISIBLE);
                    holder.mContentDesc.setMaxLines(Integer.MAX_VALUE);

                }
            });

            holder.mLess.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    holder.mLess.setVisibility(View.GONE);
                    holder.mMore.setVisibility(View.VISIBLE);
                    holder.mContentDesc.setMaxLines(2);

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

                        if(holder.mDisliked.getVisibility()==View.VISIBLE){

                            if(holder.mDislikedId.getText().toString()!=null&&!holder.mDislikedId.getText().toString().isEmpty()){


                                deleteDisLike(likes,holder.mLike,holder.mLiked,holder.mLikeCount,Integer.parseInt(holder.mDislikedId.getText().toString()),holder.mDisliked,holder.mDislike,holder.mDislikedId,holder.mDislikeCount,holder.mLikedId);
                            }
                        }else{

                            postLike(likes,holder.mLike,holder.mLiked,holder.mLikeCount,holder.mLikedId);
                        }



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

                        holder.mDislike.setEnabled(false);
                        Likes likes = new Likes();
                        likes.setContentId(contents.getContentId());
                        likes.setProfileId(profileId);
                        likes.setLiked(false);

                        if(holder.mLiked.getVisibility()==View.VISIBLE){

                            if(holder.mLikedId.getText().toString()!=null&&!holder.mLikedId.getText().toString().isEmpty()){


                                deleteLike(likes,holder.mDislike,holder.mDisliked,holder.mDislikeCount,Integer.parseInt(holder.mLikedId.getText().toString()),holder.mLiked,holder.mLike,holder.mLikedId,holder.mLikeCount,holder.mDislikedId);
                            }
                        }else{


                            postDisLike(likes,holder.mDislike,holder.mDisliked,holder.mDislikeCount,holder.mDislikedId);
                        }


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

            holder.mWhatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AsyncTask mMyTask;
                    if(contents.getContentType().equalsIgnoreCase("Video")) {

                        url = contents.getContentURL();


                        if (url != null && !url.isEmpty()) {
                            mMyTask = new DownloadTask()
                                    .execute(stringToURL(
                                            "https://img.youtube.com/vi/"+url+"/0.jpg"
                                    ));

                        }

                    }else{

                        mMyTask = new DownloadTask()
                                .execute(stringToURL(
                                        ""+contents.getContentImage().get(0).getImages()
                                ));
                    }
                }
            });

            holder.mFollowing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(profileId!=0){

                        if(holder.mFollowing.getText().toString().equalsIgnoreCase("Follow")){
                            holder.mFollowing.setEnabled(false);
                            ProfileFollowMapping pm = new ProfileFollowMapping();
                            pm.setFollowerId(contents.getProfileId());
                            pm.setProfileId(PreferenceHandler.getInstance(context).getUserId());
                            profileFollow(pm,holder.mFollowing);
                        }else{

                            Toast.makeText(context, "Already you followed "+contents.getCreatedBy(), Toast.LENGTH_SHORT).show();
                        }

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

            holder.mComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(profileId!=0){

                        Intent comments = new Intent(context, CommentsScreen.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Contents",contents);
                        bundle.putInt("Position",position);
                        comments.putExtras(bundle);
                        context.startActivity(comments);

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

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder  {

        ImageView imageView,mIcon,mLike,mLiked,mDislike,mDisliked,mComment,mWhatsapp;
        TextView contentName,contentCate,contentBy,mLikeCount,mDislikeCount,
                mCommentCount,mContentDesc,mMore,mLess,mLikedId,mDislikedId;
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
            mContentDesc = (TextView) itemView.findViewById(R.id.content_desc);
            mMore = (TextView) itemView.findViewById(R.id.read_more);
            mLess = (TextView) itemView.findViewById(R.id.read_less);
            mLikedId = (TextView) itemView.findViewById(R.id.liked_id);
            mDislikedId = (TextView) itemView.findViewById(R.id.disliked_id);
            mLike = (ImageView) itemView.findViewById(R.id.like_icon);
            mLiked = (ImageView) itemView.findViewById(R.id.liked_icon);
            mDislike = (ImageView) itemView.findViewById(R.id.dislike_icon);
            mDisliked = (ImageView) itemView.findViewById(R.id.disliked_icon);
            mComment = (ImageView) itemView.findViewById(R.id.comments_icon);
            mWhatsapp = (ImageView) itemView.findViewById(R.id.whatsaapp_share);




        }


    }


    private void postLike(final Likes likes, final ImageView like,final ImageView liked,final TextView likeCount,final TextView likedId) {



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
                         likedId.setText(""+response.body().getLikeId());
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

    private void deleteDisLike(final Likes likes, final ImageView like,final ImageView liked,final TextView likeCount,final int likeId,final ImageView disliked,final ImageView dislike,final TextView dislikeId,final TextView dislikeCount,final TextView likedIds) {



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LikeAPI mapApi = Util.getClient().create(LikeAPI.class);
                Call<Likes> response = mapApi.deleteLikes(likeId);
                response.enqueue(new Callback<Likes>() {
                    @Override
                    public void onResponse(Call<Likes> call, Response<Likes> response) {

                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {

                            dislike.setVisibility(View.VISIBLE);
                            dislike.setEnabled(true);
                            disliked.setVisibility(View.GONE);
                            dislikeId.setText("");
                            String dislikeText = dislikeCount.getText().toString();
                            if(dislikeText!=null&&!dislikeText.isEmpty()){

                                int count = Integer.parseInt(dislikeText);

                                if(count<=0){

                                }else{
                                    dislikeCount.setText(""+(count-1));
                                }

                            }
                            postLike(likes,like,liked,likeCount,likedIds);
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

    private void deleteLike(final Likes likes, final ImageView like,final ImageView liked,final TextView likeCount,final int likeId,final ImageView disliked,final ImageView dislike,final TextView dislikeId,final TextView dislikeCount,final TextView likedIds) {



        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LikeAPI mapApi = Util.getClient().create(LikeAPI.class);
                Call<Likes> response = mapApi.deleteLikes(likeId);
                response.enqueue(new Callback<Likes>() {
                    @Override
                    public void onResponse(Call<Likes> call, Response<Likes> response) {

                        System.out.println(response.code());

                        if(response.code() == 201||response.code() == 200||response.code() == 204)
                        {

                            dislike.setVisibility(View.VISIBLE);
                            dislike.setEnabled(true);
                            disliked.setVisibility(View.GONE);
                            dislikeId.setText("");
                            String dislikeText = dislikeCount.getText().toString();
                            if(dislikeText!=null&&!dislikeText.isEmpty()){

                                int count = Integer.parseInt(dislikeText);

                                if(count<=0){

                                }else{
                                    dislikeCount.setText(""+(count-1));
                                }

                            }
                            postDisLike(likes,like,liked,likeCount,likedIds);
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



    private void postDisLike(final Likes likes, final ImageView like,final ImageView liked,final TextView likeCount,final TextView dislikeId) {



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
                            dislikeId.setText(""+response.body().getLikeId());

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


    private class DownloadTask extends AsyncTask<URL,Void,Bitmap> {
        // Before the tasks execution
        protected void onPreExecute(){
            // Display the progress dialog on async task start
            //mProgressDialog.show();
            Toast.makeText(context, "Downloading image...", Toast.LENGTH_SHORT).show();
        }

        // Do the task in background/non UI thread
        protected Bitmap doInBackground(URL...urls){
            URL url = urls[0];
            HttpURLConnection connection = null;

            try{
                // Initialize a new http url connection
                connection = (HttpURLConnection) url.openConnection();

                // Connect the http url connection
                connection.connect();

                // Get the input stream from http url connection
                InputStream inputStream = connection.getInputStream();

                /*
                    BufferedInputStream
                        A BufferedInputStream adds functionality to another input stream-namely,
                        the ability to buffer the input and to support the mark and reset methods.
                */
                /*
                    BufferedInputStream(InputStream in)
                        Creates a BufferedInputStream and saves its argument,
                        the input stream in, for later use.
                */
                // Initialize a new BufferedInputStream from InputStream
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                /*
                    decodeStream
                        Bitmap decodeStream (InputStream is)
                            Decode an input stream into a bitmap. If the input stream is null, or
                            cannot be used to decode a bitmap, the function returns null. The stream's
                            position will be where ever it was after the encoded data was read.

                        Parameters
                            is InputStream : The input stream that holds the raw data
                                              to be decoded into a bitmap.
                        Returns
                            Bitmap : The decoded bitmap, or null if the image data could not be decoded.
                */
                // Convert BufferedInputStream to Bitmap object
                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);

                // Return the downloaded bitmap
                return bmp;

            }catch(IOException e){
                e.printStackTrace();
            }finally{
                // Disconnect the http url connection
                connection.disconnect();
            }
            return null;
        }

        // When all async task done
        protected void onPostExecute(Bitmap result){
            // Hide the progress dialog
            // mProgressDialog.dismiss();

            if(result!=null){
                // Display the downloaded image into ImageView
                //mImageView.setImageBitmap(result);

                // Save bitmap to internal storage

                // Set the ImageView image from internal storage
                //mImageViewInternal.setImageURI(imageInternalUri);

                try{



                    File sd = Environment.getExternalStorageDirectory();
                    String fileName = System.currentTimeMillis() + ".png";

                    File directory = new File(sd.getAbsolutePath()+"/Bihar Tourism App/Share/");
                    //create directory if not exist
                    if (!directory.exists() && !directory.isDirectory()) {
                        directory.mkdirs();
                    }

                    File file = new File(directory, fileName);

                    Intent shareIntent;


                    OutputStream out = null;
                    try {
                        out = new FileOutputStream(file);
                        mark(result).compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    fileName=file.getPath();

                    Uri bmpUri = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        bmpUri = FileProvider.getUriForFile(context, "app.zingo.merabihar.fileprovider", file);
                    }else{
                        bmpUri = Uri.parse("file://"+fileName);
                    }
                    // Uri bmpUri = Uri.parse("file://"+path);
                    shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    shareIntent.setPackage("com.whatsapp");
                    /*String sAux = "\n"+mBlogName.getText().toString()+"\n\n";
                    sAux = sAux + "to read more click here "+shortUrl+" \n\n"+"To get the latest update about Bihar Download our Bihar Tourism official mobile app by clicking goo.gl/rZfotV";*/
                    shareIntent.putExtra(Intent.EXTRA_TEXT,"Check");
                    shareIntent.setType("image/png");
                    try{

                        context.startActivity(shareIntent);

                    }catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(context, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                    }
                    //context.startActivity(Intent.createChooser(shareIntent,"Share with"));

                }catch (Exception we)
                {
                    we.printStackTrace();
                    Toast.makeText(context, "Unable to send,Check Permission", Toast.LENGTH_SHORT).show();
                }

            }else {
                // Notify user that an error occurred while downloading image
                //Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
            }
        }
    }

    // Custom method to convert string to url
    protected URL stringToURL(String urlString){
        try{
            URL url = new URL(urlString);
            return url;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }

    // Custom method to save a bitmap into internal storage
    public Bitmap mark(Bitmap src) {

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.app_logo);
        int w = src.getWidth();
        int h = src.getHeight();
        int pw=w-w;
        int ph=h-h;
        int nw = (w * 10)/100;
        int nh = (h * 10)/100;
        Bitmap result = Bitmap.createBitmap(w, h, icon.getConfig());
        Canvas canvas = new Canvas(result);
        Bitmap resized = Bitmap.createScaledBitmap(icon, nw, nh, true);

        canvas.drawBitmap(src, 0, 0, null);
        Paint paint = new Paint();

        paint.setTextSize(30);
        paint.setAntiAlias(true);
        paint.setUnderlineText(false);
        canvas.drawBitmap(resized,pw,ph,paint);
        return result;
    }


    private void profileFollow(final ProfileFollowMapping intrst, final AppCompatButton tv) {



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
