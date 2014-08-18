package com.vitalapps.bikemaps.service;

import android.os.Binder;
import android.os.Bundle;
import android.util.SparseArray;

import static com.vitalapps.bikemaps.utils.LogUtils.LOGD;
import static com.vitalapps.bikemaps.utils.LogUtils.makeLogTag;

public class AppBinder extends Binder {

    private static final String TAG = makeLogTag("AppBinder");

    private SparseArray<ServiceListener> mServiceListeners;

    public void addServiceListener(int processId, ServiceListener serviceListener) {
        if (mServiceListeners == null) {
            mServiceListeners = new SparseArray<ServiceListener>();
        }
        mServiceListeners.put(processId, serviceListener);
        LOGD(TAG, "Listener with key " + Integer.toString(processId) + " added");
    }

    public void removeServiceListener(int processId) {
        if (mServiceListeners != null && mServiceListeners.get(processId) != null) {
            mServiceListeners.remove(processId);
            LOGD(TAG, "Listener with key " + Integer.toString(processId)
                    + " removed");
        }
    }

    public void removeAllServiceListeners() {
        if (mServiceListeners != null && mServiceListeners.size() > 0) {
            mServiceListeners.clear();
            LOGD(TAG, "Removed all listeners");
        }
    }

    public boolean notifyListeners(int processId, Bundle args) {
        if (mServiceListeners != null && mServiceListeners.get(processId) != null) {
            mServiceListeners.get(processId).onProcessFinished(processId, args);
            mServiceListeners.remove(processId);
            LOGD(TAG, "Notify a listener with key " + Integer.toString(processId));
            return true;
        } else {
            return false;
        }
    }

}
