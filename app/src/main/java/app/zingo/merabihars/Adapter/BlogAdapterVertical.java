package app.zingo.merabihars.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.zingo.merabihars.Model.BlogDataModel;
import app.zingo.merabihars.R;

/**
 * Created by ZingoHotels Tech on 30-08-2018.
 */

public class BlogAdapterVertical extends RecyclerView.Adapter<BlogAdapterVertical.ViewHolder> {
    private Context context;
    private ArrayList<BlogDataModel> blogDataModelArrayList;
    public BlogAdapterVertical(Context context,ArrayList<BlogDataModel> blogDataModelArrayList) {

        this.context = context;
        this.blogDataModelArrayList = blogDataModelArrayList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blog_list_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BlogDataModel blogDataModel = blogDataModelArrayList.get(position);


        holder.mBlogName.setText(blogDataModel.getBlogName());
        holder.mBlogDescription.setText(blogDataModel.getBlogDescription());
        holder.mBlogImage.setImageResource(blogDataModel.getBlogImageId());


        holder.mReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  /*  Intent intent = new Intent(context, ActivityDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.ACTIVITY,interestsArrayList.get(position));
                    intent.putExtras(bundle);
                    context.startActivity(intent);*/
            }
        });



    }

    @Override
    public int getItemCount() {
        return blogDataModelArrayList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder  {


        TextView mBlogName,mBlogDescription;
        Button mReadMore;
        ImageView mBlogImage;
//

        public ViewHolder(View view) {
            super(view);
            context = view.getContext();
            view.setClickable(true);
            //itemView.setOnClickListener(this);

            mBlogName = (TextView)view.findViewById(R.id.blog_name);
            mBlogDescription = (TextView)view.findViewById(R.id.blog_brief);
            mReadMore = (Button) view.findViewById(R.id.blog_read);
            mBlogImage = (ImageView) view.findViewById(R.id.blog_banner_image);



        }

        /*@Override
        public void onClick(View v) {


            Intent detail = new Intent(context, ActivityDetail.class);
            context.startActivity(detail);
        }*/
    }

}
