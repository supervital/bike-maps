package com.vitalapps.bikemaps.api;

import android.net.Uri;

public class Api {

    public static String createTestUrl() {
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("http");
        uri.authority("test.com");
        uri.path("get");
        uri.appendQueryParameter("", "");
        uri.appendQueryParameter("", "");
        return uri.build().toString();
    }
}
