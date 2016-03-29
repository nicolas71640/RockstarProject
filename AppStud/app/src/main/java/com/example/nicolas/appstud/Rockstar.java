package com.example.nicolas.appstud;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

/******************************************************************************
 * File name : Rockstar.java
 *
 * Description :
 *      This class allow us to save in one object all the parameters of a
 *      rockstar.
 *
 ******************************************************************************/
public class Rockstar{
    private String mFirstname;
    private String mLastname;
    private String mStatus;
    private Bitmap mPhoto;
    private boolean mBookmark;
    private Context mContext;
    private static int count = 0;
    private int mPosition;


    /*********************************************
     * CONSTRUCTOR
     *********************************************/
    public Rockstar(Context context, String firstname, String lastname, String status, Bitmap bitmap) {
        mFirstname = firstname;
        mLastname = lastname;
        mStatus = status;
        mPhoto = bitmap;
        mContext = context;
        mPosition = count;
        count++;
        mBookmark = getBookmarkFromPreferences();
    }

    /*********************************************
     * INTERFACE
     *********************************************/
    public Bitmap getPhoto() {
        return mPhoto;
    }

    public String getFirstname() {
        return mFirstname;
    }

    public String getLastname() {
        return mLastname;
    }

    public String getStatus() {
        return mStatus;
    }

    public int getPosition() {
        return mPosition;
    }

    public boolean getBookmark() {
        return mBookmark;
    }

    public void setBookmark(boolean bookmarked) {
        mBookmark = bookmarked;

        //Save the bookmark state
        SharedPreferences settings = mContext.getSharedPreferences("bookmarks", 0);
        int temp = settings.getInt("bookmarks", 0);
        if (mBookmark)
            temp |= 1 << mPosition;
        else
            temp &= ~(1 << mPosition);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("bookmarks", temp);
        editor.commit();
    }

    public static void resetCount(){
        count = 0;
    }

    /*********************************************
     * LOAD BOOKMARKS STATE
     *********************************************/
    private boolean getBookmarkFromPreferences() {
        SharedPreferences settings = mContext.getSharedPreferences("bookmarks", 0);
        int temp = settings.getInt("bookmarks", 0);
        return ((((temp >> mPosition) & 1) == 1) ? true : false);
    }











}