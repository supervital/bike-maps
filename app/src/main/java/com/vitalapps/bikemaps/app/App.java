package com.vitalapps.bikemaps.app;

import android.app.Application;
import android.content.Context;

import com.vitalapps.bikemaps.api.VolleyRequestManager;
import com.vitalapps.bikemaps.data.AppSQLiteOpenHelper;
import com.vitalapps.bikemaps.data.DatabaseManager;
import com.vitalapps.bikemaps.service.AppServiceHelperImpl;


public class App extends Application {

    public static final String PACKAGE = "com.vitalapps.bikemaps";

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize
        VolleyRequestManager.getInstance(getApplicationContext());
        AppServiceHelperImpl.getInstance(getApplicationContext());
        DatabaseManager.initializeInstance(new AppSQLiteOpenHelper(getApplicationContext()));
    }

    public static App getApplication(Context context) {
        if (context instanceof App) {
            return (App) context;
        }
        return (App) context.getApplicationContext();
    }


}
