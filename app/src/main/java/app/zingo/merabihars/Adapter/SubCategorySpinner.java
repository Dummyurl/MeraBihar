package app.zingo.merabihars.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.zingo.merabihars.Model.SubCategories;
import app.zingo.merabihars.R;

/**
 * Created by ZingoHotels Tech on 15-10-2018.
 */

public class SubCategorySpinner extends BaseAdapter {
    private Context context;
    private ArrayList<SubCategories> mList = new ArrayList<>();

    public SubCategorySpinner(Context context, ArrayList<SubCategories> mList)
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
    public View getView(int pos, View view, ViewGroup viewGroup) {

        if(view == null)
        {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.sub_category_spinner,viewGroup,false);
        }

        TextView mSubCategoryName = (TextView) view.findViewById(R.id.sub_category_spinner_item);


        mSubCategoryName.setText(mList.get(pos).getSubCategoriesName().toString());

        return view;
    }
}