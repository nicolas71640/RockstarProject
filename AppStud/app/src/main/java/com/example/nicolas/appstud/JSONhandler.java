package com.example.nicolas.appstud;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/******************************************************************************
 * File name : JSONhanlder.java
 *
 * Description :
 *      A static class that handle the downloading of the JSON file
 *
 ******************************************************************************/

public class JSONhandler {
    /*********************************************
     *DOWNLOAD THE JSON FILE
     *********************************************/
    public static JSONObject getJSONfromUrl(String urlString){
        JSONObject jsonObject = null;
        InputStream stream = null;
        String result = "";

        /*Connection to http server and get the data stream*/
        try{
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.connect();
            stream = conn.getInputStream();

        }
        catch(Exception e){
            Log.e("Http server connection","Error reaching json file"+e.toString());
        }

        /*Convert the input stream to a string*/
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = "";
            while((line = bufferedReader.readLine()) != null){
                sb.append(line+"\n");
            }
            result = sb.toString();

        }
        catch(Exception e){
            Log.e("Stream conversion","Error converting server file"+e.toString());
        }

        /*Convert the String to a JSONObject in order to deal with it in the rockstarFragment*/
        try {
            jsonObject = new JSONObject(result);
        }
        catch(Exception e){}



        return jsonObject;
    }


    /*********************************************
     *DOWNLOAD THE ROCKSTARS LIST
     *********************************************/
    public static ArrayList<Rockstar> downloadRockstarsList(Context context){
        ArrayList<Rockstar> rockstarsList = new ArrayList<Rockstar>();
        JSONObject jsonObject;
        JSONArray jsonArray;

        //Download the JSON file
        jsonObject = JSONhandler.getJSONfromUrl("http://54.72.181.8/yolo/contacts.json");

        try {
            jsonArray = jsonObject.getJSONArray("contacts");
            //For each contact we create a rockstar object
            for(int i = 0 ; i<jsonArray.length() ; i++){
                jsonObject = jsonArray.getJSONObject(i);
                rockstarsList.add(new Rockstar(context,
                        jsonObject.getString("firstname"),
                        jsonObject.getString("lastname"),
                        jsonObject.getString("status"),
                        ImageDownloader.getBitmap("http://54.72.181.8/yolo/" + jsonObject.getString("hisface"))));
            }
        }
        catch(Exception e){
            Log.e("error", e.toString());
        }

        return rockstarsList;
    }

}
