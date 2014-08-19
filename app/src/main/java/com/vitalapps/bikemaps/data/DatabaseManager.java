package com.vitalapps.bikemaps.data;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.atomic.AtomicInteger;

import static com.vitalapps.bikemaps.utils.LogUtils.LOGD;
import static com.vitalapps.bikemaps.utils.LogUtils.makeLogTag;

public class DatabaseManager {

    private static final String TAG = makeLogTag(DatabaseManager.class);

    private AtomicInteger mOpenCounter = new AtomicInteger();

    private static DatabaseManager instance;
    private SQLiteOpenHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    private DatabaseManager(SQLiteOpenHelper helper) {
        mDatabaseHelper = helper;
    }

    public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
        if (instance == null) {
            LOGD(TAG, "initialize db manager");
            instance = new DatabaseManager(helper);
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return instance;
    }

    private synchronized SQLiteDatabase openDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
            LOGD(TAG, "Opening new writable database");
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized SQLiteDatabase openReadableDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
            LOGD(TAG, "Opening new readable database");
            mDatabase = mDatabaseHelper.getReadableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0) {
            LOGD(TAG, "Close database");
            mDatabase.close();

        }
    }

    public void executeQuery(QueryExecutor executor) {
        LOGD(TAG, "Execute query");
        SQLiteDatabase database = openDatabase();
        executor.run(database);
        closeDatabase();
    }

    public void executeQueryAsync(final QueryExecutor executor) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LOGD(TAG, "Execute async query");
                SQLiteDatabase database = openDatabase();
                executor.run(database);
                closeDatabase();
            }
        }).start();
    }
}
