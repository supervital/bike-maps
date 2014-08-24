package com.vitalapps.bikemaps.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vitalapps.bikemaps.api.request.ParkingRequest;
import com.vitalapps.bikemaps.data.models.ParkingModel;

import static com.vitalapps.bikemaps.utils.LogUtils.LOGD;

public class AppSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLiteOpenHelper";
    private static final String DATABASE_NAME = "bike_maps";
    private static final int DATABASE_VERSION = 1;

    public AppSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        LOGD(TAG, "AppSQLiteOpenHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        LOGD(TAG, "onCreate");
        db.execSQL(ParkingModel.TABLE_PARKING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + ParkingModel.TABLE_PARKING);
            onCreate(db);
        }
    }
}
