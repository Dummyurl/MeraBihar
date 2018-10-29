package app.zingo.merabihars.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import app.zingo.merabihars.UI.ActivityScreen.MainTabHostActivity.ForFollowersFragment;
import app.zingo.merabihars.UI.ActivityScreen.MainTabHostActivity.ForYouFragment;
import app.zingo.merabihars.UI.ActivityScreen.SearchScreens.InterestSearchFragment;
import app.zingo.merabihars.UI.ActivityScreen.SearchScreens.PeopleSearchFragment;

/**
 * Created by ZingoHotels Tech on 29-10-2018.
 */

public class MainContentScreenAdapter  extends FragmentStatePagerAdapter {


    String[] tabTitles = {"For You", "Follow"};

    public MainContentScreenAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ForYouFragment people = new ForYouFragment();
                return people;

            case 1:
                ForFollowersFragment interest = new ForFollowersFragment();
                return interest;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
