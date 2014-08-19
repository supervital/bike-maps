package com.vitalapps.bikemaps.api.request;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonRequest;
import com.vitalapps.bikemaps.api.Api;
import com.vitalapps.bikemaps.data.DatabaseManager;
import com.vitalapps.bikemaps.data.DatabaseParams;
import com.vitalapps.bikemaps.data.DatabaseQueries;
import com.vitalapps.bikemaps.data.QueryExecutor;
import com.vitalapps.bikemaps.utils.JSONUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.vitalapps.bikemaps.utils.LogUtils.LOGD;
import static com.vitalapps.bikemaps.utils.LogUtils.LOGE;
import static com.vitalapps.bikemaps.utils.LogUtils.makeLogTag;

public class ParkingRequest extends JsonRequest<JSONArray> {

    private static final String TAG = makeLogTag("ParkingRequest");


    public ParkingRequest(Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(Method.GET, Api.getParkingList(), null, listener, errorListener);
        setShouldCache(false);
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        String json = new String(response.data);
        LOGD(TAG, "Result is: " + json);
        try {
            final JSONArray parkingJsonArray = new JSONArray(json);
            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    for (int i = 0; i < parkingJsonArray.length(); i++) {
                        ContentValues contentValues = new ContentValues();
                        Parking parking = new Parking(JSONUtils.getJsonObject(parkingJsonArray, i));
                        contentValues.put(Parking.CN_ID, parking.getId());
                        contentValues.put(Parking.CN_TYPE, parking.getType());
                        contentValues.put(Parking.CN_LAT, parking.getLat());
                        contentValues.put(Parking.CN_LNG, parking.getLng());
                        contentValues.put(Parking.CN_IMG_URL, parking.getPhotoUrl());
                        contentValues.put(Parking.CN_DESC, parking.getDescription());
                        DatabaseParams.Insert insert = new DatabaseParams.Insert();
                        insert.values = contentValues;
                        insert.table = Parking.T_NAME;
                        DatabaseQueries.insert(database, insert);
                    }
                }
            });
            return Response.success(new JSONArray(), null);
        } catch (JSONException e) {
            LOGE(TAG, e.getMessage());
            return Response.error(new ParseError(e));
        }
    }

    public class Parking implements BaseColumns {

        public static final String T_NAME = "parking";
        public static final String CN_ID = "parkingid";
        public static final String CN_TYPE = "type";
        public static final String CN_LNG = "lng";
        public static final String CN_LAT = "lat";
        public static final String CN_IMG_URL = "photourl";
        public static final String CN_DESC = "description";

        public static final String TABLE_PARKING = "CREATE TABLE "
                + T_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CN_ID + " INTEGER,"
                + CN_TYPE + " INTEGER,"
                + CN_LNG + " TEXT,"
                + CN_LAT + " TEXT,"
                + CN_IMG_URL + " TEXT,"
                + CN_DESC + " TEXT);";

        JSONObject jsonObject;

        public Parking(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        public int getId() {
            return JSONUtils.getInt(jsonObject, "ID");
        }

        public int getType() {
            return JSONUtils.getInt(jsonObject, "Type");
        }

        public String getLat() {
            return JSONUtils.getString(jsonObject, "Lat");
        }

        public String getLng() {
            return JSONUtils.getString(jsonObject, "Lat");
        }

        public String getPhotoUrl() {
            return JSONUtils.getString(jsonObject, "FullPhotoUrl");
        }

        public String getDescription() {
            return JSONUtils.getString(jsonObject, "Description");
        }
    }
}
