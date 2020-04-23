package com.example.cinemo;

//*******************************************************************
//  HelperFile.java
//
// Class to manipulate files
//
// Author:  Alberto Fernandez Iglesias
// Email: aigaliana@gmail.com
// Created on:  22/04/2020 (Covid-19 times)
//*******************************************************************

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class HelperFile {

    public void writeToFile(String filename, String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public String readFromFile(String filename,Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                   // stringBuilder.append("\n").append(receiveString);
                    stringBuilder.append(receiveString).append("\n");
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("helper class", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("helper class", "Can not read file: " + e.toString());
        }

        return ret;
    }


}
