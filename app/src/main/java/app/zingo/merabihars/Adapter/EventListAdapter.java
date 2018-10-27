package app.zingo.merabihars.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.zingo.merabihars.Model.ActivityModel;
import app.zingo.merabihars.R;

/**
 * Created by ZingoHotels Tech on 30-08-2018.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ActivityModel> list;
    public EventListAdapter(Context context,ArrayList<ActivityModel> list) {

        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categorywise_list_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ActivityModel activityModel = list.get(position);




            if(activityModel.getActivityImages().size() != 0 && activityModel.getActivityImages().get(0) != null)
            {
                Picasso.with(context).load(activityModel.getActivityImages().get(0).getImages()).placeholder(R.drawable.no_image).
                        error(R.drawable.no_image).into( holder.mActivityImage);
                //holder.mActivityImage.setImageBitmap(Util.decodeBase64(activityModel.getActivityImages().get(1).getImages()));
            }



        holder.activity_discount.setText(activityModel.getDiscountPercentage()+" %");
        holder.mDisplayPrice.setText("₹ "+activityModel.getDisplayPrice()+"");
        holder.top_event_selling_price.setText("₹ "+activityModel.getSellingPrice());
        holder.top_event_name.setText(activityModel.getActivityName());
        holder.top_event_place.setText(activityModel.getAddress());
        holder.top_no_of_sellings.setVisibility(View.GONE);
        holder.activity_discount.setVisibility(View.GONE);
        holder.mDisplayPrice.setVisibility(View.GONE);
        holder.top_event_selling_price.setVisibility(View.GONE);
       // holder.no_of_units_left.setText("30 tickets left");



    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder  {


        TextView mDisplayPrice,top_event_name,top_event_place,top_event_selling_price,no_of_units_left,
                top_no_of_sellings,activity_discount;
        ImageView mActivityImage;
        RatingBar ratingBar;
        CardView mEventLayout;
//

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);
            //itemView.setOnClickListener(this);

            mDisplayPrice = itemView.findViewById(R.id.event_display_price);
            //TextView mDisplayPrice = (TextView)itemView.findViewById(R.id.top_event_display_price);
            top_event_name = (TextView)itemView.findViewById(R.id.event_name);
            top_event_place = (TextView)itemView.findViewById(R.id.event_place);
            top_event_selling_price = (TextView)itemView.findViewById(R.id.event_selling_price);
            no_of_units_left = (TextView)itemView.findViewById(R.id.event_no_of_units_left);
            top_no_of_sellings = (TextView)itemView.findViewById(R.id.no_of_sellings);
            activity_discount = (TextView)itemView.findViewById(R.id.activity_discount);
            mActivityImage = (ImageView)itemView.findViewById(R.id.event_banner_image);
            ratingBar = (RatingBar)itemView.findViewById(R.id.top_rating_bar);
            mEventLayout = (CardView) itemView.findViewById(R.id.event_layout);


        }

        /*@Override
        public void onClick(View v) {


            Intent detail = new Intent(context, ActivityDetail.class);
            context.startActivity(detail);
        }*/
    }

}
