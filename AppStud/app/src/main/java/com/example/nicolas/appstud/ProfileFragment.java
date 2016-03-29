package com.example.nicolas.appstud;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/******************************************************************************
 * File name : ProfileFragment.java
 *
 * Description :
 *      This is the third and final fragment of the viewpager contained in the
 *      main activity. In it the user can see and set his photo and his full name.
 *
 ******************************************************************************/
public class ProfileFragment extends Fragment {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private ImageView mPhotoImgView;
    private EditText mNameEdtTxt;
    private Bitmap mBitmap;

    /*********************************************
     *FRAGMENT LIFE CYCLE
     *********************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.fragment_profile, container, false);

        //Widget Definition
        mPhotoImgView = (ImageView) V.findViewById(R.id.imageView);
        mNameEdtTxt = (EditText) V.findViewById(R.id.editText);
        FloatingActionButton fab = (FloatingActionButton) V.findViewById(R.id.fab); //Floating button to take a photo

        mNameEdtTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(mNameEdtTxt.isFocused()) //If the editText has been changed by the user (and not automatically)
                    ((MainActivity)getActivity()).addSaveProfileMenu(); //We add the save button in the action bar
            }
        });

        //When the user click on the floating action button we open the camera application
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(imageIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        //Get the profile from memory
        getProfile();

        return V;

    }//onCreateView

    /*********************************************
     *AFTER THE PHOTO HAS BEEN TAKEN
     *********************************************/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            try {
                mBitmap = (Bitmap) data.getExtras().get("data");//Save the bitmap
                mPhotoImgView.setImageBitmap(mBitmap); //Show the bitmap
                ((MainActivity)getActivity()).addSaveProfileMenu();
            }
            catch(Exception e){}
        }
    }

    /*********************************************
     *SAVE AND LOAD THE PROFILE FROM THE MEMORY
     *********************************************/
    public void getProfile() {
        /*Get the photo from the phone memory if it exist*/
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "appstud");
        File imgFile = new File(storageDir, "image_profile.jpg");
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            mPhotoImgView.setImageBitmap(myBitmap);
        }

        /*Get the username*/
        SharedPreferences settings = getActivity().getSharedPreferences("UserInfo", 0);
        mNameEdtTxt.setText(settings.getString("userName", ""));
    }//getProfile()

    public void saveProfile() {
        /*Save the photo*/
        if (mBitmap != null) {
            File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "appstud");
            storageDir.mkdirs(); //Make the directory if it doesn't exist
            File image = new File(storageDir.getPath(), "image_profile.jpg"); //Create the image file
            try {
                FileOutputStream out = new FileOutputStream(image);
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); //Save the bitmap
            } catch (IOException e) {
                Log.e("Error","Saving the image profile"+ e.toString());
            }
        }

        /*Save the username*/
        SharedPreferences settings = getActivity().getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("userName", mNameEdtTxt.getText().toString());
        editor.commit();
        Snackbar.make(getView(), "Profile saved", Snackbar.LENGTH_LONG).show();
    }//saveProfile

}

