package app.zingo.merabihars.UI.ActivityScreen.LandingScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import app.zingo.merabihars.R;
import app.zingo.merabihars.UI.ActivityScreen.Fragment.AccountViewFragment;
import app.zingo.merabihars.UI.ActivityScreen.Fragment.ContentPostScreen;
import app.zingo.merabihars.UI.ActivityScreen.Fragment.LandingFragment;
import app.zingo.merabihars.UI.ActivityScreen.Fragment.NavigationFragment;
import app.zingo.merabihars.UI.ActivityScreen.Fragment.PostNewStory;
import app.zingo.merabihars.UI.ActivityScreen.Fragment.SearchFragment;

public class HomeLandingBottomScreen extends AppCompatActivity {



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment;
            try{

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        fragment = new LandingFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.navigation_search:
                        fragment = new SearchFragment();
                        loadFragment(fragment);
                        return true;

                    case R.id.navigation_post:
                        fragment = new PostNewStory();
                        loadFragment(fragment);
                        return true;
                    case R.id.person_info_navi:
                        fragment = new AccountViewFragment();
                        loadFragment(fragment);
                        return true;
                }

            }catch (Exception e){
                e.printStackTrace();
                return false;

            }
            return false;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_home_landing_bottom_screen);

            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

            loadFragment(new LandingFragment());

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void loadFragment(Fragment fragment) throws Exception {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

}
