package com.example.nicolas.appstud;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/******************************************************************************
 * File name : BookmarksFragment.java
 *
 * Description :
 *      This the second fragment of the viewpager contained in the main Activity.
 *      It displays a list of all the rockstars bookmarked by the user in the
 *      rockstarsFragment. The user can also delete one of the bookmarks thanks
 *      to a "garbage" button.
 *
 ******************************************************************************/

public class BookmarksFragment extends Fragment {
    private ListView mListView;

    /*********************************************
     *FRAGMENT LIFE CYCLE
     *********************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        //Widget Definition
        mListView = (ListView)V.findViewById(R.id.listView);

        return V;
    }


    /*********************************************
     *UPDATE LISTVIEW
     *********************************************/
    public void updateListView(){
        //Set the listView adpater
        ListViewAdapterBookmarks adapter = new ListViewAdapterBookmarks(getActivity(),this,
                ((MainActivity)getActivity()).getRockstarsList());
        mListView.setAdapter(adapter);
    }

}
