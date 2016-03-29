package com.example.nicolas.appstud;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;

/******************************************************************************
 * File name : RockstarsFragment.java
 *
 * Description :
 *      This the first fragment of the viewpager contained in the main activity.
 *      It display a list of the rockstars with their full names, their status
 *      and finally their photo. The user can add them to their bookmarks thanks
 *      to a checkbox.
 *
 ******************************************************************************/

public class RockstarsFragment extends Fragment {
    private ListView mRockstarsListView;
    private ListViewAdapter mAdapter;
    private SwipeRefreshLayout mSwipeContainer;
    private EditText mSearchText;


    /*********************************************
     *FRAGMENT LIFE CYCLE
     *********************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.fragment_rockstars, container, false);

        //Widgets definition
        mRockstarsListView = (ListView)V.findViewById(R.id.listView);
        mSwipeContainer = (SwipeRefreshLayout)V.findViewById(R.id.swipe_refresh_layout);
        mSearchText = (EditText)V.findViewById(R.id.editText);

        //Update the listview with the rockstarsList in MainActivity
        updateListView();

        //Refresh the listview
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new downloadJSON().execute();
            }
        });

        //Allow the user to scroll all up before refreshing
        mRockstarsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (mRockstarsListView != null && mRockstarsListView.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = mRockstarsListView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = mRockstarsListView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                mSwipeContainer.setEnabled(enable);
            }
        });

        //When the user use the search textEdit
        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return V;

    }//onCreateView

    /*********************************************
     *ASYNCHRONOUS TASK TO DOWNLOAD THE JSONOBJECT
     *********************************************/
    private class downloadJSON extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            //if the SwipeRefreshLayout is already define we show the waiting icon
            if(mSwipeContainer != null)
                mSwipeContainer.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Rockstar.resetCount(); //Before download again the rockstarslist we need to reset the static variable count
            ((MainActivity)getActivity()).setRockstarsList(JSONhandler.downloadRockstarsList(getActivity()));
            return null;
        }

        @Override
        protected void onPostExecute(Void args){
            //We update the listview and hide the refresh icon
            updateListView();
            if(mSwipeContainer != null)
                mSwipeContainer.setRefreshing(false);
        }
    }//AsyncTask

    /*********************************************
     *UPDATE THE LISTVIEW
     *********************************************/
    public void updateListView(){
        mAdapter = new ListViewAdapter(getActivity(),((MainActivity)getActivity()).getRockstarsList());
        mRockstarsListView.setAdapter(mAdapter);
    }



}
