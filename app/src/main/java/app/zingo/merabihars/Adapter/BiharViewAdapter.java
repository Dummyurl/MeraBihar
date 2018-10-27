package app.zingo.merabihars.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.zingo.merabihars.Model.BiharData;
import app.zingo.merabihars.R;

/**
 * Created by ZingoHotels Tech on 30-08-2018.
 */

public class BiharViewAdapter extends PagerAdapter {

    Context context;
    // ArrayList<String> activityImages;
    ArrayList<BiharData> biharDataArrayList;


    public BiharViewAdapter(Context context, ArrayList<BiharData> biharDataArrayList)
    {
        this.context = context;
        this.biharDataArrayList = biharDataArrayList;

        /*options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .resetViewBeforeLoading(true).cacheOnDisk(true)
                .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                .build();*/
    }

    @Override
    public int getCount() {
        return biharDataArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View)object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View removableView = (View) object;
        container.removeView(removableView);

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bihar_view_pager,container,false);
        ImageView biharImage = (ImageView) view.findViewById(R.id.bihar_images);
        TextView  biharTitle = (TextView)view.findViewById(R.id.bihar_title);
        TextView  biharDesc = (TextView)view.findViewById(R.id.bihar_desc);

        biharImage.setImageResource(biharDataArrayList.get(position).getImageId());
        biharTitle.setText(""+biharDataArrayList.get(position).getTitle());
        biharDesc.setText(""+biharDataArrayList.get(position).getDesc());
        container.addView(view);
        return view;


    }
}
