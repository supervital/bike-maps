package com.vitalapps.bikemaps.api;

import android.content.Context;

public class RequestManager {

    private static RequestManager instance;
    private RequestApi mRequestApi;

    private RequestManager(Context context) {
        mRequestApi = new RequestApi(context);
    }

    public RequestApi doRequest() {
        return mRequestApi;
    }

    // This method should be called first to do singleton initialization
    public static synchronized RequestManager getInstance(Context context) {
        if (instance == null) {
            instance = new RequestManager(context);
        }
        return instance;
    }

    public static synchronized RequestManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(RequestManager.class.getSimpleName() +
                    " is not initialized, call getInstance(context) method first.");
        }
        return instance;
    }
}
