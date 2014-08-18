package com.vitalapps.bikemaps.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.vitalapps.bikemaps.api.request.ParkingRequest;

import org.json.JSONObject;

public class RequestApi {

    public static final String PARKING_TAG = "parking";

    private RequestQueue mRequestQueue;

    public RequestApi(Context context) {
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public Request getParking(Context context, Response.Listener<JSONObject> listener,
                           Response.ErrorListener errorListener) {
        return mRequestQueue.add((new ParkingRequest(context, Method.GET, Api.getParkingList(), listener, errorListener)).setTag(PARKING_TAG));
    }

}
