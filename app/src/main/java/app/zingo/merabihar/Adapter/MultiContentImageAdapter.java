package app.zingo.merabihar.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.zingo.merabihar.Model.ActivityImages;
import app.zingo.merabihar.Model.ContentImages;
import app.zingo.merabihar.R;

/**
 * Created by ZingoHotels Tech on 22-10-2018.
 */

public class MultiContentImageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ContentImages> mList = new ArrayList<>();
    private ArrayList<ArrayList<ContentImages> > mLists = new ArrayList<>();



    public MultiContentImageAdapter(Context context,ArrayList<ArrayList<ContentImages> > mLists)
    {
        this.context = context;
        this.mLists = mLists;
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int pos) {
        return mLists.get(pos);
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
                // view = mInflater.inflate(R.layout.blog_main_design_adapter,viewGroup,false);
                view = mInflater.inflate(R.layout.insta_image_adapter,viewGroup,false);
            }

            if(pos%2==0){
                view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }else{
                view.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }




            ImageView one = (ImageView) view.findViewById(R.id.activity_image_one);
            ImageView two = (ImageView) view.findViewById(R.id.activity_image_two);
            ImageView three = (ImageView) view.findViewById(R.id.activity_image_three);
            ImageView four = (ImageView) view.findViewById(R.id.activity_image_four);
            ImageView five = (ImageView) view.findViewById(R.id.activity_image_five);
            final VideoView videoView = (VideoView) view.findViewById(R.id.videoView);

            MediaController mediaController= new MediaController(context);
            mediaController.setAnchorView(videoView);


            String path1="https://www.demonuts.com/Demonuts/smallvideo.mp4";
            String path2="http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";

            Uri uri=Uri.parse(path1);
            Uri uris=Uri.parse(path2);
            if(pos%2==0){
                videoView.setVideoURI(uri);
            }else{
                videoView.setVideoURI(uris);
            }

            videoView.start();
            videoView.setZOrderOnTop(true);


            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    videoView.start();
                }
            });

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setVolume(0f,0f);
                }
            });

            if(mLists.get(pos)!=null&&mLists.get(pos).size()!=0){


                Picasso.with(context).load(mLists.get(pos).get(0).getImages()).placeholder(R.drawable.no_image).
                        error(R.drawable.no_image).into(one);

                Picasso.with(context).load(mLists.get(pos).get(1).getImages()).placeholder(R.drawable.no_image).
                        error(R.drawable.no_image).into(two);

                Picasso.with(context).load(mLists.get(pos).get(2).getImages()).placeholder(R.drawable.no_image).
                        error(R.drawable.no_image).into(three);

                Picasso.with(context).load(mLists.get(pos).get(3).getImages()).placeholder(R.drawable.no_image).
                        error(R.drawable.no_image).into(four);

                Picasso.with(context).load(mLists.get(pos).get(4).getImages()).placeholder(R.drawable.no_image).
                        error(R.drawable.no_image).into(five);
            }



            return view;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }


}
