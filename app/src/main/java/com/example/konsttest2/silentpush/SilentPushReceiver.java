package com.example.konsttest2.silentpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.yandex.metrica.push.YandexMetricaPush;

import static com.example.konsttest2.KonstTest2.TAG;

public class SilentPushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String payload = intent.getStringExtra(YandexMetricaPush.EXTRA_PAYLOAD);
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putString("pushText", payload)
                .apply();
        Log.d(TAG, "push payload: " + payload);
        LocalBroadcastManager
                .getInstance(context)
                .sendBroadcast(new Intent("BIND_PUSH_TEXT"));
    }
}
