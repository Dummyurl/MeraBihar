package app.zingo.merabihar.UI.ActivityScreen.SearchScreens;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.zingo.merabihar.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentSearchFragment extends Fragment {


    public ContentSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_content_search, container, false);
    }

}