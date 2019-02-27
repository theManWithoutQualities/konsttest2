package com.example.konsttest2.data;

import android.provider.BaseColumns;

public final class DesktopItemContract {

    private DesktopItemContract() {}

    public static class LinkEntry implements BaseColumns {
        public static final String TABLE_NAME = "desktop_item";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_LINK = "link";
        public static final String COLUMN_NAME_X = "x";
        public static final String COLUMN_NAME_Y = "y";
        public static final String COLUMN_NAME_TYPE = "type";
    }
}
