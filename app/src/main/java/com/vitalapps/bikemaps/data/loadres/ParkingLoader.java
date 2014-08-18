package com.vitalapps.bikemaps.data.loadres;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vitalapps.bikemaps.api.request.ParkingRequest;
import com.vitalapps.bikemaps.data.DatabaseManager;
import com.vitalapps.bikemaps.data.DatabaseParams;
import com.vitalapps.bikemaps.data.DatabaseQueries;
import com.vitalapps.bikemaps.data.QueryExecutor;

public class ParkingLoader extends CursorLoader {

    public static final int PARKING = 1;

    public ParkingLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
        try {
            final Cursor[] cursor = new Cursor[1];
            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    DatabaseParams.Select allParking = new DatabaseParams.Select();
                    allParking.table = ParkingRequest.Parking.T_NAME;
                    cursor[0] = DatabaseQueries.select(database, allParking);
                }
            });
            return cursor[0];
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
