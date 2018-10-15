package app.zingo.merabihar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import app.zingo.merabihar.R;

/**
 * Created by ZingoHotels Tech on 15-09-2018.
 */

public class TextSpiltAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> mList = new ArrayList<>();

    public TextSpiltAdapter(Context context, ArrayList<String> mList)
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
                view = mInflater.inflate(R.layout.adapter_string_spilt,viewGroup,false);
            }

            //<--- list main adapter---->
            TextView mPara = (TextView)view.findViewById(R.id.para);


            if(mList.get(pos)!=null){


                mPara.setText(""+mList.get(pos));

            }

            //<--- list main adapter---->


            return view;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }
}
