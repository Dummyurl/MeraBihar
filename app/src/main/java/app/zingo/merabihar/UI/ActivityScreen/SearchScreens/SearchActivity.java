package app.zingo.merabihar.UI.ActivityScreen.SearchScreens;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import app.zingo.merabihar.Adapter.SearchPageAdapter;
import app.zingo.merabihar.R;

public class SearchActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            setContentView(R.layout.activity_search);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            setTitle("Search");

            tabLayout = (TabLayout) findViewById(R.id.following_tabs);
            tabLayout.setTabGravity(TabLayout.MODE_SCROLLABLE);
            viewPager = (ViewPager) findViewById(R.id.following_view_pager);

            SearchPageAdapter adapter = new SearchPageAdapter(getSupportFragmentManager());

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
                SearchActivity.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
