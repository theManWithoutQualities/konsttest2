package com.example.konsttest2.profileprovider;

import android.provider.BaseColumns;

public final class ProfileContract {

    private ProfileContract() {
    }

    public static final String AUTHORITY = "com.example.konsttest2.profile";
    public static class Profile implements BaseColumns {
        public static String GET_PROFILE_INFO = "get";
        public static String COLUMN_NAME_NAME = "name";
        public static String COLUMN_NAME_PHONE = "phone";
        public static String COLUMN_NAME_MAIL = "mail";
        public static String COLUMN_NAME_VK = "vk";
        public static String COLUMN_NAME_FB = "fb";
        public static String COLUMN_NAME_GIT = "git";
        public static String COLUMN_NAME_LOCATION = "location";
    }
}
