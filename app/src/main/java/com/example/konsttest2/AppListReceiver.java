package com.example.konsttest2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppListReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.sendBroadcast(new Intent("REFRESH_APPS"));
    }
}
