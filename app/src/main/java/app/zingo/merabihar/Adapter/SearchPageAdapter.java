package app.zingo.merabihar.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import app.zingo.merabihar.UI.ActivityScreen.Events.ContentSearchScreen;
import app.zingo.merabihar.UI.ActivityScreen.Mappings.CategoryFollowScreen;
import app.zingo.merabihar.UI.ActivityScreen.Mappings.InterestFollowScreen;
import app.zingo.merabihar.UI.ActivityScreen.Mappings.PeopleFollowingScreen;
import app.zingo.merabihar.UI.ActivityScreen.Mappings.SubCategoryFollowScreen;
import app.zingo.merabihar.UI.ActivityScreen.SearchScreens.CategoriesSearchFragment;
import app.zingo.merabihar.UI.ActivityScreen.SearchScreens.ContentSearchFragment;
import app.zingo.merabihar.UI.ActivityScreen.SearchScreens.InterestSearchFragment;
import app.zingo.merabihar.UI.ActivityScreen.SearchScreens.PeopleSearchFragment;
import app.zingo.merabihar.UI.ActivityScreen.SearchScreens.SubCategoriesSearchFragment;

/**
 * Created by ZingoHotels Tech on 15-10-2018.
 */

public class SearchPageAdapter extends FragmentStatePagerAdapter {


    String[] tabTitles = {"People", "Interests", "Collection","Stories"};

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
                PeopleSearchFragment people = new PeopleSearchFragment();
                return people;

            case 1:
                InterestSearchFragment interest = new InterestSearchFragment();
                return interest;

            case 2:
                CategoriesSearchFragment collection = new CategoriesSearchFragment();
                return collection;

            /*case 3:
                SubCategoriesSearchFragment experience = new SubCategoriesSearchFragment();
                return experience;*/

            case 3:
                ContentSearchFragment contents = new ContentSearchFragment();
                return contents;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
