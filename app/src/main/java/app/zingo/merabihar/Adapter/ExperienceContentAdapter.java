package app.zingo.merabihar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.zingo.merabihar.Model.Category;
import app.zingo.merabihar.Model.SubCategories;
import app.zingo.merabihar.R;
import app.zingo.merabihar.UI.ActivityScreen.Contents.CategoryBasedContents;
import app.zingo.merabihar.UI.ActivityScreen.Contents.SubCategoryBasedContent;

/**
 * Created by ZingoHotels Tech on 16-10-2018.
 */

public class ExperienceContentAdapter extends RecyclerView.Adapter<ExperienceContentAdapter.ViewHolder> {
    private Context context;
    private ArrayList<SubCategories> list;
    public ExperienceContentAdapter(Context context,ArrayList<SubCategories> list) {

        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        try{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_collection_content, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final SubCategories category = list.get(position);

        if(category!=null){

            holder.mName.setText(""+category.getSubCategoriesName());
            String base=category.getSubCategoriesImage();
            if(base != null && !base.isEmpty()){
                Picasso.with(context).load(base).placeholder(R.drawable.no_image).
                        error(R.drawable.no_image).into(holder.mPhoto);


            }else{
                holder.mPhoto.setImageResource(R.drawable.no_image);
            }


            holder.mCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent content = new Intent(context, SubCategoryBasedContent.class);
                    Bundle bn = new Bundle();
                    bn.putSerializable("SubCategory",category);
                    content.putExtras(bn);
                    context.startActivity(content);
                }
            });


        }

    }

    @Override
    public int getItemCount() {
      /*  if(list.size()>=5){
            return 5;
        }else{
            return list.size();
        }*/

        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder  {

        TextView mName;
        ImageView mPhoto;
        LinearLayout mCategory;


//

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);

            mName = (TextView)itemView.findViewById(R.id.collection_name);
            mCategory = (LinearLayout) itemView.findViewById(R.id.collection_lay);
            mPhoto = (ImageView) itemView.findViewById(R.id.collection_image);


        }


    }




}
