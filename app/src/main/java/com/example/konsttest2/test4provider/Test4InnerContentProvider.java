package com.example.konsttest2.test4provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Test4InnerContentProvider extends ContentProvider {

    private static final int URI_MATCH_COUNT = 1;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final String COUNT_PREFERENCE_KEY = "startCount";

    static {
        uriMatcher.addURI("startCount", StartCountContract.StartCountEntry.TABLE_NAME, URI_MATCH_COUNT);
    }

    @Override
    public boolean onCreate() {
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
            case URI_MATCH_COUNT:
                final MatrixCursor matrixCursor =
                        new MatrixCursor(
                                new String[]{StartCountContract.StartCountEntry.COLUMN_NAME_COUNT}
                        );
                matrixCursor.addRow(
                        new Integer[] {
                                PreferenceManager
                                        .getDefaultSharedPreferences(getContext())
                                        .getInt(COUNT_PREFERENCE_KEY, 0)
                        }
                );
                return matrixCursor;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException();
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(
            @NonNull Uri uri,
            @Nullable String selection,
            @Nullable String[] selectionArgs
    ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(
            @NonNull Uri uri,
            @Nullable ContentValues values,
            @Nullable String selection,
            @Nullable String[] selectionArgs
    ) {
        switch (uriMatcher.match(uri)) {
            case URI_MATCH_COUNT:
                final Integer count = values
                        .getAsInteger(StartCountContract.StartCountEntry.COLUMN_NAME_COUNT);
                PreferenceManager
                        .getDefaultSharedPreferences(getContext()).edit()
                        .putInt(COUNT_PREFERENCE_KEY, count)
                        .apply();
                return 1;
            default:
                throw new IllegalArgumentException();
        }
    }
}
