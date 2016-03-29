package com.example.nicolas.appstud;



import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

/******************************************************************************
 * File name : MainActivity.java
 *
 * Description :
 *      This the main Activity of this app. First it display a splash screen
 *      waiting for the downloading of the rockstars list. Then it display
 *      a view pager which contains three fragments.
 *
 ******************************************************************************/

public class MainActivity extends AppCompatActivity{
    private int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private ViewPager mViewPager;
    private Menu mMenu; //Allow us to display a "validate" button on the profile fragment
    private ProfileFragment mProfileFragment;
    private BookmarksFragment mBookmarksFragment;
    private RockstarsFragment mRockstarsFragment;
    private ArrayList<Rockstar> mRockstarsList; //Rockstar list accessible to all fragments

    /*********************************************
     *PAGER ADAPTER
     *********************************************/
    public class MyPagerAdapter extends FragmentPagerAdapter {
        static final int NUM_ITEMS = 3;

        //Construct method
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        //getItem method, allow to display a specific fragment
        @Override
        public Fragment getItem(int item){
            switch(item) {
                case 0:
                    mRockstarsFragment = new RockstarsFragment();
                    return mRockstarsFragment;
                case 1:
                    mBookmarksFragment = new BookmarksFragment();
                    return mBookmarksFragment;
                case 2:
                    mProfileFragment = new ProfileFragment();
                    return mProfileFragment;
                default:
                    return null;
            }
        }

        //Method that return the number of fragments, here 3
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        //Set the tab titles
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return getString(R.string.rockstars);
            }
            else if (position == 1){
                return getString(R.string.bookmarks);
            }
            else{
                return getString(R.string.profile);
            }
        }

    }//PagerAdapter


    /*********************************************
     *ACTIVITY LIFE CYCLE
     *********************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //If the rockstarsList hasn't been initialize yet we display the splashscreen and download the rockstars
        if(mRockstarsList == null){
            setContentView(R.layout.activity_splash_screen);
            new downloadJSON().execute();
        }
        //Else we display the three fragments
        else{
            setActivityView();
        }
    }//onCreate


    /*********************************************
     *MENU HANDLER
     *********************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //When the user click on the save profile button
        if (id == R.id.action_save_profile) {
            mProfileFragment.saveProfile();
            mMenu.clear();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addSaveProfileMenu(){
        mMenu.clear();
        getMenuInflater().inflate(R.menu.main_menu, mMenu);
    }


    /*********************************************
     *FRAGMENT INTERFACE
     *********************************************/
    /*When the user add or delete a bookmark by a checkbox*/
    public void onCheckBoxStateChange(int position){
        mBookmarksFragment.updateListView();
    }
    /*When the user delete a bookmark by the bookmarkFragment*/
    public void onDeleteButtonClick(int position){
        mRockstarsFragment.updateListView();
    }
    public ArrayList<Rockstar> getRockstarsList(){
        return mRockstarsList;
    }
    public void setRockstarsList(ArrayList<Rockstar> rockstarsList){
        mRockstarsList = (ArrayList<Rockstar>)rockstarsList.clone();
    }

    /*********************************************
     *SET ACTIVITY VIEW
     *********************************************/
    private void setActivityView(){
        setContentView(R.layout.activity_main);

        //Appbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.rockstars);

        //View pager definition
        MyPagerAdapter mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(2); //Avoid the viewpager to destroy view that is not display
        mViewPager.setAdapter(mPagerAdapter);

        //TabLayout definition
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabsFromPagerAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.list_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.bookmark_icon);
        tabLayout.getTabAt(2).setIcon(R.drawable.profile_icon);

        final Context context = this;
        //When the fragment showing change
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(final int position) {
                switch (position) {
                    case 0:
                        MainActivity.this.setTitle(R.string.rockstars);
                        mMenu.clear();
                        break;
                    case 1:
                        MainActivity.this.setTitle(R.string.bookmarks);
                        mMenu.clear();
                        break;
                    case 2:
                        MainActivity.this.setTitle(R.string.profile);
                        /*Request permission at runtime for api 23*/
                        if (ContextCompat.checkSelfPermission(context,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((MainActivity)context,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                        }
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageScrolled(int test, float test1, int test2) {
            }
        });
    }

    /*********************************************
     *HANDLE THE PERMISSION REQUEST RESPONSE
     *********************************************/
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if(requestCode== MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            //If the user didn't accept the permission we return to the bookmarks fragment.
            if (grantResults.length > 0
                    && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                mViewPager.setCurrentItem(1);
            }
            //If the user did accept the permission we update the profile (we now have the rights to go to search
            // in the external storage for the profile picture)
            else{
                    mProfileFragment.getProfile();
                }
        }
    }

    /*********************************************
     *ASYNCHRONOUS TASK TO DOWNLOAD THE JSONOBJECT
     *********************************************/
    private class downloadJSON extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Rockstar.resetCount(); //reset the static variable count
            mRockstarsList= JSONhandler.downloadRockstarsList(MainActivity.this); //Download the rockstars list
            return null;
        }

        @Override
        protected void onPostExecute(Void args){
            setActivityView();
        }
    }//AsyncTask








}
