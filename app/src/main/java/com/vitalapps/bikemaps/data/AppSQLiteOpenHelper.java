package com.vitalapps.bikemaps.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vitalapps.bikemaps.api.request.ParkingRequest;

public class AppSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bike_maps";
    private static final int DATABASE_VERSION = 1;

    public AppSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ParkingRequest.Parking.TABLE_PARKING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + ParkingRequest.Parking.TABLE_PARKING);
            onCreate(db);
        }
    }
}
