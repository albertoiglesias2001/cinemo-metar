package com.example.cinemo;

//*******************************************************************
//  Constants.java
//
// Used to create constants like URLs and Patterns
//
// Author:  Alberto Fernandez Iglesias
// Email: aigaliana@gmail.com
// Created on:  22/04/2020 (Covid-19 times)
//*******************************************************************

public class Constants {

    public static final String MainURL = "ftp://tgftp.nws.noaa.gov/data/observations/metar/";
    public static final String EndpointStations = "stations/";
    public static final String EndpointDecodes = "decoded/";
    public static final String Pattern = "^ED";   //** *REGEX for choose which files..
                                                  // In this case stations stating with "ED"
}
