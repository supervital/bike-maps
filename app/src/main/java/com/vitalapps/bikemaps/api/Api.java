package com.vitalapps.bikemaps.api;

import android.net.Uri;
import static com.vitalapps.bikemaps.utils.LogUtils.*;

public class Api {

    private static final String TAG = makeLogTag("URLS");
    private static final String BASE_URL = "http://maps.if.ua/api/";
    private static final String SCHEME = "http";
    private static final String AUTHORITY = "maps.if.ua";
    private static final String API = "api";

    public static String getParkingList() {
        Uri.Builder uri = new Uri.Builder();
        uri.scheme(SCHEME);
        uri.authority(AUTHORITY);
        uri.appendPath(API);
        uri.appendPath("bycicleParking");
        LOGD(TAG, "getParkingList - " + uri.build().toString());
        return  uri.build().toString();
    }

    public static String createParking() {
        Uri.Builder uri = new Uri.Builder();
        uri.scheme(SCHEME);
        uri.authority(AUTHORITY);
        uri.appendPath(API);
        uri.appendPath("bycicleParking");
        LOGD(TAG, "getParkingList - " + uri.build().toString());
        return  uri.build().toString();
    }

    public static String uploadFile() {
        Uri.Builder uri = new Uri.Builder();
        uri.scheme(SCHEME);
        uri.authority(AUTHORITY);
        uri.appendPath(API);
        uri.appendPath("upload");
        LOGD(TAG, "upload - " + uri.build().toString());
        return  uri.build().toString();
    }
}
