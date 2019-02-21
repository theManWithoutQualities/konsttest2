package com.example.konsttest2.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppEventReceiver extends BroadcastReceiver {

    public static final String REFRESH_APPS = "REFRESH_APPS";

    @Override
    public void onReceive(Context context, Intent intent) {
        context.sendBroadcast(new Intent(REFRESH_APPS));
    }
}
