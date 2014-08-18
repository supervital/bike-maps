package com.vitalapps.bikemaps.processor;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.vitalapps.bikemaps.api.VolleyRequestManager;
import com.vitalapps.bikemaps.service.AppServiceHelper;
import com.vitalapps.bikemaps.service.ServiceListener;

import org.json.JSONObject;

public class ParkingProcessor extends BaseProcessor {

    private Context mContext;
    private Request mParkingRequest;

    public ParkingProcessor(Context context) {
        mContext = context;
    }

    public ParkingProcessor(Parcel in) {
        super(in);
    }

    @Override
    public void executeProcess(final ServiceListener listener) {
        mParkingRequest = VolleyRequestManager.getInstance().doVolleyRequest().getParking(mContext, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getBundle().putInt(BUNDLE_PROCESS_STATUS, PROCESS_FINISHED);
                        listener.onProcessFinished(AppServiceHelper.PROCESS_PARKING, getBundle());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getBundle().putInt(BUNDLE_PROCESS_STATUS, PROCESS_FAIL);
                        listener.onProcessFinished(AppServiceHelper.PROCESS_PARKING, getBundle());
                    }
                }
        );
    }

    @Override
    public void cancelProcess() {
        mParkingRequest.cancel();
    }

    public static final Parcelable.Creator<ParkingProcessor> CREATOR = new Parcelable.Creator<ParkingProcessor>() {
        public ParkingProcessor createFromParcel(Parcel in) {
            return new ParkingProcessor(in);
        }

        public ParkingProcessor[] newArray(int size) {
            return new ParkingProcessor[size];
        }
    };
}
