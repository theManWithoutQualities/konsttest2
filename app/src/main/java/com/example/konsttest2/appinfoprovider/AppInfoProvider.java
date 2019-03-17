package com.example.konsttest2.appinfoprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.konsttest2.data.LauncherDbHelper;

import static com.example.konsttest2.KonstTest2.TAG;
import static com.example.konsttest2.data.AppInfoContract.AppInfoEntry.COLUMN_NAME_COUNT;

public class AppInfoProvider extends ContentProvider {

    private LauncherDbHelper dbHelper;
    private static final int URI_MATCH_ALL_APP_INFO = 1;
    private static final int URI_MATCH_LAST_APP_INFO = 2;
    private static final int URI_MATCH_APP_INFO_UPDATE = 3;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(
                AppInfoContract.AUTHORITY,
                AppInfoContract.Info.ALL_APP_INFO,
                URI_MATCH_ALL_APP_INFO
        );
        uriMatcher.addURI(
                AppInfoContract.AUTHORITY,
                AppInfoContract.Info.LAST_APP_INFO,
                URI_MATCH_LAST_APP_INFO
        );
        uriMatcher.addURI(
                AppInfoContract.AUTHORITY,
                AppInfoContract.Info.APP_INFO_UPDATE,
                URI_MATCH_APP_INFO_UPDATE
        );
    }

    @Override
    public boolean onCreate() {
        dbHelper = new LauncherDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(
            @NonNull Uri uri,
            @Nullable String[] projection,
            @Nullable String selection,
            @Nullable String[] selectionArgs,
            @Nullable String sortOrder
    ) {
        switch (uriMatcher.match(uri)) {
            case URI_MATCH_ALL_APP_INFO:
                return dbHelper.getAllAppInfo();
            case URI_MATCH_LAST_APP_INFO:
                return dbHelper.getLastStartedAppInfo();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(
            @NonNull Uri uri,
            @Nullable ContentValues values
    ) {
        return null;
    }

    @Override
    public int delete(
            @NonNull Uri uri,
            @Nullable String selection,
            @Nullable String[] selectionArgs
    ) {
        return 0;
    }

    @Override
    public int update(
            @NonNull Uri uri,
            @Nullable ContentValues values,
            @Nullable String selection,
            @Nullable String[] selectionArgs
    ) {
        switch (uriMatcher.match(uri)) {
            case URI_MATCH_APP_INFO_UPDATE:
                Log.d(TAG, "update clicks");
                if(values.containsKey(COLUMN_NAME_COUNT)
                        && selectionArgs.length > 0 && TextUtils.isDigitsOnly(selectionArgs[0])) {
                    int clicks = values.getAsInteger(COLUMN_NAME_COUNT);
                    int id = Integer.parseInt(selectionArgs[0]);
                    return dbHelper.updateClicks(id, clicks);
                } else {
                    return 0;
                }
            default:
                return 0;
        }
    }
}
