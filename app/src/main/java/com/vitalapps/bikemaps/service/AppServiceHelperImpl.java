package com.vitalapps.bikemaps.service;

import android.app.Application;
import android.content.Intent;

import com.vitalapps.bikemaps.processor.BaseProcessor;
import com.vitalapps.bikemaps.processor.ExampleProcessor;
import com.vitalapps.bikemaps.processor.ParkingProcessor;

public class AppServiceHelperImpl implements AppServiceHelper {

    private Application mApp;

    public AppServiceHelperImpl(Application app) {
        mApp = app;
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
        Intent intent = createExecutableServiceIntent(new ParkingProcessor(mApp),
                PROCESS_PARKING);
        startService(intent);
        return PROCESS_PARKING;
    }

    private void startService(Intent intent) {
        mApp.startService(intent);
    }

    private Intent createCancelableServiceIntent(int processId) {
        Intent intent = new Intent(mApp, AppService.class);
        intent.setAction(AppService.ACTION_CANCEL_PROCESS);
        intent.putExtra(AppService.EXTRA_PROCESS_ID, processId);
        return intent;
    }

    private Intent createExecutableServiceIntent(BaseProcessor baseProcess,
                                                 int processId) {
        Intent intent = new Intent(mApp, AppService.class);
        intent.setAction(AppService.ACTION_EXECUTE_PROCESS);
        intent.putExtra(AppService.EXTRA_PROCESS, baseProcess);
        intent.putExtra(AppService.EXTRA_PROCESS_ID, processId);
        return intent;
    }
}
