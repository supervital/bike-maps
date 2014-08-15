package com.vitalapps.bikemaps.api.request;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.vitalapps.bikemaps.data.AppSQLiteOpenHelper;
import com.vitalapps.bikemaps.data.DatabaseManager;
import com.vitalapps.bikemaps.data.QueryExecutor;
import com.vitalapps.bikemaps.utils.JSONUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.vitalapps.bikemaps.utils.LogUtils.LOGD;
import static com.vitalapps.bikemaps.utils.LogUtils.LOGE;
import static com.vitalapps.bikemaps.utils.LogUtils.makeLogTag;

public class ParkingRequest extends Request<String> {

    private static final String TAG = makeLogTag("ParkingRequest");

    private Response.Listener mListener;
    private Context mContext;

    public ParkingRequest(int method, String url, Response.Listener listener, Response.ErrorListener errorListener, Context context) {
        super(method, url, errorListener);
        mListener = listener;
        mContext = context;
        setShouldCache(false);
    }


    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String json = new String(response.data);
        LOGD(TAG, "Result is: " + json);
        try {
            final JSONArray parkingJsonArray = new JSONArray(json);
            DatabaseManager.initializeInstance(new AppSQLiteOpenHelper(mContext));
            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    for (int i = 0; i < parkingJsonArray.length(); i++) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put();
                    }
                }
            });

            return Response.success("", null);
        } catch (JSONException e) {
            LOGE(TAG, e.getMessage());
            return Response.error(new ParseError(e));
        }
    }

    public class Parking implements BaseColumns {

        public static final String T_NAME = "parking";
        public static final String CN_ID = "parkingid";
        public static final String CN_TYPE = "type";
        public static final String CN_LONG = "lng";
        public static final String CN_LAT = "lat";
        public static final String CN_IMG_URL = "photourl";
        public static final String CN_DESC = "description";

        public static final String TABLE_PARKING = "CREATE TABLE "
                + T_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CN_ID + " INTEGER,"
                + CN_TYPE + " INTEGER,"
                + CN_LONG + " TEXT,"
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
            return Long.toString(JSONUtils.getLong(jsonObject, "Lat"));
        }

        public String getLng() {
            return Long.toString(JSONUtils.getLong(jsonObject, "Lng"));
        }

        public String getPhotoUrl() {
            return JSONUtils.getString(jsonObject, "FullPhotoUrl");
        }

        public String getDescription() {
            return JSONUtils.getString(jsonObject, "Description");
        }
    }
}
