package app.zingo.merabihar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.zingo.merabihar.Model.Category;
import app.zingo.merabihar.Model.Contents;
import app.zingo.merabihar.R;
import app.zingo.merabihar.UI.ActivityScreen.Events.CategoryDetailsScreen;

/**
 * Created by ZingoHotels Tech on 16-10-2018.
 */

public class ContentCityHomePager extends PagerAdapter {

    private Context context;
    //private ArrayList<Category> bgImages;
    private ArrayList<Contents> contents;
    private LayoutInflater inflater;


    public ContentCityHomePager(Context context,ArrayList<Contents> contents)
    {
        this.context = context;
        this.contents = contents;
        inflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        if(contents.size() < 5)
        {
            return contents.size();
        }
        else
        {
            return 5;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object) ;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        try{

            View view = inflater.inflate(R.layout.content_city_adapter_home,container,false);

            ImageView imageView = (ImageView) view.findViewById(R.id.content_images);
            TextView contentName = (TextView) view.findViewById(R.id.content_name);
            TextView contentCate = (TextView) view.findViewById(R.id.content_subcategory);
            TextView contentBy = (TextView) view.findViewById(R.id.content_by);


            contentName.setText(contents.get(position).getTitle());

            if(contents.get(position).getContentImage() != null && contents.get(position).getContentImage().size()!=0)
            {

                String img = contents.get(position).getContentImage().get(0).getImages();

                if(img!=null&&!img.isEmpty()){
                    Picasso.with(context).load(img).placeholder(R.drawable.no_image).
                            error(R.drawable.no_image).into(imageView);
                }else{
                    imageView.setImageResource(R.drawable.no_image);
                }



            }else{
                imageView.setImageResource(R.drawable.no_image);
            }


            if(contents.get(position).getSubCategories()!=null){

                contentCate.setText(""+contents.get(position).getSubCategories().getSubCategoriesName());
            }

            if(contents.get(position).getCreatedBy()!=null&&!contents.get(position).getCreatedBy().isEmpty()){

                contentBy.setText(""+contents.get(position).getCreatedBy());
            }



            if(view != null)
            {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

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
