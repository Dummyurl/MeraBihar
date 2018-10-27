package app.zingo.merabihars.UI.ActivityScreen.Mappings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.zingo.merabihars.R;

public class FollowProfileList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_follow_profile_list);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
