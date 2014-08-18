package com.vitalapps.bikemaps.processor;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.vitalapps.bikemaps.service.ServiceListener;

public abstract class BaseProcessor implements Parcelable {

    public static final String BUNDLE_PROCESS_STATUS = "process_status";
    public static final String BUNDLE_ERROR_MSG = "error_msg";

    public static final int PROCESS_IN_PROGRESS = 0;
    public static final int PROCESS_FINISHED = 1;
    public static final int PROCESS_CANCELED = 2;
    public static final int PROCESS_STARTED = 3;
    public static final int PROCESS_FAIL = 4;
    public static final int PROCESS_SUCCESS = 5;

    private int mProcessStatus;
    private Bundle mBundle;

    public BaseProcessor(Parcel in) {
        mProcessStatus = in.readInt();
        mBundle = in.readBundle();
    }

    protected BaseProcessor() {
    }

    public Bundle getBundle() {
        if (mBundle == null) {
            mBundle = new Bundle();
        }
        return mBundle;
    }

    public int getProcessStatus() {
        return mProcessStatus;
    }

    protected void setProcessStatus(int processStatus) {
        mProcessStatus = processStatus;
    }

    public abstract void executeProcess(ServiceListener listener);

    public abstract void cancelProcess();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mProcessStatus);
        dest.writeBundle(mBundle);
    }

}
