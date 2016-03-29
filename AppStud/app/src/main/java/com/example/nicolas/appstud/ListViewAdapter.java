package com.example.nicolas.appstud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/******************************************************************************
 * File name : ListViewAdapter.java
 *
 * Description :
 *      This class inherits from BaseAdapter and implements the interface
 *      Filterable. It allows us to custom the listview of the rockstars
 *      and use the filterable interface to search a rockstar thanks to an editText.
 *
 ******************************************************************************/

public class ListViewAdapter extends BaseAdapter implements Filterable {
    private ArrayList<Rockstar> mOriginalData;
    private ArrayList<Rockstar> mFilteredData;
    private LayoutInflater mInflater=null;
    private Context mContext;

    /*********************************************
     *CONSTRUCTOR
     *********************************************/
    public ListViewAdapter(Context context,ArrayList<Rockstar> rockstarsList){
        mContext = context;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mOriginalData = rockstarsList;
        mFilteredData = (ArrayList<Rockstar>)rockstarsList.clone();
    }

    /*********************************************
     *BASEADAPTER METHODS
     *********************************************/
    @Override
    public int getCount() {
        return mFilteredData.size();
    }

    @Override
    public Object getItem(int position) {
        return mFilteredData.get(position);
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
            convertView = mInflater.inflate(R.layout.item_custom, null);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.status);
            viewHolder.imImage = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.cb = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder)convertView.getTag();

        final Rockstar rockstar = mFilteredData.get(position);

        //When the user check a checkbox
        viewHolder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mOriginalData.get(rockstar.getPosition()).setBookmark(isChecked); //Change the bookmark state of this rockstar
                    ((MainActivity) mContext).onCheckBoxStateChange(rockstar.getPosition()); //Update the bookmarksFragment
                }
            });

        //Update the widgets states according to the rockstars states
        viewHolder.tvName.setText(rockstar.getFirstname()+" "+rockstar.getLastname());
        viewHolder.tvStatus.setText(rockstar.getStatus());
        viewHolder.imImage.setImageBitmap(rockstar.getPhoto());
        viewHolder.cb.setChecked(mOriginalData.get(rockstar.getPosition()).getBookmark());

        return convertView;
    }

    /*********************************************
     *FILTERING
     *********************************************/
    public Filter getFilter(){
        Filter filter = new Filter(){
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilteredData = (ArrayList<Rockstar>) results.values; //Update the list
                notifyDataSetChanged(); //Allow the fragment to update its view
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<Rockstar> FilteredArray = new ArrayList<Rockstar>(mOriginalData.size());

                //Perform the search by firstname or lastname
                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < mOriginalData.size(); i++) {
                    String firstname = mOriginalData.get(i).getFirstname();
                    String lastname = mOriginalData.get(i).getLastname();
                    if (firstname.toLowerCase().startsWith(constraint.toString())
                            ||  lastname.toLowerCase().startsWith(constraint.toString()))  {
                        FilteredArray.add(mOriginalData.get(i));
                    }
                }

                results.count = FilteredArray.size();
                results.values = FilteredArray;

                return results;
            }
        };

        return filter;

    }

    /*********************************************
     *VIEW HOLDER
     *********************************************/
    private static class ViewHolder{
        TextView tvName;
        TextView tvStatus;
        ImageView imImage;
        CheckBox cb;
    }

}
