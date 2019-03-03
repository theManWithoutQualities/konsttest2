package com.example.konsttest2.profileprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.konsttest2.R;

public class ProfileProvider extends ContentProvider {

    private static final int URI_MATCH_GET_PROFILE_INFO = 1;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(
                ProfileContract.AUTHORITY,
                ProfileContract.Profile.GET_PROFILE_INFO,
                URI_MATCH_GET_PROFILE_INFO
        );
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
            case URI_MATCH_GET_PROFILE_INFO:
                final String name = getContext().getResources().getString(R.string.name);
                final String phone = getContext().getResources().getString(R.string.phone);
                final String mail = getContext().getResources().getString(R.string.mail);
                final String vk = getContext().getResources().getString(R.string.vk);
                final String fb = getContext().getResources().getString(R.string.fb);
                final String git = getContext().getResources().getString(R.string.git);
                final String location = getContext().getResources().getString(R.string.location);
                final MatrixCursor matrixCursor =
                        new MatrixCursor(
                                new String[]{
                                        ProfileContract.Profile._ID,
                                        ProfileContract.Profile.COLUMN_NAME_NAME,
                                        ProfileContract.Profile.COLUMN_NAME_PHONE,
                                        ProfileContract.Profile.COLUMN_NAME_MAIL,
                                        ProfileContract.Profile.COLUMN_NAME_VK,
                                        ProfileContract.Profile.COLUMN_NAME_FB,
                                        ProfileContract.Profile.COLUMN_NAME_GIT,
                                        ProfileContract.Profile.COLUMN_NAME_LOCATION
                                }
                        );
                matrixCursor.addRow(new Object[] {0, name, phone, mail, vk, fb, git, location});
                return matrixCursor;
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
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
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
        return 0;
    }
}
