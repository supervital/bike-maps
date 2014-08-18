package com.vitalapps.bikemaps.api;

import android.content.Context;

public class VolleyRequestManager {

    private static VolleyRequestManager instance;
    private RequestApi mRequestApi;

    private VolleyRequestManager(Context context) {
        mRequestApi = new RequestApi(context);
    }

    public RequestApi doVolleyRequest() {
        return mRequestApi;
    }

    // This method should be called first to do singleton initialization
    public static synchronized VolleyRequestManager getInstance(Context context) {
        if (instance == null) {
            instance = new VolleyRequestManager(context);
        }
        return instance;
    }

    public static synchronized VolleyRequestManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(VolleyRequestManager.class.getSimpleName() +
                    " is not initialized, call getInstance(context) method first.");
        }
        return instance;
    }
}
