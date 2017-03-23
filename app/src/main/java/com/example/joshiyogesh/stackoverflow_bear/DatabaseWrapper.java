package com.example.joshiyogesh.stackoverflow_bear;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Joshi Yogesh on 22/03/2017 at 17:00 IST.
 */
/*Database Wrapper class for Sqlite Database*/
public class DatabaseWrapper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseWrapper";
    private static final String DATABASE_NAME = "QuestionDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseWrapper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG, "Creating database [" + DATABASE_NAME + " v." + DATABASE_VERSION + "]...");
        db.execSQL(QuestionDatabase.SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +QuestionDatabase.TABLE_NAME);
        onCreate(db);
    }
}
