package com.example.konsttest2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AppInfo.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + AppInfoContract.AppInfoEntry.TABLE_NAME + " (" +
                    AppInfoContract.AppInfoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                    AppInfoContract.AppInfoEntry.COLUMN_NAME_TITLE + " TEXT," +
                    AppInfoContract.AppInfoEntry.COLUMN_NAME_COUNT + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AppInfoContract.AppInfoEntry.TABLE_NAME;

    public AppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public Integer getCount(String title) {
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(
                AppInfoContract.AppInfoEntry.TABLE_NAME,
                new String[] {AppInfoContract.AppInfoEntry.COLUMN_NAME_COUNT},
                AppInfoContract.AppInfoEntry.COLUMN_NAME_TITLE + " = ?",
                new String[] {title},
                null,
                null,
                null,
                null
        );
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
            int output = cursor.getInt(0);
            return output;
        } else {
            return null;
        }
    }

    public void addClick(String title) {
        Integer count = getCount(title);
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (count == null) {
            values.put(AppInfoContract.AppInfoEntry.COLUMN_NAME_TITLE, title);
            values.put(AppInfoContract.AppInfoEntry.COLUMN_NAME_COUNT, 1);
            database.insert(AppInfoContract.AppInfoEntry.TABLE_NAME, null, values);
            database.close();

        } else {
            values.put(AppInfoContract.AppInfoEntry.COLUMN_NAME_COUNT, ++ count);
            database.update(
                    AppInfoContract.AppInfoEntry.TABLE_NAME,
                    values,
                    AppInfoContract.AppInfoEntry.COLUMN_NAME_TITLE + " = ?",
                    new String[] {title}
            );
        }
    }


}
