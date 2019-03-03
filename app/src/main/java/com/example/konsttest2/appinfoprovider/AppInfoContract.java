package com.example.konsttest2.appinfoprovider;

import android.provider.BaseColumns;

public final class AppInfoContract {

    private AppInfoContract() {
    }

    public static final String AUTHORITY = "com.example.konsttest2.info";
    public static class Info implements BaseColumns {
        public static String ALL_APP_INFO = "appsinfo";
        public static String LAST_APP_INFO = "lastappinfo";
        public static String APP_INFO_UPDATE = "update";
    }
}
