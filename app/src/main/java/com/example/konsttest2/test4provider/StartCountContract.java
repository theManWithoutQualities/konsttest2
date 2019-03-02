package com.example.konsttest2.test4provider;

import android.provider.BaseColumns;

public final class StartCountContract {

    private StartCountContract() {
    }

    public static class StartCountEntry implements BaseColumns {
        public static final String TABLE_NAME = "start_count";
        public static final String COLUMN_NAME_COUNT = "count";
    }
}
