package app.zingo.merabihar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.zingo.merabihar.Model.BlogImages;
import app.zingo.merabihar.Model.Blogs;
import app.zingo.merabihar.R;
import app.zingo.merabihar.UI.ActivityScreen.Events.AboutBlogs;

/**
 * Created by ZingoHotels Tech on 04-09-2018.
 */

public class BlogViewPagerAdapter extends PagerAdapter {

    private Context context;
    //private ArrayList<Category> bgImages;
    private ArrayList<Blogs> mList = new ArrayList<>();
    private LayoutInflater inflater;


    public BlogViewPagerAdapter(Context context,ArrayList<Blogs> mList)
    {
        this.context = context;
        this.mList = mList;
        inflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object) ;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int pos) {

        try{
            View view = inflater.inflate(R.layout.blog_main_design_adapter,container,false);

            TextView mBlogName = (TextView)view.findViewById(R.id.blog_name_adapter);
            TextView mBlogShort = (TextView)view.findViewById(R.id.blog_short_adapter);
            TextView mBlogDescription = (TextView)view.findViewById(R.id.blog_brief_adapter);
            TextView mCreateBy = (TextView)view.findViewById(R.id.created_by_adapter);
            TextView mCreateDate = (TextView)view.findViewById(R.id.created_date_adapter);
            Button mReadMore = (Button) view.findViewById(R.id.blog_read_adapter);
            ImageView mBlogImage = (ImageView) view.findViewById(R.id.blog_banner_image_adapter);


            if(mList.get(pos)!=null){


                SimpleDateFormat sdfs = new SimpleDateFormat("MMM dd,yyyy");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                String blogName = mList.get(pos).getTitle();
                String blogShort = mList.get(pos).getShortDesc();
                String longDesc = mList.get(pos).getLongDesc();
                String createdBy = mList.get(pos).getCreatedUser();
                String createdDate = mList.get(pos).getCreateDate();

                ArrayList<BlogImages> blogImages = mList.get(pos).getBlogImages();

                if(blogName!=null&&!blogName.isEmpty()){

                    mBlogName.setText(blogName+"");
                }else{
                   // mBlogName.setText(blogName + "");
                }

                if(blogShort!=null&&!blogShort.isEmpty()){

                    mBlogShort.setText(blogShort+"");
                }else if((blogName!=null&&!blogName.isEmpty())&&(blogShort==null||blogShort.isEmpty())) {
                    //mBlogName.setText(blogName + "");
                }


                if(longDesc!=null&&!longDesc.isEmpty()){

                    if(longDesc.contains("<br>")){
                        String[] arrayString = longDesc.split("<br>");
                        mBlogDescription.setText(arrayString[0]);
                    }else{
                        mBlogDescription.setText(longDesc);
                    }


                }

                if(createdBy!=null&&!createdBy.isEmpty()){
                    mCreateBy.setText("By "+mList.get(pos).getCreatedUser());
                }

                if(createdDate!=null&&!createdDate.isEmpty()){


                    if(createdDate.contains("T")){
                        String create[] = createdDate.split("T");
                        Date df = sdf.parse(create[0]);
                        mCreateDate.setText(""+sdfs.format(df));

                    }



                }


                if(blogImages!=null&&blogImages.size()!=0){

                    Picasso.with(context).load(blogImages.get(0).getImage()).placeholder(R.drawable.no_image).
                            error(R.drawable.no_image).into(mBlogImage);
                }


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

            }

            container.addView(view);
            return view;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

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
