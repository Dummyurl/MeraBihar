package app.zingo.merabihar.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.zingo.merabihar.Model.Blogs;
import app.zingo.merabihar.Model.Category;
import app.zingo.merabihar.Model.CategoryAndContentList;
import app.zingo.merabihar.Model.Contents;
import app.zingo.merabihar.Model.UserProfile;
import app.zingo.merabihar.R;
import app.zingo.merabihar.UI.ActivityScreen.AccountScreens.ProfileBlogList;
import app.zingo.merabihar.UI.ActivityScreen.MainTabHostActivity.TabHomeScreen;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.BlogApi;
import app.zingo.merabihar.WebApi.ContentAPI;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ZingoHotels Tech on 16-10-2018.
 */

public class CategoryContentRecyclerAdapter extends RecyclerView.Adapter<CategoryContentRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<CategoryAndContentList> list;
    public CategoryContentRecyclerAdapter(Context context,ArrayList<CategoryAndContentList> list) {

        this.context = context;
        this.list = list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        try{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_content_adapter, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final CategoryAndContentList category = list.get(position);

        if(category!=null){

            holder.mName.setText("Stories in "+category.getCategories().getCategoriesName());
            //getContents(category.getContentList(),holder.mConteList);

            ArrayList<Contents> contentsList = category.getContentList();

            if(contentsList != null && contentsList.size() != 0)
            {

                ContentCategoryRecyclerAdapter adapter = new ContentCategoryRecyclerAdapter(context,contentsList);
                holder.mConteList.setAdapter(adapter);


            }

        }

    }

    @Override
    public int getItemCount() {

      /*  if(list.size()>=5){
            return 5;
        }else{
            return list.size();
        }*/

        return list.size();

    }


    class ViewHolder extends RecyclerView.ViewHolder  {

        TextView mName;
        RecyclerView mConteList;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);

            mName = (TextView)itemView.findViewById(R.id.content_name);
            mConteList = (RecyclerView) itemView.findViewById(R.id.content_by_categories);
            mConteList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));



        }


    }


}
