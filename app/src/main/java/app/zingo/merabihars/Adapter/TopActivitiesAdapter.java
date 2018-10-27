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

import app.zingo.merabihars.Model.ActivityModel;
import app.zingo.merabihars.R;
import app.zingo.merabihars.UI.ActivityScreen.Events.ActivityDetail;
import app.zingo.merabihars.Util.Constants;

/**
 * Created by ZingoHotels Tech on 29-08-2018.
 */

public class TopActivitiesAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<ActivityModel> interestsArrayList;
    private LayoutInflater inflater;


    public TopActivitiesAdapter(Context context,ArrayList<ActivityModel> interestsArrayList)//,ArrayList<PagerModel> bgImages)
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
        View view = inflater.inflate(R.layout.top_activities_adapter_layout, container, false);
        TextView mDisplayPrice = (TextView)view.findViewById(R.id.top_event_display_price);
        TextView top_event_name = (TextView)view.findViewById(R.id.top_event_name);
        TextView top_event_place = (TextView)view.findViewById(R.id.top_event_place);
        TextView top_event_selling_price = (TextView)view.findViewById(R.id.top_event_selling_price);
        TextView no_of_units_left = (TextView)view.findViewById(R.id.no_of_units_left);
        TextView top_no_of_sellings = (TextView)view.findViewById(R.id.top_no_of_sellings);
        TextView activity_discount = (TextView)view.findViewById(R.id.activity_discount);
        ImageView mActivityImage = (ImageView)view.findViewById(R.id.event_banner_image);
        RatingBar ratingBar = (RatingBar)view.findViewById(R.id.top_rating_bar);

        if(activityModel.getActivityImages()!=null && activityModel.getActivityImages().size() != 0 && activityModel.getActivityImages().get(0) != null)
        {
            Picasso.with(context).load(activityModel.getActivityImages().get(0).getImages()).placeholder(R.drawable.no_image).
                    error(R.drawable.no_image).into(mActivityImage);
            //mActivityImage.setImageBitmap(Util.decodeBase64(activityModel.getActivityImages().get(0).getImages()));
        }

        mDisplayPrice.setText("₹ "+activityModel.getDisplayPrice()+"");
        mDisplayPrice.setVisibility(View.GONE);
        top_no_of_sellings.setVisibility(View.GONE);
        top_event_name.setText(activityModel.getActivityName()+"");
        top_event_place.setText(activityModel.getAddress()+"");
        top_event_selling_price.setText("₹ "+activityModel.getSellingPrice()+"");
        top_event_selling_price.setVisibility(View.GONE);
        //mDisplayPrice.setText(activityModel.getDisplayPrice()+"");
        //mDisplayPrice.setText(activityModel.getDisplayPrice()+"");
        /*double discount = ((activityModel.getDisplayPrice()- activityModel.getSellingPrice())/activityModel.getDisplayPrice())*100;
        System.out.println("discount = "+discount);*/
        NumberFormat df = new DecimalFormat("##");
        activity_discount.setText(df.format(activityModel.getDiscountPercentage())+"% Discunt");
        activity_discount.setVisibility(View.GONE);
        ratingBar.setRating((float) activityModel.getRatings());


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

