package app.zingo.merabihar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.zingo.merabihar.Model.Category;
import app.zingo.merabihar.Model.Contents;
import app.zingo.merabihar.R;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.ContentAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ZingoHotels Tech on 16-10-2018.
 */

public class ContentCategoryRecyclerAdapter extends RecyclerView.Adapter<ContentCategoryRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Contents> list;
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


            if(contents.getSubCategories()!=null){

                holder.contentCate.setText(""+contents.getSubCategories().getSubCategoriesName());
            }

            if(contents.getCreatedBy()!=null&&!contents.getCreatedBy().isEmpty()){

                holder.contentBy.setText(""+contents.getCreatedBy());
            }


        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder  {

        ImageView imageView;
        TextView contentName;
        TextView contentCate;
        TextView contentBy;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);

            imageView = (ImageView) itemView.findViewById(R.id.content_images);
            contentName = (TextView) itemView.findViewById(R.id.content_name);
            contentCate = (TextView) itemView.findViewById(R.id.content_subcategory);
            contentBy = (TextView) itemView.findViewById(R.id.content_by);



        }


    }




}
