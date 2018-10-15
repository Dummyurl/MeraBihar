package app.zingo.merabihar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.zingo.merabihar.Model.ActivityModel;
import app.zingo.merabihar.Model.Category;
import app.zingo.merabihar.Model.PackageDetails;
import app.zingo.merabihar.R;

/**
 * Created by ZingoHotels Tech on 29-09-2018.
 */

public class CategoryContentAdapter extends RecyclerView.Adapter<CategoryContentAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Category> categories;
    private ActivityModel activityModel;

    public CategoryContentAdapter(Context context, ArrayList<Category> categories)
    {
        this.context = context;
        //  this.activityModel = activityModel;
        this.categories = categories;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collection_main_adapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Category category = categories.get(position);

        if(category!=null){

            holder.mCategoryName.setText(""+category.getCategoriesName());

            if(category.getCategoriesImage()!=null){

                Picasso.with(context).load(category.getCategoriesImage()).placeholder(R.drawable.no_image).
                        error(R.drawable.no_image).into(holder.mCategoryImage);
            }
        }


    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mCategoryName ;
        ImageView mCategoryImage;
        public ViewHolder(View itemView) {
            super(itemView);
            mCategoryName = (TextView)itemView.findViewById(R.id.collection_name);
            mCategoryImage = (ImageView) itemView.findViewById(R.id.collection_image);

        }
    }
}

