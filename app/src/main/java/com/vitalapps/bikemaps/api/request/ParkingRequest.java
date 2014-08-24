package com.vitalapps.bikemaps.api.request;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonRequest;
import com.vitalapps.bikemaps.api.Api;
import com.vitalapps.bikemaps.data.DatabaseManager;
import com.vitalapps.bikemaps.data.DatabaseParams;
import com.vitalapps.bikemaps.data.DatabaseQueries;
import com.vitalapps.bikemaps.data.QueryExecutor;
import com.vitalapps.bikemaps.data.models.ParkingModel;
import com.vitalapps.bikemaps.utils.JSONUtils;

import org.json.JSONArray;
import org.json.JSONException;

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
                    DatabaseParams.Delete delete = new DatabaseParams.Delete();
                    delete.table = ParkingModel.T_NAME;
                    DatabaseQueries.delete(database, delete);
                    for (int i = 0; i < parkingJsonArray.length(); i++) {
                        ContentValues contentValues = new ContentValues();
                        ParkingModel parking = new ParkingModel(JSONUtils.getJsonObject(parkingJsonArray, i));
                        contentValues.put(ParkingModel.CN_ID, parking.getId());
                        contentValues.put(ParkingModel.CN_TYPE, parking.getType());
                        contentValues.put(ParkingModel.CN_LAT, parking.getLat());
                        contentValues.put(ParkingModel.CN_LNG, parking.getLng());
                        contentValues.put(ParkingModel.CN_IMG_URL, parking.getPhotoUrl());
                        contentValues.put(ParkingModel.CN_DESC, parking.getDescription());
                        DatabaseParams.Insert insert = new DatabaseParams.Insert();
                        insert.values = contentValues;
                        insert.table = ParkingModel.T_NAME;
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

}
