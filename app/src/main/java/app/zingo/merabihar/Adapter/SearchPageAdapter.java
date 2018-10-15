package app.zingo.merabihar.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import app.zingo.merabihar.UI.ActivityScreen.Mappings.CategoryFollowScreen;
import app.zingo.merabihar.UI.ActivityScreen.Mappings.InterestFollowScreen;
import app.zingo.merabihar.UI.ActivityScreen.Mappings.PeopleFollowingScreen;
import app.zingo.merabihar.UI.ActivityScreen.Mappings.SubCategoryFollowScreen;

/**
 * Created by ZingoHotels Tech on 15-10-2018.
 */

public class SearchPageAdapter extends FragmentStatePagerAdapter {


    String[] tabTitles = {"People", "Interests", "Collection","Experiences","Stories"};

    public SearchPageAdapter(FragmentManager fm) {
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
                PeopleFollowingScreen people = new PeopleFollowingScreen();
                return people;

            case 1:
                InterestFollowScreen interest = new InterestFollowScreen();
                return interest;

            case 2:
                CategoryFollowScreen collection = new CategoryFollowScreen();
                return collection;

            case 3:
                SubCategoryFollowScreen experience = new SubCategoryFollowScreen();
                return experience;

            case 4:
                SubCategoryFollowScreen experiences = new SubCategoryFollowScreen();
                return experiences;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
