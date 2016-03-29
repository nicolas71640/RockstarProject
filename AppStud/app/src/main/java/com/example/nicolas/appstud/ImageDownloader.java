package com.example.nicolas.appstud;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/******************************************************************************
 * File name : ImageDownloader.java
 *
 * Description :
 *      A static class that handles the download of a bitmap
 *
 ******************************************************************************/
public class ImageDownloader {
    /*********************************************
     *DOWNLOAD BITMAP
     *********************************************/
    public static Bitmap getBitmap(String urlString){
        Bitmap bitmap = null;
        InputStream stream;

        try {
            //Connect to the http server and get the Stream
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            stream = conn.getInputStream();

            //Convert the stream to bitmap
            bitmap = BitmapFactory.decodeStream(stream);

        }
        catch(Exception e){
            Log.e("Download bitmap","Error downloading bitmap"+ e.toString());
        }

        return bitmap;
    }

}
