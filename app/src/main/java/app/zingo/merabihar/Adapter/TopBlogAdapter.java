package app.zingo.merabihar.Adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.zingo.merabihar.Model.BlogDataModel;
import app.zingo.merabihar.R;

/**
 * Created by ZingoHotels Tech on 30-08-2018.
 */

public class TopBlogAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<BlogDataModel> blogDataModelArrayList;
    private LayoutInflater inflater;


    public TopBlogAdapter(Context context,ArrayList<BlogDataModel> blogDataModelArrayList)//,ArrayList<PagerModel> bgImages)
    {
        this.context = context;
        this.blogDataModelArrayList = blogDataModelArrayList;
        //this.bgImages = bgImages;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {

        if(blogDataModelArrayList.size() < 5)
        {
            return blogDataModelArrayList.size();
        }
        else
        {
            return 5;
        }

        /*bgImages.size();*/
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        BlogDataModel blogDataModel = blogDataModelArrayList.get(position);
        View view = inflater.inflate(R.layout.blog_list_adapter, container, false);
        TextView mBlogName = (TextView)view.findViewById(R.id.blog_name);
        TextView mBlogDescription = (TextView)view.findViewById(R.id.blog_brief);
        Button mReadMore = (Button) view.findViewById(R.id.blog_read);
        ImageView mBlogImage = (ImageView) view.findViewById(R.id.blog_banner_image);


        mBlogName.setText(blogDataModel.getBlogName());
        mBlogDescription.setText(blogDataModel.getBlogDescription());
        mBlogImage.setImageResource(blogDataModel.getBlogImageId());


        mReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  /*  Intent intent = new Intent(context, ActivityDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.ACTIVITY,interestsArrayList.get(position));
                    intent.putExtras(bundle);
                    context.startActivity(intent);*/
            }
        });

        if(view != null)
        {
            view.setOnClickListener(new View.OnClickListener() {
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
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }



}

