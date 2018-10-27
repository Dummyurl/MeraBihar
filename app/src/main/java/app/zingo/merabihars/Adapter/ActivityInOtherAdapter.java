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
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

import app.zingo.merabihars.CustomInterface.SortPackageDetails;
import app.zingo.merabihars.Model.ActivityModel;
import app.zingo.merabihars.R;
import app.zingo.merabihars.UI.ActivityScreen.Events.ActivityDetail;
import app.zingo.merabihars.Util.Constants;

/**
 * Created by ZingoHotels Tech on 05-09-2018.
 */

public class ActivityInOtherAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<ActivityModel> interestsArrayList;
    private LayoutInflater inflater;


    public ActivityInOtherAdapter(Context context,ArrayList<ActivityModel> interestsArrayList)//,ArrayList<PagerModel> bgImages)
    {
        this.context = context;
        this.interestsArrayList = interestsArrayList;
        //this.bgImages = bgImages;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {

        if(interestsArrayList.size() < 10)
        {
            return interestsArrayList.size();
        }
        else
        {
            return 10;
        }

        /*bgImages.size();*/
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        ActivityModel activityModel = interestsArrayList.get(position);
        View view = inflater.inflate(R.layout.activity_in_others, container, false);
        TextView mDisplayPrice = (TextView)view.findViewById(R.id.top_event_display_price);
        TextView top_event_name = (TextView)view.findViewById(R.id.top_event_name);
        TextView top_event_place = (TextView)view.findViewById(R.id.top_event_place);
        TextView top_event_selling_price = (TextView)view.findViewById(R.id.top_event_selling_price);
        TextView activity_discount = (TextView)view.findViewById(R.id.activity_discount);
        ImageView mActivityImage = (ImageView)view.findViewById(R.id.event_banner_image);
        RatingBar ratingBar = (RatingBar)view.findViewById(R.id.top_rating_bar);

        if(activityModel.getActivityImages()!=null && activityModel.getActivityImages().size() != 0 && activityModel.getActivityImages().get(0) != null)
        {
            Picasso.with(context).load(activityModel.getActivityImages().get(0).getImages()).placeholder(R.drawable.no_image).
                    error(R.drawable.no_image).into(mActivityImage);
            //mActivityImage.setImageBitmap(Util.decodeBase64(activityModel.getActivityImages().get(0).getImages()));
        }

        top_event_name.setText(activityModel.getActivityName()+"");
        top_event_place.setText(activityModel.getAddress()+"");

        NumberFormat df = new DecimalFormat("##");
        ratingBar.setRating((float) activityModel.getRatings());

        if(activityModel.getPackageDetails() != null && activityModel.getPackageDetails().size() != 0)
        {
            Collections.sort(activityModel.getPackageDetails(),new SortPackageDetails());



            top_event_selling_price.setText("₹ "+activityModel.getPackageDetails().get(0).getSellRate());//activityList.getSellingPrice());
            mDisplayPrice.setText("₹ "+activityModel.getPackageDetails().get(0).getDeclaredRate());//activityList.getDisplayPrice());
            double diff = (activityModel.getPackageDetails().get(0).getDeclaredRate() - activityModel.getPackageDetails().get(0).getSellRate())*1.0;
            double value =diff/activityModel.getPackageDetails().get(0).getDeclaredRate();
            double per = value*100;
            activity_discount.setText(df.format(per)+"% Discunt");
            System.out.println("per = "+per);


        }else{

            top_event_selling_price.setVisibility(View.GONE);
            mDisplayPrice.setVisibility(View.GONE);
            activity_discount.setVisibility(View.GONE);

        }


        if(view != null)
        {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ActivityDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.ACTIVITY,interestsArrayList.get(position));
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

