package com.example.cinemo;

//*******************************************************************
//  AsyncLoadData.java
//
// Service to download data on background
//
// Author:  Alberto Fernandez Iglesias
// Email: aigaliana@gmail.com
// Created on:  22/04/2020 (Covid-19 times)
//*******************************************************************

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class AsyncLoadData extends AsyncTask<String, Void, String> {
    private Context mContext;
    private ILoadDataListener mListener;
    private HelperFile clsHelper;

    public AsyncLoadData(Context context, ILoadDataListener listener) {
        this.mContext = context;
        this.mListener = listener;
        clsHelper = new HelperFile();
    }

    @Override
    protected String doInBackground(String... params) {

        String strRet = "";

        try {

            mListener.event("Starting to Download Station Names...");
            List<String> lstFiles = DownloadStationNames();

            strRet = lstFiles.size() + " Files Downloaded!";

            mListener.event(strRet);

            for(int i=0; i < lstFiles.size() ; i++)
            {
                String station = lstFiles.get(i);
                Log.d(TAG, "Downloading: " + station );
                mListener.event("Downloading: " + station + " [" + i + " of " + lstFiles.size() + "]");

                DownloadFile(Constants.EndpointStations,station);
                DownloadFile(Constants.EndpointDecodes,station);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return strRet;
    }

    private List<String> DownloadStationNames() throws IOException {

        List<String> lFiles = new ArrayList<String>();

        String ftpUrl = Constants.MainURL + Constants.EndpointStations;

        URL url = new URL(ftpUrl);

        URLConnection conn = url.openConnection();
        InputStream inputStream = conn.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line = null;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);

            String stationFile = line.substring(line.lastIndexOf(" ")+1);
            System.out.println(stationFile);

            Pattern pattern =   Pattern.compile(Constants.Pattern, Pattern.CASE_INSENSITIVE);
            Matcher matcher =   pattern.matcher(stationFile);
            boolean bFileAllowed = matcher.find();

            if(bFileAllowed) {
               lFiles.add(stationFile);
            }
        }

        inputStream.close();

        return lFiles;

    }

    private void DownloadFile(String pType, String pFilename) throws IOException {

        try {
            String ftpUrl = Constants.MainURL + pType + pFilename;
            URL url = new URL(ftpUrl);
            URLConnection conn = url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String decodedString;
            String strContent = "";

            while ((decodedString = in.readLine()) != null)
            {
                System.out.println(decodedString);
                strContent  = strContent + decodedString + "\n";
            }

            String prefix = "";
            if(pType == Constants.EndpointStations)
                prefix="station_";
            else
                prefix="decoded_";

            clsHelper.writeToFile(prefix+pFilename,strContent,mContext);

            in.close();
        }
        catch(Exception e) {
            Log.d(TAG, "[DownloadFile]Error: " + e.toString() );
        }

        return;

    }

    @Override
    protected void onPostExecute(String result) {
        mListener.complete(result);
    }

    @Override
    protected void onPreExecute() {
        mListener.loading();
    }

    public interface ILoadDataListener {
        void loading();
        void complete(String result);
        void event(String result);
    }
}