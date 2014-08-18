package com.vitalapps.bikemaps.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.SparseArray;

import com.vitalapps.bikemaps.app.App;
import com.vitalapps.bikemaps.processor.BaseProcessor;

import static com.vitalapps.bikemaps.utils.LogUtils.LOGD;
import static com.vitalapps.bikemaps.utils.LogUtils.makeLogTag;

public class AppService extends Service {

    private static final String TAG = makeLogTag("AppService");

    public static final String ACTION_EXECUTE_PROCESS = App.PACKAGE
            .concat(".ACTION_EXECUTE_PROCESS");

    public static final String ACTION_CANCEL_PROCESS = App.PACKAGE
            .concat(".ACTION_CANCEL_PROCESS");

    public static final String EXTRA_PROCESS = App.PACKAGE
            .concat(".process");

    public static final String EXTRA_PROCESS_ID = App.PACKAGE
            .concat(".process_id");

    private AppBinder mBinder = new AppBinder();

    private SparseArray<BaseProcessor> mProcessors = new SparseArray<BaseProcessor>();

    @Override
    public IBinder onBind(Intent intent) {
        LOGD(TAG, "onBind");
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        LOGD(TAG, "onRebind");
        if (mProcessors != null && mProcessors.size() > 0) {
            int processId = 0;
            for (int i = 0; i < mProcessors.size(); i++) {
                processId = mProcessors.keyAt(i);
                BaseProcessor process = mProcessors.get(processId);
                notifyListener(processId, process.getBundle());
            }
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LOGD(TAG, "onStartCommand");
        String action = intent.getAction();
        int processId = getProcessId(intent.getExtras());

        if (!TextUtils.isEmpty(action) && ACTION_EXECUTE_PROCESS.equals(action)) {
            int processStatus = getProcessStatus(processId);
            if (processStatus == BaseProcessor.PROCESS_FINISHED) {
                // TODO: Notify the listeners.
                LOGD(TAG,
                        "The process ["
                                + Integer.toString(processId)
                                + "] is finished. The listeners have not been notified."
                );
            } else if (processStatus == BaseProcessor.PROCESS_IN_PROGRESS) {
                // TODO: Notify the listeners.
                LOGD(TAG, "The process [" + Integer.toString(processId)
                        + "] is in progress");
            } else if (processStatus == BaseProcessor.PROCESS_CANCELED) {
                LOGD(TAG, "The process [" + Integer.toString(processId)
                        + "] is canceled");
            } else {
                // Process have not been started. Start the process
                BaseProcessor processor = getProcessor(intent.getExtras());
                mProcessors.append(processId, processor);
                processor.executeProcess(mProcessorListener);
                LOGD(TAG,
                        "Execute the process [" + Integer.toString(processId)
                                + "]"
                );
            }

        } else if (!TextUtils.isEmpty(action)
                && ACTION_CANCEL_PROCESS.equals(action)) {
            LOGD(TAG,
                    "Cancel the process id = " + Integer.toString(processId));
            // TODO: Cancel the process
            BaseProcessor processor = mProcessors.get(processId);
            if (processor != null) {
                processor.cancelProcess();
                mProcessors.remove(processId);
            }
        }
        return START_NOT_STICKY;
    }


    private int getProcessStatus(int processorId) {
        BaseProcessor processor = mProcessors.get(processorId);
        if (processor == null) {
            return BaseProcessor.PROCESS_STARTED;
        }
        if (processor.getProcessStatus() == BaseProcessor.PROCESS_IN_PROGRESS) {
            // Process is in process
            return BaseProcessor.PROCESS_IN_PROGRESS;
        } else if (processor.getProcessStatus() == BaseProcessor.PROCESS_FINISHED) {
            // Process is finished and listeners have not been notified.
            return BaseProcessor.PROCESS_FINISHED;
        } else if (processor.getProcessStatus() == BaseProcessor.PROCESS_CANCELED) {
            // Process have been finished
            return BaseProcessor.PROCESS_CANCELED;
        }
        return -1;
    }


    private int getProcessId(Bundle args) {
        if (args != null) {
            return args.getInt(EXTRA_PROCESS_ID, -1);
        }
        return -1;
    }

    private BaseProcessor getProcessor(Bundle args) {
        if (args != null) {
            return args.getParcelable(EXTRA_PROCESS);
        }
        return null;
    }

    private void removeProcessFromList(int processId) {
        mProcessors.remove(processId);
        if (mProcessors.size() == 0) {
            LOGD(TAG, "Stop the SERVICE");
            stopSelf();
        }
    }

    private void notifyListener(int processId, Bundle args) {
        boolean isNotified = mBinder.notifyListeners(processId, args);
        if (isNotified) {
            // Callback is received
            removeProcessFromList(processId);
        }
    }

    ServiceListener mProcessorListener = new ServiceListener() {
        @Override
        public void onProcessFinished(int processId, Bundle args) {
            notifyListener(processId, args);
        }
    };

    /**
     * Lifecycle
     */

    @Override
    public void onCreate() {
        LOGD(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LOGD(TAG, "onDestroy");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LOGD(TAG, "onUnbind");
        return true;
    }

}
