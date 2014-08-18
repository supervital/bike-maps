package com.vitalapps.bikemaps.api;

import android.net.Uri;

public class Api {

    private static final String BASE_URL = "http://maps.if.ua/api/";
    private static final String SCHEME = "http";
    private static final String AUTHORITY = "maps.if.ua/api";

    public static String getParkingList() {
        Uri.Builder uri = new Uri.Builder();
        uri.scheme(SCHEME);
        uri.authority(AUTHORITY);
        uri.appendPath("bycicleParking");
        return  uri.build().toString();
    }
}
