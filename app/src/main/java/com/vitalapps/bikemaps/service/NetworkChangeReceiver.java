package com.vitalapps.bikemaps.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import com.vitalapps.bikemaps.utils.LogUtils;
import com.vitalapps.bikemaps.utils.Utils;

import static com.vitalapps.bikemaps.utils.LogUtils.LOGD;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final String TAG = LogUtils.makeLogTag("NetworkChangeReceiver");

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            LOGD(TAG, "WIFI_STATE_CHANGED");
            if (Utils.isNetworkAvailable(context)) {

            }
        }


    }
}
