package app.zingo.merabihars.Adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import app.zingo.merabihars.Model.Blogs;
import app.zingo.merabihars.R;

/**
 * Created by ZingoHotels Tech on 04-09-2018.
 */

public class BlogMainViewPager extends BaseAdapter {

    private Context context;
    private ArrayList<Blogs> mList = new ArrayList<>();
    private ArrayList<ArrayList<Blogs> > mLists = new ArrayList<>();

   /* public BlogMainAdapterList(Context context, ArrayList<Blogs> mList)
    {
        this.context = context;
        this.mList = mList;
    }*/

    public BlogMainViewPager(Context context,ArrayList<ArrayList<Blogs> > mLists)
    {
        this.context = context;
        this.mLists = mLists;
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int pos) {
        return mLists.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int pos, View view, ViewGroup viewGroup) {

        try{
            if(view == null)
            {
                LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                // view = mInflater.inflate(R.layout.blog_main_design_adapter,viewGroup,false);
                view = mInflater.inflate(R.layout.blog_list_viewpager,viewGroup,false);
            }

            //<--- list main adapter---->
            /*TextView mBlogName = (TextView)view.findViewById(R.id.blog_name_adapter);
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

                if(blogName!=null&&!blogName.isEmpty()&&blogShort!=null&&!blogShort.isEmpty()){

                    mBlogName.setText(blogName+" - "+blogShort);
                }else if((blogName!=null&&!blogName.isEmpty())&&(blogShort==null||blogShort.isEmpty())) {
                    mBlogName.setText(blogName + "");
                }


                if(longDesc!=null&&!longDesc.isEmpty()){
                    mBlogDescription.setText(mList.get(pos).getLongDesc());
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
                });*/



            // }

            //<--- list main adapter---->

            ViewPager mBlogs = (ViewPager) view.findViewById(R.id.blog_pager);
            mBlogs.setClipToPadding(false);
            mBlogs.setPageMargin(18);

            if(mLists.get(pos)!=null&&mLists.size()!=0){
                BlogViewPagerAdapter adapter = new BlogViewPagerAdapter(context,mLists.get(pos));
                mBlogs.setAdapter(adapter);

            }
            return view;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }
}
