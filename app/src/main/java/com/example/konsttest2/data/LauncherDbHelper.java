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
             "CREATE TABLE " + LinkContract.LinkEntry.TABLE_NAME + " (" +
                    LinkContract.LinkEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                    LinkContract.LinkEntry.COLUMN_NAME_TITLE + " TEXT," +
                    LinkContract.LinkEntry.COLUMN_NAME_WEBLINK + " INTEGER," +
                    LinkContract.LinkEntry.COLUMN_NAME_X + " INTEGER," +
                    LinkContract.LinkEntry.COLUMN_NAME_Y + " INTEGER);";

    private static final String SQL_DELETE_TABLE_APPINFO =
            "DROP TABLE IF EXISTS " + AppInfoContract.AppInfoEntry.TABLE_NAME + "; ";
    private static final String SQL_DELETE_TABLE_LINK =
            "DROP TABLE IF EXISTS " + LinkContract.LinkEntry.TABLE_NAME + ";";

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
            database.close();
        }
    }

    public Link getLinkByXAndY(int x, int y) {
        Link link = null;
        String selectQuery = "SELECT  * FROM " + LinkContract.LinkEntry.TABLE_NAME +
                " WHERE " + LinkContract.LinkEntry.COLUMN_NAME_X + " = " + x +
                " AND " + LinkContract.LinkEntry.COLUMN_NAME_Y + "=" + y + ";";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor c = database.rawQuery(selectQuery, null);
        if (c != null && c.moveToFirst()) {
            link = new Link()
                    .setId((c.getInt(c.getColumnIndex(LinkContract.LinkEntry._ID))))
                    .setTitle(c.getString(c.getColumnIndex(LinkContract.LinkEntry.COLUMN_NAME_TITLE)))
                    .setWeblink(c.getString(c.getColumnIndex(LinkContract.LinkEntry.COLUMN_NAME_WEBLINK)))
                    .setX(c.getInt(c.getColumnIndex(LinkContract.LinkEntry.COLUMN_NAME_X)))
                    .setY(c.getInt(c.getColumnIndex(LinkContract.LinkEntry.COLUMN_NAME_Y)));
        }
        c.close();
        database.close();
        return link;
    }

    public void saveLinkAndDeleteOld(Link link) {
        Link oldLink = getLinkByXAndY(link.getX(), link.getY());
        if(oldLink != null) {
            deleteLink(oldLink.getId());
        }
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LinkContract.LinkEntry.COLUMN_NAME_TITLE, link.getTitle());
        values.put(LinkContract.LinkEntry.COLUMN_NAME_WEBLINK, link.getWeblink());
        values.put(LinkContract.LinkEntry.COLUMN_NAME_X, link.getX());
        values.put(LinkContract.LinkEntry.COLUMN_NAME_Y, link.getY());
        link.setId(database.insert(LinkContract.LinkEntry.TABLE_NAME, null, values));
        database.close();
    }

    public List<Link> getAllLinks() {
        List<Link> links = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + LinkContract.LinkEntry.TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor c = database.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Link link = new Link();
                link.setTitle(c.getString(c.getColumnIndex(LinkContract.LinkEntry.COLUMN_NAME_TITLE)));
                link.setWeblink(c.getString(c.getColumnIndex(LinkContract.LinkEntry.COLUMN_NAME_WEBLINK)));
                link.setX(c.getInt(c.getColumnIndex(LinkContract.LinkEntry.COLUMN_NAME_X)));
                link.setY(c.getInt(c.getColumnIndex(LinkContract.LinkEntry.COLUMN_NAME_Y)));
                links.add(link);
            } while (c.moveToNext());
        }
        c.close();
        database.close();
        return links;
    }

    public void deleteLink(long id) {
        Log.d("Konst", "delete link, id = " + id);
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(
                LinkContract.LinkEntry.TABLE_NAME,
                LinkContract.LinkEntry._ID + " = ?",
                new String[] {String.valueOf(id)}
                );
        database.close();
    }

}
