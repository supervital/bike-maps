package com.vitalapps.bikemaps.processor;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.vitalapps.bikemaps.api.VolleyRequestManager;
import com.vitalapps.bikemaps.service.AppServiceHelper;
import com.vitalapps.bikemaps.service.ServiceListener;

import org.json.JSONArray;

import static com.vitalapps.bikemaps.utils.LogUtils.LOGD;
import static com.vitalapps.bikemaps.utils.LogUtils.makeLogTag;

public class ParkingProcessor extends BaseProcessor {

    private static final String TAG = makeLogTag("ParkingProc");

    private Request mParkingRequest;

    public ParkingProcessor() {
    }

    public ParkingProcessor(Parcel in) {
        super(in);
    }

    @Override
    public void executeProcess(final ServiceListener listener) {
        LOGD(TAG, "execute");
        setProcessStatus(PROCESS_STARTED);
        mParkingRequest = VolleyRequestManager.getInstance().doVolleyRequest().getParking(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                LOGD(TAG, "onResponse");
                setProcessStatus(PROCESS_FINISHED);
                getBundle().putInt(BUNDLE_PROCESS_STATUS, PROCESS_SUCCESS);
                listener.onProcessFinished(AppServiceHelper.PROCESS_PARKING, getBundle());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LOGD(TAG, "VolleyError " + error.getMessage());
                setProcessStatus(PROCESS_FINISHED);
                getBundle().putInt(BUNDLE_PROCESS_STATUS, PROCESS_FAIL);
                listener.onProcessFinished(AppServiceHelper.PROCESS_PARKING, getBundle());
            }
        });
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
