package com.vitalapps.bikemaps.data;

import android.database.sqlite.SQLiteDatabase;

public interface QueryExecutor {
    public void run(SQLiteDatabase database);
}
