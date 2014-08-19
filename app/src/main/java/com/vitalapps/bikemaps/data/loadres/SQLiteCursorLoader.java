package com.vitalapps.bikemaps.data.loadres;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.vitalapps.bikemaps.data.AppSQLiteOpenHelper;
import com.vitalapps.bikemaps.data.DatabaseManager;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;

import static com.vitalapps.bikemaps.utils.LogUtils.LOGD;
import static com.vitalapps.bikemaps.utils.LogUtils.makeLogTag;

public class SQLiteCursorLoader extends AbstractCursorLoader {

    private static final String TAG = makeLogTag("SQLiteCursorLoader");

    public static final int LOADER_PARKING = 1;

    SQLiteOpenHelper db = null;
    String rawQuery = null;
    String[] args = null;

    /**
     * @param context
     * @param rawQuery
     * @param args
     */
    public SQLiteCursorLoader(Context context, String rawQuery, String[] args) {
        super(context);
        LOGD(TAG, "Constructor rawQuery - " + rawQuery + ". args - " + args);
        this.db = new AppSQLiteOpenHelper(context);
        this.rawQuery = rawQuery;
        this.args = args;
    }

    /**
     * Runs on a worker thread and performs the actual
     * database query to retrieve the Cursor.
     */
    @Override
    protected Cursor buildCursor() {
        LOGD(TAG, "buildCursor");
        return (DatabaseManager.getInstance().openReadableDatabase().rawQuery(rawQuery, args));
    }

    /**
     * Writes a semi-user-readable roster of contents to
     * supplied output.
     */
    @Override
    public void dump(String prefix, FileDescriptor fd,
                     PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
        LOGD(TAG, "dump");
        writer.print(prefix);
        writer.print("rawQuery=");
        writer.println(rawQuery);
        writer.print(prefix);
        writer.print("args=");
        writer.println(Arrays.toString(args));
    }

    @Override
    protected void onAbandon() {
        super.onAbandon();
        LOGD(TAG, "onAbandon. Close database");
        DatabaseManager.getInstance().closeDatabase();
    }

    @Override
    protected void onReset() {
        super.onReset();
        LOGD(TAG, "onReset. Close database");
        DatabaseManager.getInstance().closeDatabase();
    }
}
