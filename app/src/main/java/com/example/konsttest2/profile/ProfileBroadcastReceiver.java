package com.example.konsttest2.profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ProfileBroadcastReceiver extends BroadcastReceiver {
    private ProfileActivity activity;

    public ProfileBroadcastReceiver(ProfileActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        activity.bindPushText();
    }
}
