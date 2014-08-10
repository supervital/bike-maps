package com.vitalapps.bikemaps.app;

import android.app.Application;
import android.content.Context;

import com.vitalapps.bikemaps.api.request.RequestManager;
import com.vitalapps.bikemaps.service.AppServiceHelper;
import com.vitalapps.bikemaps.service.AppServiceHelperImpl;


public class App extends Application {

    public static final String PACKAGE = "com.vitalapps.bikemaps";

    private AppServiceHelper mAppServiceHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppServiceHelper = new AppServiceHelperImpl(this);
        RequestManager.getInstance(getApplicationContext());
    }

    public static App getApplication(Context context) {
        if (context instanceof App) {
            return (App) context;
        }
        return (App) context.getApplicationContext();
    }

    public AppServiceHelper getServiceHelper() {
        return mAppServiceHelper;
    }

}
