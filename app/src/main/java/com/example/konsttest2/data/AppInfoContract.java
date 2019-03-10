package com.example.konsttest2.data;

import android.provider.BaseColumns;

public final class AppInfoContract {

    private AppInfoContract() {}

    public static class AppInfoEntry implements BaseColumns {
        public static final String TABLE_NAME = "appinfo";
        public static final String COLUMN_NAME_PACKAGE_NAME = "package_name";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_COUNT = "count";
        public static final String COLUMN_NAME_DATE = "date";
    }
}
