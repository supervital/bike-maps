package com.vitalapps.bikemaps.api;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vitalapps.bikemaps.api.request.ParkingRequest;

import org.json.JSONArray;

import java.util.Map;

public class RequestApi {

    public static final String PARKING_TAG = "parking";
    public static final String ADD_PARKING_TAG = "add_parking";

    private RequestQueue mRequestQueue;

    public RequestApi(Context context) {
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public Request getParking(Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        return mRequestQueue.add((new ParkingRequest(listener, errorListener)).setTag(PARKING_TAG));
    }

    public Request postParking(Response.Listener<String> listener, Response.ErrorListener errorListener, final Map<String, String> params) {
        return mRequestQueue.add(new StringRequest(Request.Method.POST, "url", listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        }).setTag(ADD_PARKING_TAG);
    }

}
