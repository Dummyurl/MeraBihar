package app.zingo.merabihar.UI.ActivityScreen.Mappings;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import app.zingo.merabihar.Adapter.FollowingPageAdapter;
import app.zingo.merabihar.R;
import app.zingo.merabihar.UI.ActivityScreen.Events.AboutCity;

public class FollowingMainScreen extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_following_main_screen);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            setTitle("Following");

            tabLayout = (TabLayout) findViewById(R.id.following_tabs);
            tabLayout.setTabGravity(TabLayout.MODE_FIXED);
            viewPager = (ViewPager) findViewById(R.id.following_view_pager);

            FollowingPageAdapter adapter = new FollowingPageAdapter(getSupportFragmentManager());

            //Adding adapter to pager
            viewPager.setAdapter(adapter);

            //Adding onTabSelectedListener to swipe views
            tabLayout.setOnTabSelectedListener(this);
            tabLayout.setupWithViewPager(viewPager);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                FollowingMainScreen.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
