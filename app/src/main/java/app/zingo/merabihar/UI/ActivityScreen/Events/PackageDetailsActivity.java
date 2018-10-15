package app.zingo.merabihar.UI.ActivityScreen.Events;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.zingo.merabihar.Model.Itinerary;
import app.zingo.merabihar.Model.ItineraryDescription;
import app.zingo.merabihar.Model.PackageDetails;
import app.zingo.merabihar.R;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.ItineraryAPI;
import app.zingo.merabihar.WebApi.PackageDetailsAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackageDetailsActivity extends AppCompatActivity {

    TextView mPackageName,mPackageDesc,mStartTime,mEndTime,mAdultRate,mChildRate;
    CardView mItinerayLay;
    Button mbookPackage;
    //RecyclerView mItineraryList;
    LinearLayout mItineraryList;

    PackageDetails packageDetails;

    ArrayList<Itinerary> itinerariesList ;

    int countParent=0,countChild=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            setContentView(R.layout.activity_package_details);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            setTitle("Package Detail");

            mPackageName = (TextView)findViewById(R.id.package_detail_name);
            mPackageDesc = (TextView)findViewById(R.id.package_detail_desc);
            mbookPackage = (Button) findViewById(R.id.book_package_btn);
            mStartTime = (TextView)findViewById(R.id.start_time);
            mEndTime = (TextView)findViewById(R.id.end_time);
            mAdultRate = (TextView)findViewById(R.id.adult_sell_rate);
            mChildRate = (TextView)findViewById(R.id.child_sell_rate);
            mItinerayLay = (CardView) findViewById(R.id.itinerary_layout);
            mItineraryList = (LinearLayout) findViewById(R.id.itinerary_list);

            Bundle bundle = getIntent().getExtras();

            itinerariesList = new ArrayList<>();
            //getItineraryDesc();

            if(bundle!=null){

                packageDetails = (PackageDetails)bundle.getSerializable("Packages");
            }

            if(packageDetails!=null){

                mPackageName.setText(""+packageDetails.getName());
                mPackageDesc.setText(""+packageDetails.getDescription());
                mAdultRate.setText("Rs "+packageDetails.getSellRate()+"/");
                mChildRate.setText("Rs "+packageDetails.getSellRateForChild()+"/");

                getPackageDetails(packageDetails.getPackageDetailsId());
                //getItineraryDesc();

            }

            mbookPackage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    packagePop();

                }
            });



        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void addView(final int i,final Itinerary itinerary)
    {
        countParent = countParent+i;
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try{
            View v = vi.inflate(R.layout.itinerary_list_desc, null);
            final TextView day = (TextView)v.findViewById(R.id.day_count);
            final LinearLayout mItinerarydesc = (LinearLayout)v.findViewById(R.id.add_itinerary_desc);

            day.setText("Day "+countParent);



            //getItineraryDesc();

            if(itinerary!=null){

                if(itinerary.getItineraryDesc()!=null&&itinerary.getItineraryDesc().size()!=0){

                    for(int it = 0;it<itinerary.getItineraryDesc().size();it++){
                        addChildView(1,itinerary.getItineraryDesc().get(it),mItinerarydesc);
                    }
                }



            }else{
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            /*if(itinerariesList!=null && itinerariesList.size()!=0){

                for(int k =0;k<itinerariesList.size();k++){

                    if(itineraryId==itinerariesList.get(k).getItineraryId()){

                        if(itinerariesList.get(k).getItineraryDesc()!=null&&itinerariesList.get(k).getItineraryDesc().size()!=0){

                            for(int j=0;j<itinerariesList.get(k).getItineraryDesc().size();j++){

                                addChildView(1,itinerariesList.get(k).getItineraryDesc().get(j),mItinerarydesc);

                            }

                            break;

                        }

                    }

                }


            }else{

                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }*/
            mItineraryList.addView(v);



        }catch (Exception e){
            e.printStackTrace();
        }




    }

    public void addChildView(final int i,final ItineraryDescription itineraryDescription,final LinearLayout layout)
    {
        countChild = countChild+i;
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try{
            View v = vi.inflate(R.layout.itinerary_desc_details, null);
            final TextView time = (TextView)v.findViewById(R.id.time);
            final TextView desc = (TextView)v.findViewById(R.id.desc);

            time.setText(""+itineraryDescription.getTime());
            desc.setText(""+itineraryDescription.getDescription());
            layout.addView(v);



        }catch (Exception e){
            e.printStackTrace();
        }




    }



    public void getPackageDetails(final int id){

        final ProgressDialog dialog = new ProgressDialog(PackageDetailsActivity.this);
        dialog.setMessage("Loading Packages");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final PackageDetailsAPI blogApi = Util.getClient().create(PackageDetailsAPI.class);
                Call<PackageDetails> getBlog = blogApi.getPackageById(id);
                //Call<ArrayList<Blogs>> getBlog = blogApi.getBlogs();

                getBlog.enqueue(new Callback<PackageDetails>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<PackageDetails> call, Response<PackageDetails> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        if (response.code() == 200)
                        {

                            PackageDetails blogsArrayList = response.body();




                            if(blogsArrayList!=null){

                                packageDetails = response.body();

                                String time = blogsArrayList.getTimeSlot();
                                if(time.contains(" ")){

                                    String[] timeArray = time.split(" ");
                                    if(blogsArrayList.getItinerary()!=null&&blogsArrayList.getItinerary().size()!=0){

                                        int size = blogsArrayList.getItinerary().size();
                                        if(size == 1){
                                            mStartTime.setText(""+timeArray[0]+"(Day 1)");
                                            mEndTime.setText(""+timeArray[2]+"(Day 1)");
                                        }else{
                                            mStartTime.setText(""+timeArray[0]+"(Day 1)");
                                            mEndTime.setText(""+timeArray[2]+"(Day "+size+")");
                                        }

                                        for(int i =0;i<size;i++){
                                            //addView(1,blogsArrayList.getItinerary().get(i).getItineraryId());
                                            addView(1,blogsArrayList.getItinerary().get(i));
                                        }


                                    }else{
                                        mStartTime.setText(""+timeArray[0]+"(Day 1)");
                                        mEndTime.setText(""+timeArray[2]+"(Day 1)");
                                    }
                                }




                            }else{

                            }




                        }
                    }

                    @Override
                    public void onFailure(Call<PackageDetails> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        Toast.makeText(PackageDetailsActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });
    }

    public void getItineraryDesc(){

        final ProgressDialog dialog = new ProgressDialog(PackageDetailsActivity.this);
        dialog.setMessage("Loading Itinerary");
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                final ItineraryAPI blogApi = Util.getClient().create(ItineraryAPI.class);
                Call<ArrayList<Itinerary>> getBlog = blogApi.getItineraries();
                //Call<ArrayList<Blogs>> getBlog = blogApi.getBlogs();

                getBlog.enqueue(new Callback<ArrayList<Itinerary>>() {
                    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    /*@SuppressLint("NewApi")*/
                    //System.out.println("thread inside on response");
                    @Override
                    public void onResponse(Call<ArrayList<Itinerary>> call, Response<ArrayList<Itinerary>> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        if (response.code() == 200)
                        {

                            ArrayList<Itinerary> itinerary = response.body();



                            if(itinerary!=null&&itinerary.size()!=0){

                                itinerariesList = response.body();
                              //  getPackageDetails(packageDetails.getPackageDetailsId());



                            }else{

                                Toast.makeText(PackageDetailsActivity.this, "No Itineraries", Toast.LENGTH_SHORT).show();

                            }




                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Itinerary>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        Toast.makeText(PackageDetailsActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


                //System.out.println(TAG+" thread started");

            }

        });
    }

    public void packagePop(){

        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(PackageDetailsActivity.this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View views = inflater.inflate(R.layout.package_book_adapter, null);

            builder.setView(views);

            final Button mBook = (Button) views.findViewById(R.id.book_package);
            final Button mClose = (Button) views.findViewById(R.id.btnClose);
            final ImageView mAdultRem = (ImageView) views.findViewById(R.id.adult_remove);
            final ImageView mAdultAdd = (ImageView) views.findViewById(R.id.adult_add);
            final ImageView mChildRem = (ImageView) views.findViewById(R.id.child_remove);
            final ImageView mChildAdd = (ImageView) views.findViewById(R.id.child_add);
            final TextView mAdultCount = (TextView) views.findViewById(R.id.book_adult_count);
            final TextView mChildCount = (TextView) views.findViewById(R.id.book_child_count);





            final AlertDialog dialog = builder.create();
            dialog.show();

            mClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();
                }
            });

            mBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            mAdultAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String count = mAdultCount.getText().toString();
                    int countValue = Integer.parseInt(count);

                    if(countValue<5){
                        mAdultCount.setText(""+(countValue+1));
                    }


                }
            });

            mAdultRem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String count = mAdultCount.getText().toString();
                    int countValue = Integer.parseInt(count);

                    if(countValue>0){
                        mAdultCount.setText(""+(countValue-1));
                    }



                }
            });

            mChildAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String count = mChildCount.getText().toString();
                    int countValue = Integer.parseInt(count);

                    if(countValue<5){
                        mChildCount.setText(""+(countValue+1));
                    }


                }
            });

            mChildRem.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    String count = mChildCount.getText().toString();
                    int countValue = Integer.parseInt(count);

                    if(countValue>0){

                        mChildCount.setText(""+(countValue-1));

                    }


                }
            });

            mBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                PackageDetailsActivity.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
