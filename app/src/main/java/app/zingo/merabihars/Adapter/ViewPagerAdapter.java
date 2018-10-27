package app.zingo.merabihars.Adapter;

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

import app.zingo.merabihars.Model.Category;
import app.zingo.merabihars.R;
import app.zingo.merabihars.UI.ActivityScreen.Events.CategoryDetailsScreen;

/**
 * Created by ZingoHotels Tech on 29-08-2018.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    //private ArrayList<Category> bgImages;
    private ArrayList<Category> bgImages;
    private LayoutInflater inflater;


    public ViewPagerAdapter(Context context,ArrayList<Category> bgImages)
    {
        this.context = context;
        this.bgImages = bgImages;
        inflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        if(bgImages.size() < 7)
        {
            return bgImages.size();
        }
        else
        {
            return 7;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object) ;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View view = inflater.inflate(R.layout.viewpager_adapter_layout,container,false);

        ImageView imageView = (ImageView) view.findViewById(R.id.category_banner);
        TextView categoryName = (TextView) view.findViewById(R.id.banner_category_name);
        TextView noofexperience = (TextView) view.findViewById(R.id.no_of_experience);

        categoryName.setText(bgImages.get(position).getCategoriesName());
         if(bgImages.get(position).getCategoriesImage() != null && !bgImages.get(position).getCategoriesImage().isEmpty())
        {

            Picasso.with(context).load(bgImages.get(position).getCategoriesImage()).placeholder(R.drawable.no_image).
                    error(R.drawable.no_image).into(imageView);
           /* byte[] imagebytes = Base64.decode(bgImages.get(position).getCategoriesImage(),Base64.DEFAULT);
            Bitmap catimage = BitmapFactory.decodeByteArray(imagebytes,0,imagebytes.length);
            imageView.setImageBitmap(catimage);*/
        }

        if(view != null)
        {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CategoryDetailsScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("title",bgImages.get(position).getCategoriesName());
                    bundle.putInt("cat_id",bgImages.get(position).getCategoriesId());
                    bundle.putSerializable("Category",bgImages.get(position));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
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
