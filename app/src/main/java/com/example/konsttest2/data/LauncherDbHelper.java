package com.example.konsttest2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class LauncherDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Launcher.db";

    private static final String SQL_CREATE_TABLE_APPINFO =
            "CREATE TABLE " + AppInfoContract.AppInfoEntry.TABLE_NAME + " (" +
                    AppInfoContract.AppInfoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                    AppInfoContract.AppInfoEntry.COLUMN_NAME_TITLE + " TEXT," +
                    AppInfoContract.AppInfoEntry.COLUMN_NAME_COUNT + " INTEGER);";
    private static final String SQL_CREATE_TABLE_LINK =
             "CREATE TABLE " + DesktopItemContract.LinkEntry.TABLE_NAME + " (" +
                    DesktopItemContract.LinkEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                    DesktopItemContract.LinkEntry.COLUMN_NAME_TITLE + " TEXT," +
                    DesktopItemContract.LinkEntry.COLUMN_NAME_LINK + " TEXT," +
                    DesktopItemContract.LinkEntry.COLUMN_NAME_X + " INTEGER," +
                    DesktopItemContract.LinkEntry.COLUMN_NAME_Y + " INTEGER," +
                    DesktopItemContract.LinkEntry.COLUMN_NAME_TYPE + " INTEGER);";

    private static final String SQL_DELETE_TABLE_APPINFO =
            "DROP TABLE IF EXISTS " + AppInfoContract.AppInfoEntry.TABLE_NAME + "; ";
    private static final String SQL_DELETE_TABLE_LINK =
            "DROP TABLE IF EXISTS " + DesktopItemContract.LinkEntry.TABLE_NAME + ";";

    public LauncherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_APPINFO);
        db.execSQL(SQL_CREATE_TABLE_LINK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE_APPINFO);
        db.execSQL(SQL_DELETE_TABLE_LINK);
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
            database.close();
            return output;
        } else {
            database.close();
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
            database.close();
        }
    }

    public DesktopItem getDesktopItemByXAndY(int x, int y) {
        DesktopItem desktopItem = null;
        String selectQuery = "SELECT  * FROM " + DesktopItemContract.LinkEntry.TABLE_NAME +
                " WHERE " + DesktopItemContract.LinkEntry.COLUMN_NAME_X + " = " + x +
                " AND " + DesktopItemContract.LinkEntry.COLUMN_NAME_Y + "=" + y + ";";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor c = database.rawQuery(selectQuery, null);
        if (c != null && c.moveToFirst()) {
            desktopItem = new DesktopItem()
                    .setId((c.getInt(c.getColumnIndex(DesktopItemContract.LinkEntry._ID))))
                    .setTitle(c.getString(c.getColumnIndex(DesktopItemContract.LinkEntry.COLUMN_NAME_TITLE)))
                    .setLink(c.getString(c.getColumnIndex(DesktopItemContract.LinkEntry.COLUMN_NAME_LINK)))
                    .setX(c.getInt(c.getColumnIndex(DesktopItemContract.LinkEntry.COLUMN_NAME_X)))
                    .setY(c.getInt(c.getColumnIndex(DesktopItemContract.LinkEntry.COLUMN_NAME_Y)))
                    .setType(c.getInt(c.getColumnIndex(DesktopItemContract.LinkEntry.COLUMN_NAME_TYPE)));
        }
        c.close();
        database.close();
        return desktopItem;
    }

    public void saveDesktopItemAndDeleteOld(DesktopItem desktopItem) {
        DesktopItem oldDesktopItem = getDesktopItemByXAndY(desktopItem.getX(), desktopItem.getY());
        if(oldDesktopItem != null) {
            deleteDesktopItem(oldDesktopItem.getId());
        }
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DesktopItemContract.LinkEntry.COLUMN_NAME_TITLE, desktopItem.getTitle());
        values.put(DesktopItemContract.LinkEntry.COLUMN_NAME_LINK, desktopItem.getLink());
        values.put(DesktopItemContract.LinkEntry.COLUMN_NAME_X, desktopItem.getX());
        values.put(DesktopItemContract.LinkEntry.COLUMN_NAME_Y, desktopItem.getY());
        values.put(DesktopItemContract.LinkEntry.COLUMN_NAME_TYPE, desktopItem.getType());
        desktopItem.setId(database.insert(DesktopItemContract.LinkEntry.TABLE_NAME, null, values));
        database.close();
    }

    public List<DesktopItem> getAllDesktopItems() {
        List<DesktopItem> desktopItems = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DesktopItemContract.LinkEntry.TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor c = database.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                DesktopItem desktopItem = new DesktopItem();
                desktopItem
                        .setTitle(c.getString(c.getColumnIndex(DesktopItemContract.LinkEntry.COLUMN_NAME_TITLE)))
                        .setLink(c.getString(c.getColumnIndex(DesktopItemContract.LinkEntry.COLUMN_NAME_LINK)))
                        .setX(c.getInt(c.getColumnIndex(DesktopItemContract.LinkEntry.COLUMN_NAME_X)))
                        .setY(c.getInt(c.getColumnIndex(DesktopItemContract.LinkEntry.COLUMN_NAME_Y)))
                        .setType(c.getInt(c.getColumnIndex(DesktopItemContract.LinkEntry.COLUMN_NAME_TYPE)));
                desktopItems.add(desktopItem);
            } while (c.moveToNext());
        }
        c.close();
        database.close();
        return desktopItems;
    }

    public void deleteDesktopItem(long id) {
        Log.d("Konst", "delete desktop item, id = " + id);
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(
                DesktopItemContract.LinkEntry.TABLE_NAME,
                DesktopItemContract.LinkEntry._ID + " = ?",
                new String[] {String.valueOf(id)}
                );
        database.close();
    }

}
