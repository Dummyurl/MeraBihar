package app.zingo.merabihars.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.zingo.merabihars.Model.ActivityModel;
import app.zingo.merabihars.Model.PackageDetails;
import app.zingo.merabihars.R;
import app.zingo.merabihars.UI.ActivityScreen.Events.PackageDetailsActivity;

/**
 * Created by ZingoHotels Tech on 01-09-2018.
 */

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PackageDetails> packageDetails;
    private ActivityModel activityModel;

    public PackageAdapter(Context context, ArrayList<PackageDetails> packageDetails)
    {
        this.context = context;
      //  this.activityModel = activityModel;
        this.packageDetails = packageDetails;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.package_details_recycler_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        PackageDetails dto = packageDetails.get(position);

        holder.mPackageTitle.setText(dto.getName());
        holder.mPackageDetails.setText(dto.getDescription());
        holder.mPackageSellRate.setText("₹"+dto.getSellRate()+"");
        holder.mPackageDisplayRate.setText("₹"+dto.getDeclaredRate()+"");
        holder.mPackageDiscount.setText(dto.getDiscount()+" %");

        holder.mPackageDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(context, PackageDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.PACKAGE_DESCRIPTION,packageDetails.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);*/
            }
        });

        holder.mBookPakage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("Package = "+packageDetails.get(position).getName());
                Intent intent = new Intent(context, PackageDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Packages",packageDetails.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return packageDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mPackageTitle,mPackageDetails,mPackageSellRate,mPackageDisplayRate,mPackageDiscount,mBookPakage;
        public ViewHolder(View itemView) {
            super(itemView);
            mPackageTitle = (TextView) itemView.findViewById(R.id.package_title);
            mPackageDetails = (TextView) itemView.findViewById(R.id.package_details);
            mPackageSellRate = (TextView) itemView.findViewById(R.id.package_details_sell_rate);
            mPackageDisplayRate = (TextView) itemView.findViewById(R.id.package_details_display_rate);
            mPackageDiscount = (TextView) itemView.findViewById(R.id.package_discount);
            mBookPakage = (TextView) itemView.findViewById(R.id.package_book);
        }
    }
}

