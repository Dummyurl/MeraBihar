package app.zingo.merabihars.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.zingo.merabihars.Model.SubCategories;
import app.zingo.merabihars.R;
import app.zingo.merabihars.UI.ActivityScreen.Events.SubCategoryDetails;

/**
 * Created by ZingoHotels Tech on 05-09-2018.
 */

public class SubCategoryGridAdapter extends BaseAdapter {

    Context context;
    ArrayList<SubCategories> mList;
    public SubCategoryGridAdapter(Context context, ArrayList<SubCategories> mList)
    {
        this.context = context;
        this.mList = mList;
    }
    @Override
    public int getCount() {
        //System.out.println("class SelectRoomGridViewAdapter = "+rooms.size());

        return mList.size();

    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        try{
            if(convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.sub_grid_view,parent,false);
            }

            final ImageView mSubImage = (ImageView) convertView.findViewById(R.id.event_banner_image);
            final TextView mSubName = (TextView) convertView.findViewById(R.id.sub_name);
            final CardView mainLayout = (CardView) convertView.findViewById(R.id.event_layout);

            if(mList.get(position)!=null){
                mSubName.setText(""+mList.get(position).getSubCategoriesName());
                Picasso.with(context).load(mList.get(position).getSubCategoriesImage()).placeholder(R.drawable.no_image).
                        error(R.drawable.no_image).into( mSubImage);
            }

            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, SubCategoryDetails.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("SubCategory",mList.get(position));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

            return convertView;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }
}
