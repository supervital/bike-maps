package com.vitalapps.bikemaps.api.request;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class RequestApi {
    private RequestQueue mRequestQueue;

    public RequestApi(Context context) {
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public void login(Response.Listener<JSONObject> listener,
                      Response.ErrorListener errorListener) {
    }

}
