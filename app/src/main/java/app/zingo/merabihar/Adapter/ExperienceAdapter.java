package app.zingo.merabihar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.zingo.merabihar.Model.SubCategories;
import app.zingo.merabihar.R;
import app.zingo.merabihar.UI.ActivityScreen.Events.SubCategoryDetails;

/**
 * Created by ZingoHotels Tech on 15-09-2018.
 */

public class ExperienceAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<SubCategories> mList = new ArrayList<>();

    public ExperienceAdapter(Context context, ArrayList<SubCategories> mList)
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
            if(view == null)
            {
                LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = mInflater.inflate(R.layout.collection_main_adapter,viewGroup,false);
            }

            //<--- list main adapter---->
            TextView mCategoryName = (TextView)view.findViewById(R.id.collection_name);
            ImageView mCategoryImage = (ImageView) view.findViewById(R.id.collection_image);


            if(mList.get(pos)!=null){


                mCategoryName.setText(""+mList.get(pos).getSubCategoriesName());



                if(mList.get(pos).getSubCategoriesImage()!=null){

                    Picasso.with(context).load(mList.get(pos).getSubCategoriesImage()).placeholder(R.drawable.no_image).
                            error(R.drawable.no_image).into(mCategoryImage);
                }




                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, SubCategoryDetails.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("SubCategory",mList.get(pos));
                        intent.putExtras(bundle);
                        context.startActivity(intent);

                    }
                });



            }

            //<--- list main adapter---->


            return view;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }
}
