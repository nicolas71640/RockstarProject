package com.example.nicolas.appstud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/******************************************************************************
 * File name : ListViewAdapterBookmarks.java
 *
 * Description :
 *      This class inherits from BaseAdapter. It allow us to custom the
 *      bookmarks listView.
 *
 ******************************************************************************/

public class ListViewAdapterBookmarks extends BaseAdapter{
    private ArrayList<Rockstar> mRockstarsList;
    private ArrayList<Rockstar> mBookmarksList = new ArrayList<Rockstar>();
    private LayoutInflater mInflater=null;
    private MainActivity mContext;
    private BookmarksFragment mBookmarksFragment;

    /*********************************************
     *CONSTRUCTOR
     *********************************************/
    public ListViewAdapterBookmarks(Context context,BookmarksFragment bookmarksFragment, ArrayList<Rockstar> rockstarsList){
        mContext = (MainActivity)context;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mBookmarksFragment = bookmarksFragment;
        mRockstarsList = rockstarsList;

        //Search for all the rockstar objects that have been bookmarked
        mBookmarksList = new ArrayList<Rockstar>();
        for(int i = 0 ; i<mRockstarsList.size();i++){
            if(mRockstarsList.get(i).getBookmark()){
                mBookmarksList.add(mRockstarsList.get(i));
            }
        }
    }

    /*********************************************
     *BASEADAPTER METHODS
     *********************************************/
    @Override
    public int getCount() {
        return mBookmarksList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBookmarksList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView==null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_custom_bookmarks, null);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.status);
            viewHolder.imImage = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.button = (ImageButton)convertView.findViewById(R.id.imageButton);
            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder)convertView.getTag();

        final Rockstar rockstar = mBookmarksList.get(position);
        viewHolder.tvName.setText(rockstar.getFirstname()+" "+rockstar.getLastname());
        viewHolder.tvStatus.setText(rockstar.getStatus());
        viewHolder.imImage.setImageBitmap(rockstar.getPhoto());
        //When the user click on the delete button
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MainActivity.rockstarsList.
                mRockstarsList.get(rockstar.getPosition()).setBookmark(false); //Change the bookmark variable of the rockstar
                mBookmarksFragment.updateListView(); //Update the listView of the bookmarks
                mContext.onDeleteButtonClick(rockstar.getPosition());//Update the listview of the rockstarsFragment
            }
        });

        return convertView;
    }

    /*********************************************
     *VIEW HOLDER
     *********************************************/
    private static class ViewHolder{
        TextView tvName;
        TextView tvStatus;
        ImageView imImage;
        ImageButton button;
    }

}
