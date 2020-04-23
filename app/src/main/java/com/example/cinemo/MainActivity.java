package com.example.cinemo;

//*******************************************************************
//  MainActivity.java
//
// This the main activity to handle the objects and processes
//
// Author:  Alberto Fernandez Iglesias
// Email: aigaliana@gmail.com
// Created on:  22/04/2020 (Covid-19 times)
//*******************************************************************

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.content.Context;

public class MainActivity extends AppCompatActivity implements AsyncLoadData.ILoadDataListener {

    private ProgressDialog dialog;
    private TextView txtLog;
    private TextView txtRaw;
    private TextView txtDecoded;
    private HelperFile clsHelper;
    private Context mContext;
    private ImageButton imgButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText txtStation = (EditText) findViewById(R.id.txtStation);
        txtLog = (TextView) findViewById(R.id.txtLog);
        txtRaw = (TextView) findViewById(R.id.txtRaw);
        txtDecoded = (TextView) findViewById(R.id.txtDecoded);
        imgButton = (ImageButton)findViewById(R.id.imgDownload);
        clsHelper = new HelperFile();
        this.mContext = this;

        //***** Touch on DONE of keyboard *****
        txtStation.setOnKeyListener(new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event)
                    {
                        if (keyCode == KeyEvent.KEYCODE_ENTER)
                        {
                            String strStation = txtStation.getText().toString();
                            Toast.makeText(getApplicationContext(),"Searching..." + strStation ,Toast.LENGTH_SHORT).show();
                            String contentStation = clsHelper.readFromFile("station_"+strStation + ".TXT",mContext);
                            txtRaw.setText(contentStation);
                            String contentDecode = clsHelper.readFromFile("decoded_"+strStation + ".TXT",mContext);
                            txtDecoded.setText(contentDecode);

                            if(contentStation.equals("") || contentDecode.equals("")) {
                                Toast.makeText(getApplicationContext(),"Data not available, try to download again!" + strStation ,Toast.LENGTH_LONG).show();
                            }

                        }
                        return false;
                    }});

        //***** Touch to dowload manually *****
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadData();
            }
        });

        DownloadData();

    }


    public void DownloadData() {
        if(isNetworkAvailable())
        {
            dialog = new ProgressDialog(mContext);
            new AsyncLoadData(this, this).execute("");
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Network not available, try again later!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void loading() {
        dialog.setMessage("Please wait, downloading database...");
        dialog.show();
        Log.d("","Starting Downloading...");
    }

    @Override
    public void complete(String result) {
        Log.d("",result);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
        txtLog.setText(result);
    }

    @Override
    public void event(String result) {
        txtLog.setText(result);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
