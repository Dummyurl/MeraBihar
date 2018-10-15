package app.zingo.merabihar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

import app.zingo.merabihar.CustomInterface.SortPackageDetails;
import app.zingo.merabihar.Model.ActivityModel;
import app.zingo.merabihar.R;
import app.zingo.merabihar.UI.ActivityScreen.Events.ActivityDetail;
import app.zingo.merabihar.Util.Constants;

/**
 * Created by ZingoHotels Tech on 05-09-2018.
 */

public class ActivityListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ActivityModel> mList = new ArrayList<>();

    public ActivityListAdapter(Context context, ArrayList<ActivityModel> mList)
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

        try{

            final  ActivityModel activityModel = mList.get(pos);
            if(view == null)
            {
                LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = mInflater.inflate(R.layout.activity_in_others,viewGroup,false);
            }

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
                        bundle.putSerializable(Constants.ACTIVITY,activityModel);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
            }


            return view;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }
}
