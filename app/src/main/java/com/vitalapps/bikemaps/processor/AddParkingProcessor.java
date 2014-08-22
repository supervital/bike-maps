package com.vitalapps.bikemaps.processor;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.vitalapps.bikemaps.api.VolleyRequestManager;
import com.vitalapps.bikemaps.service.ServiceListener;

import static com.vitalapps.bikemaps.utils.LogUtils.*;

public class AddParkingProcessor extends BaseProcessor {

    private static final String TAG = makeLogTag("AddParkingPro");

    private Request mAddParkingRequest;

    public AddParkingProcessor(Parcel parcel) {
        super(parcel);
    }

    @Override
    public void executeProcess(ServiceListener listener) {
        LOGD(TAG, "execute the process");
        setProcessStatus(PROCESS_IN_PROGRESS);
        mAddParkingRequest = VolleyRequestManager.getInstance().doVolleyRequest().postParking(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                setProcessStatus(PROCESS_FINISHED);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setProcessStatus(PROCESS_FINISHED);
            }
        }, null);

    }

    @Override
    public void cancelProcess() {
        mAddParkingRequest.cancel();
    }

    public static final Parcelable.Creator<AddParkingProcessor> CREATOR = new Parcelable.Creator<AddParkingProcessor>() {
        public AddParkingProcessor createFromParcel(Parcel in) {
            return new AddParkingProcessor(in);
        }

        public AddParkingProcessor[] newArray(int size) {
            return new AddParkingProcessor[size];
        }
    };
}
