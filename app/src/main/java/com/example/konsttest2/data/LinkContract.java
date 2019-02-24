package com.example.konsttest2.data;

import android.provider.BaseColumns;

public final class LinkContract {

    private LinkContract() {}

    public static class LinkEntry implements BaseColumns {
        public static final String TABLE_NAME = "link";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_WEBLINK = "weblink";
        public static final String COLUMN_NAME_X = "x";
        public static final String COLUMN_NAME_Y = "y";
    }
}
