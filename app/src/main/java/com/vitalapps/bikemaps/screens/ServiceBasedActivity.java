package com.vitalapps.bikemaps.screens;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.vitalapps.bikemaps.service.AppBinder;
import com.vitalapps.bikemaps.service.AppService;
import com.vitalapps.bikemaps.service.ServiceListener;

import java.util.ArrayList;

import static com.vitalapps.bikemaps.utils.LogUtils.LOGD;

public abstract class ServiceBasedActivity extends BaseActivity {

    private static final String TAG = "ServiceBasedActivity";
    private static final String EXTRA_QUEUE = "EXTRA_QUEUE";

    private ArrayList<Integer> mListenerQueue;

    public AppBinder mBinder;
    public String mClassHash;
    public boolean mIsBound;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(EXTRA_QUEUE, mListenerQueue);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mListenerQueue = savedInstanceState.getIntegerArrayList(EXTRA_QUEUE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mClassHash = "[" + Integer.toString(this.hashCode()) + "]";
        if (savedInstanceState != null) {
            mListenerQueue = savedInstanceState
                    .getIntegerArrayList(EXTRA_QUEUE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LOGD(TAG, mClassHash + "onPause");
        if (mBinder != null) {
            mBinder.removeAllServiceListeners();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, AppService.class);
        intent.setAction("TEST ACTION - Service binded");
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        LOGD(TAG, mClassHash + "Service binded");

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mIsBound) {
            unbindService(mConnection);
            LOGD(TAG, mClassHash + "Service unbinded");
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            LOGD(TAG, mClassHash + "onServiceConnected");
            mBinder = (AppBinder) binder;
            mIsBound = true;
            if (mListenerQueue != null && mListenerQueue.size() > 0 && getServiceListener()!= null) {
                for (Integer processId : mListenerQueue) {
                    mBinder.addServiceListener(processId, getServiceListener());
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            LOGD(TAG, mClassHash + "onServiceDisconnected");
            mIsBound = false;
        }
    };

    public void addListenerToQueue(int processId) {
        if (mListenerQueue == null) {
            mListenerQueue = new ArrayList<Integer>();
        }
        if (!mListenerQueue.contains(processId)) {
            mListenerQueue.add(processId);
        }
    }

    public void removeListenerFromQueue(int processId) {
        if (mListenerQueue != null) {
            mListenerQueue.remove(processId);
        }
    }

    public abstract ServiceListener getServiceListener();
}
