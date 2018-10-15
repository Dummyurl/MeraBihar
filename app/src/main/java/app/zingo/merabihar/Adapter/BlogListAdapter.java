package app.zingo.merabihar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.zingo.merabihar.Model.BlogDataModel;
import app.zingo.merabihar.R;
import app.zingo.merabihar.UI.ActivityScreen.Events.AboutBlogs;

/**
 * Created by ZingoHotels Tech on 30-08-2018.
 */

public class BlogListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<BlogDataModel> mList = new ArrayList<>();

    public BlogListAdapter(Context context, ArrayList<BlogDataModel> mList)
    {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int pos) {
        return mList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int pos, View view, ViewGroup viewGroup) {

        if(view == null)
        {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.blog_list_adapter,viewGroup,false);
        }

        TextView mBlogName = (TextView)view.findViewById(R.id.blog_name);
        TextView mBlogDescription = (TextView)view.findViewById(R.id.blog_brief);
        Button mReadMore = (Button) view.findViewById(R.id.blog_read);
        ImageView mBlogImage = (ImageView) view.findViewById(R.id.blog_banner_image);


        mBlogName.setText(mList.get(pos).getBlogName());
        mBlogDescription.setText(mList.get(pos).getBlogDescription());
        mBlogImage.setImageResource(mList.get(pos).getBlogImageId());


        mReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(context, AboutBlogs.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Blogs",mList.get(pos));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
            }
        });

       /* if(view != null)
        {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ActivityDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.ACTIVITY,mList.get(pos));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }*/
        return view;
    }
}
