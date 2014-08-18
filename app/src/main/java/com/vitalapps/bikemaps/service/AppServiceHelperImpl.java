package com.vitalapps.bikemaps.service;

import android.content.Context;
import android.content.Intent;

import com.vitalapps.bikemaps.processor.BaseProcessor;
import com.vitalapps.bikemaps.processor.ExampleProcessor;
import com.vitalapps.bikemaps.processor.ParkingProcessor;

public class AppServiceHelperImpl implements AppServiceHelper {

    private static AppServiceHelperImpl instance;
    private Context mContext;

    public AppServiceHelperImpl(Context context) {
        mContext = context;
    }

    // This method should be called first to do singleton initialization
    public static synchronized AppServiceHelperImpl getInstance(Context context) {
        if (instance == null) {
            instance = new AppServiceHelperImpl(context);
        }
        return instance;
    }

    public static synchronized AppServiceHelperImpl getInstance() {
        if (instance == null) {
            throw new IllegalStateException(AppServiceHelperImpl.class.getSimpleName() +
                    " is not initialized, call getInstance(context) method first.");
        }
        return instance;
    }


    @Override
    public void cancelProcess(int processId) {
        Intent intent = createCancelableServiceIntent(processId);
        startService(intent);
    }

    @Override
    public int exampleProcess() {
        Intent intent = createExecutableServiceIntent(new ExampleProcessor(),
                PROCESS_EXAMPLE);
        startService(intent);
        return PROCESS_EXAMPLE;
    }

    @Override
    public int parkingProcess() {
        Intent intent = createExecutableServiceIntent(new ParkingProcessor(),
                PROCESS_PARKING);
        startService(intent);
        return PROCESS_PARKING;
    }

    private void startService(Intent intent) {
        mContext.startService(intent);
    }

    private Intent createCancelableServiceIntent(int processId) {
        Intent intent = new Intent(mContext, AppService.class);
        intent.setAction(AppService.ACTION_CANCEL_PROCESS);
        intent.putExtra(AppService.EXTRA_PROCESS_ID, processId);
        return intent;
    }

    private Intent createExecutableServiceIntent(BaseProcessor baseProcess,
                                                 int processId) {
        Intent intent = new Intent(mContext, AppService.class);
        intent.setAction(AppService.ACTION_EXECUTE_PROCESS);
        intent.putExtra(AppService.EXTRA_PROCESS, baseProcess);
        intent.putExtra(AppService.EXTRA_PROCESS_ID, processId);
        return intent;
    }
}
