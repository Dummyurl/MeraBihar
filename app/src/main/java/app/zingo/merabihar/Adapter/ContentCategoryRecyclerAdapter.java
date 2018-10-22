package app.zingo.merabihar.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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
import app.zingo.merabihar.R;
import app.zingo.merabihar.UI.ActivityScreen.Contents.ConentDetailScreen;
import app.zingo.merabihar.UI.ActivityScreen.Events.AboutBlogs;
import app.zingo.merabihar.UI.ActivityScreen.Events.AboutCity;
import app.zingo.merabihar.Util.Constants;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.ContentAPI;
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

        if(contents!=null){


            holder.contentName.setText(contents.getTitle());

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


        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder  {

        ImageView imageView,mIcon;
        TextView contentName;
        TextView contentCate;
        TextView contentBy;
        LinearLayout mContentDetail;

        //private YouTubePlayerFragment playerFragment;



        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);

            imageView = (ImageView) itemView.findViewById(R.id.content_images);
            mContentDetail = (LinearLayout) itemView.findViewById(R.id.content_detail_layout);
            mIcon = (ImageView) itemView.findViewById(R.id.youtube_icon);
            contentName = (TextView) itemView.findViewById(R.id.content_name);
            contentCate = (TextView) itemView.findViewById(R.id.content_subcategory);
            contentBy = (TextView) itemView.findViewById(R.id.content_by);




        }


    }




}
