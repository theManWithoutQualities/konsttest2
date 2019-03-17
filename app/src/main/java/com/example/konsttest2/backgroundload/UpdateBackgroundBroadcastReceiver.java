package com.example.konsttest2.backgroundload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.example.konsttest2.main.MainActivity;

public class UpdateBackgroundBroadcastReceiver extends BroadcastReceiver {

    private MainActivity mainActivity;

    public UpdateBackgroundBroadcastReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        String action = intent.getAction();
        if (BackgroundLoadService.BROADCAST_ACTION_UPDATE_IMAGE.equals(action)) {
            final String imageName = intent
                    .getStringExtra(BackgroundLoadService.BROADCAST_EXTRA_IMAGE_NAME);
            if (!TextUtils.isEmpty(imageName)) {
                mainActivity.setBackground();
            }
        }
    }
}
