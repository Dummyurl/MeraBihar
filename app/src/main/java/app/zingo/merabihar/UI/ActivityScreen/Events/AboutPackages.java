package app.zingo.merabihar.UI.ActivityScreen.Events;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.zingo.merabihar.R;

public class AboutPackages extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            setContentView(R.layout.activity_about_packages);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
